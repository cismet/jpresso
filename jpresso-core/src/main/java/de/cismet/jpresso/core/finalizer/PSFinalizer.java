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
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * WARNING! This finalizer is not finished yet!issues on postgis gteometry as well as 
 * string quotation!
 * @author  srichter
 */
//TODO logs rausschmeissen und ganz durch buff ersetzen!
public final class PSFinalizer extends Finalizer {

    /** Logger */
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private boolean debug = log.isDebugEnabled();
    public static final int MAX_LOG_ERROR = 20;
    /** Holds value of property rollback. */
    private String rollback;
    private final StringBuilder buff = new StringBuilder();
    boolean rb;
    private int[] sqlTypes;

    /** Creates a new instance of StandardFinalizer */
    public PSFinalizer() {
    }

    /** Setter for property rollback.
     * @param rollback New value of property rollback.
     *
     */
    public void setRollback(final String rollback) throws IllegalArgumentException {
        log.debug("Rollback got: " + rollback);
        // :-(
        // GEFAHR
        this.rollback = rollback;
        if (rollback.equalsIgnoreCase("true")) {
            log.info("Rollback was set true. The transcation will be rolled back!");
            rb = true;
        } else if (rollback.equalsIgnoreCase("false")) {
            rb = false;
        } else {
            throw new IllegalArgumentException("Illegal Rollback argument. Found " + rollback + "! Please provide 'true' or 'false'!");
        }
    }

    /** 
     * The method that actually performs all the writing to DB.
     */
    public long finalise() throws Exception {
        log.debug("finalise");
        long errorCounter = 0;
        String stmnt;
        final Connection conn = getIntermedTables().getTargetConn();
        conn.setAutoCommit(false);
        for (final String tableName : getIntermedTables().getMetaInfo().getTopologicalTableSequence()) {
            final IntermedTable itab = getIntermedTables().getIntermedTable(tableName);
            final int tableRowCount = itab.getRowCount();
            sqlTypes = new int[itab.getColumnCount()];
            final PreparedStatement metaPS = conn.prepareStatement("SELECT * from " + tableName + " WHERE 1 = 0");
            final ResultSetMetaData rsm = metaPS.getMetaData();
            try {
                for (int i = 0; true; ++i) {
                    final String colName = rsm.getColumnName(i + 1);
                    final int col = itab.getColumnNumberIgnoreCase(colName);
                    if (col > -1) {
                        final int ctype = rsm.getColumnType(i + 1);
                        sqlTypes[col] = ctype;
                    }
                }
            } catch (SQLException se) {
            }
//        for (int i = 0; i < getIntermedTables().getNumberOfTargetTables(); ++i) {
//            String tableName = getIntermedTables().getMetaInfo().getTopologicalTableSequence().get(i);
            System.out.println("finalizing ---> " + tableName);
            if (debug) {
                String debugString = "Import into table: " + tableName + " (" + tableRowCount + " rows)\n";
                log.debug(debugString);
            }
            buff.append("\n" + "Import into table: " + tableName + " (" + tableRowCount + " rows)\n");

            int logErrorCounter = 0;
            stmnt = getPreparedStatementText(itab);
            if (debug) {
                log.debug("Statement: " + stmnt);
            }
            final PreparedStatement s = conn.prepareStatement(stmnt);
            //Statement s = conn.createStatement();
            for (int j = 0; j < tableRowCount; ++j) {
                //TODO processCancelCommand handling in superclass
                if (isCanceled()) {
                    conn.rollback();
                    log.info("cancel -> rollback");
                    logs += buff.toString();
                    setProgressCanceled(tableName);
                    return errorCounter;
                }

                try {
                    fillPreparedStatement(s, itab, j);
                    s.executeUpdate();
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
            s.close();
        }
        try {
            if (rb) {
                conn.rollback();
            } else {
                conn.commit();
            }
        //.execute("ROLLBACK");
        } catch (SQLException ex) {

            final String msg = "Error on: ROLLBACK: " + ex;
            log.error(msg);
            log.debug(msg);
            logs += "    Import error .. rollback statement\n" + ex.toString() + "\n";
//            System.out.println("done.");
        }
//HINT: Do not close, so that multiple finalizations with one connection are possible!!
//        if (!conn.isClosed()) {
//            conn.close();
//        } // todo: check
        log.info("Import finished");
        buff.append("\n\n-----------------Import finished");
        logs += buff.toString();
        return errorCounter;
    }

    protected String getPreparedStatementText(final IntermedTable itab) throws JPressoException {
        itab.getColumnCount();
        StringBuilder endPart = new StringBuilder("INSERT INTO " + itab.getTableName() + "(" + getFieldList(itab) + ") VALUES (");
        for (int i = 0; i < itab.getColumnCount(); ++i) {
            endPart.append(" ?,");
        }
        endPart.deleteCharAt(endPart.length() - 1);
        endPart.append(")");
        return endPart.toString();
    }

    protected void fillPreparedStatement(PreparedStatement ps, final IntermedTable itab, int row) throws SQLException {
        for (int i = 0; i < itab.getColumnCount(); ++i) {
            int myst = sqlTypes[i];
            if (myst == java.sql.Types.OTHER) {
                myst = java.sql.Types.VARCHAR;
            }
            final String value = itab.getValueAt(row, i);
            if (value != null) {
                ps.setObject(i + 1, value, myst);
            } else {
                ps.setNull(i + 1, myst);
            }
        }

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
