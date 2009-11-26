/*
 * JDBCImportExecutor.java
 *
 * Created on 28. Oktober 2003, 11:40
 */
package de.cismet.jpresso.core.finalizer;

import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;
import de.cismet.jpresso.core.kernel.Finalizer;
import de.cismet.jpresso.core.kernel.IntermedTable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author  srichter
 */
public final class BatchFinalizer extends Finalizer {

    /** Logger */
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private boolean debug = log.isDebugEnabled();
    public static final int MAX_LOG_ERROR = 20;
    /** Holds value of property rollback. */
    private final StringBuilder buff = new StringBuilder();
    private boolean rb = true;
    private boolean force = false;
    private String[] updateSequences = null;

    private boolean evalString(final String param) {
        if (param != null) {

            if (param.equalsIgnoreCase("true")) {
                return true;
            } else if (param.equalsIgnoreCase("false")) {
                return false;
            } else {
                throw new IllegalArgumentException("Illegal Rollback argument. Found " + param + "! Please provide 'true' or 'false'!");
            }
        }
        return false;
    }

    public void setForceWrite(final String in) {
        force = evalString(in);
    }

    public void setUpdateSequences(final String in) {
        if (in != null) {
            try {
                if (in.contains(",")) {
                    updateSequences = in.split(",");
                    for (int i = 0; i < updateSequences.length; ++i) {
                        updateSequences[i] = updateSequences[i].trim();
                    }
                } else {
                    updateSequences = new String[]{in.trim()};
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Illegal UpdateSequences argument. Found " + in + "!\nPlease provide a list like: Tabname1:Sequence1[, Tabname2:Sequence2,...]!");
            }
        }
    }

    /** Setter for property param.
     * @param param New value of property param.
     *
     */
    public void setRollback(final String rollback) {
        log.debug("Rollback got: " + rollback);
        if (evalString(rollback)) {
            log.info("Rollback was set true. The transcation will be rolled back!");
            rb = true;
        } else {
            rb = false;
        }
    }

    /** 
     * The method that actually performs all the writing to DB.
     */
    @Override
    public long finalise() throws Exception {
        log.debug("finalise");
        long errorCounter = 0;
        String stmnt;
        final Connection conn = getIntermedTables().getTargetConn();
//        conn.setAutoCommit(rb);
        try {
            conn.setAutoCommit(force);
        } catch (SQLException ex) {
            log.error("Could not execute conn.setAutoCommit(" + force + ");", ex);
        }
        for (final String tableName : getIntermedTables().getMetaInfo().getTopologicalTableSequence()) {
            final IntermedTable itab = getIntermedTables().getIntermedTable(tableName);
            final int tableRowCount = itab.getRowCount();
            System.out.println("finalizing ---> " + tableName);
            if (debug) {
                String debugString = "Import into table: " + tableName + " (" + tableRowCount + " rows)\n";
                log.debug(debugString);
            }
            buff.append("\n" + "Import into table: " + tableName + " (" + tableRowCount + " rows)\n");

            int logErrorCounter = 0;
            final Statement s = conn.createStatement();
            int j = 0;
            for (; j < tableRowCount; ++j) {
                //TODO processCancelCommand handling in superclass
                if (isCanceled()) {
                    conn.rollback();
                    log.info("cancel -> rollback");
                    logs += buff.toString();
                    setProgressCanceled(tableName);
                    return errorCounter;
                }
                stmnt = getFixedPartOfInsertStatement(itab) + getValuesForInsert(itab, j);
                if (debug) {
                    log.debug("Statement: " + stmnt);
                }
                try {
                    s.addBatch(stmnt);
                    setProgressValue(tableName, j + 1, logErrorCounter);
                } catch (SQLException ex) {
                    ++errorCounter;
                    ++logErrorCounter;
                    final String msg = "Error at:" + stmnt + ": " + ex;
                    log.error(msg);
                    log.debug(msg + stmnt, ex);
                    setProgressValue(tableName, j + 1, logErrorCounter);
                    //switch to rollback as error occured
                    rb = true;
                    if (logErrorCounter < MAX_LOG_ERROR) {
                        logs += "    Import error @ statement:" + stmnt + "\n" + ex.toString() + "\n";
                    } else if (logErrorCounter == MAX_LOG_ERROR) {
                        logs += "    ************** more errors (output stopped)\n";
                    }
                }
            }
            try {
                final int[] res = s.executeBatch();
                s.close();
                setProgressValue(tableName, j, errorCounter);
            } catch (SQLException sqlEx) {
                ++errorCounter;
                ++logErrorCounter;
                do {
                    log.error(sqlEx);
                    sqlEx = sqlEx.getNextException();
                    if (logErrorCounter < MAX_LOG_ERROR) {
                        logs += "    Import error @ statement:" + sqlEx + "\n";
                    } else if (logErrorCounter == MAX_LOG_ERROR) {
                        logs += "    ************** more errors (output stopped)\n";
                    }
                } while (sqlEx != null);
                setProgressValue(tableName, j, errorCounter);
                //switch to rollback as error occured
                rb = true;
            }
        }
        try {
            if (rb && !force) {
                conn.rollback();
            } else {
                conn.commit();
                if (updateSequences != null) {
                    final Statement s = conn.createStatement();
                    for (final String seq : updateSequences) {
                        final String[] split = seq.split(":");
                        if (split != null && split.length == 2) {
                            s.execute("SELECT SETVAL('" + split[1].trim() + "',(SELECT max(ID)+1 FROM " + split[0].trim() + "))");
                        }
                    }
                    s.close();
                }
            }
            //.execute("ROLLBACK");
        } catch (SQLException ex) {
            final String msg = "Error on: ROLLBACK or COMMIT: " + ex;
            log.error(msg);
            log.debug(msg);
            logs += "    Import error .. rollback statement\n" + ex.toString() + "\n";
        }
        log.info("Import finished");
        buff.append("\n\n-----------------Import finished");
        logs += buff.toString();
        return errorCounter;
    }

    protected String getValuesForInsert(final IntermedTable itab, final int position) throws JPressoException {
        return "(" + itab.getRowStringWithGivenEnclosingChar(position, ",") + ")";
    }

    protected String getFixedPartOfInsertStatement(final IntermedTable itab) throws JPressoException {
        return "INSERT INTO " + itab.getTableName() + "(" + getFieldList(itab) + ") VALUES";
    }

    protected String getFieldList(final IntermedTable itab) throws JPressoException {
        final StringBuilder sBuff = new StringBuilder();
        for (int i = 0; i < itab.getColumnCount() - 1; ++i) {
            sBuff.append(itab.getColumnName(i)).append(",");
        }
        sBuff.append(itab.getColumnName(itab.getColumnCount() - 1));
        return sBuff.toString();
    }

    @Override
    protected void processCancelCommand() {
        setCanceled(true);
    }
}
