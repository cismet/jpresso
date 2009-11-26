/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.serviceprovider;

import de.cismet.jpresso.core.data.RuntimeProperties;
import de.cismet.jpresso.core.serviceacceptor.ProgressListener;
import de.cismet.jpresso.core.serviceprovider.exceptions.FinalizerException;
import java.util.Collection;
import javax.swing.table.TableModel;

/**
 * Can create multiple FinalizerController to controll the writing-to-target-DB
 * procedure.
 * 
 * @author stefan
 */
public interface FinalizerCreator {
    
    /**
     * Method that creates the FinalizerController
     * 
     * @param rP
     * @param pl
     * @return a finalizer controller for the import process
     * @throws de.cismet.jpressocore.serviceprovider.exceptions.FinalizerException
     */
    public FinalizerController createFinalizer(final RuntimeProperties rP, final ProgressListener pl) throws FinalizerException;
    
    /**
     * 
     * @return the filled Intermediate Table, which contains the data to be written
     * to target DB by the finalizer.
     */
    public Collection<? extends TableModel> getIntermedTableModels();

    /**
     *
     * @param table
     * @return the enclosing charcters for a given table
     */
    public String[] getEnclosingCharacters(TableModel table);

    /**
     * close all connections and release resources.
     */
    public void close();
}
