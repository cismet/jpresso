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
package de.cismet.jpresso.project.gui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

/**
 * The basic operations for Transfer handling in the editor's tables (mappings, references).
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public abstract class TableTransferHandler<T extends JTable, D> extends TransferHandler {

    //~ Instance fields --------------------------------------------------------

    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final T table;
    private final DataFlavor[] flavors;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new TableTransferHandler object.
     *
     * @param  table    DOCUMENT ME!
     * @param  flavors  DOCUMENT ME!
     */
    public TableTransferHandler(final T table, final DataFlavor[] flavors) {
        this.table = table;
        this.flavors = flavors.clone();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  source  DOCUMENT ME!
     * @param  data    DOCUMENT ME!
     * @param  action  DOCUMENT ME!
     */
    @Override
    protected final void exportDone(final JComponent source, final Transferable data, final int action) {
        super.exportDone(source, data, action);
        if (action == TransferHandler.MOVE) {
            deleteCurrentSelectionFromTable();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   c  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public final int getSourceActions(final JComponent c) {
        if (c == table) {
            return DnDConstants.ACTION_COPY_OR_MOVE;
        }
        return DnDConstants.ACTION_NONE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   c  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected final TableTransferable<D> createTransferable(final JComponent c) {
        if (c == table) {
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }
            final int[] selectedRowsView = table.getSelectedRows();
            final int[] selectedRowsModel = new int[selectedRowsView.length];
            for (int i = 0; i < selectedRowsView.length; ++i) {
                selectedRowsModel[i] = table.convertRowIndexToModel(selectedRowsView[i]);
            }
            return new TableTransferable<D>(createExportDataListFromTable(selectedRowsModel), flavors);
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   support  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public final boolean canImport(final TransferSupport support) {
        final DataFlavor[] foreignFlavors = support.getDataFlavors();
        for (final DataFlavor ff : foreignFlavors) {
            for (int i = 0; i < this.flavors.length; ++i) {
                if (ff.equals(this.flavors[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   e  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    @SuppressWarnings("unchecked") // classcastexception caught
    public final boolean importData(final TransferSupport e) {
        try {
            final Transferable tr = e.getTransferable();
            final DataFlavor[] foreignFlavors = tr.getTransferDataFlavors();
            for (int i = 0; i < foreignFlavors.length; ++i) {
                for (final DataFlavor df : flavors) {
                    if (foreignFlavors[i].equals(df)) {
                        final Object possibleData = tr.getTransferData(foreignFlavors[i]);
                        try {
                            // cast
                            final D data = (D)possibleData;
                            return insertImportDataListIntoTable(data, e);
                        } catch (ClassCastException ex) {
                            log.error("ClassCastException!", ex);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            log.error(t.getClass(), t);
        }
        // Ein Problem ist aufgetreten
        return false;
    }

    /**
     * delete the currently selected rows from the table.
     */
    public abstract void deleteCurrentSelectionFromTable();

    /**
     * import rows from data into table.
     *
     * @param   data             DOCUMENT ME!
     * @param   transferSupport  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract boolean insertImportDataListIntoTable(D data, TransferSupport transferSupport);

    /**
     * create data for the Transferable from the table.
     *
     * @param   selectedRows  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract D createExportDataListFromTable(int[] selectedRows);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public final T getTable() {
        return table;
    }
}
