/*
 * Finalizer.java
 * Created on 27. Oktober 2003, 11:11
 */
package de.cismet.jpresso.core.kernel;

import de.cismet.jpresso.core.serviceacceptor.ProgressListener;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.awt.EventQueue;
import javax.swing.table.DefaultTableModel;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JProgressBar;

/**
 * @author  srichter
 * @author  hell
 */
public abstract class Finalizer extends DefaultTableModel {

    protected String logs = "";
    /** Holds value of property intermedTables and can deliver the meta info. */
    private IntermedTablesContainer intermedTables;
    protected int tableCount = 0;
    protected String[] tableNames = null;
    protected final Map<String, JProgressBar> progress = TypeSafeCollections.newLinkedHashMap();
    private ProgressListener progressListener;
    private transient boolean canceled;
    private transient ProgressFilter progressFilter;
    private static final String TARGET_TABLE = "Target Table";
    private static final String STATUS = "Status";

    /** Creates a new instance of Finalizer */
    public Finalizer() {
        canceled = false;
        progressListener = null;
    }
    
    /**
     * 
     * @param intermedTables
     */
    protected void setIntermedTables(final IntermedTablesContainer intermedTables) {
        setIntermedTables(intermedTables, null);
    }

    /** Setter for property intermedTables.
     * @param intermedTables New value of property intermedTables.
     *
     */
    protected void setIntermedTables(final IntermedTablesContainer intermedTables, final ProgressListener progressListener) {
        if (this.getIntermedTables() != null) {
            throw new IllegalStateException("IntermedTables for this Finalizer already set!");
        }
        if(intermedTables == null) {
            throw new IllegalArgumentException("IntermedTables for Finalizer can not be null!");
        }
        this.intermedTables = intermedTables;
        tableCount = intermedTables.getNumberOfTargetTables();
        tableNames = new String[tableCount];
        int i = 0;
        int rowSum = 0;
        for (final IntermedTable iTab : intermedTables.getIntermedTables()) {
            tableNames[i] = iTab.getTableName();
            int rCount = iTab.getRowCount();
            //DANGER is not in EDT...but not used from other threads...so it should be ok
            final JProgressBar pro;
            if (rCount == 0) {
                rCount = 1;
                pro = new JProgressBar(0, rCount);
                pro.setValue(1);
            } else {
                pro = new JProgressBar(0, rCount);
            }
            pro.setBorderPainted(false);
            rowSum += rCount;
            progress.put(tableNames[i], pro);
            ++i;
        }
        if (progressListener != null) {
            progressListener.start("Finalizing...", rowSum);
            this.progressListener = progressListener;
        }
        this.progressFilter = new ProgressFilter(rowSum, progressListener);
    }

    /**
     * 
     * @param tabName
     * @param value
     * @param errors
     */
    protected void setProgressValue(final String tabName, final int value, final long errors) {
//        //System.out.println(EventQueue.isDispatchThread());
//        if (progressListener != null) {
//            progressListener.progress(++overallProgress);
//        }
//        EventQueue.invokeLater(new Runnable() {
//
//            public void run() {
//                JProgressBar j = progress.get(tabName);
//                j.setStringPainted(true);
//                j.setString(value + " (" + errors + " Fehler)");
//                j.setValue(value);
//                Finalizer.this.fireTableDataChanged();
//
//            }
//        });
//    //this.fireTableCellUpdated(row, 1);
        progressFilter.manageProgressVisualisation(progress.get(tabName), value, errors);
    }

    /**
     * 
     * @param tabName
     * @param value
     */
    protected void setProgressValue(final String tabName, final int value) {
//        if (progressListener != null) {
//        //progressListener.progress(++overallProgress);
//        }
//        JProgressBar j = progress.get(tabName);
//        j.setStringPainted(false);
//        j.setValue(value);
//        this.fireTableDataChanged();
//    //this.fireTableCellUpdated(row, 1);
        progressFilter.manageProgressVisualisation(progress.get(tabName), value);
    }

    /**
     * 
     * @param tabName
     */
    protected void setProgressCanceled(final String tabName) {
        if (isCanceled()) {
            JProgressBar b = progress.get(tabName);
            b.setStringPainted(true);
            b.setString(b.getString() + " CANCELED!");
        }
    }

    @Override
    public boolean isCellEditable(final int row, final int column) {
        return false;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public int getRowCount() {
        return tableCount;
    }

    @Override
    public Object getValueAt(final int row, final int column) {
        switch (column) {
            case 0:
                return tableNames[row];
            case 1:
                return progress.get(tableNames[row]);
        }
        return null;
    }

    @Override
    public String getColumnName(final int column) {
        switch (column) {
            case 0:
                return TARGET_TABLE;
            case 1:
                return STATUS;
        }
        return "";
    }

    @Override
    public Class getColumnClass(final int columnIndex) {
        switch (columnIndex) {
            case 0:
                return java.lang.String.class;
            case 1:
                return javax.swing.JProgressBar.class;
        }
        return Object.class;
    }

    public String getLogs() {
        return logs;
    }

    public boolean isCanceled() {
        return canceled;
    }

    /**
     * sets the cancel-flag
     * @param b
     */
    protected void setCanceled(final boolean b) {
        this.canceled = b;
    }

    /**
     * cancel the finalization
     */
    public void cancel() {
        processCancelCommand();
    }

    /**
     * Every Finalizer should implement this method in the following way:
     * 
     * setCanceled(true) and test for if(isCanceled) in the main loop
     * to interrupt the loop on "true".
     */
    protected abstract void processCancelCommand();

    public abstract long finalise() throws Exception;
    
    /**
     * 
     * @return the finalizers IntermedTables
     */
    protected IntermedTablesContainer getIntermedTables() {
        return intermedTables;
    }

    /**
     * A class that helps dealing with multiple progress visualizers, converting
     * row status to overall status and reducing calls to "setProgressForBarAndGetOverall" and 
     * therefore reduces EDT time.
     */
    class ProgressFilter {

        private int currentBarMax;
        private int latestBarValue;
        private int barThreshold;
        private JProgressBar currentBar;
        private int overAllProgress;
        private int latest;
        private final int overAllMax;
        private final ProgressListener listener;

        public ProgressFilter(final int overAllMax, final ProgressListener listener) {
            if (overAllMax < 0) {
                throw new IllegalArgumentException("Maximum for Progess can not be < 0!");
            }
            this.overAllMax = overAllMax;
            this.listener = listener;
            this.latest = 0;
            this.overAllProgress = 0;
        }

        public int manageProgressVisualisation(final JProgressBar bar, final int progress) {
            return manageProgressVisualisation(bar, progress, -9999L);
        }

        /**
         * 
         * @param bar Current bar
         * @param progress Current bar Progress
         * @param errors ErrorCounter, disabled for values < 0
         * @return Current overall Progress
         */
        public int manageProgressVisualisation(final JProgressBar bar, final int progress, final long errors) {
            if (bar == null) {
                throw new NullPointerException("No Progressbar given!");
            }
            if (!(bar == currentBar)) {
                barThreshold = bar.getMaximum() / 100;
                if (barThreshold < 1) {
                    barThreshold = 1;
                }
                latestBarValue = bar.getValue();
                currentBarMax = bar.getMaximum();
                this.currentBar = bar;
                this.latest = 0;
            }
            if (progress < latestBarValue) {
                throw new IllegalArgumentException("Invalid Progress: new value < old value!");
            }
            if (progress - latestBarValue >= barThreshold || progress == currentBarMax) {
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        bar.setStringPainted(true);
                        if (errors > -1) {
                            bar.setString(progress + " (" + errors + " Fehler)");
                        }
                        bar.setValue(progress);
                        Finalizer.this.fireTableDataChanged();
                    }
                });
                latestBarValue = progress;
            }
            overAllProgress += (progress - latest);
            latest = progress;
            if (listener != null) {
                if (latest > 0) {
                    listener.progress(overAllProgress);
                }
            }
            return 0;
        }
    }
}
