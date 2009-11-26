/*
 * ConnectionEditor.java
 *
 * Created on 12. November 2003, 15:39
 */
package de.cismet.jpresso.project.gui.editors;

import de.cismet.jpresso.project.gui.AbstractJPTopComponent;
import de.cismet.jpresso.project.gui.ProgressHandler;
import de.cismet.jpresso.project.gui.output.OutputQuery;
import de.cismet.jpresso.project.gui.run.RunTopComponent;
import de.cismet.jpresso.core.serviceprovider.ClassResourceProvider;
import de.cismet.jpresso.core.data.Query;
import de.cismet.jpresso.core.data.DatabaseConnection;
import de.cismet.jpresso.project.serviceprovider.ExecutorProvider;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.text.Document;
import org.openide.util.Cancellable;
import org.openide.util.Exceptions;

/**
 * @author  srichter
 * @author  hell
 */
public final class QueryEditor extends TopComponentFinderPanel {

    private static final String BACKSLASH_N = "\n";
    private static final String SPACEBACKSLASH_N = " " + BACKSLASH_N;
    private static final String DOUBLE_SPACE = "  ";
    private static final String DEFINED_AS = ":=";
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private Query qry;
    private String[] sourceFieldsForClipboard = null;
    //TODO private AbstractJPTopComponent tc .... ein einziges findTopComponent..oder besser noch das ding gleich mitgeben...

    /** Creates new form ConnectionEditor */
    public QueryEditor() {
        this(null);
    }

    public QueryEditor(Query s) {
        this.qry = s;
        initComponents();
        myInit();
        jSpinner1.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        setContent(s);

    }

    public void myInit() {
        txtSourceParam.setEditable(false);
        txtSourceParam.setEnabled(false);
        txtSourceDriver.setEditable(false);
        txtSourceUrl.setEditable(false);

    }

    public Query getContent() {
        final Query query = new Query();
        query.setDriverClass(txtSourceDriver.getText().trim());
        query.setUrl(txtSourceUrl.getText().trim());
        String stmnt = txtSourceStatement.getText().trim();
        stmnt = stmnt.replace(SPACEBACKSLASH_N, DOUBLE_SPACE);
        stmnt = stmnt.replace(BACKSLASH_N, DOUBLE_SPACE);
//        stmnt = stmnt.replaceAll(SPACEBACKSLASH_N, DOUBLE_SPACE);
//        stmnt = stmnt.replaceAll(BACKSLASH_N, DOUBLE_SPACE);
        query.setQueryStatement(stmnt);

        final String sParam = txtSourceParam.getText().trim();
        final String[] qp = sParam.split(BACKSLASH_N);
        final Properties p = new Properties();
        for (final String s : qp) {
            final String[] keyValue = s.split(DEFINED_AS);
            if (keyValue.length != 2) {
                continue;
            }
            p.put(keyValue[0], keyValue[1]);
        }
        query.setProps(p);
        query.setPreviewMaxRows((Integer) jSpinner1.getValue());
        query.setLabelCase(jComboBox1.getSelectedItem().toString());
        return query;
    }

//    public void setContent(ConnectionInfo ci) {
//        setContent(ci.getSourceJdbcConnectionInfo());
//    }
    public void setContent(final Query s) {
        if (s == null) {
            setSrc(new Query());
        }

        final Query source = s;
        if (source != null) {
            txtSourceDriver.setText(source.getDriverClass());
            txtSourceUrl.setText(source.getUrl());

            String stmnt = source.getQueryStatement();
            if (stmnt != null) {
                stmnt = stmnt.replaceAll(DOUBLE_SPACE, SPACEBACKSLASH_N);
            }
            txtSourceStatement.setText(stmnt);
            //Fehlerquelle?
            String sParams = "";
            final Properties p = source.getProps();
            for (final String txt : p.stringPropertyNames()) {
                sParams = new StringBuffer(sParams).append(txt).append(DEFINED_AS).append(p.getProperty(txt)).append(BACKSLASH_N).toString();
            }
//            while (sProp.hasMoreElements()) {
//                Prop p = (Prop) sProp.nextElement();
//                sParams = sParams + p.getKey() + ":=" + p.getContent() + "\n";
//            }
            txtSourceParam.setText(sParams.trim());
            jSpinner1.setValue(source.getPreviewMaxRows());
            if (s.labelsToLowerCase()) {
                jComboBox1.setSelectedItem(Query.LOWER_CASE);
            } else if (s.labelsToUpperCase()) {
                jComboBox1.setSelectedItem(Query.UPPER_CASE);
            } else {
                jComboBox1.setSelectedIndex(0);
            }
        } else {
            txtSourceDriver.setText("");
            txtSourceUrl.setText("");
            txtSourceStatement.setText("");
            txtSourceParam.setText("");
            jSpinner1.setValue(0);
            jComboBox1.setSelectedIndex(0);
        }
        //}
    }

    public String getStatement() {
        return txtSourceStatement.getText();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        txtSourceDriver = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        txtSourceUrl = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        scpSourceParam = new javax.swing.JScrollPane();
        txtSourceParam = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtSourceStatement = new javax.swing.JEditorPane();
        txtSourceStatement.setContentType("text/x-sql");
        //UndoRedoSupport.decorate(txtSourceStatement);
        jPanel6 = new javax.swing.JPanel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnColumnNamesToClip = new javax.swing.JButton();

        setOpaque(false);
        setLayout(new java.awt.BorderLayout());

        pnlMain.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Query Editor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        pnlMain.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Adjust Labelname Case", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.BorderLayout());

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", Query.LOWER_CASE, Query.UPPER_CASE }));
        jPanel1.add(jComboBox1, java.awt.BorderLayout.PAGE_END);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlMain.add(jPanel1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Driver", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        jPanel2.setLayout(new java.awt.BorderLayout());

        txtSourceDriver.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.add(txtSourceDriver, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlMain.add(jPanel2, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "URL", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), javax.swing.UIManager.getDefaults().getColor("ComboBox.selectionBackground")), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        jPanel3.setLayout(new java.awt.BorderLayout());

        txtSourceUrl.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.add(txtSourceUrl, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlMain.add(jPanel3, gridBagConstraints);

        jPanel4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Additional Parameters", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        jPanel4.setMinimumSize(new java.awt.Dimension(200, 64));
        jPanel4.setPreferredSize(new java.awt.Dimension(200, 64));
        jPanel4.setLayout(new java.awt.BorderLayout());

        scpSourceParam.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scpSourceParam.setPreferredSize(new java.awt.Dimension(25, 25));
        scpSourceParam.setViewportView(txtSourceParam);

        jPanel4.add(scpSourceParam, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlMain.add(jPanel4, gridBagConstraints);

        jPanel5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Statement", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        jPanel5.setMinimumSize(new java.awt.Dimension(132, 63));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane1.setViewportView(txtSourceStatement);

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.95;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlMain.add(jPanel5, gridBagConstraints);

        jPanel6.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Max.Preview Rows", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        jPanel6.setOpaque(false);
        jPanel6.setLayout(new java.awt.BorderLayout());

        jSpinner1.setFont(new java.awt.Font("Dialog", 0, 12));
        jSpinner1.setMinimumSize(new java.awt.Dimension(50, 20));
        jSpinner1.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel6.add(jSpinner1, java.awt.BorderLayout.CENTER);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setText("   (0 = no limit)");
        jPanel6.add(jLabel3, java.awt.BorderLayout.EAST);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlMain.add(jPanel6, gridBagConstraints);

        jPanel7.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Copy Columnlabels To Clipboard", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")), null)); // NOI18N

        btnColumnNamesToClip.setText("Copy");
        btnColumnNamesToClip.setEnabled(false);
        btnColumnNamesToClip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnColumnNamesToClipActionPerformed(evt);
            }
        });
        jPanel7.add(btnColumnNamesToClip);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlMain.add(jPanel7, gridBagConstraints);

        add(pnlMain, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnColumnNamesToClipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnColumnNamesToClipActionPerformed
        final String[] cols = sourceFieldsForClipboard;
        if (cols != null) {
            StringSelection transferable = new StringSelection(Arrays.deepToString(cols));
            final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(transferable, transferable);
        }

    }//GEN-LAST:event_btnColumnNamesToClipActionPerformed

    public final void checkSourceConnection() {
        try {
            jSpinner1.commitEdit();
        } catch (ParseException ex) {
            //ignore
        }
        checkSourceConnection(((Integer) jSpinner1.getValue()) + 1);
    }

    public final void checkSourceConnection(final int maxRows) {
        btnColumnNamesToClip.setEnabled(false);
        final QueryWorker qw = new QueryWorker(maxRows);
        ExecutorProvider.execute(qw);
//        qw.execute();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnColumnNamesToClip;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JScrollPane scpSourceParam;
    private javax.swing.JTextField txtSourceDriver;
    private javax.swing.JTextArea txtSourceParam;
    private javax.swing.JEditorPane txtSourceStatement;
    private javax.swing.JTextField txtSourceUrl;
    // End of variables declaration//GEN-END:variables

    public Query getSrc() {
        return qry;
    }

    public void setSrc(Query src) {
        this.qry = src;
    }

    public int getPreviewMaxRows() {
        return (Integer) jSpinner1.getValue();
    }

    public Document getStatementDocument() {
        return txtSourceStatement.getDocument();
    }

    public JSpinner getMaxRowSpinner() {
        return jSpinner1;
    }

    public JComboBox getLabelCaseComboBox() {
        return jComboBox1;
    }

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
            sParams = new StringBuffer(sParams).append(txt).append(DEFINED_AS).append(p.getProperty(txt)).append(BACKSLASH_N).toString();
        }
        txtSourceParam.setText(sParams);
    }

    public void setEditable(boolean editable) {
        txtSourceStatement.setEditable(editable);
        jComboBox1.setEnabled(editable);
        //txtSourceStatement.setEnabled(editable);
        jSpinner1.setEnabled(editable);
    }

    class QueryWorker extends SwingWorker<String[], Void> implements Cancellable {

        public QueryWorker(final int maxRows) {
            if (maxRows < 0) {
                throw new IllegalArgumentException("maximum Rows can not be negative!");
            }
            this.tc = findTopComponent();
            this.maxRows = maxRows;
            ph = new ProgressHandler(this);
        }
        private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
        private final AbstractJPTopComponent tc;
        private final int maxRows;
        private ProgressHandler ph;
        private OutputQuery out;
        private Connection conn = null;

        @Override
        protected String[] doInBackground() throws Exception {
            final String driver = txtSourceDriver.getText();
            final String url = txtSourceUrl.getText();
            final String statement = txtSourceStatement.getText();
            final String props = txtSourceParam.getText();
            final Properties pro = new Properties();
            out = null;

            final String[] sp = props.split(BACKSLASH_N);
            for (int i = 0; i < sp.length; ++i) {
                String[] keyValue = sp[i].split(DEFINED_AS);
                if (keyValue.length != 2) {
                    continue;
                }
                pro.setProperty(keyValue[0], keyValue[1]);
            }

            final ClassResourceProvider clp = findProject().getLookup().lookup(ClassResourceProvider.class);
            conn = clp.getDriverManager().getConnection(driver, url, pro);
            publish();
            final boolean upper = qry.labelsToUpperCase();
            final boolean lower = qry.labelsToLowerCase();
            final Statement st = conn.createStatement();
            st.setMaxRows((Integer) jSpinner1.getValue());
            final ResultSet rs = st.executeQuery(statement);
            final String[] sourceFields = new String[rs.getMetaData().getColumnCount()];
            for (int i = 0; i < rs.getMetaData().getColumnCount(); ++i) {
                String s = rs.getMetaData().getColumnLabel(i + 1);
                if (s != null) {
                    if (lower) {
                        s = s.toLowerCase();
                    } else if (upper) {
                        s = s.toUpperCase();
                    }
                }
                sourceFields[i] = s;
            }
            out = new OutputQuery(rs, maxRows);
            if (!conn.isClosed()) {
                log.info("Source " + conn + " CLOSED");
                conn.close();
            }
            sourceFieldsForClipboard = sourceFields;
            return sourceFields;
        }

        @Override
        protected void process(List<Void> chunks) {
            if (ph != null) {
                ph.start("Executing Query...");
            }
        }

        @Override
        protected void done() {
            log.debug("worker DONE");
            try {
                final String[] sourceFields = get();
                if (this.out != null) {
                    tc.addOutput(this.out);
                }
                final RunTopComponent rtc = findSpecificTopComponent(RunTopComponent.class);
                //if this is shown in a RunTopComponent -> set Autocomplete Fields
                if (rtc != null) {
                    rtc.setSourceFields(sourceFields);
                    rtc.setAutocompleteStatus(true);
                    log.info("Source query tested: OK.");
                }
                btnColumnNamesToClip.setEnabled(true);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            } catch (ExecutionException ex) {
                String msg;
                if (!isCancelled()) {
                    final Throwable e = ex.getCause();
                    msg = "Could execute query.\n" + e.getMessage();
                    JOptionPane.showMessageDialog(QueryEditor.this, msg, "Error", JOptionPane.ERROR_MESSAGE);
                    log.error(msg, e);
                } else {
                    msg = "Query canceled by user.";
                    JOptionPane.showMessageDialog(QueryEditor.this, msg, "Canceled", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                final Connection toClose = conn;
                if (toClose != null) {
                    try {
                        toClose.close();
                        conn = null;
                    } catch (SQLException ex) {
                    }
                }
                if (ph != null) {
                    ph.finish();
                    ph = null;
                }
            }
        }

        public boolean cancel() {
            final Connection toClose = conn;
            if (toClose != null) {
                try {
                    cancel(true);
                    toClose.close();
                    conn = null;
                    return true;
                } catch (SQLException ex) {
                }
            }
            return true;
        }
    }
}
