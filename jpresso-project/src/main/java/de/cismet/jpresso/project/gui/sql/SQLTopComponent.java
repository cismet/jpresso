/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.sql;

import de.cismet.jpresso.core.serviceprovider.exceptions.InitializingException;
import de.cismet.jpresso.core.data.DatabaseConnection;
import de.cismet.jpresso.core.data.SQLRun;
import de.cismet.jpresso.project.gui.output.OutputSQL;
import de.cismet.jpresso.project.gui.output.OutputTabbedPaneFactory;
import de.cismet.jpresso.project.gui.editors.SQLEditor;
import de.cismet.jpresso.project.gui.AbstractJPTopComponent;
import de.cismet.jpresso.project.gui.ProgressHandler;
import de.cismet.jpresso.project.gui.dnd.JPDataFlavors;
import de.cismet.jpresso.core.serviceprovider.ClassResourceProvider;
import de.cismet.jpresso.core.serviceprovider.ControllerFactory;
import de.cismet.jpresso.core.serviceprovider.SQLScriptExecutionController;
import de.cismet.jpresso.project.filetypes.connection.ConnectionDataNode;
import de.cismet.jpresso.project.filetypes.connection.ConnectionDataObject;
import de.cismet.jpresso.project.filetypes.cookies.ConnectionListModelProvider;
import de.cismet.jpresso.project.filetypes.sql.SQLDataObject;
import de.cismet.jpresso.project.serviceprovider.ExecutorProvider;
import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.TransferHandler;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.cookies.OpenCookie;
import org.openide.explorer.view.NodeListModel;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataNode;
import org.openide.util.Cancellable;
import org.openide.util.NbBundle;

/**
 * Top component which displays something.
 */
public final class SQLTopComponent extends AbstractJPTopComponent<SQLDataObject> implements ListDataListener {

    private SQLEditor se;
    private ConnectionDataObject currentConnection;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "SQLTopComponent";
    //blocks combobox listener during init.
    public SQLTopComponent(final SQLDataObject data) {
        super(data);
        setListenerActive(false);
        initComponents();
        final SQLDataObject sqldata = getData();
        se = new SQLEditor(sqldata.getData());
        initComboBox(sqldata);
        jPanel8.add(se, BorderLayout.CENTER);
        setName(NbBundle.getMessage(SQLTopComponent.class, "CTL_SQLTopComponent"));
        setToolTipText(NbBundle.getMessage(SQLTopComponent.class, "HINT_SQLTopComponent"));
        se.getStatementDocument().addDocumentListener(this);
        getData().setModified(false);
        setListenerActive(true);
        final TransferHandler th = new TransferHandler() {

            @Override
            public int getSourceActions(JComponent c) {
                if (c == jSplitPane1) {
                    return DnDConstants.ACTION_COPY_OR_MOVE;
                }
                return DnDConstants.ACTION_NONE;
            }

            @Override
            public boolean canImport(TransferSupport support) {
                DataFlavor[] flavs = support.getDataFlavors();
                for (DataFlavor df : flavs) {
                    if (df.equals(JPDataFlavors.NETBEANS_NODE_FLAVOR)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean importData(TransferSupport e) {
                try {
                    Transferable tr = e.getTransferable();
                    DataFlavor[] flavors = tr.getTransferDataFlavors();
                    for (int i = 0; i < flavors.length; ++i) {
                        if (flavors[i].equals(JPDataFlavors.NETBEANS_NODE_FLAVOR)) {
                            DataNode dn = (DataNode) tr.getTransferData(flavors[i]);
                            if (dn instanceof ConnectionDataNode) {
                                setComboBoxItemForNode(jComboBox1, dn);
                            } else {
                                OpenCookie oc = dn.getLookup().lookup(OpenCookie.class);
                                if (oc != null) {
                                    oc.open();
                                }
                            }
                        }
                    }
                } catch (Throwable t) {
                    log.debug("D&D problem!", t);
                }
                // Ein Problem ist aufgetreten
                return false;
            }
        };
        jSplitPane1.setTransferHandler(th);
    }

    private void initComboBox(final SQLDataObject sqldata) {
        final boolean listenerState = isListenerActive();
        setListenerActive(false);
        ConnectionListModelProvider connectionModelProv = getProject().getLookup().lookup(ConnectionListModelProvider.class);
        if (connectionModelProv != null) {
            NodeListModel model = connectionModelProv.getConnectionListModel();
            if (model != null) {
                model.addListDataListener(this);
                jComboBox1.setModel(model);
            }
        }
        try {
            if (!setComboBoxItemForFileName(jComboBox1, sqldata.getData().getConnectionFile())) {
                String message = ("Can not find this SQL script's specified connection " + sqldata.getData().getConnectionFile() + ". No connection selected.");
                log.error(message);
                NotifyDescriptor err = new NotifyDescriptor.Message(message);
                DialogDisplayer.getDefault().notify(err);
            }
        } catch (NullPointerException e) {
            String message = ("Can not find this SQL script's specified connection as it is == null. No connection selected.");
            log.error(message);
            NotifyDescriptor err = new NotifyDescriptor.Message(message);
            DialogDisplayer.getDefault().notify(err);
        }
        setListenerActive(listenerState);
    }

    public boolean updateDataObject() {
        SQLDataObject sqlData = getData();
        if (sqlData != null) {
            sqlData.setData(se.getContent());
            ConnectionDataObject cC = getCurrentConnection();
            if (cC != null && cC.getConnectionFile() != null) {
                DatabaseConnection c = cC.getData();
                sqlData.getData().setConnectionFile(cC.getConnectionFile().getNameExt());
                sqlData.getData().setConnection(c);
            } else {
                sqlData.getData().setConnectionFile("");
                sqlData.getData().setConnection(new DatabaseConnection());
            }
        } else {
            log.error("Can not save: DataObject not existent!");
            return false;
        }
        return true;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = OutputTabbedPaneFactory.createOutputTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton3 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jSplitPane1.setDividerLocation(500);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setRightComponent(jTabbedPane1);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/search.png"))); // NOI18N
        jButton1.setToolTipText("Test with automatic Rollback");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/agt_runit.png"))); // NOI18N
        jButton2.setToolTipText("Execute");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);
        jToolBar1.add(jSeparator1);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, "  Target Connection  ");
        jToolBar1.add(jLabel1);

        jPanel2.setMaximumSize(new java.awt.Dimension(210, 30));
        jPanel2.setMinimumSize(new java.awt.Dimension(210, 30));
        jPanel2.setPreferredSize(new java.awt.Dimension(210, 30));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 5));

        jComboBox1.setMinimumSize(new java.awt.Dimension(200, 20));
        jComboBox1.setPreferredSize(new java.awt.Dimension(200, 20));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jPanel2.add(jComboBox1);

        jToolBar1.add(jPanel2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/edit.png"))); // NOI18N
        jButton3.setToolTipText("Edit");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jPanel1.add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jPanel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel8MouseClicked(evt);
            }
        });
        jPanel8.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(jPanel3);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        startScript(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        startScript(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        openConnectionEditor();
    }//GEN-LAST:event_jButton3ActionPerformed
    private void openConnectionEditor() {
        if (getCurrentConnection() != null) {
            OpenCookie oc = getCurrentConnection().getCookie(OpenCookie.class);
            if (oc != null) {
                oc.open();
            }
        }
    }
    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            DatabaseConnection c = null;
            if (isListenerActive()) {
                getData().setModified(true);
            }
            ConnectionDataObject cC = getDataFromComboBox(jComboBox1, ConnectionDataObject.class);
            setCurrentConnection(cC);
            if (cC != null) {
                c = cC.getData();
            }
            if (c != null) {
                se.setDatabaseConnection(c);
            } else {
                se.setDatabaseConnection(new DatabaseConnection());
            }
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

private void jPanel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8MouseClicked
    if (evt.isControlDown()) {
        openConnectionEditor();
    }
}//GEN-LAST:event_jPanel8MouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
        super.componentClosed();
        jTabbedPane1.removeAll();
//        if (getData() != null) {
//            getData().setTopComponent(null);
//        }
    }

    @Override
    public void owningProjectChanged() {
        setListenerActive(false);
        se.setDatabaseConnection(new DatabaseConnection());
        initComboBox(getData());
        setListenerActive(true);
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    public ConnectionDataObject getCurrentConnection() {
        return currentConnection;
    }

    public void setCurrentConnection(ConnectionDataObject currentConnection) {
        if (currentConnection == this.currentConnection) {
            return;
        }
        if (this.currentConnection != null) {
            this.currentConnection.removePropertyChangeListener(this);
        }
        if (currentConnection != null) {
            currentConnection.addPropertyChangeListener(this);
        }
        this.currentConnection = currentConnection;
    }

    final static class ResolvableHelper implements Serializable {
    }

    private boolean canExecute() {
        Confirmation msg = new NotifyDescriptor.Confirmation("You are trying to execute " + getData().getPrimaryFile().getNameExt() + ".\n" +
                "There are changes that have to be saved before.\n" +
                "Save file and continue?");
        Object result = DialogDisplayer.getDefault().notify(msg);
        if (result.equals(NotifyDescriptor.YES_OPTION)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addOutput(final JPanel out) {
        final Calendar now = Calendar.getInstance();
        final DateFormat format = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.getDefault());

        jTabbedPane1.addTab("SQL Script Output @ " + format.format(now.getTime()) + "    ", out);
        jTabbedPane1.setSelectedIndex(jTabbedPane1.getTabCount() - 1);
        if (!isOpened()) {
            SQLTopComponent.this.open();
        }
        SQLTopComponent.this.requestActive();
        SQLTopComponent.this.requestFocusInWindow(true);

    }

    private void startScript(final boolean test) {
        final SQLDataObject sqlData = getData();
        boolean start = true;
        if (sqlData.isModified()) {
            try {
                start = canExecute();
                if (start) {
                    sqlData.save();
                }
            } catch (IOException ex) {
                //TODO
                log.error("IOException when trying to start Script " + sqlData.getPrimaryFile().getPath(), ex);
            }
        }
        if (start) {
            SQLRun tmp = getData().getData();
            try {
                ClassResourceProvider clp = getProject().getLookup().lookup(ClassResourceProvider.class);
                String projDir = FileUtil.toFile(sqlData.getPrimaryFile().getParent().getParent()).getAbsolutePath();
                log.debug("Instantiating SQLScriptExecutor with SQLRunfile " + tmp + " and Project Directory = " + projDir);
                final SQLScriptExecutionController exec = ControllerFactory.createSQLScriptExecutionController(tmp, clp);
                SQLWorker sw = new SQLWorker(exec, test);
                ExecutorProvider.execute(sw);
//                sw.execute();
//                RequestProcessor.getDefault().post(new Runnable() {
//
//                    public void run() {
//                        try {
//                            exec.setTest(test);
//                            exec.execute(new ProgressHandler());
//                            EventQueue.invokeLater(new Runnable() {
//
//                                public void run() {
//                                    SQLTopComponent.this.addOutput(new OutputSQL(exec.getLogs()));
//                                }
//                            });
//                        } catch (final InitializingException ex) {
//                            final String message = "Error while executing SQL script " + sqlData.getPrimaryFile().getNameExt() + "\n\n" + ex.toString();
//                            log.error(message, ex);
//                            EventQueue.invokeLater(new Runnable() {
//
//                                public void run() {
//                                    NotifyDescriptor msg = new NotifyDescriptor.Exception(ex, message);
//                                    DialogDisplayer.getDefault().notify(msg);
//                                }
//                            });
//                        }
//                    }
//                });
            } catch (InitializingException ex) {
                String message = "Error: The referenced connection file " + tmp.getConnectionFile() + " can not be found!";
                log.warn(message, ex);
                NotifyDescriptor msg = new NotifyDescriptor.Exception(ex, message);
                DialogDisplayer.getDefault().notify(msg);
            }
        }
    }

    public void intervalAdded(ListDataEvent e) {
    }

    public void intervalRemoved(ListDataEvent e) {
        if (jComboBox1.getSelectedIndex() < 0) {
            jComboBox1.setSelectedItem(null);
        }
    }

    public void contentsChanged(ListDataEvent e) {
    }

    @Override
    protected void removeAllListenerOnClosed() {
//        jComboBox1.getModel().removeListDataListener(this);
//        if (getCurrentConnection() != null) {
//            getCurrentConnection().removeConnectionDataListener(this);
//        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        if (evt.getPropertyName().equals(ConnectionDataObject.class.getCanonicalName())) {
            if (getCurrentConnection() != null && getCurrentConnection().getData() != null) {
                se.setDatabaseConnection(getCurrentConnection().getData());
            } else {
                se.setDatabaseConnection(new DatabaseConnection());
            }
        }
    }

    class SQLWorker extends SwingWorker<Long, Void> implements Cancellable {

        public SQLWorker(SQLScriptExecutionController exec, boolean test) {
            if (exec == null) {
                throw new NullPointerException();
            }
            this.test = test;
            this.exec = exec;
        }
        private final boolean test;
        private final SQLScriptExecutionController exec;

        @Override
        protected Long doInBackground() throws Exception {
            exec.setTest(test);
            return exec.execute(new ProgressHandler(this));
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (InterruptedException ex) {
                log.warn("Interrupted Exception", ex);
            } catch (ExecutionException ex) {
                Throwable t = ex.getCause();
                final String message = "Error while executing SQL script " + getData().getPrimaryFile().getNameExt() + "\n\n" + t.toString();
                log.error(message, t);
                NotifyDescriptor msg = new NotifyDescriptor.Exception(t, message);
                DialogDisplayer.getDefault().notify(msg);
            }
            if (exec.getLogs() != null) {
                SQLTopComponent.this.addOutput(new OutputSQL(exec.getLogs()));
            }
        }

        public boolean cancel() {
            exec.setCanceled(true);
            return true;
        }
    }
}
