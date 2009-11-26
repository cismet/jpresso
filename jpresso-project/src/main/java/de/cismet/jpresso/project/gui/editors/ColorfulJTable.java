/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.editors;

import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 *
 * @author srichter
 */
public class ColorfulJTable extends JTable {

    public ColorfulJTable() {
        super();
    }

    public ColorfulJTable(TableModel tm, TableColumnModel cm) {
        super(tm, cm);
    }

    public ColorfulJTable(int rows, int cols) {
        super(rows, cols);
    }

    public ColorfulJTable(TableModel tm) {
        super(tm);
    }

    public ColorfulJTable(Object[][] rowData, String[] columnNames) {
        super(rowData, columnNames);
    }

    public ColorfulJTable(Vector rowData, Vector columnNames) {
        super(rowData, columnNames);
    }

    public ColorfulJTable(TableModel tm, TableColumnModel cm, ListSelectionModel sm) {
        super(tm, cm, sm);
    }
    private final Map<Integer, Map<Integer, Color>> colorMap = TypeSafeCollections.newHashMap();

    public void clearColors() {
        colorMap.clear();
    }

    /**
     * 
     * @param row
     * @param column
     * @param color
     */
    public void setColor(int row, int column, Color color) {
        if (color != null) {
            row = convertRowIndexToModel(row);
//            column = convertColumnIndexToModel(column);
            Map<Integer, Color> rowMap = colorMap.get(row);
            if (rowMap == null) {
                rowMap = TypeSafeCollections.newHashMap();
                colorMap.put(row, rowMap);
            }
            rowMap.put(column, color);
        }
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        // fetch the table's parent
        Container viewport = getParent();

        // if the parent is not a viewport, calling this isn't useful
        if (!(viewport instanceof JViewport)) {
            return false;
        }

        // return true if the table's preferred height is smaller
        // than the viewport height, else false
        return getPreferredSize().height < viewport.getHeight();
    }

    /**
     * 
     * @param renderer
     * @param row
     * @param column
     * @return
     */
    @Override
    public Component prepareRenderer(final TableCellRenderer renderer, int row, int column) {

        final Component c = super.prepareRenderer(renderer, row, column);

        if (!isRowSelected(row)) {
            row = convertRowIndexToModel(row);
            column = convertColumnIndexToModel(column);
            final Map<Integer, Color> rowMap = colorMap.get(row);
            final Color color = rowMap != null ? rowMap.get(column) : null;
            if (color != null) {
                c.setBackground(color);
            } else {
                c.setBackground(Color.WHITE);
            }
        }
        return c;
    }
}
