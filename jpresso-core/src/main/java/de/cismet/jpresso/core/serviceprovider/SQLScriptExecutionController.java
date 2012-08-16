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

import java.sql.Connection;

import de.cismet.jpresso.core.serviceacceptor.ProgressListener;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface SQLScriptExecutionController {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    long execute();

    /**
     * DOCUMENT ME!
     *
     * @param   progress  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    long execute(final ProgressListener progress);

    /**
     * DOCUMENT ME!
     *
     * @return  the logs
     */
    String getLogs();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Connection getTargetConn();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isCanceled();

    /**
     * DOCUMENT ME!
     *
     * @param  canceled  DOCUMENT ME!
     */
    void setCanceled(final boolean canceled);

    /**
     * DOCUMENT ME!
     *
     * @param  targetConn  DOCUMENT ME!
     */
    void setTargetConn(final Connection targetConn);

    /**
     * DOCUMENT ME!
     *
     * @param  test  DOCUMENT ME!
     */
    void setTest(final boolean test);
}
