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
package de.cismet.jpresso.project.gui.editors.autocomplete;

import java.awt.event.MouseEvent;

import java.util.EventObject;

import javax.swing.JComboBox;

/**
 * A combobox-celleditor that can set a particular minimum click count to start editing.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class MultiClickComboBoxCellEditor extends org.jdesktop.swingx.autocomplete.ComboBoxCellEditor {

    //~ Static fields/initializers ---------------------------------------------

    private static final int STD_CLICKCOUNT = 2;

    //~ Instance fields --------------------------------------------------------

    private int clickCount;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MultiClickComboBoxCellEditor object.
     *
     * @param  cBox  DOCUMENT ME!
     */
    public MultiClickComboBoxCellEditor(final JComboBox cBox) {
        super(cBox);
        clickCount = STD_CLICKCOUNT;
    }

    /**
     * Creates a new MultiClickComboBoxCellEditor object.
     *
     * @param  cBox        DOCUMENT ME!
     * @param  clickCount  DOCUMENT ME!
     */
    public MultiClickComboBoxCellEditor(final JComboBox cBox, final int clickCount) {
        super(cBox);
        this.clickCount = (clickCount > 0) ? clickCount : STD_CLICKCOUNT;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean isCellEditable(final EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent)anEvent).getClickCount() >= getClickCount();
        }
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getClickCount() {
        return clickCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  clickCount  DOCUMENT ME!
     */
    public void setClickCount(final int clickCount) {
        if (clickCount > 0) {
            this.clickCount = clickCount;
        }
    }
}
