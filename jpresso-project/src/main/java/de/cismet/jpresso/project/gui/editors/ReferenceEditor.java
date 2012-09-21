/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ReferenceEditor.java
 *
 * Created on 12. November 2003, 15:39
 */
package de.cismet.jpresso.project.gui.editors;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import java.awt.Color;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import de.cismet.jpresso.core.data.Mapping;
import de.cismet.jpresso.core.data.Reference;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

import de.cismet.jpresso.project.gui.dnd.JPDataFlavors;
import de.cismet.jpresso.project.gui.dnd.Referenceable;
import de.cismet.jpresso.project.gui.dnd.ReferenceableCollector;
import de.cismet.jpresso.project.gui.dnd.TableTransferHandler;
import de.cismet.jpresso.project.gui.editors.autocomplete.MultiClickComboBoxCellEditor;
import de.cismet.jpresso.project.gui.output.filtering.TableSortDecorator;

/**
 * DOCUMENT ME!
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public final class ReferenceEditor extends javax.swing.JPanel {

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private TableModelListener topComponent;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdAddRow;
    private javax.swing.JButton cmdDeleteRow;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane scpRelations;
    private ReferenceTable tblReference;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form ReferenceEditor.
     */
    public ReferenceEditor() {
        this(null);
    }

    /**
     * Creates a new ReferenceEditor object.
     *
     * @param  rel  DOCUMENT ME!
     */
    public ReferenceEditor(final List<Reference> rel) {
        initComponents();
        setContent(rel);
        tblReference.setModel(new ReferenceModel(rel));
        TableSortDecorator.decorate(tblReference);
        scpRelations.setTransferHandler(tblReference.getTransferHandler());
        tblReference.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    public void clearErrors() {
        tblReference.clearColors();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  mappings  DOCUMENT ME!
     */
    public void createReferenceAutoCompleteHash(final List<Mapping> mappings) {
        tblReference.createReferenceAutoCompleteHash(mappings);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  tml  DOCUMENT ME!
     */
    public void setTableListener(final TableModelListener tml) {
        this.topComponent = tml;
        if ((tblReference != null) && (tblReference.getModel() != null)) {
            this.tblReference.getModel().addTableModelListener(topComponent);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  ref  DOCUMENT ME!
     */
    public void setContent(List<Reference> ref) {
        if (ref == null) {
            ref = TypeSafeCollections.newArrayList();
        }
        if (tblReference.getModel() != null) {
            tblReference.getModel().removeTableModelListener(topComponent);
        }
        tblReference.setModel(new ReferenceModel(ref));
        tblReference.getModel().addTableModelListener(topComponent);
    }

    /**
     * DOCUMENT ME!
     */
    public void stopEditing() {
        if (tblReference.isEditing()) {
            tblReference.getCellEditor().stopCellEditing();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  row     DOCUMENT ME!
     * @param  column  DOCUMENT ME!
     */
    public void markPotentialError(final int row, final int column) {
        tblReference.setColor(tblReference.convertRowIndexToView(row), column, Color.RED);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<Reference> getContent() {
        return getReferenceModel().getRows();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private ReferenceModel getReferenceModel() {
        // return ((ReferenceModel) ((TableSorter) tblReference.getModel()).getModel());
        return tblReference.getModel();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  ref  DOCUMENT ME!
     */
    public void addReference(final List<Reference> ref) {
        for (final Reference r : ref) {
            getReferenceModel().addRow(r);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scpRelations = new javax.swing.JScrollPane();
        tblReference = new ReferenceTable();
        cmdDeleteRow = new javax.swing.JButton();
        cmdAddRow = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    null,
                    "References",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", 0, 12),
                    javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground")),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        scpRelations.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblReference.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                    {},
                    {},
                    {},
                    {}
                },
                new String[] {}));
        scpRelations.setViewportView(tblReference);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(scpRelations, gridBagConstraints);

        cmdDeleteRow.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/jpresso/project/res/edit-add.png"))); // NOI18N
        cmdDeleteRow.setToolTipText("Add new Row");
        cmdDeleteRow.setBorderPainted(false);
        cmdDeleteRow.setFocusPainted(false);
        cmdDeleteRow.setMargin(new java.awt.Insets(2, 5, 1, 5));
        cmdDeleteRow.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
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

        cmdAddRow.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/jpresso/project/res/edit-delete.png"))); // NOI18N
        cmdAddRow.setToolTipText("Remove selected Rows");
        cmdAddRow.setBorderPainted(false);
        cmdAddRow.setFocusPainted(false);
        cmdAddRow.setMargin(new java.awt.Insets(2, 5, 1, 5));
        cmdAddRow.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cmdAddRowActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 1, 0);
        add(cmdAddRow, gridBagConstraints);

        jButton1.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/jpresso/project/res/format-font-size-more.png"))); // NOI18N
        jButton1.setToolTipText("To upper case");
        jButton1.setMaximumSize(new java.awt.Dimension(36, 29));
        jButton1.setMinimumSize(new java.awt.Dimension(36, 29));
        jButton1.setPreferredSize(new java.awt.Dimension(36, 29));
        jButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 1, 0);
        add(jButton1, gridBagConstraints);

        jButton2.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/jpresso/project/res/format-font-size-less.png"))); // NOI18N
        jButton2.setToolTipText("To lower case");
        jButton2.setMaximumSize(new java.awt.Dimension(36, 29));
        jButton2.setMinimumSize(new java.awt.Dimension(36, 29));
        jButton2.setPreferredSize(new java.awt.Dimension(36, 29));
        jButton2.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 1, 0);
        add(jButton2, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents
    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cmdDeleteRowActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_cmdDeleteRowActionPerformed
        if (tblReference.isEditing()) {
            tblReference.getCellEditor().stopCellEditing();
        }
        getReferenceModel().addRow(new Reference());
    }                                                                                //GEN-LAST:event_cmdDeleteRowActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cmdAddRowActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_cmdAddRowActionPerformed
        tblReference.deleteCurrentSelection();
    }                                                                             //GEN-LAST:event_cmdAddRowActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton1ActionPerformed
        swichCase(true);
    }                                                                            //GEN-LAST:event_jButton1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton2ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton2ActionPerformed
        swichCase(false);
    }                                                                            //GEN-LAST:event_jButton2ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  toUpper  DOCUMENT ME!
     */
    private void swichCase(final boolean toUpper) {
        if (tblReference != null) {
            final int[] x = tblReference.getSelectedColumns();
            final int[] y = tblReference.getSelectedRows();
            if (toUpper) {
                for (final int i : y) {
                    for (final int j : x) {
                        tblReference.setValueAt(tblReference.getValueAt(i, j).toString().toUpperCase(), i, j);
                    }
                }
            } else {
                for (final int i : y) {
                    for (final int j : x) {
                        tblReference.setValueAt(tblReference.getValueAt(i, j).toString().toLowerCase(), i, j);
                    }
                }
            }
        }
    }
}
/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class ReferenceModel extends ListTableModel<Reference> {

    //~ Static fields/initializers ---------------------------------------------

    private static final String ENCLOSING_CHARACTER = "'";

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final String[] cNames = new String[] {
            "Referencing Table",
            "Referencing Field",
            "Referenced Table",
            "Referenced Field",
            "Compare",
            "String/Date"
        };

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ReferenceModel object.
     *
     * @param  references  DOCUMENT ME!
     */
    public ReferenceModel(final List<Reference> references) {
        super(references);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Class getColumnClass(final int columnIndex) {
        switch (columnIndex) {
            case 0: {
                return java.lang.String.class;
            }
            case 1: {
                return java.lang.String.class;
            }
            case 2: {
                return java.lang.String.class;
            }
            case 3: {
                return java.lang.String.class;
            }
            case 4: {
                return java.lang.Boolean.class;
            }
            case 5: {
                return java.lang.Boolean.class;
            }
        }
        return null;
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public String getColumnName(final int c) {
        return cNames[c];
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final Reference r = getRow(rowIndex);
        if (r != null) {
            switch (columnIndex) {
                case 0: {
                    return r.getReferencingTable();
                }
                case 1: {
                    return r.getReferencingField();
                }
                case 2: {
                    return r.getReferencedTable();
                }
                case 3: {
                    return r.getReferencedField();
                }
                case 4: {
                    return r.isComparing();
                }
                case 5: {
                    Boolean stringOrDate;
                    if ((r.getEnclosingChar() == null) || (r.getEnclosingChar().length() == 0)) {
                        stringOrDate = false;
                    } else {
                        stringOrDate = true;
                    }
                    return stringOrDate;
                }
            }
        }
        return null;
    }

    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        final Reference r = getRow(rowIndex);
        try {
            switch (columnIndex) {
                case 0: {
                    r.setReferencingTable(((String)aValue));
                    break;
                }
                case 1: {
                    r.setReferencingField((String)aValue);
                    break;
                }
                case 2: {
                    r.setReferencedTable(((String)aValue));
                    break;
                }
                case 3: {
                    r.setReferencedField((String)aValue);
                    break;
                }
                case 4: {
                    r.setComparing(((Boolean)aValue).booleanValue());
                    break;
                }
                case 5: {
                    final boolean stringOrDate = ((Boolean)aValue).booleanValue();
                    if (stringOrDate) {
                        r.setEnclosingChar(ENCLOSING_CHARACTER);
                    } else {
                        r.setEnclosingChar(null);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Error setting value in reference table", e);
        }
        setRow(r, rowIndex);
        fireTableCellUpdated(rowIndex, columnIndex);
//        fireTableDataChanged();
    }
}
/**
 * A JTable with row-dependent autocomplete support.
 *
 * <p>HINT: table columns hard-coded for performance.</p>
 *
 * @version  $Revision$, $Date$
 */
class ReferenceTable extends ColorfulJTable {

    //~ Static fields/initializers ---------------------------------------------

    private static final String OPEN_BRACKET = "[";

    //~ Instance fields --------------------------------------------------------

    private ReferenceModel referenceModel;
    private final HashMap<String, org.jdesktop.swingx.autocomplete.ComboBoxCellEditor> autoCompletHash;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ReferenceTable object.
     */
    public ReferenceTable() {
        this(new ReferenceModel(new ArrayList<Reference>()));
    }

    /**
     * Creates a new ReferenceTable object.
     *
     * @param  model  DOCUMENT ME!
     */
    public ReferenceTable(final ReferenceModel model) {
        super(model);
        this.referenceModel = model;
        this.autoCompletHash = TypeSafeCollections.newHashMap();
        final Action deleteAction = new AbstractAction() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    deleteCurrentSelection();
                }
            };

        final InputMap im = getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        final KeyStroke delete = KeyStroke.getKeyStroke("DELETE");
        im.put(delete, "delete");
        getActionMap().put("delete", deleteAction);
        final TransferHandler th = new TableTransferHandler<ReferenceTable, Referenceable>(
                this,
                new DataFlavor[] { JPDataFlavors.REF_FLAVOR }) {

                @Override
                public void deleteCurrentSelectionFromTable() {
                    getTable().deleteCurrentSelection();
                }

                @Override
                public boolean insertImportDataListIntoTable(final Referenceable data, final TransferSupport s) {
                    int rowIndex;
                    int colIndex;
                    if (s.isDrop()) {
                        final Point p = s.getDropLocation().getDropPoint();
                        rowIndex = rowAtPoint(p);
                        colIndex = columnAtPoint(p);
                    } else {
                        rowIndex = getSelectedRow();
                        colIndex = getSelectedColumn();
                    }
                    if (rowIndex > -1) {
                        rowIndex = convertRowIndexToModel(rowIndex);
                    }
                    if (colIndex > -1) {
                        colIndex = convertColumnIndexToModel(colIndex);
                    }
                    if (rowIndex < 0) {
                        rowIndex = getTable().getRowCount() - 1;
                        if (rowIndex < 0) {
                            rowIndex = 0;
                        }
                    }
                    if (data.getReferences() == null) {
                        return false;
                    }

                    // HACK to have 2-part Drag&Drop for either master or detail table
                    if (s.isDrop() && (colIndex > 1) && (data.getReferences().size() == 1)) {
                        final Reference r = referenceModel.getRow(rowIndex);
                        if (r != null) {
                            final Reference toImp = data.getReferences().get(0);
                            if ((toImp.getReferencedTable().length() == 0)
                                        && (toImp.getReferencedField().length() == 0)) {
                                r.setReferencedTable(toImp.getReferencingTable());
                                r.setReferencedField(toImp.getReferencingField());
                                referenceModel.fireTableRowsUpdated(rowIndex, rowIndex);
                                return true;
                            }
                        }
                    }
                    // the standard behaviour
                    for (final Reference ref : data.getReferences()) {
                        referenceModel.addRow(ref.copy(), ++rowIndex);
                    }
//                }
                    return true;
                }

                @Override
                public Referenceable createExportDataListFromTable(final int[] selectedRows) {
                    final List<Reference> references = TypeSafeCollections.newArrayList();
                    for (final int selRow : selectedRows) {
                        if (selRow != -1) {
                            final Reference r = referenceModel.getRow(selRow);
                            if (r != null) {
                                references.add(r);
                            }
                        }
                    }
                    return new ReferenceableCollector(references);
                }
            };
        setTransferHandler(th);
        setDragEnabled(true);

        // jalopy doesn't like these inline listeners

        //J-
        getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                clearColors();
            }
        });
        //J+
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void setModel(final TableModel dataModel) {
        if ((dataModel != null) && (dataModel instanceof ReferenceModel)) {
            super.setModel(dataModel);
            this.referenceModel = (ReferenceModel)dataModel;
        }
    }

    @Override
    public ReferenceModel getModel() {
        return this.referenceModel;
    }

    @Override
    public TableCellEditor getCellEditor(final int row, final int column) {
        if ((column == 1) || (column == 3)) {
            final Object o = getValueAt(row, column - 1);
            if (o instanceof String) {
                String key = (String)o;
                final int relPathIndex = key.lastIndexOf(OPEN_BRACKET);
                if (relPathIndex > 0) {
                    key = key.substring(0, relPathIndex);
                }
                final TableCellEditor tce = autoCompletHash.get(key);
                if (tce != null) {
                    return tce;
                }
            }
        }
        clearColors();
        return super.getCellEditor(row, column);
    }
    /**
     * Prepares the comboboxes for autocomplete.
     *
     * @param  mappings  references
     */
    public void createReferenceAutoCompleteHash(final List<Mapping> mappings) {
        autoCompletHash.clear();
        if ((mappings != null) && (mappings.size() > 0)) {
            final HashMap<String, Set<String>> tmp = TypeSafeCollections.newHashMap();
            String tab;
            String fld;
            // fill hash: table name -> Set<fieldnames>
            for (final Mapping m : mappings) {
                tab = m.getTargetTable();
                if ((tab != null) && !(tab.length() == 0)) {
                    Set<String> cur = tmp.get(tab);
                    if (cur == null) {
                        cur = TypeSafeCollections.newHashSet();
                        tmp.put(tab, cur);
                    }
                    fld = m.getTargetField();
                    if ((fld != null) && !(fld.length() == 0)) {
                        cur.add(fld);
                    }
                }
            }
            // prepare the tablenames
            final String[] tabNames = tmp.keySet().toArray(new String[] {});
            Arrays.sort(tabNames);
            final JComboBox cbTab = new JComboBox(tabNames);
            cbTab.setEditable(true);
            cbTab.setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
            AutoCompleteDecorator.decorate(cbTab);
            final TableColumn master = getColumnModel().getColumn(0);
            final TableColumn detail = getColumnModel().getColumn(2);
            // set cell editors for tablenames
            final MultiClickComboBoxCellEditor ted = new MultiClickComboBoxCellEditor(cbTab);
            master.setCellEditor(ted);
            detail.setCellEditor(ted);

            // prepare hash tablename -> keyAutoCompleteCBox
            for (final String key : tabNames) {
                final Set<String> ss = tmp.get(key);
                final String[] fieldNames = ss.toArray(new String[] {});
                Arrays.sort(fieldNames);
                final JComboBox cbField = new JComboBox(fieldNames);
                cbField.setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
                cbField.setEditable(true);
                AutoCompleteDecorator.decorate(cbField);
                final MultiClickComboBoxCellEditor fed = new MultiClickComboBoxCellEditor(cbField);
                autoCompletHash.put(key, fed);
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void deleteCurrentSelection() {
        if (isEditing()) {
            getCellEditor().stopCellEditing();
        }
        final int[] selRows = getSelectedRows();
        Arrays.sort(selRows);
        for (int i = selRows.length - 1; i >= 0; --i) {
            final int selRow = selRows[i];
            if (selRow != -1) {
                referenceModel.removeRow(convertRowIndexToModel(selRow));
                if (referenceModel.getRowCount() > 0) {
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
