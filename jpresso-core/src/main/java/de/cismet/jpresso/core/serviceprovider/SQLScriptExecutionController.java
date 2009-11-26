/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.jpresso.core.serviceprovider;

import de.cismet.jpresso.core.serviceacceptor.ProgressListener;
import java.sql.Connection;

/**
 *
 * @author srichter
 */
public interface SQLScriptExecutionController {

    /**
     *
     * @return
     * @throws de.cismet.jpressocore.exceptions.InitializingException
     */
    long execute();

    /**
     *
     * @param progress
     * @return
     * @throws de.cismet.jpressocore.exceptions.InitializingException
     */
    long execute(final ProgressListener progress);

    /**
     *
     * @return the logs
     */
    String getLogs();

    /**
     *
     * @return
     */
    Connection getTargetConn();

    /**
     *
     * @return
     */
    boolean isCanceled();

    /**
     *
     * @param canceled
     */
    void setCanceled(final boolean canceled);

    /**
     *
     * @param targetConn
     */
    void setTargetConn(final Connection targetConn);

    /**
     *
     * @param test
     */
    void setTest(final boolean test);

}
