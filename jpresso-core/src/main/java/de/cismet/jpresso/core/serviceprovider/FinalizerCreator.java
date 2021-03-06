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
package de.cismet.jpresso.core.serviceprovider;

import java.util.Collection;

import javax.swing.table.TableModel;

import de.cismet.jpresso.core.data.RuntimeProperties;
import de.cismet.jpresso.core.serviceacceptor.ProgressListener;
import de.cismet.jpresso.core.serviceprovider.exceptions.FinalizerException;

/**
 * Can create multiple FinalizerController to controll the writing-to-target-DB procedure.
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public interface FinalizerCreator {

    //~ Methods ----------------------------------------------------------------

    /**
     * Method that creates the FinalizerController.
     *
     * @param   rP  DOCUMENT ME!
     * @param   pl  DOCUMENT ME!
     *
     * @return  a finalizer controller for the import process
     *
     * @throws  FinalizerException  de.cismet.jpressocore.serviceprovider.exceptions.FinalizerException
     */
    FinalizerController createFinalizer(final RuntimeProperties rP, final ProgressListener pl)
            throws FinalizerException;

    /**
     * DOCUMENT ME!
     *
     * @return  the filled Intermediate Table, which contains the data to be written to target DB by the finalizer.
     */
    Collection<? extends TableModel> getIntermedTableModels();

    /**
     * DOCUMENT ME!
     *
     * @param   table  DOCUMENT ME!
     *
     * @return  the enclosing charcters for a given table
     */
    String[] getEnclosingCharacters(TableModel table);

    /**
     * close all connections and release resources.
     */
    void close();
}
