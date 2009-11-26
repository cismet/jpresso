/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.editors;

import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 * A basic class for a generic List-based TableModel
 * 
 * @author srichter
 */
public abstract class ListTableModel<T> extends DefaultTableModel {

    public ListTableModel(List<T> data) {
        super();
        this.tableData = data;
        adjusting = false;
        fireTableDataChanged();
    }
    private List<T> tableData;
    private boolean adjusting;

    public void setTableData(final List<T> data) {
        this.tableData = data;
        if (!adjusting) {
            fireTableDataChanged();
        }
    }

    public void addRow(final T data) {
        tableData.add(data);
        if (!adjusting) {
//            fireTableRowsInserted(index, index);
            this.fireTableDataChanged();
        }
    }

    public void setRow(final T rowData, int index) {
        if (index > -1 && index < tableData.size()) {
            tableData.set(index, rowData);
            if (!adjusting) {
//            fireTableRowsUpdated(index, index);
                this.fireTableDataChanged();
            }
        }
    }

    public void addRow(final T rowData, int index) {
        if (index > tableData.size()) {
            index = tableData.size();
        }
        tableData.add(index, rowData);
        if (!adjusting) {
//            fireTableRowsInserted(index, index);
            this.fireTableDataChanged();
        }
    }

    @Override
    public void removeRow(final int row) {
        tableData.remove(row);
        if (!adjusting) {
//            fireTableRowsDeleted(row, row);
            this.fireTableDataChanged();
        }
    }

    public void removeRow(final T rowData) {
        final int index = tableData.indexOf(rowData);
        tableData.remove(rowData);
        if (!adjusting) {
            fireTableRowsDeleted(index, index);
        }
//        tableData.remove(rowData);
//        if (!adjusting) {
//            this.fireTableDataChanged();
//        }
    }

    public List<T> getRows() {
        return tableData;
    }

    public T getRow(final int i) {
        if (i < tableData.size() && i > -1) {
            return tableData.get(i);
        }
        return null;
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return true;
    }

    @Override
    public final int getRowCount() {
        if (tableData == null) {
            return 0;
        }
        return tableData.size();
    }
    //---Methods to reimplement by concrete instances
    @Override
    public abstract Class getColumnClass(final int columnIndex);

    @Override
    public abstract int getColumnCount();

    @Override
    public abstract String getColumnName(final int columnIndex);

    @Override
    public abstract Object getValueAt(final int rowIndex, final int columnIndex);

    @Override
    public abstract void setValueAt(final Object aValue, final int rowIndex, final int columnIndex);

    public boolean isAdjusting() {
        return adjusting;
    }

    public void setAdjusting(boolean adjusting) {
        this.adjusting = adjusting;
    }
}
