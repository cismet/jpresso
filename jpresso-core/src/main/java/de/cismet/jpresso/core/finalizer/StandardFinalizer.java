/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * JDBCImportExecutor.java
 *
 * Created on 28. Oktober 2003, 11:40
 */
package de.cismet.jpresso.core.finalizer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import de.cismet.jpresso.core.kernel.Finalizer;
import de.cismet.jpresso.core.kernel.IntermedTable;
import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class StandardFinalizer extends Finalizer {

    //~ Static fields/initializers ---------------------------------------------

    public static final int MAX_LOG_ERROR = 20;

    //~ Instance fields --------------------------------------------------------

    /** Logger. */
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private boolean debug = log.isDebugEnabled();
    /** Holds value of property rollback. */
    private final StringBuilder buff = new StringBuilder();
    private boolean rb = true;
    private boolean force = false;
    private String[] updateSequences = null;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   param  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    private boolean evalString(final String param) {
        if (param.equalsIgnoreCase("true")) {
            return true;
        } else if (param.equalsIgnoreCase("false")) {
            return false;
        } else {
            throw new IllegalArgumentException("Illegal Rollback argument. Found " + param
                        + "! Please provide 'true' or 'false'!");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  in  DOCUMENT ME!
     */
    public void setForceWrite(final String in) {
        force = evalString(in);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   in  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public void setUpdateSequences(final String in) {
        if (in != null) {
            try {
                if (in.contains(",")) {
                    updateSequences = in.split(",");
                    for (int i = 0; i < updateSequences.length; ++i) {
                        updateSequences[i] = updateSequences[i].trim();
                    }
                } else {
                    updateSequences = new String[] { in.trim() };
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Illegal UpdateSequences argument. Found " + in
                            + "!\nPlease provide a list like: Tabname1:Sequence1[, Tabname2:Sequence2,...]!");
            }
        }
    }

    /**
     * Setter for property param.
     *
     * @param  rollback  param New value of property param.
     */
    public void setRollback(final String rollback) {
        if (log.isDebugEnabled()) {
            log.debug("Rollback got: " + rollback);
        }
        if (evalString(rollback)) {
            log.info("Rollback was set true. The transcation will be rolled back!");
            rb = true;
        } else {
            rb = false;
        }
    }

    /**
     * The method that actually performs all the writing to DB.
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Override
    public long finalise() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("finalise");
        }
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
                final String debugString = "Import into table: " + tableName + " (" + tableRowCount + " rows)\n";
                if (log.isDebugEnabled()) {
                    log.debug(debugString);
                }
            }
            buff.append("\n" + "Import into table: " + tableName + " (" + tableRowCount + " rows)\n");

            int logErrorCounter = 0;
            for (int j = 0; j < tableRowCount; ++j) {
                // TODO processCancelCommand handling in superclass
                if (isCanceled()) {
                    conn.rollback();
                    log.info("cancel -> rollback");
                    logs += buff.toString();
                    setProgressCanceled(tableName);
                    return errorCounter;
                }
                stmnt = getFixedPartOfInsertStatement(itab) + getValuesForInsert(itab, j);
                if (debug) {
                    if (log.isDebugEnabled()) {
                        log.debug("Statement: " + stmnt);
                    }
                }
                final Statement s = conn.createStatement();
                try {
                    s.execute(stmnt);
                    s.close();
                    setProgressValue(tableName, j + 1, logErrorCounter);
                } catch (SQLException ex) {
                    ++errorCounter;
                    ++logErrorCounter;
                    final String msg = "Error at:" + stmnt + ": " + ex;
                    log.error(msg);
                    if (log.isDebugEnabled()) {
                        log.debug(msg + stmnt, ex);
                    }
                    setProgressValue(tableName, j + 1, logErrorCounter);
                    // switch to rollback as error occured
                    rb = true;
                    if (logErrorCounter < MAX_LOG_ERROR) {
                        logs += "    Import error @ statement:" + stmnt + "\n" + ex.toString() + "\n";
                    } else if (logErrorCounter == MAX_LOG_ERROR) {
                        logs += "    ************** more errors (output stopped)\n";
                    }
                }
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
                        if ((split != null) && (split.length == 2)) {
                            s.execute("SELECT SETVAL('" + split[1].trim() + "',(SELECT max(ID)+1 FROM "
                                        + split[0].trim() + "))");
                        }
                    }
                    s.close();
                }
            }
            // .execute("ROLLBACK");
        } catch (SQLException ex) {
            final String msg = "Error on: ROLLBACK or COMMIT: " + ex;
            log.error(msg);
            if (log.isDebugEnabled()) {
                log.debug(msg);
            }
            logs += "    Import error .. rollback statement\n" + ex.toString() + "\n";
        }
        log.info("Import finished");
        buff.append("\n\n-----------------Import finished");
        logs += buff.toString();
        return errorCounter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   itab      DOCUMENT ME!
     * @param   position  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  JPressoException  DOCUMENT ME!
     */
    protected String getValuesForInsert(final IntermedTable itab, final int position) throws JPressoException {
        return "(" + itab.getRowStringWithGivenEnclosingChar(position, ",") + ")";
    }

    /**
     * DOCUMENT ME!
     *
     * @param   itab  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  JPressoException  DOCUMENT ME!
     */
    protected String getFixedPartOfInsertStatement(final IntermedTable itab) throws JPressoException {
        return "INSERT INTO " + itab.getTableName() + "(" + getFieldList(itab) + ") VALUES";
    }

    /**
     * DOCUMENT ME!
     *
     * @param   itab  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  JPressoException  DOCUMENT ME!
     */
    protected String getFieldList(final IntermedTable itab) throws JPressoException {
        final StringBuilder sBuff = new StringBuilder();
        for (int i = 0; i < (itab.getColumnCount() - 1); ++i) {
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
