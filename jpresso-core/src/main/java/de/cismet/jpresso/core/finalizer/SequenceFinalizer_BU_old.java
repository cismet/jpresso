/*
 * JDBCImportExecutor.java
 *
 * Created on 28. Oktober 2003, 11:40
 */
package de.cismet.jpresso.core.finalizer;

import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;
import de.cismet.jpresso.core.kernel.Finalizer;
import de.cismet.jpresso.core.kernel.IntermedTable;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  srichter
 */
public final class SequenceFinalizer_BU_old extends Finalizer {

    /** Logger */
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private boolean debug = log.isDebugEnabled();
    public static final int MAX_LOG_ERROR = 20;
    /** Holds value of property rollback. */
    private String rollback;
    private final StringBuilder buff = new StringBuilder();
    boolean rb;
    private final Map<String, Map<String, List<String>>> referenceMap;
    //enthält die funktionsaufruf für den next value eines feldes einer tabelle
    private final Map<String, Map<String, String>> nextVal;
    //enthält die funktionsaufruf für den next value eines feldes einer tabelle
    private final Map<String, Map<String, String>> curVal;

    /** Creates a new instance of ABFImporter */
    public SequenceFinalizer_BU_old() {
        referenceMap = TypeSafeCollections.newHashMap();
        nextVal = TypeSafeCollections.newHashMap();
        curVal = TypeSafeCollections.newHashMap();
    }

    public void setSequences(final String in) {
        final String[] tabStrings = in.split("&&");
        for (String ts : tabStrings) {
            final HashMap<String, String> next = TypeSafeCollections.newHashMap();
            final HashMap<String, String> cur = TypeSafeCollections.newHashMap();
            ts = ts.trim();
            String[] fldStrings = ts.split(":");
            String tab = fldStrings[0];
            String fs = fldStrings[1];
            fs = fs.trim();
            final String seqNext = fs.substring(fs.indexOf("(") + 1, fs.indexOf(",")).trim();
            final String seqCur = fs.substring(fs.indexOf(",") + 1, fs.lastIndexOf(")")).trim();
            next.put(fldStrings[1].substring(0, fldStrings[1].indexOf("(")), seqNext);
            cur.put(fldStrings[1].substring(0, fldStrings[1].indexOf("(")), seqCur);

            nextVal.put(tab, next);
            curVal.put(tab, cur);
        }
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
        //maps x -> generated values list.
        HashMap<String, List<String>> refs = TypeSafeCollections.newHashMap();
        for (final String tableName : getIntermedTables().getMetaInfo().getTopologicalTableSequence()) {
            final IntermedTable itab = getIntermedTables().getIntermedTable(tableName);
            final int tableRowCount = itab.getRowCount();
//            final int tableCount = itab.getRowCount();
            
            System.out.println("finalizing ---> " + tableName);
            if (debug) {
                String debugString = "Import for table: " + tableName + " (" + tableRowCount + " rows)\n";
                log.debug(debugString);
            }
            buff.append("\n" + "Import for table: " + tableName + " (" + tableRowCount + " rows)\n");

            int logErrorCounter = 0;
            //maps field -> function
            final Map<String, String> cur = curVal.get(tableName);
            final Map<String, String> next = nextVal.get(tableName);
            //testen ob hier sequence action gefragt ist!
            if (next != null) {
                for (final String key : next.values()) {
                    refs.put(key, new ArrayList<String>());
                }
            }
            for (int j = 0; j < itab.getRowCount(); ++j) {
                //TODO processCancelCommand handling in superclass
                if (isCanceled()) {
                    conn.rollback();
                    log.info("cancel -> rollback");
                    logs += buff.toString();
                    setProgressCanceled(tableName);
                    return errorCounter;
                }
                stmnt = createStatement(itab, j, next, refs);
                if (debug) {
                    log.debug("Statement: " + stmnt);
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
            if (refs != null) {
                //referenzmapping für diese tabelle der map hinzufügen (=speichern)
                referenceMap.put(tableName, refs);
            }
            System.out.println(referenceMap);
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
        log.info("Import finished");
        buff.append("\n\n-----------------Import finished");
        logs += buff.toString();
        return errorCounter;
    }

    protected String createStatement(final IntermedTable itab, final int i, Map<String, String> next, Map<String, List<String>> refs) throws JPressoException {
        return "INSERT INTO " + itab.getTableName() + "(" + getFieldList(itab) + ") VALUES " + generateSetPart(itab, i, next, refs);
    }

    protected String generateSetPart(final IntermedTable itab, int pos, Map<String, String> next, Map<String, List<String>> refs) throws JPressoException {
        final int columnCount = itab.getColumnCount();
        final StringBuilder sb = new StringBuilder("(");
        final List<String> values = itab.getValueListWithGivenEnclosingChar(pos);
        for (int i = 0; i < columnCount; ++i) {
            final String colName = itab.getColumnName(i);
            String fct = null;
            if (next != null) {
                fct = next.get(colName);
            }
            String stdVal = values.get(i);
            final String val;
            if (fct == null) {
                val = stdVal;
            } else {
                val = fct;
                refs.get(val).add(stdVal);
            }
            sb.append(val);
            sb.append(", ");
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(")");
        return sb.toString();
    }

    protected String getFieldList(final IntermedTable itab) throws JPressoException {
        final StringBuilder sBuff = new StringBuilder();
        for (int i = 0; i < itab.getColumnCount() - 1; ++i) {
            sBuff.append(itab.getColumnName(i)).append(", ");
        }
        sBuff.append(itab.getColumnName(itab.getColumnCount() - 1));
        return sBuff.toString();
    }

    @Override
    protected void processCancelCommand() {
        setCanceled(true);
    }
}
