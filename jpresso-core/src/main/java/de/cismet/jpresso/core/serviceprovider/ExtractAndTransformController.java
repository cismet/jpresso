/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.serviceprovider;

import de.cismet.jpresso.core.serviceacceptor.ProgressListener;
import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;

/**
 * FinalizerCreator, which is capable of creating multiple FinalizerController
 * that controle the writing of the extracted data into the target DB.
 * 
 * @author stefan
 */
public interface ExtractAndTransformController {

    /**
     * Create a FinalizerCreator, which is capable of creating multiple FinalizerController
     * that controle the writing of the extracted data into the target DB.
     * 
     * @param pl
     * @return
     * @throws de.cismet.jpressocore.serviceprovider.exceptions.JPressoException
     */
    public FinalizerCreator runImport(final ProgressListener pl) throws JPressoException;

    /**
     * cancel the extraction/transformation-routine.
     */
    public void cancel();

    /**
     * 
     * @return the process logging.
     */
    public String getInitializeLog();
}
