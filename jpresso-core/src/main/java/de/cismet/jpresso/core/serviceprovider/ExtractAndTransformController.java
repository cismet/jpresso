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

import de.cismet.jpresso.core.serviceacceptor.ProgressListener;
import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;

/**
 * FinalizerCreator, which is capable of creating multiple FinalizerController that controle the writing of the
 * extracted data into the target DB.
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public interface ExtractAndTransformController {

    //~ Methods ----------------------------------------------------------------

    /**
     * Create a FinalizerCreator, which is capable of creating multiple FinalizerController that controle the writing of
     * the extracted data into the target DB.
     *
     * @param   pl  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  JPressoException  de.cismet.jpressocore.serviceprovider.exceptions.JPressoException
     */
    FinalizerCreator runImport(final ProgressListener pl) throws JPressoException;

    /**
     * cancel the extraction/transformation-routine.
     */
    void cancel();

    /**
     * DOCUMENT ME!
     *
     * @return  the process logging.
     */
    String getInitializeLog();
}
