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

import javax.swing.table.TableModel;

import de.cismet.jpresso.core.serviceprovider.exceptions.FinalizerException;

/**
 * Controller for the finalization of an import process.
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public interface FinalizerController {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the output visualisation as a table model
     */
    TableModel getFinalizerOutputTable();

    /**
     * invokes the finalization of the import process.
     *
     * @return  the error count of the finalization process
     *
     * @throws  FinalizerException  de.cismet.jpressocore.serviceprovider.exceptions.FinalizerException
     */
    long finalise() throws FinalizerException;

    /**
     * DOCUMENT ME!
     *
     * @return  the logging of the finalization process
     */
    String getLogs();

    /**
     * recquests cancellation of a running finalization process.
     */
    void cancel();

    /**
     * DOCUMENT ME!
     *
     * @return  true if the finalization was canceled
     */
    boolean isCanceled();
}
