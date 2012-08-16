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
import java.awt.datatransfer.UnsupportedFlavorException;

import java.io.IOException;

/**
 * The transferable class with input for the editor tables (mappings, references).
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class TableTransferable<T> implements Transferable {

    //~ Instance fields --------------------------------------------------------

    // The Data to transfer
    private T data;
    // The Dataflavor beloning to this kind of data.
    private DataFlavor[] flavor;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new TableTransferable object.
     *
     * @param  data    DOCUMENT ME!
     * @param  flavor  DOCUMENT ME!
     */
    public TableTransferable(final T data, final DataFlavor[] flavor) {
        this.data = data;
        this.flavor = flavor.clone();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return flavor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   foreign  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @see     DataFlavor
     */
    @Override
    public boolean isDataFlavorSupported(final DataFlavor foreign) {
        for (final DataFlavor df : flavor) {
            if (df.equals(foreign)) {
                return true;
            }
        }
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   foreignFlavor  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  UnsupportedFlavorException  DOCUMENT ME!
     * @throws  IOException                 DOCUMENT ME!
     *
     * @see     DataFlavor
     */
    @Override
    public T getTransferData(final DataFlavor foreignFlavor) throws UnsupportedFlavorException, IOException {
        for (final DataFlavor df : flavor) {
            if (foreignFlavor.equals(df)) {
                return this.data;
            }
        }
        throw new UnsupportedFlavorException(foreignFlavor);
    }
}
