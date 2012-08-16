/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.query;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.cookies.OpenCookie;
import org.openide.explorer.view.NodeListModel;
import org.openide.loaders.DataNode;
import org.openide.util.NbBundle;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.beans.PropertyChangeEvent;

import java.io.Serializable;

import java.text.DateFormat;

import java.util.Calendar;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import de.cismet.jpresso.core.data.DatabaseConnection;

import de.cismet.jpresso.project.filetypes.connection.ConnectionDataNode;
import de.cismet.jpresso.project.filetypes.connection.ConnectionDataObject;
import de.cismet.jpresso.project.filetypes.cookies.ConnectionListModelProvider;
import de.cismet.jpresso.project.filetypes.query.QueryDataObject;
import de.cismet.jpresso.project.gui.AbstractJPTopComponent;
import de.cismet.jpresso.project.gui.dnd.JPDataFlavors;
import de.cismet.jpresso.project.gui.editors.QueryEditor;
import de.cismet.jpresso.project.gui.output.OutputTabbedPaneFactory;

/**
 * Top component to visualize a query.
 *
 * @version  $Revision$, $Date$
 */
public final class QueryTopComponent extends AbstractJPTopComponent<QueryDataObject> implements ListDataListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final String PREFERRED_ID = "QueryTopComponent";

    //~ Instance fields --------------------------------------------------------

    private QueryEditor qe;
    private ConnectionDataObject currentConnection;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new QueryTopComponent object.
     *
     * @param  data  DOCUMENT ME!
     */
    public QueryTopComponent(final QueryDataObject data) {
        super(data);
        setListenerActive(false);
        initComponents();
        qe = new QueryEditor(data.getData());
        initComboBox(data);

        jPanel2.add(qe, BorderLayout.CENTER);

        setName(NbBundle.getMessage(QueryTopComponent.class, "CTL_QueryTopComponent"));
        setToolTipText(NbBundle.getMessage(QueryTopComponent.class, "HINT_QueryTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));
        qe.getStatementDocument().addDocumentListener(this);

        // jalopy doesn't like these inline listeners

        //J-
        qe.getMaxRowSpinner().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                getData().setModified(true);
            }
        });
        qe.getLabelCaseComboBox().addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                getData().setModified(true);
            }
        });
        //J+
        getData().setModified(false);
        setListenerActive(true);
        final TransferHandler th = new TransferHandler() {

                @Override
                public int getSourceActions(final JComponent c) {
                    if (c == jSplitPane1) {
                        return DnDConstants.ACTION_COPY_OR_MOVE;
                    }
                    return DnDConstants.ACTION_NONE;
                }

                @Override
                public boolean canImport(final TransferSupport support) {
                    final DataFlavor[] flavs = support.getDataFlavors();
                    for (final DataFlavor df : flavs) {
                        if (df.equals(JPDataFlavors.NETBEANS_NODE_FLAVOR)) {
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
                            if (flavors[i].equals(JPDataFlavors.NETBEANS_NODE_FLAVOR)) {
                                final DataNode dn = (DataNode)tr.getTransferData(flavors[i]);
                                if (dn instanceof ConnectionDataNode) {
                                    setComboBoxItemForNode(jComboBox1, dn);
                                } else {
                                    final OpenCookie oc = dn.getLookup().lookup(OpenCookie.class);
                                    if (oc != null) {
                                        oc.open();
                                    }
                                }
                            }
                        }
                    } catch (Throwable t) {
                        if (log.isDebugEnabled()) {
                            log.debug("D&D problem!", t);
                        }
                    }
                    // Ein Problem ist aufgetreten
                    return false;
                }
            };
        jSplitPane1.setTransferHandler(th);
        // because combobox selection above sets it true
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  data  DOCUMENT ME!
     */
    private void initComboBox(final QueryDataObject data) {
        final boolean listenerState = isListenerActive();
        setListenerActive(false);
        final ConnectionListModelProvider connectionModelProv = getProject().getLookup()
                    .lookup(ConnectionListModelProvider.class);
        if ((connectionModelProv != null) && (connectionModelProv.getConnectionListModel() != null)) {
            final NodeListModel model = connectionModelProv.getConnectionListModel();
            model.addListDataListener(this);
            jComboBox1.setModel(model);
        }
        try {
            if (!setComboBoxItemForFileName(jComboBox1, data.getData().getConnectionFile())) {
                final String message = ("Can not find this query's specified connection "
                                + data.getData().getConnectionFile() + ". No connection selected.");
                log.error(message);
                final NotifyDescriptor err = new NotifyDescriptor.Message(message);
                DialogDisplayer.getDefault().notify(err);
            }
        } catch (NullPointerException e) {
            final String message =
                ("Can not find this query's specified connection as it is == null. No connection selected.");
            log.error(message);
            final NotifyDescriptor err = new NotifyDescriptor.Message(message);
            DialogDisplayer.getDefault().notify(err);
        }
        setListenerActive(listenerState);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = OutputTabbedPaneFactory.createOutputTabbedPane();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jSplitPane1.setDividerLocation(600);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(final java.awt.event.MouseEvent evt) {
                    jPanel2MouseClicked(evt);
                }
            });
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.GridBagLayout());
        jPanel2.add(jPanel4, java.awt.BorderLayout.SOUTH);

        jSplitPane1.setTopComponent(jPanel2);

        jPanel3.setLayout(new java.awt.BorderLayout());
        jPanel3.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel3);

        jPanel1.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jToolBar1.setFloatable(false);

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, "  Connection:  ");
        jToolBar1.add(jLabel1);

        jPanel8.setMaximumSize(new java.awt.Dimension(210, 30));
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jComboBox1.setMinimumSize(new java.awt.Dimension(200, 20));
        jComboBox1.setPreferredSize(new java.awt.Dimension(200, 20));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(final java.awt.event.ItemEvent evt) {
                    jComboBox1ItemStateChanged(evt);
                }
            });
        jPanel8.add(jComboBox1);

        jToolBar1.add(jPanel8);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/edit.png"))); // NOI18N
        jButton2.setToolTipText("Edit");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });
        jToolBar1.add(jButton2);

        jButton1.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/jpresso/project/res/search.png"))); // NOI18N
        jButton1.setToolTipText("Preview Query");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
        jToolBar1.add(jButton1);

        jPanel1.add(jToolBar1, java.awt.BorderLayout.NORTH);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton1ActionPerformed
        qe.checkSourceConnection();
    }                                                                            //GEN-LAST:event_jButton1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton2ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton2ActionPerformed
        openConnectionEditor();
    }                                                                            //GEN-LAST:event_jButton2ActionPerformed

    /**
     * DOCUMENT ME!
     */
    private void openConnectionEditor() {
        if (getCurrentConnection() != null) {
            final OpenCookie oc = getCurrentConnection().getCookie(OpenCookie.class);
            if (oc != null) {
                oc.open();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jComboBox1ItemStateChanged(final java.awt.event.ItemEvent evt) { //GEN-FIRST:event_jComboBox1ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            DatabaseConnection c = null;
            if (isListenerActive()) {
                getData().setModified(true);
            }
            final ConnectionDataObject cC = getDataFromComboBox(jComboBox1, ConnectionDataObject.class);
            if (cC != null) {
                setCurrentConnection(cC);
                c = cC.getData();
            }
            qe.setDatabaseConnection(c);
        }
    }                                                                             //GEN-LAST:event_jComboBox1ItemStateChanged

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jPanel2MouseClicked(final java.awt.event.MouseEvent evt) { //GEN-FIRST:event_jPanel2MouseClicked
        if (evt.isControlDown()) {
            openConnectionEditor();
        }
    }                                                                       //GEN-LAST:event_jPanel2MouseClicked
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only, i.e. deserialization routines;
     * otherwise you could get a non-deserialized instance. To obtain the singleton instance, use {@link findInstance}.
     */
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
        qe.setDatabaseConnection(new DatabaseConnection());
        initComboBox(getData());
        setListenerActive(true);
    }

    /**
     * replaces this in object stream.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ConnectionDataObject getCurrentConnection() {
        return currentConnection;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  currentConnection  DOCUMENT ME!
     */
    public void setCurrentConnection(final ConnectionDataObject currentConnection) {
        if (currentConnection == this.currentConnection) {
            return;
        }
        if (this.currentConnection != null) {
            this.currentConnection.removePropertyChangeListener(this);
        }
        if (currentConnection != null) {
            currentConnection.addPropertyChangeListener(this);
        }
        getData().setConData(currentConnection);
        this.currentConnection = currentConnection;
    }

    @Override
    public void addOutput(final JPanel out) {
        final Calendar now = Calendar.getInstance();
        final DateFormat format = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.getDefault());
        jTabbedPane1.addTab("Query Preview @ " + format.format(now.getTime()) + "    ", out);
        jTabbedPane1.setSelectedIndex(jTabbedPane1.getTabCount() - 1);
        if (!isOpened()) {
            QueryTopComponent.this.open();
        }
        requestActive();
        requestFocusInWindow(true);
    }

    // </editor-fold>
    @Override
    public boolean updateDataObject() {
        final QueryDataObject data = getData();
        if (data != null) {
            final ConnectionDataObject cC = getDataFromComboBox(jComboBox1, ConnectionDataObject.class);
            if ((cC != null) && (cC.getConnectionFile() != null)) {
//                final DatabaseConnection c = cC.getData();
                data.getData().setConnectionFile(cC.getConnectionFile().getNameExt());
            } else {
                data.getData().setConnectionFile("");
            }
            data.getData().setQueryStatement(qe.getStatement());
            data.getData().setPreviewMaxRows(qe.getPreviewMaxRows());
            data.getData().setLabelCase(qe.getLabelCaseComboBox().getSelectedItem().toString());
        } else {
            log.error("Can not update Data: DataObject not existent!");
            return false;
        }
        return true;
    }

    @Override
    public void intervalAdded(final ListDataEvent e) {
    }

    @Override
    public void intervalRemoved(final ListDataEvent e) {
        if (jComboBox1.getSelectedIndex() < 0) {
            jComboBox1.setSelectedItem(null);
        }
    }

    @Override
    public void contentsChanged(final ListDataEvent e) {
    }

    @Override
    protected void removeAllListenerOnClosed() {
//        jComboBox1.getModel().removeListDataListener(this);
//        if (getCurrentConnection() != null) {
//            getCurrentConnection().removeConnectionDataListener(this);
//        }
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        super.propertyChange(evt);
        if (evt.getPropertyName().equals(ConnectionDataObject.class.getCanonicalName())) {
            if ((getCurrentConnection() != null) && (getCurrentConnection().getData() != null)) {
                qe.setDatabaseConnection(getCurrentConnection().getData());
            } else {
                qe.setDatabaseConnection(new DatabaseConnection());
            }
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    static final class ResolvableHelper implements Serializable {

        //~ Static fields/initializers -----------------------------------------

        private static final long serialVersionUID = 1L;

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Object readResolve() {
            return null; // QueryTopComponent.getDefault();
        }
    }
}
