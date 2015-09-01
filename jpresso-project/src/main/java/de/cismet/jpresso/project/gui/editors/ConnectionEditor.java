/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ConnectionEditor.java
 *
 * Created on 12. November 2003, 15:39
 */
package de.cismet.jpresso.project.gui.editors;

import org.openide.util.Exceptions;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.text.Document;

import de.cismet.jpresso.core.data.DatabaseConnection;
import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.core.serviceprovider.ClassResourceProvider;
import de.cismet.jpresso.core.serviceprovider.DynamicDriverManager;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

import de.cismet.jpresso.project.filetypes.DefaultURLProvider;
import de.cismet.jpresso.project.gui.AbstractJPTopComponent;
import de.cismet.jpresso.project.gui.output.*;
import de.cismet.jpresso.project.serviceprovider.ExecutorProvider;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class ConnectionEditor extends TopComponentFinderPanel {

    //~ Instance fields --------------------------------------------------------

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JScrollPane scpTargetParam;
    private javax.swing.JTextField txtTargetDriver;
    private javax.swing.JTextArea txtTargetParam;
    private javax.swing.JTextField txtTargetUrl;

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private DatabaseConnection tgt;
    private DriverDescription lastChosenDD;

    /**
     * Creates new form ConnectionEditor.
     */
    public ConnectionEditor() {
        this(null);
    }

    /**
     * Creates a new ConnectionEditor object.
     *
     * @param  t  DOCUMENT ME!
     */
    public ConnectionEditor(final DatabaseConnection t) {
        this.tgt = t;
        initComponents();
        setContent(t);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public DatabaseConnection getTargetConContent() {
        final DatabaseConnection target = new DatabaseConnection();
        target.setDriverClass(txtTargetDriver.getText().trim());
        target.setUrl(txtTargetUrl.getText().trim());

        final String tParam = txtTargetParam.getText().trim();
        final String[] tp = tParam.split("\n");
        final Properties p = new Properties();
        for (int i = 0; i < tp.length; ++i) {
            final String[] keyValue = tp[i].split(":=");
            if (keyValue.length != 2) {
                continue;
            }
            p.setProperty(keyValue[0], keyValue[1]);
        }
        target.setProps(p);
        return target;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public DatabaseConnection getContent() {
        final DatabaseConnection target = new DatabaseConnection();

        target.setDriverClass(txtTargetDriver.getText().trim());
        target.setUrl(txtTargetUrl.getText().trim());

        final String tParam = txtTargetParam.getText().trim();
        final Properties p = new Properties();
        final String[] tp = tParam.split("\n");
        for (final String s : tp) {
            final String[] keyValue = s.split(":=");
            if (keyValue.length != 2) {
                continue;
            }
            p.put(keyValue[0], keyValue[1]);
        }
        target.setProps(p);
        return target;
    }

    /**
     * DOCUMENT ME!
     */
    private void clearUndoRedo() {
        UndoRedoSupport.discardAllEdits(txtTargetUrl);
        UndoRedoSupport.discardAllEdits(txtTargetDriver);
        UndoRedoSupport.discardAllEdits(txtTargetParam);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  t  DOCUMENT ME!
     */
    public void setContent(final DatabaseConnection t) {
        if (t == null) {
            setTgt(new DatabaseConnection());
        }
        final DatabaseConnection target = t;
        if (target != null) {
            txtTargetDriver.setText(target.getDriverClass());
            txtTargetUrl.setText(target.getUrl());
//            Enumeration tProp = target.enumerateProp();
//            String tParams = "";
//            while (tProp.hasMoreElements()) {
//                Prop p = (Prop) tProp.nextElement();
//                tParams = tParams + p.getKey() + ":=" + p.getContent() + "\n";
//            }
            String tParams = "";
            final Properties p = target.getProps();
            for (final String txt : p.stringPropertyNames()) {
                tParams = new StringBuffer(tParams).append(txt).append(":=").append(p.getProperty(txt)).append("\n")
                            .toString();
            }
            txtTargetParam.setText(tParams.trim());
        } else {
            txtTargetDriver.setText("");
            txtTargetUrl.setText("");
            txtTargetParam.setText("");
        }
        clearUndoRedo();
        // }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  b  DOCUMENT ME!
     */
    public void setEditable(final boolean b) {
        txtTargetDriver.setEditable(b);
        txtTargetParam.setEditable(b);
        txtTargetParam.setEnabled(b);
        txtTargetUrl.setEditable(b);
        jButton1.setVisible(b);
        jButton2.setVisible(b);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        txtTargetDriver = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        txtTargetUrl = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        scpTargetParam = new javax.swing.JScrollPane();
        txtTargetParam = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        pnlMain.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    null,
                    "Connection Editor",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 0, 12),
                    javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        pnlMain.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    null,
                    "Driver",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 0, 12),
                    javax.swing.UIManager.getDefaults().getColor("ComboBox.selectionBackground")),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jButton1.setText("...");
        jButton1.setMaximumSize(new java.awt.Dimension(35, 18));
        jButton1.setMinimumSize(new java.awt.Dimension(35, 18));
        jButton1.setPreferredSize(new java.awt.Dimension(35, 18));
        jButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel1.add(jButton1, gridBagConstraints);

        jPanel5.setLayout(new java.awt.BorderLayout());

        UndoRedoSupport.decorate(txtTargetDriver);
        txtTargetDriver.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.add(txtTargetDriver, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jPanel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 0.75;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlMain.add(jPanel1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    null,
                    "URL",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 0, 12),
                    javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        jPanel2.setLayout(new java.awt.GridBagLayout());

        UndoRedoSupport.decorate(txtTargetUrl);
        txtTargetUrl.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(txtTargetUrl, gridBagConstraints);

        jButton2.setText("...");
        jButton2.setMaximumSize(new java.awt.Dimension(35, 18));
        jButton2.setMinimumSize(new java.awt.Dimension(35, 18));
        jButton2.setPreferredSize(new java.awt.Dimension(35, 18));
        jButton2.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });
        jPanel2.add(jButton2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlMain.add(jPanel2, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    null,
                    "Additional Parameter",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 0, 12),
                    javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        jPanel3.setMinimumSize(new java.awt.Dimension(275, 65));
        jPanel3.setPreferredSize(new java.awt.Dimension(275, 65));
        jPanel3.setLayout(new java.awt.BorderLayout());

        scpTargetParam.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scpTargetParam.setMinimumSize(new java.awt.Dimension(35, 35));
        scpTargetParam.setPreferredSize(new java.awt.Dimension(35, 35));

        UndoRedoSupport.decorate(txtTargetParam);
        scpTargetParam.setViewportView(txtTargetParam);

        jPanel3.add(scpTargetParam, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlMain.add(jPanel3, gridBagConstraints);

        jPanel4.setMinimumSize(new java.awt.Dimension(35, 10));
        jPanel4.setPreferredSize(new java.awt.Dimension(35, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.77;
        pnlMain.add(jPanel4, gridBagConstraints);

        add(pnlMain, java.awt.BorderLayout.CENTER);
    } // </editor-fold>//GEN-END:initComponents

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton1ActionPerformed
        final Enumeration<Driver> dmDrv = DriverManager.getDrivers();
        final DynamicDriverManager man = findTopComponent().getProject()
                    .getLookup()
                    .lookup(ClassResourceProvider.class)
                    .getDriverManager();
        final List<String> aliases = TypeSafeCollections.newArrayList();
        while (dmDrv.hasMoreElements()) {
            aliases.add(dmDrv.nextElement().getClass().getCanonicalName());
        }
        for (final DriverDescription dd : man.getValidDrivers()) {
            aliases.add(dd.getName());
        }
        final Object[] possibilities = aliases.toArray();

        Object selection = null;
        if (possibilities.length > 1) {
            selection = possibilities[0];
        }
        final Object res = JOptionPane.showInputDialog(
                this,
                "Please select a driver:",
                "Known Drivers",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                selection);
        if (res != null) {
            if (res instanceof DriverDescription) {
                final DriverDescription dd = (DriverDescription)res;
                txtTargetDriver.setText(dd.getName());
                if (txtTargetUrl.getText().length() == 0) {
                    txtTargetUrl.setText(dd.getUrlFormat());
                }
                lastChosenDD = dd;
            } else {
                txtTargetDriver.setText(res.toString());
                lastChosenDD = null;
            }
        }
    } //GEN-LAST:event_jButton1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton2ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton2ActionPerformed
        if ((lastChosenDD != null) && (lastChosenDD.getUrlFormat() != null)) {
            txtTargetUrl.setText(lastChosenDD.getUrlFormat());
        } else {
            final String defURL = DefaultURLProvider.getDefaultURL(txtTargetDriver.getText());
            if (defURL != null) {
                txtTargetUrl.setText(defURL);
            }
        }
    }                                                                            //GEN-LAST:event_jButton2ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  checkOnly  DOCUMENT ME!
     */
    public void checkTargetConnection(final boolean checkOnly) {
        final ConnectionWorker cw = new ConnectionWorker(checkOnly);
        final AbstractJPTopComponent tc = findTopComponent();
        if (tc != null) {
            tc.startWait("Checking Connection...");
        }
        ExecutorProvider.execute(cw);
    }
    /**
     * End of variables declaration//GEN-END:variables.
     *
     * @return  DOCUMENT ME!
     */
    public DatabaseConnection getTgt() {
        return tgt;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  tgt  DOCUMENT ME!
     */
    public void setTgt(final DatabaseConnection tgt) {
        this.tgt = tgt;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Document getDriverDocument() {
        return this.txtTargetDriver.getDocument();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Document getParamDocument() {
        return this.txtTargetParam.getDocument();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Document getURLDocument() {
        return this.txtTargetUrl.getDocument();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Document[] getDocuments() {
        return new Document[] { getDriverDocument(), getParamDocument(), getURLDocument() };
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Worker that creates an Connection.
     *
     * @version  $Revision$, $Date$
     */
    class ConnectionWorker extends SwingWorker<Connection, Void> {

        //~ Instance fields ----------------------------------------------------

        private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
        private boolean checkOnly;
        private final AbstractJPTopComponent tc;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ConnectionWorker object.
         *
         * @param  checkOnly  DOCUMENT ME!
         */
        public ConnectionWorker(final boolean checkOnly) {
            this.checkOnly = checkOnly;
            this.tc = findTopComponent();
        }

        //~ Methods ------------------------------------------------------------

        @Override
        protected Connection doInBackground() throws Exception {
            final String driver = txtTargetDriver.getText();
            final String url = txtTargetUrl.getText();
            final String props = txtTargetParam.getText();
            final Properties pro = new Properties();

            final String[] sp = props.split("\n");
            for (int i = 0; i < sp.length; ++i) {
                final String[] keyValue = sp[i].split(":=");
                if (keyValue.length != 2) {
                    continue;
                }
                pro.setProperty(keyValue[0], keyValue[1]);
            }
            final ClassResourceProvider clp = tc.getProject().getLookup().lookup(ClassResourceProvider.class);
            final Connection conn = clp.getDriverManager().getConnection(driver, url, pro);
            return conn;
        }

        @Override
        protected void done() {
            Connection conn = null;
            try {
                conn = get();
                if (conn != null) {
                    if (!checkOnly) {
                        if (tc != null) {
                            tc.addOutput(new OutputDatabaseConnection(conn));
                        }
                    } else {
                        final String msg = "Connection to database succesfully established.";
                        JOptionPane.showMessageDialog(
                            ConnectionEditor.this,
                            msg,
                            "Check successful",
                            JOptionPane.INFORMATION_MESSAGE);
                        if (log.isDebugEnabled()) {
                            log.debug(msg);
                        }
                    }
                }
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            } catch (ExecutionException ex) {
                final Throwable e = ex.getCause();
                final String msg = "Could not connect to database.\n(" + e.getMessage() + ")";
                JOptionPane.showMessageDialog(ConnectionEditor.this, msg, "Fehler", JOptionPane.ERROR_MESSAGE);
                log.error(msg, e);
            } finally {
                try {
                    if ((conn != null) && !conn.isClosed()) {
                        log.info("Target " + conn + " CLOSED");
                        conn.close();
                    }
                } catch (SQLException ex) {
                    log.error("Can not close open Connection", ex);
                }
                if (tc != null) {
                    tc.stopWait();
                }
            }
        }
    }
}
