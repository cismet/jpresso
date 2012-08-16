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
package de.cismet.jpresso.project.serviceprovider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingWorker;

/**
 * Provides an ExecutorService for all SwingWorker to surpass the 10 threads limit.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public abstract class ExecutorProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final ExecutorService ex = Executors.newCachedThreadPool();

    //~ Methods ----------------------------------------------------------------

    /**
     * Execute the SwingWorker.
     *
     * @param  workerThread  DOCUMENT ME!
     */
    public static final void execute(final SwingWorker workerThread) {
        ex.execute(workerThread);
    }
}
