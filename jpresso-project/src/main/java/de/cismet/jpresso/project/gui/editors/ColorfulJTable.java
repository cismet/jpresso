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
package de.cismet.jpresso.project.gui.editors;

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

import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class ColorfulJTable extends JTable {

    //~ Instance fields --------------------------------------------------------

    private final Map<Integer, Map<Integer, Color>> colorMap = TypeSafeCollections.newHashMap();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ColorfulJTable object.
     */
    public ColorfulJTable() {
        super();
    }

    /**
     * Creates a new ColorfulJTable object.
     *
     * @param  tm  DOCUMENT ME!
     */
    public ColorfulJTable(final TableModel tm) {
        super(tm);
    }

    /**
     * Creates a new ColorfulJTable object.
     *
     * @param  tm  DOCUMENT ME!
     * @param  cm  DOCUMENT ME!
     */
    public ColorfulJTable(final TableModel tm, final TableColumnModel cm) {
        super(tm, cm);
    }

    /**
     * Creates a new ColorfulJTable object.
     *
     * @param  rows  DOCUMENT ME!
     * @param  cols  DOCUMENT ME!
     */
    public ColorfulJTable(final int rows, final int cols) {
        super(rows, cols);
    }

    /**
     * Creates a new ColorfulJTable object.
     *
     * @param  rowData      DOCUMENT ME!
     * @param  columnNames  DOCUMENT ME!
     */
    public ColorfulJTable(final Object[][] rowData, final String[] columnNames) {
        super(rowData, columnNames);
    }

    /**
     * Creates a new ColorfulJTable object.
     *
     * @param  rowData      DOCUMENT ME!
     * @param  columnNames  DOCUMENT ME!
     */
    public ColorfulJTable(final Vector rowData, final Vector columnNames) {
        super(rowData, columnNames);
    }

    /**
     * Creates a new ColorfulJTable object.
     *
     * @param  tm  DOCUMENT ME!
     * @param  cm  DOCUMENT ME!
     * @param  sm  DOCUMENT ME!
     */
    public ColorfulJTable(final TableModel tm, final TableColumnModel cm, final ListSelectionModel sm) {
        super(tm, cm, sm);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    public void clearColors() {
        colorMap.clear();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  row     DOCUMENT ME!
     * @param  column  DOCUMENT ME!
     * @param  color   DOCUMENT ME!
     */
    public void setColor(int row, final int column, final Color color) {
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
        final Container viewport = getParent();

        // if the parent is not a viewport, calling this isn't useful
        if (!(viewport instanceof JViewport)) {
            return false;
        }

        // return true if the table's preferred height is smaller
        // than the viewport height, else false
        return getPreferredSize().height < viewport.getHeight();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   renderer  DOCUMENT ME!
     * @param   row       DOCUMENT ME!
     * @param   column    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Component prepareRenderer(final TableCellRenderer renderer, int row, int column) {
        final Component c = super.prepareRenderer(renderer, row, column);

        if (!isRowSelected(row)) {
            row = convertRowIndexToModel(row);
            column = convertColumnIndexToModel(column);
            final Map<Integer, Color> rowMap = colorMap.get(row);
            final Color color = (rowMap != null) ? rowMap.get(column) : null;
            if (color != null) {
                c.setBackground(color);
            } else {
                c.setBackground(Color.WHITE);
            }
        }
        return c;
    }
}
