/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.jpresso.project.gui.options;

import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.Repository;
import org.openide.util.Exceptions;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileFilter;

import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.core.data.ProjectOptions;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

import de.cismet.jpresso.project.gui.drivermanager.DriverManagerPanel;
import de.cismet.jpresso.project.gui.editors.UndoRedoSupport;
import de.cismet.jpresso.project.serviceprovider.ClassResourceListener;

/**
 * Editor for the project's options, like additional ClassPath and Driver.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class ProjectOptionsEditor extends JPanel {

    //~ Instance fields --------------------------------------------------------

// private ProjectOptions options;
    private final Vector<ProjectOptionsEditorListener> listener;
    private final DriverManagerPanel panDriver;
    private final DefaultListModel classPathModel;
    private final JFileChooser chooser;
    private JpressofiletypesOptionsPanelController controller;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JList jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel panClasspath;
    private javax.swing.JPanel panDrv;
    private javax.swing.JPanel panOptions;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProjectOptionsEditor object.
     *
     * @param  data  DOCUMENT ME!
     */
    public ProjectOptionsEditor(ProjectOptions data) {
        initComponents();
        if (data == null) {
            data = new ProjectOptions();
        }
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileFilter(new FileFilter() {

                @Override
                public boolean accept(final File f) {
                    return (f.isDirectory() || f.getName().endsWith("." + JPressoFileManager.END_JAR));
                }

                @Override
                public String getDescription() {
                    return "." + JPressoFileManager.END_JAR;
                }
            });
        chooser.setAcceptAllFileFilterUsed(false);
        panDriver = new DriverManagerPanel();
        panDriver.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    notifyDriverChanged();
                }
            });
        listener = new Vector<ProjectOptionsEditorListener>();
        classPathModel = new DefaultListModel();
        jList2.setModel(classPathModel);
        panDrv.add(panDriver, BorderLayout.CENTER);
        jList2.setCellRenderer(new ClasspathListCellRenderer());
        final TransferHandler th = new TransferHandler() {

                @Override
                public int getSourceActions(final JComponent c) {
                    if (c == jList2) {
                        return DnDConstants.ACTION_COPY;
                    }
                    return DnDConstants.ACTION_NONE;
                }

                @Override
                protected Transferable createTransferable(final JComponent c) {
                    if (c == jList2) {
                        return new Transferable() {

                                @Override
                                public DataFlavor[] getTransferDataFlavors() {
                                    return new DataFlavor[] { DataFlavor.javaFileListFlavor };
                                }

                                @Override
                                public boolean isDataFlavorSupported(final DataFlavor flavor) {
                                    return DataFlavor.javaFileListFlavor.equals(flavor);
                                }

                                @Override
                                public Object getTransferData(final DataFlavor flavor)
                                        throws UnsupportedFlavorException, IOException {
                                    final Object[] vals = jList2.getSelectedValues();
                                    final List<File> filelist = TypeSafeCollections.newArrayList();
                                    for (final Object o : vals) {
                                        if (o instanceof File) {
                                            filelist.add(new File(((File)o).getAbsolutePath()));
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
                                // CAST
                                final Object possibleFileList = tr.getTransferData(flavors[i]);
                                if (possibleFileList instanceof List) {
                                    final List<File> mp = (List<File>)possibleFileList;
                                    final File[] toAdd = new File[mp.size()];
                                    int j = -1;
                                    for (final File f : mp) {
                                        toAdd[++j] = new File(f.getAbsolutePath());
                                    }
                                    addFilesToClasspath(toAdd);
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
        jList2.setTransferHandler(th);
        jList2.setDragEnabled(true);
        setContent(data);
        classPathModel.addListDataListener(new ListDataListener() {

                @Override
                public void intervalAdded(final ListDataEvent e) {
                    notifyClassPathChanged();
                }

                @Override
                public void intervalRemoved(final ListDataEvent e) {
                    notifyClassPathChanged();
                }

                @Override
                public void contentsChanged(final ListDataEvent e) {
                    // ignore
                }
            });

        final DocumentListener dl = new DocumentListener() {

                @Override
                public void insertUpdate(final DocumentEvent e) {
                    notifyOtherOptionsChanged();
                }

                @Override
                public void removeUpdate(final DocumentEvent e) {
                    notifyOtherOptionsChanged();
                }

                @Override
                public void changedUpdate(final DocumentEvent e) {
                    notifyOtherOptionsChanged();
                }
            };
        jTextArea1.getDocument().addDocumentListener(dl);
        jTextField1.getDocument().addDocumentListener(dl);
        UndoRedoSupport.decorate(jTextArea1);
        UndoRedoSupport.decorate(jTextField1);
    }

    /**
     * Creates a new ProjectOptionsEditor object.
     *
     * @param  controller  DOCUMENT ME!
     */
    ProjectOptionsEditor(final JpressofiletypesOptionsPanelController controller) {
        this(loadGlobalProjectOptions());
        this.controller = controller;
        jButton2.setVisible(false);
        jButton5.setVisible(false);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        panOptions = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        panDrv = new javax.swing.JPanel();
        panClasspath = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(593, 697));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(593, 697));

        panOptions.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    null,
                    org.openide.util.NbBundle.getMessage(
                        ProjectOptionsEditor.class,
                        "ProjectOptionsEditor.panOptions.border.outsideBorder.title"),
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 0, 12),
                    javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        panOptions.setMinimumSize(new java.awt.Dimension(593, 697));
        panOptions.setPreferredSize(new java.awt.Dimension(593, 697));
        panOptions.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    null,
                    org.openide.util.NbBundle.getMessage(
                        ProjectOptionsEditor.class,
                        "ProjectOptionsEditor.jPanel1.border.outsideBorder.title"),
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 0, 12),
                    javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        jPanel1.setLayout(new java.awt.BorderLayout());

        jTextField1.setText(org.openide.util.NbBundle.getMessage(
                ProjectOptionsEditor.class,
                "ProjectOptionsEditor.jTextField1.text")); // NOI18N
        jPanel1.add(jTextField1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panOptions.add(jPanel1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    null,
                    org.openide.util.NbBundle.getMessage(
                        ProjectOptionsEditor.class,
                        "ProjectOptionsEditor.jPanel2.border.outsideBorder.title"),
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 0, 12),
                    javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        jPanel2.setLayout(new java.awt.BorderLayout());

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panOptions.add(jPanel2, gridBagConstraints);

        jButton2.setText(org.openide.util.NbBundle.getMessage(
                ProjectOptionsEditor.class,
                "OptionsTopComponent.jButton2.text_1")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });
        jPanel4.add(jButton2);

        jButton5.setText(org.openide.util.NbBundle.getMessage(
                ProjectOptionsEditor.class,
                "OptionsTopComponent.jButton5.text")); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton5ActionPerformed(evt);
                }
            });
        jPanel4.add(jButton5);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panOptions.add(jPanel4, gridBagConstraints);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(
                ProjectOptionsEditor.class,
                "OptionsTopComponent.panOptions.TabConstraints.tabTitle"),
            panOptions); // NOI18N

        panDrv.setMinimumSize(new java.awt.Dimension(593, 697));
        panDrv.setPreferredSize(new java.awt.Dimension(593, 697));
        panDrv.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(
                ProjectOptionsEditor.class,
                "OptionsTopComponent.panDrv.TabConstraints.tabTitle"),
            panDrv); // NOI18N

        panClasspath.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    null,
                    org.openide.util.NbBundle.getMessage(
                        ProjectOptionsEditor.class,
                        "ProjectOptionsEditor.panClasspath.border.outsideBorder.title"),
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 0, 12),
                    javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        panClasspath.setMinimumSize(new java.awt.Dimension(593, 697));
        panClasspath.setPreferredSize(new java.awt.Dimension(593, 697));
        panClasspath.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jList2.setModel(new DefaultListModel());
        jScrollPane2.setViewportView(jList2);

        panClasspath.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jButton3.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/jpresso/project/res/edit-add.png"))); // NOI18N
        jButton3.setText(org.openide.util.NbBundle.getMessage(
                ProjectOptionsEditor.class,
                "OptionsTopComponent.jButton3.text"));                                   // NOI18N
        jButton3.setToolTipText(org.openide.util.NbBundle.getMessage(
                ProjectOptionsEditor.class,
                "ProjectOptionsEditor.jButton3.toolTipText"));                           // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton3ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jButton3, gridBagConstraints);

        jButton4.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/jpresso/project/res/edit-delete.png"))); // NOI18N
        jButton4.setText(org.openide.util.NbBundle.getMessage(
                ProjectOptionsEditor.class,
                "OptionsTopComponent.jButton4.text"));                                      // NOI18N
        jButton4.setToolTipText(org.openide.util.NbBundle.getMessage(
                ProjectOptionsEditor.class,
                "ProjectOptionsEditor.jButton4.toolTipText"));                              // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton4ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jButton4, gridBagConstraints);

        panClasspath.add(jPanel3, java.awt.BorderLayout.LINE_END);

        jTabbedPane1.addTab("Add. Classpath", panClasspath);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
        jTabbedPane1.getAccessibleContext()
                .setAccessibleName(org.openide.util.NbBundle.getMessage(
                        ProjectOptionsEditor.class,
                        "ProjectOptionsEditor.jTabbedPane1.AccessibleContext.accessibleName")); // NOI18N
    }                                                                                           // </editor-fold>//GEN-END:initComponents
    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton3ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton3ActionPerformed
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            final File[] files = chooser.getSelectedFiles();
            addFilesToClasspath(files);
        }
    }                                                                            //GEN-LAST:event_jButton3ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton4ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton4ActionPerformed
        final DefaultListModel model = (DefaultListModel)jList2.getModel();
        final Object[] rem = jList2.getSelectedValues();
        for (final Object act : rem) {
            if (act != null) {
                model.removeElement(act);
            }
        }
    }                                                                            //GEN-LAST:event_jButton4ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton2ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton2ActionPerformed
        final Set<String> filterDrv = TypeSafeCollections.newHashSet();
        final ProjectOptions globPO = loadGlobalProjectOptions();
        final List<DriverDescription> projDrv = panDriver.getDriverList();
        final List<DriverDescription> globjDrv = globPO.getDriver();
        final List<DriverDescription> resultDrv = TypeSafeCollections.newArrayList();
        for (final DriverDescription dd : projDrv) {
            if (filterDrv.add(dd.getName())) {
                resultDrv.add(dd);
            }
        }
        for (final DriverDescription dd : globjDrv) {
            if (filterDrv.add(dd.getName())) {
                resultDrv.add(dd);
            }
        }
        // ---------------------
        final Set<File> filterClassPath = TypeSafeCollections.newHashSet();
        filterClassPath.addAll(getClassPathFiles());
        filterClassPath.addAll(globPO.getAddClassPath());
        // ----------------------
        classPathModel.clear();
        addFilesToClasspath(filterClassPath.toArray(new File[] {}));
        panDriver.setDriver(resultDrv);
        if ((jTextField1.getText().length() == 0) && (jTextArea1.getText().length() == 0)) {
            setFinalizerOptions(globPO); //GEN-LAST:event_jButton2ActionPerformed
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton5ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton5ActionPerformed
        classPathModel.clear();
        final ProjectOptions globPO = loadGlobalProjectOptions();
        addFilesToClasspath(globPO.getAddClassPath().toArray(new File[] {}));
        panDriver.setDriver(globPO.getDriver());
    }                                                                            //GEN-LAST:event_jButton5ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected static ProjectOptions loadGlobalProjectOptions() {
        final FileSystem fs = Repository.getDefault().getDefaultFileSystem();
        final FileObject result = fs.findResource(JPressoFileManager.PROJECT_OPTIONS);
        ProjectOptions ret = null;
        try {
            ret = JPressoFileManager.getDefault().load(FileUtil.toFile(result), ProjectOptions.class);
        } catch (Exception ex) {
            // TODO benachrichtigen & fragen was weiter zu tun
            Exceptions.printStackTrace(ex);
        }
        if (result == null) {
            return new ProjectOptions();
        }
        return ret;
    }

    /**
     * DOCUMENT ME!
     */
    protected void saveGlobalProjectOptions() {
        try {
            final FileSystem fs = Repository.getDefault().getDefaultFileSystem();
            FileObject result = fs.findResource(JPressoFileManager.PROJECT_OPTIONS);
            if (result == null) {
                result = fs.getRoot().createData(JPressoFileManager.PROJECT_OPTIONS);
            }
            JPressoFileManager.getDefault().persist(FileUtil.toFile(result), getContent());
        } catch (IOException ioe) {
            ErrorManager.getDefault().notify(ErrorManager.WARNING, ioe);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  files  DOCUMENT ME!
     */
    private void addFilesToClasspath(final File[] files) {
        for (final File f : files) {
            if ((f != null) && f.isFile()) {
                classPathModel.addElement(new File(f.getAbsolutePath()));
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void cancel() {
        panDriver.cancelScan();
        setContent(loadGlobalProjectOptions());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected boolean valid() {
        // check if no scan is running...
        return !panDriver.isTaskRunning();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ProjectOptions getContent() {
        final Properties p = new Properties();
        final String tParam = this.jTextArea1.getText().trim();
        final String[] tp = tParam.split("\n");
        for (int i = 0; i < tp.length; ++i) {
            final String[] keyValue = tp[i].split("=");
            if (keyValue.length != 2) {
                continue;
            }
            p.setProperty(keyValue[0], keyValue[1]);
        }
        final ProjectOptions ret = new ProjectOptions(
                getClassPathFiles(),
                panDriver.getDriverList(),
                jTextField1.getText(),
                p);
        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  po  DOCUMENT ME!
     */
    public void setContent(final ProjectOptions po) {
//        options = po;
        panDriver.setDriver(po.getDriver());
        setClassPathFiles(po.getAddClassPath());
        setFinalizerOptions(po);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  classPath  DOCUMENT ME!
     */
    public void setClassPathFiles(final Set<File> classPath) {
        classPathModel.clear();
        for (final File f : classPath) {
            classPathModel.addElement(f);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  po  DOCUMENT ME!
     */
    public void setFinalizerOptions(final ProjectOptions po) {
        jTextField1.setText(po.getDefaultFinalizerClass());
        final Properties p = po.getDefaultFinalizerProperties();
        final StringBuilder sb = new StringBuilder();
        for (final String s : p.stringPropertyNames()) {
            sb.append(s);
            sb.append("=");
            sb.append(p.getProperty(s));
            sb.append("\n");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        jTextArea1.setText(sb.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  l  DOCUMENT ME!
     */
    public void addListener(final ProjectOptionsEditorListener l) {
        listener.add(l);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  l  DOCUMENT ME!
     */
    public void removeListener(final ProjectOptionsEditorListener l) {
        listener.remove(l);
    }

    /**
     * DOCUMENT ME!
     */
    public void removeAllListener() {
        listener.clear();
    }

    /**
     * The additional classpath list changed.
     */

    private void notifyClassPathChanged() {
        for (final ClassResourceListener crl : listener) {
            crl.projectClassPathChanged(new ArrayList<File>(getClassPathFiles()));
        }
        if (controller != null) {
            controller.changed();
        }
    }

    /**
     * The driver list changed.
     */
    private void notifyDriverChanged() {
        final List<DriverDescription> change = panDriver.getDriverList();
        for (final ClassResourceListener crl : listener) {
            try {
                crl.projectDriverListChanged(change);
            } catch (Exception ex) {
                // ignore
            }
        }
        if (controller != null) {
            controller.changed();
        }
    }

    /**
     * The other options (like finalizer stuff) changed.
     */
    private void notifyOtherOptionsChanged() {
        for (final ProjectOptionsEditorListener crl : listener) {
            crl.otherOptionsChanged();
        }
        if (controller != null) {
            controller.changed();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the classpath files
     */
    private Set<File> getClassPathFiles() {
        final Set<File> ret = TypeSafeCollections.newHashSet();
        for (int i = 0; i < classPathModel.getSize(); ++i) {
            ret.add((File)classPathModel.get(i));
        }
        return ret;
    }
}
