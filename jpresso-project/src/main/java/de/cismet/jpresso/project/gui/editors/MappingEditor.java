/*
 * MappingEditor.java
 *
 * Created on 11. November 2003, 11:33
 */
package de.cismet.jpresso.project.gui.editors;

import de.cismet.jpresso.project.gui.dnd.TableTransferHandler;
import de.cismet.jpresso.project.serviceprovider.CodeFunctionProvider;
import de.cismet.jpresso.project.gui.editors.autocomplete.SmartComboBox;
import de.cismet.jpresso.project.gui.dnd.Mapable;
import de.cismet.jpresso.project.gui.output.filtering.TableSortDecorator;
import de.cismet.jpresso.core.data.Mapping;
import de.cismet.jpresso.project.gui.dnd.JPDataFlavors;
import de.cismet.jpresso.project.gui.dnd.MapableCollector;
import de.cismet.jpresso.project.gui.editors.autocomplete.MultiClickComboBoxCellEditor;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.awt.Color;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 * @author  srichter
 */
public final class MappingEditor extends javax.swing.JPanel {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private CodeFunctionProvider codeProvider;
    private TableModelListener topComponent;
    private String[] fldNames;
    private static final String STRING_EMPTY = "";

    /** Creates new form MappingEditor */
    public MappingEditor() {
        this(null);
    }

    public MappingEditor(final List<Mapping> mappings) {
        fldNames = null;
        initComponents();
        setContent(mappings);
        tblMappings.setModel(new MappingModel(mappings));
        //add alphanumeric sorting
        TableSortDecorator.decorate(tblMappings);
        scpMappings.setTransferHandler(tblMappings.getTransferHandler());
        this.tblMappings.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        TableColumn col = tblMappings.getColumnModel().getColumn(3);
        col.getCellRenderer();
    }

    public MappingEditor(final List<Mapping> mappings, final CodeFunctionProvider codeProvider) {
        this(mappings);
        this.codeProvider = codeProvider;
    }

    public void setTableListener(final TableModelListener tml) {
        this.topComponent = tml;
        if (tblMappings != null && tblMappings.getModel() != null) {
            this.tblMappings.getModel().addTableModelListener(topComponent);
        }
    }

    public List<Mapping> getContent() {
        return this.getMappingModel().getRows();
    }

    public void setContent(List<Mapping> mappings) {
        if (mappings == null) {
            mappings = TypeSafeCollections.newArrayList();
        }
        //this.mappings = mappings;
        //TableSorter ts = new TableSorter(new MappingModel(mappings));
        //tblMappings.setModel(ts);
        if (tblMappings.getModel() != null) {
            tblMappings.getModel().removeTableModelListener(topComponent);
        }
        tblMappings.setModel(new MappingModel(mappings));
        tblMappings.getModel().addTableModelListener(topComponent);
    }

    /**
     * 
     * @param fieldNames
     */
    public void prepareAutoComplete(final String[] fieldNames) {
        final List<String> tmp = TypeSafeCollections.newArrayList();
        if (codeProvider != null) {
            tmp.addAll(codeProvider.getFunctionList());
        }
        if (fieldNames != null) {
            this.fldNames = fieldNames.clone();
        }
        if (this.fldNames != null) {
            for (final String s : this.fldNames) {
                tmp.add(s);
            }
        }
        final TableColumn tc = tblMappings.getColumnModel().getColumn(1);
        Collections.sort(tmp);

        final SmartComboBox combo = new SmartComboBox(tmp.toArray(new String[]{}));
        combo.setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
        combo.setEditable(true);
        combo.setCaseSensitive(true);
        final MultiClickComboBoxCellEditor ed = new MultiClickComboBoxCellEditor(combo);
        tc.setCellEditor(ed);
        ed.addCellEditorListener(combo);
    }

    /**
     * 
     * @param maps
     */
    public void addMappings(final List<Mapping> maps) {
        for (final Mapping m : maps) {
            getMappingModel().addRow(m);
        }
    }

    /**
     * 
     * @return the mapping model
     */
    private MappingModel getMappingModel() {
        return tblMappings.getModel();
    }

    /**
     * stop cell editor
     */
    public void stopEditing() {
        if (tblMappings.isEditing()) {
            tblMappings.getCellEditor().stopCellEditing();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scpMappings = new javax.swing.JScrollPane();
        tblMappings = new MappingTable();
        cmdAddRow = new javax.swing.JButton();
        cmdDeleteRow = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Mappings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        scpMappings.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblMappings.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        scpMappings.setViewportView(tblMappings);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(scpMappings, gridBagConstraints);

        cmdAddRow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/edit-delete.png"))); // NOI18N
        cmdAddRow.setToolTipText("Remove selected Rows");
        cmdAddRow.setBorderPainted(false);
        cmdAddRow.setFocusPainted(false);
        cmdAddRow.setMargin(new java.awt.Insets(2, 5, 1, 5));
        cmdAddRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddRowActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 1, 0);
        add(cmdAddRow, gridBagConstraints);

        cmdDeleteRow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/edit-add.png"))); // NOI18N
        cmdDeleteRow.setToolTipText("Add new Row");
        cmdDeleteRow.setBorderPainted(false);
        cmdDeleteRow.setFocusPainted(false);
        cmdDeleteRow.setMargin(new java.awt.Insets(2, 5, 1, 5));
        cmdDeleteRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeleteRowActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 1, 0);
        add(cmdDeleteRow, gridBagConstraints);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/format-font-size-more.png"))); // NOI18N
        jButton1.setToolTipText("To upper case");
        jButton1.setMaximumSize(new java.awt.Dimension(36, 29));
        jButton1.setMinimumSize(new java.awt.Dimension(36, 29));
        jButton1.setPreferredSize(new java.awt.Dimension(36, 29));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 1, 0);
        add(jButton1, gridBagConstraints);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/format-font-size-less.png"))); // NOI18N
        jButton2.setToolTipText("To lower case");
        jButton2.setMaximumSize(new java.awt.Dimension(36, 29));
        jButton2.setMinimumSize(new java.awt.Dimension(36, 29));
        jButton2.setPreferredSize(new java.awt.Dimension(36, 29));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 1, 0);
        add(jButton2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    private void cmdDeleteRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteRowActionPerformed
        if (tblMappings.isEditing()) {//GEN-LAST:event_cmdDeleteRowActionPerformed
            tblMappings.getCellEditor().stopCellEditing();
        }
        getMappingModel().addRow(new Mapping());
    }

    private void cmdAddRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddRowActionPerformed
        tblMappings.deleteCurrentSelection();
    }//GEN-LAST:event_cmdAddRowActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    swichCase(true);
}//GEN-LAST:event_jButton1ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    swichCase(false);
}//GEN-LAST:event_jButton2ActionPerformed

    private void swichCase(final boolean toUpper) {
        if (tblMappings != null) {
            final int[] x = tblMappings.getSelectedColumns();
            final int[] y = tblMappings.getSelectedRows();
            if (toUpper) {
                for (int i : y) {
                    for (int j : x) {
                        tblMappings.setValueAt(tblMappings.getValueAt(i, j).toString().toUpperCase(), i, j);
                    }
                }
            } else {
                for (int i : y) {
                    for (int j : x) {
                        tblMappings.setValueAt(tblMappings.getValueAt(i, j).toString().toLowerCase(), i, j);
                    }
                }
            }
        }
    }

    /**
     * 
     * @return
     */
    public boolean allMappingsComplete() {
        tblMappings.clearColors();
        final List<Mapping> content = getContent();
        for (final Mapping m : content) {
            if (m.getContent().equals(STRING_EMPTY) && !m.isAutoIncrement()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Marks potential error fields red in the table.
     * @param s
     */
    public void markPotentialErrors(List<String> errors, int column) {
        tblMappings.markPotentialErrors(errors, column);
    }
    
    public void markPotentialError(int row, int column) {    
        tblMappings.setColor(tblMappings.convertRowIndexToView(row), column, Color.RED);
    }
    
    public void clearErrors() {
        tblMappings.clearColors();
    }
    
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdAddRow;
    private javax.swing.JButton cmdDeleteRow;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane scpMappings;
    private MappingTable tblMappings;
    // End of variables declaration//GEN-END:variables
}


class MappingModel extends ListTableModel<Mapping> {

    public static final int COLUMN_TAGRET = 0;
    public static final int COLUMN_SOURCE = 1;
    public static final int COLUMN_RELATIONPATH = 2;
    public static final int COLUMN_COUNTER = 3;
    public static final int COLUMN_COMPARE = 4;
    public static final int COLUMN_STRINGDATE = 5;
    public static final String ENCLOSING_CHARACTER = "'";
    public static final String PATH_SEPARATOR = "/";
    private static final String SPLITT_DOT = "\\.";
    public static final String[] COLUMN_NAMES = new String[]{"Target", "Source", "Relationpath", "Counter", "Compare", "String/Date"};

    public MappingModel(final List<Mapping> mappings) {
        super(mappings);
    }

    /** 
     * Liefert das allgemeinste Klassenobjekt, welches die Spalte beschreiben kann.
     */
    @Override
    public Class getColumnClass(final int columnIndex) {
        switch (columnIndex) {
            case COLUMN_TAGRET:
                return java.lang.String.class;
            case COLUMN_SOURCE:
                return java.lang.String.class;
            case COLUMN_RELATIONPATH:
                return java.lang.String.class;
            case COLUMN_COUNTER:
                return java.lang.Boolean.class;
            case COLUMN_COMPARE:
                return java.lang.Boolean.class;
            case COLUMN_STRINGDATE:
                return java.lang.Boolean.class;
        }
        return null;
    }

    /** Liefert die Anzahl Spalten.*/
    @Override
    public int getColumnCount() {
        return 6;
    }

    /** Gibt den Namen der Spalte columnIndex zurueck. */
    @Override
    public String getColumnName(final int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    /** Gibt den Eintrag an der Stelle columnIndex und rowIndex zurueck.*/
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final Mapping m = getRows().get(rowIndex);
        switch (columnIndex) {
            case COLUMN_TAGRET:
                return m.getCompleteTarget();
            case COLUMN_SOURCE:
                return m.getContent();
            case COLUMN_RELATIONPATH:
                final String relNa = m.getPath();
                if (relNa == null) {
                    return "";
                } else {
                    return String.valueOf(relNa);
                }
            case COLUMN_COUNTER:
                return m.isAutoIncrement();
            case COLUMN_COMPARE:
                return m.isComparing();
            case COLUMN_STRINGDATE:
                final Boolean stringOrDate;
                if (m.getEnclosingChar() == null || m.getEnclosingChar().length() == 0) {
                    stringOrDate = false;
                } else {
                    stringOrDate = true;
                }
                return stringOrDate;
        }
        return null;
    }

    /** Setzt den Wert an die gegebene Stelle.*/
    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        final Mapping m = getRow(rowIndex);
        try {
            switch (columnIndex) {
                case COLUMN_TAGRET:
                    final String tableAndField = ((String) aValue);
                    final String[] s = tableAndField.split(SPLITT_DOT);
                    if (s.length == 2) {
                        m.setTargetTable(s[0]);
                        m.setTargetField(s[1]);
                    }
//                    //---Auto Path---
//                    int max = -1;
//                    Mapping cur;
//                    for (int i = 0; i < mappings.size(); ++i) {
//                        cur = mappings.get(i);
//                        if (cur != null && cur.getTargetTable().equals(m.getTargetTable())) {
//                            if (cur.getTargetField().equals(m.getTargetField())) {
//                                if (cur != m) {
//                                //found another mapping with the same target.
//                                    if (cur.getPath().length() > 0) {
//                                        final Integer test = Integer.parseInt(cur.getPath());
//                                        if (test > max) {
//                                            max = test;
//                                        }
//                                    } else {
//                                        //if we found another mapping with the same target, that has no path, it has to become path "0"!
//                                        cur.setPath("0");
//                                        fireTableRowsUpdated(i, i);
//                                        max = 0;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    if (++max > 0) {
//                        //our path value is now the highest among all other mappings with the same target +1
//                        m.setPath(max + "");
//                    }
//                    //--------------
                    break;
                case COLUMN_SOURCE:
                    final String source = ((String) aValue);
                    m.setContent(source);
//                    //TODO wenns aerger macht -> raus, aber increment un content zusammen = sinnlos
                    m.setAutoIncrement(false);
                    break;
                case COLUMN_RELATIONPATH:
                    if (aValue != null && aValue instanceof String) {
                        m.setPath((String) aValue);
                    }
                    break;
                case COLUMN_COUNTER:
                    final Boolean autoInc = ((Boolean) aValue);
                    m.setAutoIncrement(autoInc.booleanValue());
//                    //TODO wenns aerger macht -> raus, aber increment un content zusammen = sinnlos
                    m.setContent("");
                    break;
                case COLUMN_COMPARE:
                    final Boolean comp = ((Boolean) aValue);
                    m.setComparing(comp.booleanValue());
                    break;
                case COLUMN_STRINGDATE:
                    final Boolean enclChar = ((Boolean) aValue);
                    if (enclChar.booleanValue()) {
                        m.setEnclosingChar(ENCLOSING_CHARACTER);
                    } else {
                        m.setEnclosingChar(null);
                    }
                    break;
            }
        } catch (Exception e) {
            //keine Veraenderung vornehmen;
        }
        setRow(m, rowIndex);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }
}
final class MappingTable extends ColorfulJTable {

    public static final String STRING_EMPTY = "";
    public static final String STRING_FALSE = "false";

    public MappingTable() {
        this(new MappingModel(new ArrayList<Mapping>()));
    }

    public MappingTable(final MappingModel model) {
        super(model);
        this.mappingModel = model;
        final Action deleteAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                deleteCurrentSelection();
            }
        };
        final InputMap im = getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        final KeyStroke delete = KeyStroke.getKeyStroke("DELETE");
        im.put(delete, "delete");
        getActionMap().put("delete", deleteAction);
        final TransferHandler th = new TableTransferHandler<MappingTable, Mapable>(this, new DataFlavor[]{JPDataFlavors.MAP_FLAVOR}) {

            @Override
            public void deleteCurrentSelectionFromTable() {
                getTable().deleteCurrentSelection();
            }

            @Override
            public boolean insertImportDataListIntoTable(Mapable data, TransferSupport s) {
                int rowIndex;
                if (s.isDrop()) {
                    final Point p = s.getDropLocation().getDropPoint();
                    rowIndex = rowAtPoint(p);
                } else {
                    rowIndex = getSelectedRow();
                }
                if (rowIndex > -1) {
                    rowIndex = convertRowIndexToModel(rowIndex);
                }
                getTable().getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
                if (rowIndex < 0) {
                    rowIndex = getTable().getRowCount() - 1;
                    if(rowIndex < 0) {
                        rowIndex = -1;
                    }
                }
                if (data.getMappings() == null) {
                    return false;
                }
//                if (s.getComponent() == getTable()) {
//                    for (final Mapping map : data.getMappings()) {
//                        mappingModel.addMapping(map.copy(), rowIndex++);
//                        mappingModel.removeMapping(map);
//                    }
//                } else {
                for (final Mapping map : data.getMappings()) {
                    mappingModel.addRow(map.copy(), ++rowIndex);
                }

//                }
                return true;
            }

            @Override
            public Mapable createExportDataListFromTable(int[] selectedRows) {
                final List<Mapping> mappings = TypeSafeCollections.newArrayList();
                for (int selRow : selectedRows) {
                    if (selRow != -1) {
                        final Mapping m = mappingModel.getRow(selRow);
                        if (m != null) {
                            mappings.add(m);
                        }
                    }
                }
                return new MapableCollector(mappings);
            }
        };
        setTransferHandler(th);
        setDragEnabled(true);
        getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                clearColors();
            }
        });
    }
    private MappingModel mappingModel;
//
//    @Override
//    
//    public TableCellRenderer getCellRenderer(int row, int column) {
//        final TableCellRenderer tcr = super.getCellRenderer(row, column);
//        final Object value = getValueAt(row, column);
//        Color color = Color.WHITE;
//        //source column hardcoded
//        if (column == MappingModel.COLUMN_SOURCE && errorSet.contains(value)) {
//            color = Color.RED;
//        } else if (column == MappingModel.COLUMN_TAGRET && value.toString().equals(STRING_EMPTY)) {
//            color = Color.YELLOW;
//        } else if (column == MappingModel.COLUMN_SOURCE && value.toString().equals(STRING_EMPTY) && getValueAt(row, MappingModel.COLUMN_COUNTER).toString().equals(STRING_FALSE)) {
//            color = Color.YELLOW;
//        }
//        ((JComponent) tcr).setBackground(color);
//        return tcr;
//    }
    /**
     * 
     * @param dataModel only mappingmodels accepted
     */
    @Override
    public void setModel(final TableModel dataModel) {
        if (dataModel != null && dataModel instanceof MappingModel) {
            this.mappingModel = (MappingModel) dataModel;
            super.setModel(dataModel);
        }
    }

    @Override
    public MappingModel getModel() {
        return mappingModel;
    }

    @Override
    public TableCellEditor getCellEditor(final int row, final int col) {
        clearColors();
        return super.getCellEditor(row, col);
    }

    /**
     * Mark errors red in the table, until they are edited agin.
     * @param errors
     */
    
    public void markPotentialErrors(final List<String> errors, final int column) {
        final int columnAtView = convertColumnIndexToView(column);
        if (errors != null) {
            for (int i = 0; i < getRowCount(); ++i) {
                final Object v = getValueAt(i, columnAtView);
                if (v != null) {
                    if (errors.contains(v.toString())) {
                        setColor(i, column, Color.RED);
                    }
                }
            }
        }
        repaint();
    }

    /**
     * delete the currently selected rows from the table
     */
    public void deleteCurrentSelection() {
        if (isEditing()) {
            getCellEditor().stopCellEditing();
        }
        clearColors();
        final int[] selRows = getSelectedRows();
        for (int i = selRows.length - 1; i >= 0; --i) {
            int selRow = selRows[i];
            if (selRow != -1) {
                mappingModel.removeRow(convertRowIndexToModel(selRow));
                if (mappingModel.getRowCount() > 0) {
                    if (selRow == 0) {
                        getSelectionModel().setSelectionInterval(0, 0);
                    } else {
                        getSelectionModel().setSelectionInterval(selRow - 1, selRow - 1);
                    }
                }
            }
        }
    }
}


