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

import java.util.List;
import java.util.Properties;

import javax.swing.text.Document;

import de.cismet.jpresso.core.data.DatabaseConnection;
import de.cismet.jpresso.core.data.SQLRun;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @author   hell
 * @version  $Revision$, $Date$
 */
public final class SQLEditor extends TopComponentFinderPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final String SPLIT_PATTERN = ";[\\s]*\n";

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private SQLRun run;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JScrollPane scpSourceParam;
    private javax.swing.JEditorPane txtSQLEditor;
    private javax.swing.JTextField txtSourceDriver;
    private javax.swing.JTextArea txtSourceParam;
    private javax.swing.JTextField txtSourceUrl;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form ConnectionEditor.
     */
    public SQLEditor() {
        this(null);
        myInit();
    }

    /**
     * Creates a new SQLEditor object.
     *
     * @param  s  DOCUMENT ME!
     */
    public SQLEditor(final SQLRun s) {
        this.run = s;
        initComponents();
        myInit();
        setContent(this.run);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    public void myInit() {
        txtSourceParam.setEditable(false);
        txtSourceDriver.setEnabled(false);
        txtSourceUrl.setEnabled(false);
        txtSourceParam.setEnabled(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public SQLRun getContent() {
        final SQLRun newrun = new SQLRun();
        final List<String> stmnt = TypeSafeCollections.newArrayList();
        final String[] token = txtSQLEditor.getText().trim().split(SPLIT_PATTERN);
        String toAdd = null;
        for (final String s : token) {
            toAdd = s.trim();
            if (toAdd.length() > 1) {
                stmnt.add(toAdd);
            }
        }
        if ((toAdd != null) && (toAdd.length() > 2) && toAdd.endsWith(";")) {
            stmnt.set(stmnt.size() - 1, toAdd.substring(0, toAdd.length() - 1));
        }
        newrun.setScript(stmnt);
        if (log.isDebugEnabled()) {
            log.debug("SQL Content:");
        }
        for (final String st : stmnt) {
            if (log.isDebugEnabled()) {
                log.debug("SCRIPT: " + st);
            }
        }
        return newrun;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  run  DOCUMENT ME!
     */
    public void setContent(final SQLRun run) {
        if ((run != null) && (run.getConnection() != null)) {
            final DatabaseConnection source = run.getConnection();
            txtSourceDriver.setText(source.getDriverClass());
            txtSourceUrl.setText(source.getUrl());
            final StringBuilder sb = new StringBuilder();

            for (final String str : run.getScript()) {
                sb.append(str);
                sb.append(";\n");
            }
            txtSQLEditor.setText(sb.toString().trim());
            // Fehlerquelle?
            String sParams = "";
            final Properties p = source.getProps();
            for (final String txt : p.stringPropertyNames()) {
                sParams = new StringBuffer(sParams).append(txt).append(":=").append(p.getProperty(txt)).append("\n")
                            .toString();
            }
//            while (sProp.hasMoreElements()) {
//                Prop p = (Prop) sProp.nextElement();
//                sParams = sParams + p.getKey() + ":=" + p.getContent() + "\n";
//            }
            txtSourceParam.setText(sParams.trim());
        } else {
            setRun(new SQLRun());
            txtSourceDriver.setText("");
            txtSourceUrl.setText("");
            txtSourceParam.setText("");
        }
        // }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        scpSourceParam = new javax.swing.JScrollPane();
        txtSourceParam = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        txtSourceUrl = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        txtSourceDriver = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtSQLEditor = new javax.swing.JEditorPane();
        txtSQLEditor.setContentType("text/x-sql");

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new java.awt.BorderLayout());

        pnlMain.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    null,
                    "SQLRun Editor",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 0, 12),
                    javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnlMain.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridBagLayout());

        scpSourceParam.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    null,
                    "Additional Parameter",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 0, 12),
                    javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")),
                javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5),
                    javax.swing.BorderFactory.createEtchedBorder())));

        txtSourceParam.setBackground(java.awt.SystemColor.control);
        scpSourceParam.setViewportView(txtSourceParam);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(scpSourceParam, gridBagConstraints);

        jPanel4.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    null,
                    "URL",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 0, 12),
                    javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        jPanel4.setLayout(new java.awt.BorderLayout());

        txtSourceUrl.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtSourceUrl.setEnabled(false);
        jPanel4.add(txtSourceUrl, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel4, gridBagConstraints);

        jPanel5.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    null,
                    "Driver",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 0, 12),
                    javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        jPanel5.setLayout(new java.awt.BorderLayout());

        txtSourceDriver.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtSourceDriver.setEnabled(false);
        txtSourceDriver.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    txtSourceDriverActionPerformed(evt);
                }
            });
        jPanel5.add(txtSourceDriver, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel5, gridBagConstraints);

        pnlMain.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    null,
                    "SQL Editor",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 0, 12),
                    javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane1.setViewportView(txtSQLEditor);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jPanel1, gridBagConstraints);

        pnlMain.add(jPanel3, java.awt.BorderLayout.CENTER);

        add(pnlMain, java.awt.BorderLayout.CENTER);
    } // </editor-fold>//GEN-END:initComponents
    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void txtSourceDriverActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_txtSourceDriverActionPerformed
        // TODO add your handling code here:
    } //GEN-LAST:event_txtSourceDriverActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Document getStatementDocument() {
        return txtSQLEditor.getDocument();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  dc  DOCUMENT ME!
     */
    public void setDatabaseConnection(final DatabaseConnection dc) {
        DatabaseConnection con = dc;
        if (con == null) {
            con = new DatabaseConnection();
        }
        txtSourceDriver.setText(con.getDriverClass());
        txtSourceUrl.setText(con.getUrl());
        final Properties p = con.getProps();
        String sParams = "";
        for (final String txt : p.stringPropertyNames()) {
            sParams = new StringBuffer(sParams).append(txt).append(":=").append(p.getProperty(txt)).append("\n")
                        .toString();
        }
        txtSourceParam.setText(sParams);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public SQLRun getRun() {
        return run;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  run  DOCUMENT ME!
     */
    public void setRun(final SQLRun run) {
        this.run = run;
    }
}
