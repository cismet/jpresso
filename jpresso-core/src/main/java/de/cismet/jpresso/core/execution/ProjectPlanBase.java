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
package de.cismet.jpresso.core.execution;

import de.cismet.jpresso.core.serviceprovider.AntUniversalExecutor;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public abstract class ProjectPlanBase {

    //~ Static fields/initializers ---------------------------------------------

    public static AntUniversalExecutor EXECUTOR;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    public abstract void start();
}
