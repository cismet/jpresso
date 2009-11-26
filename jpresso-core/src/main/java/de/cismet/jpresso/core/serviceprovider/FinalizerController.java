/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.serviceprovider;

import de.cismet.jpresso.core.serviceprovider.exceptions.FinalizerException;
import javax.swing.table.TableModel;

/**
 * Controller for the finalization of an import process.
 * 
 * @author stefan
 */
public interface FinalizerController {
    
    /**
     * 
     * @return the output visualisation as a table model
     */
    public TableModel getFinalizerOutputTable();
    
    /**
     * invokes the finalization of the import process.
     * 
     * @return the error count of the finalization process
     * @throws de.cismet.jpressocore.serviceprovider.exceptions.FinalizerException
     */
    public long finalise() throws FinalizerException;
    
    /**
     * 
     * @return the logging of the finalization process
     */
    public String getLogs();
    
    /**
     * recquests cancellation of a running finalization process
     */
    public void cancel();
    
    /**
     * 
     * @return true if the finalization was canceled
     */
    public boolean isCanceled();
}
