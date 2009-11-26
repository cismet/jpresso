/*
 * DriverManagerPanel.java
 *
 * Created on 17. März 2008, 17:32
 */
package de.cismet.jpresso.project.gui.drivermanager;

import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.core.data.DriverJar;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import de.cismet.jpresso.project.filetypes.DefaultURLProvider;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.TransferHandler;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
//TODO cleanup listeners: a little to many update and changes, but yet acceptable

/**
 * 
 * @author  stefan
 */
public final class DriverManagerPanel extends JPanel implements PropertyChangeListener {
    //the proptery for progess changes

    public static final String PROGRESS = "progress";
    //the proptery that indicates if a task starts or stops
    public static final String STATE = "state";
    private DriverListModel drvList;
    //cache that maps driverdescriptions to treemodel. if a driver is deleted, the model should also be deleted!
    private final Map<DriverDescription, DriverTreeModel> modelMap;
    //the tree model for the driver currently selected in driver list
    private DriverTreeModel currentView;
    //the driverdescription currently selected in the list
    private DriverDescription currentDriver;
    //indicates whether or not a background task is running.
    private boolean taskRunning;
    //a flag to disable listeners on tasks with a lot of changes
    private boolean listenerEnabled;
    //our listener
    private final Vector<ChangeListener> listener;
    private final JFileChooser chooser;

    /** Creates new form DriverManagerPanel */
    public DriverManagerPanel() {
        this(null);
    }

    public DriverManagerPanel(final List<DriverDescription> driverDescriptions) {
        listener = new Vector<ChangeListener>();
        modelMap = DriverTreeModel.getDriverTreeModelsForDriverDescriptions(driverDescriptions);
        drvList = new DriverListModel();
        final List<DriverDescription> descrs = TypeSafeCollections.newArrayList(modelMap.keySet());
        Collections.sort(descrs);
        for (Object o : descrs) {
            drvList.addElement(o);
        }
        initComponents();
        jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList1.setCellRenderer(new DriverListCellRenderer());

        jTree1.addTreeSelectionListener(new DriverTreeSelectionListener(jList1));
        jTree1.setCellRenderer(new DriverTreeCellRenderer());
        jTextField1.getDocument().addDocumentListener(new DriverAliasChangeListener());
        jTextField2.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                notifyChangeListener();
            }

            public void removeUpdate(DocumentEvent e) {
                notifyChangeListener();
            }

            public void changedUpdate(DocumentEvent e) {
                notifyChangeListener();
            }
        });
        jTextField1.setEnabled(false);
        jTextField2.setEnabled(false);
        jComboBox1.setEnabled(false);
        taskRunning = false;
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return (f.isDirectory() || f.getName().endsWith("." + JPressoFileManager.END_JAR));
            }

            @Override
            public String getDescription() {
                return "." + JPressoFileManager.END_JAR;
            }
        });
        chooser.setAcceptAllFileFilterUsed(false);
        final TransferHandler th = new TransferHandler() {

            @Override
            public int getSourceActions(final JComponent c) {
                if (c == jTree1) {
                    return DnDConstants.ACTION_COPY;
                }
                return DnDConstants.ACTION_NONE;
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
                if (c == jTree1) {

                    return new Transferable() {

                        public DataFlavor[] getTransferDataFlavors() {
                            return new DataFlavor[]{DataFlavor.javaFileListFlavor};
                        }

                        public boolean isDataFlavorSupported(final DataFlavor flavor) {
                            return DataFlavor.javaFileListFlavor.equals(flavor);
                        }

                        public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                            final TreePath[] paths = jTree1.getSelectionPaths();
                            final List<File> filelist = TypeSafeCollections.newArrayList();
                            for (final TreePath tp : paths) {
                                if (tp.getLastPathComponent() != null && tp.getLastPathComponent() instanceof JarNode) {
                                    final JarNode j = (JarNode) tp.getLastPathComponent();
                                    if (j.getJarPath().getName().endsWith("." + JPressoFileManager.END_JAR)) {
                                        filelist.add(j.getJarPath());
                                    }
                                }
                            }
                            return filelist;
                        }
                    };
                }
                return super.createTransferable(c);
            }

            @Override
            public boolean canImport(final TransferSupport support) {
                final DataFlavor[] flavs = support.getDataFlavors();
                for (final DataFlavor df : flavs) {
                    if (df.equals(DataFlavor.javaFileListFlavor)) {
                        return true;
                    }
                }

                return false;
            }

            @Override
            public boolean importData(final TransferSupport e) {
                try {
                    final Transferable tr = e.getTransferable();
                    final DataFlavor[] flavors = tr.getTransferDataFlavors();
                    for (int i = 0; i < flavors.length; ++i) {
                        if (flavors[i].equals(DataFlavor.javaFileListFlavor)) {
                            //cast
                            final List<File> mp = (List<File>) tr.getTransferData(flavors[i]);
                            final DriverTreeModel currentView = getCurrentView();
                            if (currentView != null) {
                                currentView.addJars(mp.toArray(new File[]{}));
                                return true;
                            }
                        }
                    }
                } catch (Throwable t) {
//                    log.debug("D&D problem!", t);
                }
                // Ein Problem ist aufgetreten
                return false;
            }
        };
        jTree1.setTransferHandler(th);
        jTree1.setDragEnabled(true);
        setListenerEnabled(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jToolBar1 = new javax.swing.JToolBar();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree(new DriverTreeModel(new HashSet<DriverJar>()));
        jPanel3 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();

        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Driver Manager", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        setMinimumSize(new java.awt.Dimension(592, 679));
        setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/arrow-down.png"))); // NOI18N
        jButton9.setToolTipText("Sort a -> z");
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton9);

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/arrow-up.png"))); // NOI18N
        jButton10.setToolTipText("Sort z -> a");
        jButton10.setFocusable(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton10);

        add(jToolBar1, java.awt.BorderLayout.NORTH);

        jScrollPane3.setBorder(null);

        jPanel1.setMinimumSize(new java.awt.Dimension(570, 631));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/edit-add.png"))); // NOI18N
        jButton6.setToolTipText("Add new Driver");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton6, new java.awt.GridBagConstraints());

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/edit-delete.png"))); // NOI18N
        jButton7.setToolTipText("Remove selected Drivers");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton7, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(jPanel4, gridBagConstraints);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Driver File paths", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")), javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5), javax.swing.BorderFactory.createEtchedBorder()))); // NOI18N
        jScrollPane2.setMaximumSize(new java.awt.Dimension(350, 100));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(350, 100));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(350, 100));

        jTree1.setModel(null);
        jTree1.setRootVisible(false);
        jScrollPane2.setViewportView(jTree1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        jPanel1.add(jScrollPane2, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/edit-add.png"))); // NOI18N
        jButton4.setText("Add jar"); // NOI18N
        jButton4.setEnabled(false);
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jButton4, gridBagConstraints);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/document-preview-archive.png"))); // NOI18N
        jButton5.setText("Scan"); // NOI18N
        jButton5.setEnabled(false);
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jButton5, gridBagConstraints);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/edit-delete.png"))); // NOI18N
        jButton8.setText("Remove"); // NOI18N
        jButton8.setEnabled(false);
        jButton8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jButton8, gridBagConstraints);

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/process-stop.png"))); // NOI18N
        jButton11.setText("Cancel");
        jButton11.setEnabled(false);
        jButton11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel3.add(jButton11, gridBagConstraints);

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/go-up.png"))); // NOI18N
        jButton12.setText("Move Up");
        jButton12.setEnabled(false);
        jButton12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel3.add(jButton12, gridBagConstraints);

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/go-down.png"))); // NOI18N
        jButton13.setText("Move down");
        jButton13.setEnabled(false);
        jButton13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel3.add(jButton13, gridBagConstraints);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/view-tree.png"))); // NOI18N
        jButton2.setText("Expand");
        jButton2.setEnabled(false);
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel3.add(jButton2, gridBagConstraints);

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/view-restore.png"))); // NOI18N
        jButton14.setText("Collapse");
        jButton14.setEnabled(false);
        jButton14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jButton14, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        jPanel1.add(jPanel3, gridBagConstraints);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Driver List", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")), javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5), javax.swing.BorderFactory.createEtchedBorder()))); // NOI18N
        jScrollPane1.setMaximumSize(new java.awt.Dimension(299, 188));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(299, 188));

        jList1.setModel(this.drvList
        );
        jList1.setMaximumSize(new java.awt.Dimension(282, 171));
        jList1.setMinimumSize(new java.awt.Dimension(282, 171));
        jList1.setPreferredSize(new java.awt.Dimension(282, 171));
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/edit.png"))); // NOI18N
        jButton1.setText("Find URL"); // NOI18N
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.setMaximumSize(new java.awt.Dimension(93, 31));
        jButton1.setMinimumSize(new java.awt.Dimension(93, 31));
        jButton1.setPreferredSize(new java.awt.Dimension(93, 31));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel5.add(jButton1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        jPanel1.add(jPanel5, gridBagConstraints);

        jPanel6.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Alias", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        jPanel6.setLayout(new java.awt.BorderLayout());

        jTextField1.setMaximumSize(new java.awt.Dimension(6, 20));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });
        jPanel6.add(jTextField1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jPanel6, gridBagConstraints);

        jPanel7.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "URL Format", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        jPanel7.setLayout(new java.awt.BorderLayout());

        jTextField2.setMaximumSize(new java.awt.Dimension(6, 20));
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField2FocusLost(evt);
            }
        });
        jPanel7.add(jTextField2, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jPanel7, gridBagConstraints);

        jPanel8.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Driver Class", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        jPanel8.setLayout(new java.awt.BorderLayout());

        jComboBox1.setMaximumSize(new java.awt.Dimension(28, 20));
        jComboBox1.setMinimumSize(new java.awt.Dimension(28, 20));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jPanel8.add(jComboBox1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jPanel8, gridBagConstraints);

        jPanel9.setLayout(new java.awt.BorderLayout());

        jProgressBar1.setMinimumSize(new java.awt.Dimension(145, 20));
        jProgressBar1.setPreferredSize(new java.awt.Dimension(145, 20));
        jPanel9.add(jProgressBar1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        jPanel1.add(jPanel9, gridBagConstraints);

        jScrollPane3.setViewportView(jPanel1);

        add(jScrollPane3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (!taskRunning) {

            //    
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                final File[] files = chooser.getSelectedFiles();
                //replace possible shell32 files with "pure" files for better xstream persistence
                for (int i = 0; i < files.length; ++i) {
                    files[i] = new File(files[i].getAbsolutePath());
                }
                getCurrentView().addJars(files);
                notifyChangeListener();
                jButton4.setEnabled(false);
                jButton8.setEnabled(false);
                jButton5.setEnabled(false);
//                jButton2.setEnabled(false);
                jButton12.setEnabled(false);
                jButton13.setEnabled(false);
//                jButton14.setEnabled(false);
                jButton11.setEnabled(true);
//            for (File f : files) {
//                getCurrentView().addJar(f);
//            }
            }
        }
        expandAll(jTree1);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        if (!taskRunning) {
            jButton4.setEnabled(false);
            jButton8.setEnabled(false);
            jButton5.setEnabled(false);
//        jButton2.setEnabled(false);
            jButton12.setEnabled(false);
            jButton13.setEnabled(false);
//        jButton14.setEnabled(false);
            jButton11.setEnabled(true);
            notifyChangeListener();
            getCurrentView().scanDriver();

//        ComboBoxModel model = new DefaultComboBoxModel(currentView.getFoundDrivers().toArray(new String[]{}));
//        jComboBox1.setModel(model);
            expandAll(jTree1);
            updateCurrentDriver();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        final DriverDescription dd = new DriverDescription();
        dd.setName("NEW DRIVER");
        drvList.addElement(dd);
        jList1.setSelectedValue(dd, true);
        notifyChangeListener();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        //TODO nullpointer auf selpath
//        TreePath[] selPaths = jTree1.getSelectionPaths();
//        if (selPaths != null) {
//            for (TreePath tp : selPaths) {
//                if (tp != null) {
//                    Object sel = tp.getLastPathComponent();
//                    if (sel != null && sel instanceof MutableTreeNode) {
//                        MutableTreeNode toRemove = (MutableTreeNode) sel;
//                        if (!toRemove.equals(currentView.getRoot())) {
//                            getCurrentView().removeNodeFromParent(toRemove);
//                            getCurrentView().reload();
//                        }
//                        updateCurrentDriver();
//                    }
//                }
//            }
//        }
        final TreePath[] selPaths = jTree1.getSelectionPaths();
        if (selPaths != null) {
            final List<MutableTreeNode> removeList = TypeSafeCollections.newArrayList();
            for (final TreePath tp : selPaths) {
                if (tp != null) {
                    final Object sel = tp.getLastPathComponent();
                    if (sel != null && sel instanceof MutableTreeNode) {
                        final MutableTreeNode toRemove = (MutableTreeNode) sel;
                        if (!toRemove.equals(currentView.getRoot())) {
                            //TODO danger!
                            removeList.add(toRemove);
//                            currentView.removeNodeFromParent(toRemove);
//                            ((DefaultMutableTreeNode) toRemove.getParent()).remove(toRemove);
                        }
                    }
                }
            }
            currentView.removeNodesFromParentNode(removeList.toArray(new DefaultMutableTreeNode[]{}));
            currentView.updateDriverComboboxModel();
//            getCurrentView().reload();
            notifyChangeListener();
            updateCurrentDriver();
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        int index = jList1.getSelectedIndex();
        if (currentView != null) {
            currentView.cancelScan();
        }
        final Object sel = jList1.getSelectedValue();
        if (sel != null && sel instanceof DriverDescription) {
            final DriverDescription dd = (DriverDescription) sel;
            removeDriverDescription(dd);
            notifyChangeListener();
//            jList1.setSelectedIndex(0);

        }
        jList1.setSelectedIndex(index);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost

        updateCurrentDriver();
    }//GEN-LAST:event_jTextField1FocusLost

    private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusLost

        updateCurrentDriver();
    }//GEN-LAST:event_jTextField2FocusLost

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
//        jList1.setValueIsAdjusting(true);
//        jList1.setSelectedIndex(-1);
        drvList.sort(false);
//        jList1.setSelectedValue(currentDriver, true);
//        jList1.setValueIsAdjusting(false);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
//        jList1.setValueIsAdjusting(true);
//        jList1.setSelectedIndex(-1);
        drvList.sort(true);
//        jList1.setSelectedValue(currentDriver, true);
//        jList1.setValueIsAdjusting(false);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jComboBox1.getSelectedItem() != null) {
            jTextField2.setText(DefaultURLProvider.getDefaultURL((String) jComboBox1.getSelectedItem()));
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * If the selection changes, all changes to the current driver are saved in the object.
     * After that other tree and comboboxmodels are shown and the textfields are filled with
     * the new selected driver's values.
     * 
     * @param evt
     */
    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged

        //        if (evt.getValueIsAdjusting()) {
        final Object o = jList1.getSelectedValue();
        if (o != null && o instanceof DriverDescription && !o.equals(currentDriver)) {
            updateCurrentDriver();
            try {
                setListenerEnabled(false);
                final DriverDescription dd = (DriverDescription) jList1.getSelectedValue();
                //current = the new one
                currentDriver = dd;
                jTextField1.setText(dd.getName());
                jTextField2.setText(dd.getUrlFormat());
                DriverTreeModel model = modelMap.get(dd);
                if (model == null) {
                    model = new DriverTreeModel(dd.getJarFiles());
                    modelMap.put(dd, model);
                }
                if (getCurrentView() != null) {
                    getCurrentView().removeAllListener();
                }
//            model.addWorkerPropertyChangeListener(new ProgressListener(jProgressBar1, jTree1));
                model.addWorkerPropertyChangeListener(this);
                jTree1.setModel(model);
                setCurrentView(model);
                jComboBox1.setModel(getCurrentView().getDriverList());
                //            jComboBox1.setModel(new DefaultComboBoxModel(model.getFoundDrivers().toArray(new String[]{})));
                jComboBox1.setSelectedItem(currentDriver.getDefaultClass());
                expandAll(jTree1);
                if (currentView != null) {
                    if (!taskRunning) {
                        jButton4.setEnabled(true);
                        jButton8.setEnabled(true);
                        jButton5.setEnabled(true);
                    }
                    jButton2.setEnabled(true);
                    jButton14.setEnabled(true);
                    jButton12.setEnabled(false);
                    jButton13.setEnabled(false);
                    jTextField1.setEnabled(true);
                    jTextField2.setEnabled(true);
                    jComboBox1.setEnabled(true);
                } else {
                    jButton4.setEnabled(false);
                    jButton8.setEnabled(false);
                    jButton5.setEnabled(false);
                    jButton12.setEnabled(false);
                    jButton13.setEnabled(false);
                    jButton2.setEnabled(false);
                    jButton14.setEnabled(false);
                    jTextField1.setEnabled(false);
                    jTextField2.setEnabled(false);
                    jComboBox1.setEnabled(false);
                }
            } finally {
                setListenerEnabled(true);
            }
        } else if (o == null) {
            currentDriver = null;
            if (getCurrentView() != null) {
                getCurrentView().removeAllListener();
            }
            setCurrentView(null);
            jTree1.setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));
            jTextField1.setText("");
            jTextField2.setText("");
            jComboBox1.setModel(new DefaultComboBoxModel());
            jButton4.setEnabled(false);
            jButton8.setEnabled(false);
            jButton5.setEnabled(false);
            jButton12.setEnabled(false);
            jButton13.setEnabled(false);
            jButton2.setEnabled(false);
            jButton14.setEnabled(false);
            jTextField1.setEnabled(false);
            jTextField2.setEnabled(false);
            jComboBox1.setEnabled(false);
        }
//GEN-LAST:event_jList1ValueChanged
    }

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

        cancelScan();

    }//GEN-LAST:event_jButton11ActionPerformed

    public void cancelScan() {
        if (currentView != null) {
            currentView.cancelScan();
        }
    }

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        if (jTree1.getSelectionPath() != null) {
            final Object o = jTree1.getSelectionPath().getLastPathComponent();
            if (o instanceof JarNode) {
                final JarNode p = (JarNode) o;
                currentView.moveNode(p, true);
                final TreePath tp = new TreePath(p.getPath());
                jTree1.setSelectionPath(tp);
                updateCurrentDriver();
                notifyChangeListener();
            }
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        if (jTree1.getSelectionPath() != null) {
            Object o = jTree1.getSelectionPath().getLastPathComponent();
            if (o instanceof JarNode) {
                JarNode p = (JarNode) o;
                currentView.moveNode(p, false);
                TreePath tp = new TreePath(p.getPath());
                jTree1.setSelectionPath(tp);
                updateCurrentDriver();
                notifyChangeListener();
            }
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        expandAll(jTree1);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        collapseAll(jTree1);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            updateCurrentDriver();
            notifyChangeListener();
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    public void setDriver(final List<DriverDescription> driverDescriptions) {
        modelMap.clear();
        modelMap.putAll(DriverTreeModel.getDriverTreeModelsForDriverDescriptions(driverDescriptions));
        drvList.clear();
        final List<DriverDescription> descrs = TypeSafeCollections.newArrayList(modelMap.keySet());
        Collections.sort(descrs);
        for (final Object o : descrs) {
            drvList.addElement(o);
        }
    }

    /**
     * Removes a DriverDesription from the list
     * 
     * @param dd
     */
    private void removeDriverDescription(DriverDescription dd) {
        if (dd != null) {
            for (DriverJar jar : dd.getJarFiles()) {
                if (jar != null && jar.getJarFile() != null) {
                    //remove driver and classloader from manager
//                    manager.removeURL(jar.getJarFile());
                }
            }
            //remove from jlist
            drvList.removeElement(dd);
            //remove treemodel from map
            modelMap.remove(dd);
        }
    }

    /**
     * Updates the currently selected DriverDescription with the changes from GUI
     */
    private void updateCurrentDriver() {
        if (listenerEnabled) {
            if (currentDriver != null) {
                //Save default URL from textfield
                currentDriver.setUrlFormat(jTextField2.getText());
                //Save default class from combobox
                Object selection = jComboBox1.getSelectedItem();
                if (selection != null && selection instanceof String) {
                    currentDriver.setDefaultClass((String) selection);
                } else {
                    currentDriver.setDefaultClass("");
                }
                //Save "DriverJars" from TreeModel
                currentDriver.setJarFiles(getCurrentView().getDriverJars());
                //refresh list representation
                drvList.refresh(jList1.getSelectedIndex());
            }
        }
    }
    //TODO check duplicate entries!

    public List<DriverDescription> getDriverList() {
        updateCurrentDriver();
        final List<DriverDescription> lst = TypeSafeCollections.newArrayList();
        for (final Object o : drvList.toArray()) {
            lst.add((DriverDescription) o);
        }
        return lst;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables

    private void expandAll(final JTree tree) {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.expandRow(row);
            row++;
        }
    }

    private void collapseAll(final JTree tree) {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.collapseRow(row);
            row++;
        }
    }

    public void addChangeListener(ChangeListener l) {
        listener.add(l);
    }

    public void removeChangeListener(ChangeListener l) {
        listener.remove(l);
    }

    private void notifyChangeListener() {
        if (listenerEnabled) {
            for (final ChangeListener l : listener) {
                if (l != null) {
                    l.stateChanged(new ChangeEvent(this));
                }
            }
        }
    }

    public void propertyChange(final PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PROGRESS) && isTaskRunning()) {
            final Integer val = (Integer) evt.getNewValue();
            jProgressBar1.setValue(val);
//            bar.setString(val + " / 100");
//
//            for (int i = 0; i < tree.getRowCount(); ++i) {
//                tree.expandRow(i);
//            }
        } else if (evt.getPropertyName().equals(STATE)) {
            jProgressBar1.setValue(0);
            if (evt.getNewValue().equals(SwingWorker.StateValue.DONE)) {
                taskRunning = false;
                jProgressBar1.setStringPainted(false);
                expandAll(jTree1);
                jButton5.setEnabled(true);
                jButton11.setEnabled(false);
                jButton4.setEnabled(true);
                jButton8.setEnabled(true);
                jButton2.setEnabled(true);
                jButton14.setEnabled(true);
                updateCurrentDriver();
                notifyChangeListener();
//                bar.setVisible(false);
            } else if (evt.getNewValue().equals(SwingWorker.StateValue.STARTED)) {
                if (!taskRunning) {
                    taskRunning = true;
                    notifyChangeListener();
                }
                jProgressBar1.setStringPainted(true);
//                bar.setVisible(true);
            }
        }
    }

    public DriverTreeModel getCurrentView() {
        return currentView;
    }

    public void setCurrentView(final DriverTreeModel currentView) {
        this.currentView = currentView;
    }

    public boolean isListenerEnabled() {
        return listenerEnabled;
    }

    public void setListenerEnabled(final boolean listenerEnabled) {
        this.listenerEnabled = listenerEnabled;
    }

    public boolean isTaskRunning() {
        return taskRunning;
    }

    class DriverTreeSelectionListener implements TreeSelectionListener {

        public DriverTreeSelectionListener(final JList observer) {
            if (observer == null) {
                throw new NullPointerException("Observer can not be null!");
            }
            this.observer = observer;
        }
        private final JList observer;

        public void valueChanged(final TreeSelectionEvent e) {
            jTree1.repaint();
            if (e.getPath() != null) {
                final DefaultMutableTreeNode sel = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
                if (sel instanceof JarNode && sel.getParent() != null) {
                    int index = sel.getParent().getIndex(sel);
                    if (index > 0) {
                        jButton12.setEnabled(true);
                    } else {
                        jButton12.setEnabled(false);
                    }
                    if (index < sel.getParent().getChildCount() - 1) {
                        jButton13.setEnabled(true);
                    } else {
                        jButton13.setEnabled(false);
                    }
                } else {
                    jButton12.setEnabled(false);
                    jButton13.setEnabled(false);
                }
            } else {
                jButton12.setEnabled(false);
                jButton13.setEnabled(false);
            }
        }
    }

    class DriverAliasChangeListener implements DocumentListener {

        public DriverAliasChangeListener() {
            ref = null;
        }
        private DriverDescription ref;

        public void insertUpdate(final DocumentEvent e) {
            ref = currentDriver;
            if (ref != null) {
                ref.setName(jTextField1.getText());
                drvList.refresh(jList1.getSelectedIndex());
                notifyChangeListener();
            }
            ref = null;
        }

        public void removeUpdate(final DocumentEvent e) {
            insertUpdate(e);
        }

        public void changedUpdate(final DocumentEvent e) {
            insertUpdate(e);
        }
    }
}
