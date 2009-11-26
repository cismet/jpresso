/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.drivermanager;

import de.cismet.jpresso.project.serviceprovider.ExecutorProvider;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultListModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListDataListener;

/**
 * A list model for the driver list.
 * 
 * @author srichter
 */
public class DriverListModel extends DefaultListModel {

    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(getClass());

    /**
     * 
     * @param i
     */
    public void refresh(int i) {
        super.fireContentsChanged(this, i, i);
    }

    /**
     * 
     * @param descending
     */
    public void sort(boolean descending) {
        ListSortWorker lsw = new ListSortWorker(descending);
        ExecutorProvider.execute(lsw);
//        lsw.execute();
    }

    /**
     * Sorts the driverlist without gui-block
     */
    final class ListSortWorker extends SwingWorker<Object[], Void> {

        public ListSortWorker(boolean descending) {
            this.descending = descending;
        }
        private boolean descending;

        @Override
        protected Object[] doInBackground() throws Exception {
            Object[] toSort = toArray();
            Arrays.sort(toSort);
            return toSort;
        }

        @Override
        protected void done() {
            try {
                Object[] toAdd = get();
                ListDataListener[] listener = getListDataListeners();
                for (ListDataListener l : listener) {
                    removeListDataListener(l);
                }
                clear();
                if (descending) {
                    for (Object o : toAdd) {
                        addElement(o);
                    }
                } else {
                    for (int i = toAdd.length - 1; i >= 0; --i) {
                        addElement(toAdd[i]);
                    }
                }
                for (ListDataListener l : listener) {
                    addListDataListener(l);
                }
                fireContentsChanged(this, 0, size());
            } catch (InterruptedException ex) {
                log.warn("Error while sorting driver list!", ex);
            } catch (ExecutionException ex) {
                log.warn("Error while sorting driver list!", ex);
            }
        }
    }
}
