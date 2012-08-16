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
package de.cismet.jpresso.core.serviceacceptor;

/**
 * Interface for classes that can handle progress values.
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public interface ProgressListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  name  DOCUMENT ME!
     */
    void start(String name);

    /**
     * DOCUMENT ME!
     *
     * @param  name       DOCUMENT ME!
     * @param  workUnits  DOCUMENT ME!
     */
    void start(String name, int workUnits);

    /**
     * DOCUMENT ME!
     */
    void finish();

    /**
     * DOCUMENT ME!
     *
     * @param  progress  DOCUMENT ME!
     */
    void switchToDeterminate(int progress);

    /**
     * DOCUMENT ME!
     */
    void switchToIndeterminate();

    /**
     * DOCUMENT ME!
     *
     * @param  progress  DOCUMENT ME!
     */
    void progress(int progress);
}
