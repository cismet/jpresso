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
package de.cismet.jpresso.project.gui;

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;

import org.openide.util.Cancellable;

import de.cismet.jpresso.core.serviceacceptor.ProgressListener;

/**
 * A class that implements the jpressocore interface "ProgressListener" to visualize progress in Netbeans.
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public class ProgressHandler implements ProgressListener {

    //~ Instance fields --------------------------------------------------------

    private ProgressHandle ph;
    private Cancellable cancel;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProgressHandler object.
     */
    public ProgressHandler() {
        this.ph = null;
        this.cancel = null;
    }

    /**
     * Creates a new ProgressHandler object.
     *
     * @param  cancel  DOCUMENT ME!
     */
    public ProgressHandler(final Cancellable cancel) {
        this();
        this.cancel = cancel;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void start(final String name) {
        if (ph == null) {
            ph = createHandle(name);
            ph.start();
        }
    }

    @Override
    public void start(final String name, final int workUnits) {
        if (ph == null) {
            ph = createHandle(name);
            ph.start(workUnits);
        }
    }

    @Override
    public void finish() {
        if (ph != null) {
            ph.finish();
        }
    }

    @Override
    public void switchToDeterminate(final int progress) {
        if (ph != null) {
            ph.switchToDeterminate(progress);
        }
    }

    @Override
    public void switchToIndeterminate() {
        if (ph != null) {
            ph.switchToIndeterminate();
        }
    }

    @Override
    public void progress(final int progress) {
        if (ph != null) {
            ph.progress(progress);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   name  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private ProgressHandle createHandle(final String name) {
        if (this.cancel == null) {
            return ProgressHandleFactory.createHandle(name);
        } else {
            return ProgressHandleFactory.createHandle(name, this.cancel);
        }
    }
}
