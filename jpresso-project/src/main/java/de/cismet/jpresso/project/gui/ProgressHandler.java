/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui;

import de.cismet.jpresso.core.serviceacceptor.ProgressListener;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;

/**
 * A class that implements the jpressocore interface "ProgressListener"
 * to visualize progress in Netbeans.
 * 
 * @author stefan
 */
public class ProgressHandler implements ProgressListener {

    public ProgressHandler() {
        this.ph = null;
        this.cancel = null;
    }

    public ProgressHandler(Cancellable cancel) {
        this();
        this.cancel = cancel;
    }
    private ProgressHandle ph;
    private Cancellable cancel;

    public void start(final String name) {
        if (ph == null) {
            ph = createHandle(name);
            ph.start();
        }
    }

    public void start(final String name, final int workUnits) {
        if (ph == null) {
            ph = createHandle(name);
            ph.start(workUnits);
        }

    }

    public void finish() {
        if (ph != null) {
            ph.finish();
        }
    }

    public void switchToDeterminate(final int progress) {
        if (ph != null) {
            ph.switchToDeterminate(progress);
        }
    }

    public void switchToIndeterminate() {
        if (ph != null) {
            ph.switchToIndeterminate();
        }
    }

    public void progress(final int progress) {
        if (ph != null) {
            ph.progress(progress);
        }
    }

    private ProgressHandle createHandle(String name) {
        if (this.cancel == null) {
            return ProgressHandleFactory.createHandle(name);
        } else {
            return ProgressHandleFactory.createHandle(name, this.cancel);
        }
    }
}
