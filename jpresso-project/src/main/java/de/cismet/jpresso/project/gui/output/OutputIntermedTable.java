/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * OutputIntermedTable.java
 *
 * Created on 30. Januar 2008, 18:45
 */
package de.cismet.jpresso.project.gui.output;

import java.awt.Color;
import java.awt.Component;

import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import de.cismet.jpresso.project.gui.output.filtering.TableSortDecorator;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class OutputIntermedTable extends JPanel {

    //~ Static fields/initializers ---------------------------------------------

    public static final String NULL = "<NULL>";
    public static final String SINGLE_QUOTE = "'";
    public static final String DOUBLE_QUOTE = "''";
    public static final String SINGLE_BACKSLASH = "\\";
    public static final String DOUBLE_BACKSLASH = "\\\\";
    public static final String CIDS_CROSS_REFERENCE = "-->CIDS-CROSS-REFERENCE:";

    //~ Instance fields --------------------------------------------------------

    private final boolean[] deduplicate;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblRowCount;
    private javax.swing.JScrollPane scpMain;
    private javax.swing.JTable tblIntermed;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form OutputIntermedTable.
     *
     * @param  intermed        DOCUMENT ME!
     * @param  enclosingChars  DOCUMENT ME!
     */
    public OutputIntermedTable(final TableModel intermed, final String[] enclosingChars) {
        initComponents();
        deduplicate = new boolean[intermed.getColumnCount()];
        if ((enclosingChars != null) && (enclosingChars.length == intermed.getColumnCount())) {
            for (int i = 0; i < intermed.getColumnCount(); ++i) {
                deduplicate[i] = "'".equals(enclosingChars[i]);
            }
        } else {
            Arrays.fill(deduplicate, false);
        }
        tblIntermed.setFont(new java.awt.Font("Dialog", 0, 12));
        this.tblIntermed.setModel(intermed);
        this.tblIntermed.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.tblIntermed.setCellSelectionEnabled(true);
        setName(intermed.toString());
        lblRowCount.setText("Rows: " + intermed.getRowCount());
        TableSortDecorator.decorate(tblIntermed);
        final DefaultTableCellRenderer r = new DefaultTableCellRenderer() {

                @Override
                public final Component getTableCellRendererComponent(final JTable table,
                        final Object value,
                        final boolean isSelected,
                        final boolean hasFocus,
                        final int row,
                        final int column) {
                    final Component c;
                    if (value != null) {
                        // hardcoded unescaping for performance
                        final char[] chars = value.toString().toCharArray();
                        final char[] res;
                        if ((chars.length > 1) && deduplicate[column]) {
                            res = new char[chars.length];
                            int ptr = -1;
                            char cur = chars[0];
                            char next;
                            for (int j = 0; j < (chars.length - 1); ++j) {
                                next = chars[j + 1];
                                if ((cur == '\'') && (next == '\'')) {
                                    ++j;
                                }
                                res[++ptr] = cur;
                                cur = next;
                            }
                            res[++ptr] = chars[chars.length - 1];
                            c = super.getTableCellRendererComponent(
                                    table,
                                    new String(res, 0, ++ptr),
                                    isSelected,
                                    hasFocus,
                                    row,
                                    column);
                        } else {
                            c = super.getTableCellRendererComponent(
                                    table,
                                    new String(chars, 0, chars.length),
                                    isSelected,
                                    hasFocus,
                                    row,
                                    column);
                        }
                        c.setForeground(Color.BLACK);
                    } else {
                        c = super.getTableCellRendererComponent(table, NULL, isSelected, hasFocus, row, column);
//                    c.setBackground(Color.WHITE);
                        c.setForeground(Color.LIGHT_GRAY);
                    }
                    return c;
                }
            };
//        tblIntermed.setDefaultRenderer(String.class, r);
        tblIntermed.setDefaultRenderer(Object.class, r);
    }

    //~ Methods ----------------------------------------------------------------

// private int findColumnPos(String name) {
// int i = 0;
// IntermedTable tbl = (IntermedTable) tblIntermed.getModel();
// for (; i < tbl.getColumnCount(); ++i) {
// if (tbl.getColumnName(i).equals(name)) {
// return i;
// }
// }
// return -1;
// }
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        final java.awt.GridBagConstraints gridBagConstraints;

        scpMain = new javax.swing.JScrollPane();
        tblIntermed = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblRowCount = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        tblIntermed.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                    {},
                    {},
                    {},
                    {}
                },
                new String[] {}));
        tblIntermed.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        scpMain.setViewportView(tblIntermed);

        add(scpMain, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        lblRowCount.setText(org.openide.util.NbBundle.getMessage(
                OutputIntermedTable.class,
                "OutputIntermedTable.lblRowCount.text")); // NOI18N
        jPanel2.add(lblRowCount, java.awt.BorderLayout.EAST);

        jPanel1.add(jPanel2, java.awt.BorderLayout.EAST);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jButton1.setText(org.openide.util.NbBundle.getMessage(
                OutputIntermedTable.class,
                "OutputIntermedTable.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
        jPanel3.add(jButton1, new java.awt.GridBagConstraints());

        jTextField1.setText(org.openide.util.NbBundle.getMessage(
                OutputIntermedTable.class,
                "OutputIntermedTable.jTextField1.text")); // NOI18N
        jTextField1.setMinimumSize(new java.awt.Dimension(100, 20));
        jTextField1.setPreferredSize(new java.awt.Dimension(100, 20));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jTextField1ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jTextField1, gridBagConstraints);

        jButton2.setText(org.openide.util.NbBundle.getMessage(
                OutputIntermedTable.class,
                "OutputIntermedTable.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });
        jPanel3.add(jButton2, new java.awt.GridBagConstraints());

        jPanel1.add(jPanel3, java.awt.BorderLayout.WEST);

        add(jPanel1, java.awt.BorderLayout.PAGE_END);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton1ActionPerformed
        if (tblIntermed != null) {
            final int[] x = tblIntermed.getSelectedColumns();
            final int[] y = tblIntermed.getSelectedRows();
            for (final int i : y) {
                for (final int j : x) {
                    tblIntermed.setValueAt(null, i, j);
                }
            }
        }
    }                                                                            //GEN-LAST:event_jButton1ActionPerformed
    /**
     * Sets the values of the selected cells to the textfield content.
     */
    private void fillTextIntoTable() {
        if (tblIntermed != null) {
            final int[] x = tblIntermed.getSelectedColumns();
            final int[] y = tblIntermed.getSelectedRows();
            for (final int i : y) {
                for (final int j : x) {
                    tblIntermed.setValueAt(jTextField1.getText(), i, j);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton2ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton2ActionPerformed
        fillTextIntoTable();
    }                                                                            //GEN-LAST:event_jButton2ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jTextField1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jTextField1ActionPerformed
        fillTextIntoTable();
    }                                                                               //GEN-LAST:event_jTextField1ActionPerformed
}
