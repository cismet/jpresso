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
 * @author  hell
 */
//TODO logs rausschmeissen und ganz durch buff ersetzen!
public final class JDBCImportExecutor extends Finalizer {

    /** Logger */
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private boolean debug = log.isDebugEnabled();
    public static final int MAX_LOG_ERROR = 20;
    /** Holds value of property rollback. */
    private String rollback;
    private final StringBuilder buff = new StringBuilder();
    boolean rb;

    /** Creates a new instance of JDBCImportExecutor */
    public JDBCImportExecutor() {
    }

    /** Setter for property rollback.
     * @param rollback New value of property rollback.
     *
     */
    public void setRollback(String rollback) throws IllegalArgumentException {
        log.debug("Rollback got: " + rollback);
        // :-(
        // GEFAHR
        this.rollback = rollback;
        if (rollback.equals("true")) {
            log.info("Rollback wurde auf true gesetzt. Das Einfuegen wird wieder rueckgaengig gemacht!");
            rb = true;
        } else if (rollback.equals("false")) {
            rb = false;
        } else {
            throw new IllegalArgumentException("Illegal Rollback argument. Found " + rollback + "! Please provide 'true' or 'false'!");
        }
    }

    /** Die Methode die die ganze Arbeit erledigt ;-)
     *
     */
    public long finalise() throws Exception {
        log.debug("finalise");
        long errorCounter = 0;
        String stmnt;
        Connection conn = getIntermedTables().getTargetConn();
        if (rb) {
            conn.setAutoCommit(false);
        } else {
            conn.setAutoCommit(true);
        }
//        for (int i = 0; i < getIntermedTables().getNumberOfTargetTables(); ++i) {
//            String tableName = getIntermedTables().getMetaInfo().getTopologicalTableSequence().get(i);
        for (String tableName : getIntermedTables().getMetaInfo().getTopologicalTableSequence()) {
            if (debug) {
                String debugString = "Import in Tabelle: " + tableName + " (" + getIntermedTables().getIntermedTable(tableName).getRowCount() + " Datensaetze)\n";
                log.debug(debugString);
            }
            buff.append("\n" + "Import in Tabelle: " + tableName + " (" + getIntermedTables().getIntermedTable(tableName).getRowCount() + " Datensaetze)\n");
            IntermedTable itab = getIntermedTables().getIntermedTable(tableName);

            int logErrorCounter = 0;

            //Statement s = conn.createStatement();
            for (int j = 0; j < itab.getRowCount(); ++j) {
                if(isCanceled()) {
                    return errorCounter;
                }
                stmnt = getFixedPartOfInsertStatement(itab) + getValuesForInsert(itab, j);
                Statement s = conn.createStatement();
                try {
                    s.execute(stmnt);
                    setProgressValue(tableName, j + 1, logErrorCounter);
                    s.close();
                } catch (SQLException ex) {
                    errorCounter++;
                    logErrorCounter++;
                    log.error("Fehler bei:" + stmnt + ": " + ex);
                    log.debug("Fehler bei:" + stmnt, ex);
                    setProgressValue(tableName, j + 1, logErrorCounter);

                    if (logErrorCounter < MAX_LOG_ERROR) {
                        logs += "    Fehler beim Import .. Statement:" + stmnt + "\n" + ex.toString() + "\n";
                    } else if (logErrorCounter == MAX_LOG_ERROR) {
                        logs += "    ************** mehr Fehler (Abbruch der Ausgabe)\n";
                    }
                }
                try {
                    if (rb) {
                        conn.rollback();
                    }
                //.execute("ROLLBACK");
                } catch (SQLException ex) {
                    logErrorCounter++;
                    log.error("Fehler bei:ROLLBACK: " + ex);
                    log.debug("Fehler bei:ROLLBACK", ex);
                    if (logErrorCounter < MAX_LOG_ERROR) {
                        logs += "    Fehler beim Import .. Rollback des Statement:" + stmnt + "\n" + ex.toString() + "\n";
                    } else if (logErrorCounter == MAX_LOG_ERROR) {
                        logs += "    ************** mehr Fehler (Abbruch der Ausgabe)\n";
                    }
                }
            }
        }
//HINT: Do not close, so that multiple finalizations with one connection are possible
//        if (!conn.isClosed()) {
//            conn.close();
//        } // todo: check
        log.info("Import abgeschlossen");
        //logs += "\n\n-----------------Import abgeschlossen";
        buff.append("\n\n-----------------Import abgeschlossen");
        logs += buff.toString();
        return errorCounter;
    }

    protected String getValuesForInsert(IntermedTable itab, int position) throws JPressoException {
        return "(" + itab.getRowStringWithGivenEnclosingChar(position, ",") + ")";
    }

    protected String getFixedPartOfInsertStatement(IntermedTable itab) throws JPressoException {
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

//    @Override
//    public void cancel(boolean processCancelCommand) {
//        super.cancel(processCancelCommand);
//        throw new UnsupportedOperationException("Cancel not supported by this Finalizer!");
//    }
}
