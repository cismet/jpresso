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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.cismet.jpresso.core.exceptions.WrongNameException;
import de.cismet.jpresso.core.kernel.Finalizer;
import de.cismet.jpresso.core.kernel.IntermedTable;
import de.cismet.jpresso.core.serviceprovider.exceptions.FinalizerException;
import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class UpdateFinalizer extends Finalizer {

    //~ Static fields/initializers ---------------------------------------------

    /** Logger. */
    private static final String KOMMA_SPACE = ", ";
    private static final String SPACE_AND_SPACE = " AND ";
    public static final int MAX_LOG_ERROR = 20;

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private boolean debug = log.isDebugEnabled();
    /** Holds value of property rollback. */
    private final StringBuilder buff = new StringBuilder();
    private final Map<String, int[]> whereMap;
    private String[] tableCompareString;
    private boolean rb = true;
    private boolean force = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of ABFImporter.
     */
    public UpdateFinalizer() {
        this.whereMap = TypeSafeCollections.newHashMap();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  in  DOCUMENT ME!
     */
    public void setTableCompareFields(final String in) {
        if (in != null) {
            tableCompareString = in.split("&&");
        }
    }

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
     * @param   in  DOCUMENT ME!
     *
     * @throws  JPressoException  DOCUMENT ME!
     */
    public void setForceWrite(final String in) throws JPressoException {
        force = evalString(in);
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
     * @throws  Exception                 DOCUMENT ME!
     * @throws  FinalizerException        DOCUMENT ME!
     * @throws  IllegalArgumentException  DOCUMENT ME!
     * @throws  WrongNameException        DOCUMENT ME!
     */
    @Override
    public long finalise() throws Exception {
        if (tableCompareString == null) {
            throw new FinalizerException(
                "Missing TableCompareFields arguments.\nSyntax: TableCompareFields=TABLENAME1:FIELD1[, FIELD2, ...] [&& TABLENAME2:FIELD1[, FIELD2, ...]]");
        }
        if (log.isDebugEnabled()) {
            log.debug("finalise");
        }
        for (final String in : tableCompareString) {
            final String[] sA = in.trim().split(":");
            if (sA.length != 2) {
                throw new IllegalArgumentException("Illegal TableCompareFields arguments: " + in
                            + ".\nSyntax: TableCompareFields=TABLENAME1:FIELD1[, FIELD2, ...] [&& TABLENAME2:FIELD1[, FIELD2, ...]]");
            }
            final IntermedTable itab = getIntermedTables().getIntermedTable(sA[0].trim());
            if (itab == null) {
                throw new WrongNameException("Wrong table name: " + sA[0].trim());
            }
            final String[] tA = sA[1].split(",");
            final int[] iA = new int[tA.length];
            if (tA.length < 1) {
                throw new IllegalArgumentException("Illegal TableCompareFields arguments: " + in
                            + ".\nSyntax: TableCompareFields=TABLENAME1:FIELD1[, FIELD2, ...] [&& TABLENAME2:FIELD1[, FIELD2, ...]]");
            }
            int i = 0;
            for (final String s : tA) {
                iA[i++] = itab.getColumnNumber(s.trim());
            }
            // irgendwas in den hash
            whereMap.put(itab.getTableName(), iA);
        }
        long errorCounter = 0;
        String stmnt;
        final Connection conn = getIntermedTables().getTargetConn();
        conn.setAutoCommit(force);
        for (final String tableName : getIntermedTables().getMetaInfo().getTopologicalTableSequence()) {
            final IntermedTable itab = getIntermedTables().getIntermedTable(tableName);
            final int tableRowCount = itab.getRowCount();
            System.out.println("finalizing ---> " + tableName);
            if (debug) {
                final String debugString = "Update for table: " + tableName + " (" + tableRowCount + " rows)\n";
                if (log.isDebugEnabled()) {
                    log.debug(debugString);
                }
            }
            buff.append("\n" + "Update for table: " + tableName + " (" + tableRowCount + " rows)\n");

            int logErrorCounter = 0;

            // Statement s = conn.createStatement();
            final int[] where = whereMap.get(itab.getTableName());
            Arrays.sort(where);
            if ((where == null) || (where.length < 1)) {
                continue;
            }
            for (int j = 0; j < tableRowCount; ++j) {
                // TODO processCancelCommand handling in superclass
                if (isCanceled()) {
                    conn.rollback();
                    log.info("cancel -> rollback");
                    logs += buff.toString();
                    setProgressCanceled(tableName);
                    return errorCounter;
                }

                stmnt = createUpdateStatement(itab, j, where);

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
                        logs += "    Updates error @ statement:" + stmnt + "\n" + ex.toString() + "\n";
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
            }
            // .execute("ROLLBACK");
        } catch (SQLException ex) {
            final String msg = "Error on: ROLLBACK: " + ex;
            log.error(msg);
            if (log.isDebugEnabled()) {
                log.debug(msg);
            }
            logs += "    Updates error .. rollback statement\n" + ex.toString() + "\n";
//            System.out.println("done.");
        }
//HINT: Do not close, so that multiple finalizations with one connection are possible!!
//        if (!conn.isClosed()) {
//            conn.close();
//        } // todo: check
        log.info("Updates finished");
        buff.append("\n\n-----------------Updates finished");
        logs += buff.toString();
        return errorCounter;
    }
//    public static String UPDATE =;
//    public static String SET =;
//    public static String EQUALS =;
//    public static String KOMMA =;
//    public static String WHERE =;

    /**
     * DOCUMENT ME!
     *
     * @param   itab   DOCUMENT ME!
     * @param   i      DOCUMENT ME!
     * @param   where  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  JPressoException  DOCUMENT ME!
     */
    protected String createUpdateStatement(final IntermedTable itab, final int i, final int[] where)
            throws JPressoException {
        return "UPDATE " + itab.getTableName() + generateSetPart(itab, i, where);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   itab   DOCUMENT ME!
     * @param   pos    DOCUMENT ME!
     * @param   where  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  JPressoException  DOCUMENT ME!
     */
    protected String generateSetPart(final IntermedTable itab, final int pos, final int[] where)
            throws JPressoException {
        final int columnCount = itab.getColumnCount();
        final StringBuilder sb = new StringBuilder(" SET ");
        final List<String> values = itab.getValueListWithGivenEnclosingChar(pos);
        int wherePointer = 0;
        for (int i = 0; i < columnCount; ++i) {
            if (i != where[wherePointer]) {
                final String colName = itab.getColumnName(i);
                final String val = values.get(i);
                sb.append(colName);
                sb.append(" = ");
                sb.append(val);
                sb.append(KOMMA_SPACE);
            } else {
                if (where.length <= ++wherePointer) {
                    --wherePointer;
                }
            }
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - KOMMA_SPACE.length());
        }
        sb.append(" WHERE ");
        for (final int i : where) {
            String comparator;
            final String val = values.get(i);
            if (!val.equalsIgnoreCase("null")) {
                comparator = " = ";
            } else {
                comparator = " IS ";
            }
            sb.append(itab.getColumnName(i));
            sb.append(comparator);
            sb.append(val);
            sb.append(SPACE_AND_SPACE);
        }
        if (sb.length() > 4) {
            sb.setLength(sb.length() - SPACE_AND_SPACE.length());
        }
        return sb.toString();
    }

    @Override
    protected void processCancelCommand() {
        setCanceled(true);
    }
}
