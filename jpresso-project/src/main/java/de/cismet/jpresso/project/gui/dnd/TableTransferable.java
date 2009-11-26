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
 * @author srichter
 */
public class TableTransferable<T> implements Transferable {
    
    /**
     * 
     * @param data
     * @param flavor
     */
    public TableTransferable(final T data, final DataFlavor[] flavor) {
        this.data = data;
        this.flavor = flavor.clone();
    }    
    
    //The Data to transfer
    private T data;
    //The Dataflavor beloning to this kind of data.
    private DataFlavor[] flavor;

    public DataFlavor[] getTransferDataFlavors() {
        return flavor;
    }

    /**
     * @see DataFlavor
     * @param foreign
     * @return
     */
    public boolean isDataFlavorSupported(final DataFlavor foreign) {
        for (DataFlavor df : flavor) {
            if (df.equals(foreign)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see DataFlavor
     * @param foreignFlavor
     * @return
     * @throws java.awt.datatransfer.UnsupportedFlavorException
     * @throws java.io.IOException
     */
    public T getTransferData(final DataFlavor foreignFlavor) throws UnsupportedFlavorException, IOException {
        for (DataFlavor df : flavor) {
            if (foreignFlavor.equals(df)) {
                return this.data;
            }
        }
        throw new UnsupportedFlavorException(foreignFlavor);

    }
}
