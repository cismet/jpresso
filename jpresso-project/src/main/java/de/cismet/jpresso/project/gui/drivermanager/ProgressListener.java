/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.drivermanager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JProgressBar;
import javax.swing.JTree;
import javax.swing.SwingWorker;

/**
 * Keeps track of a workers progress and notifies a WorkEndListener when the worker has finished. 
 * 
 * @author srichter
 */
public class ProgressListener implements PropertyChangeListener {

    public ProgressListener(final JProgressBar bar, final JTree tree) {
        this.bar = bar;
        this.tree = tree;
    }
    private final JProgressBar bar;
    private final JTree tree;
    public static final String PROGRESS = "progress";
    public static final String STATE = "state";

    public void propertyChange(final PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PROGRESS)) {
            Integer val = (Integer) evt.getNewValue();
            bar.setValue(val);
//            bar.setString(val + " / 100");
//
//            for (int i = 0; i < tree.getRowCount(); ++i) {
//                tree.expandRow(i);
//            }
        } else if (evt.getPropertyName().equals(STATE)) {
            bar.setValue(0);
            if (evt.getNewValue().equals(SwingWorker.StateValue.DONE)) {
                bar.setStringPainted(false);
                for (int i = 0; i < tree.getRowCount(); ++i) {
                    tree.expandRow(i);
                }
//                bar.setVisible(false);
            } else if (evt.getNewValue().equals(SwingWorker.StateValue.STARTED)) {
                bar.setStringPainted(true);
//                bar.setVisible(true);
            }
        }
    }
}
