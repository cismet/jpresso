/*
 * JDBCImportExecutor.java
 *
 * Created on 28. Oktober 2003, 11:40
 */
package de.cismet.jpresso.core.finalizer;

import de.cismet.jpresso.core.kernel.FieldDescription;
import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;
import de.cismet.jpresso.core.kernel.Finalizer;
import de.cismet.jpresso.core.kernel.ImportMetaInfo;
import de.cismet.jpresso.core.kernel.IntermedTable;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author  srichter
 */
public final class SequenceFinalizer_BU extends Finalizer {

    /** Logger */
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private boolean debug = log.isDebugEnabled();
    public static final int MAX_LOG_ERROR = 20;
    /** Holds value of property rollback. */
    private String rollback;
    private final StringBuilder buff = new StringBuilder();
    boolean rb;
    private final Map<String, Integer> positions = TypeSafeCollections.newHashMap();
    private final Map<String, Map<FieldDescription, Integer>> fieldNumbers = TypeSafeCollections.newHashMap();
    private static final String LASTVAL = "select lastval()";
    private static final String NEXTVAL_PRE = "nextval('";
    private static final String NEXTVAL_POST = "_seq')";
    private String sequences = "";

    public void setSequences(final String in) throws JPressoException {
        this.sequences = in;
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
    @Override
    public long finalise() throws Exception {
        log.debug("finalise");
        final ImportMetaInfo imi = getIntermedTables().getMetaInfo();
        final String[] tabStrings = sequences.split(",");
        for (final String cur : tabStrings) {
            final String[] tabfield = cur.trim().split("\\.");
            final String tab = tabfield[0];
            final String fld = tabfield[1];
            final int pos = imi.getPositionInTable(tab, fld);
            positions.put(tab, pos);
            final IntermedTable itab = getIntermedTables().getIntermedTable(tab);
            if (!itab.getValueAt(0, pos).startsWith(NEXTVAL_PRE)) {
                final String seqStmt = NEXTVAL_PRE + tab + NEXTVAL_POST;
                for (int i = 0; i < itab.getRowCount(); ++i) {
                    itab.setValueAt(seqStmt, i, pos);
                }
                imi.getTargetEnclosingCharsAsStringArray(tab)[pos] = "";
                final Iterable<FieldDescription> masterFields = imi.getAllMasterFields(new FieldDescription(tab, fld));
                if (masterFields != null) {
                    for (final FieldDescription fd : masterFields) {
                        if (fd.getTableName().equals(tab)) {
                            final int mPos = imi.getPositionInTable(fd.getTableName(), fd.getFieldName());
                            for (int i = 0; i < itab.getRowCount(); ++i) {
                                itab.setValueAt(LASTVAL, i, mPos);
                            }
                        } else {
                            Map<FieldDescription, Integer> toUpdate = fieldNumbers.get(tab);
                            if (toUpdate == null) {
                                toUpdate = TypeSafeCollections.newHashMap();
                                fieldNumbers.put(tab, toUpdate);
                            }
                            toUpdate.put(fd, imi.getPositionInTable(fd.getTableName(), fd.getFieldName()));
                        }
                    }
                }
            } else {
                //TODO
            }
        }
        long errorCounter = 0;
        String stmnt;
        final Connection conn = getIntermedTables().getTargetConn();
        final PreparedStatement curVal = conn.prepareStatement(LASTVAL);
//        conn.setAutoCommit(rb);
        conn.setAutoCommit(false);
        for (final String tableName : getIntermedTables().getMetaInfo().getTopologicalTableSequence()) {
            final IntermedTable itab = getIntermedTables().getIntermedTable(tableName);
            final int tableRowCount = itab.getRowCount();
            final int pos = positions.get(tableName);
            System.out.println("finalizing ---> " + tableName);
            if (debug) {
                String debugString = "Import into table: " + tableName + " (" + tableRowCount + " rows)\n";
                log.debug(debugString);
            }
            buff.append("\n" + "Import into table: " + tableName + " (" + tableRowCount + " rows)\n");
            int logErrorCounter = 0;
            final int offset = itab.getAutoIncOffsetForColumn(pos);
            for (int j = 0; j < tableRowCount; ++j) {
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
                final Statement s = conn.createStatement();
                try {
                    s.execute(stmnt);
                    final ResultSet rs = curVal.executeQuery();
                    rs.next();
                    itab.setValueAt(rs.getString(1), j, pos);
//                    rs.refreshRow()
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
            final Map<FieldDescription, Integer> toUpdate = fieldNumbers.get(tableName);
            if (toUpdate != null && !toUpdate.isEmpty()) {
                for (final Entry<FieldDescription,Integer> entry : toUpdate.entrySet()) {
                    final IntermedTable masterTab = getIntermedTables().getIntermedTable(entry.getKey().getTableName());
                    final int updatePos = entry.getValue();
                    for (int i = 0; i < masterTab.getRowCount(); ++i) {
                        masterTab.setValueAt(itab.getValueAt(Integer.parseInt(masterTab.getValueAt(i, updatePos)) - offset, pos), i, updatePos);
                    }
                }
            }
            itab.setAutoIncOffsetForColumn(pos, Integer.parseInt(itab.getValueAt(0, pos)));
        }
        try {
            if (rb) {
                conn.rollback();
            } else {
                conn.commit();
            }
            curVal.close();
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
