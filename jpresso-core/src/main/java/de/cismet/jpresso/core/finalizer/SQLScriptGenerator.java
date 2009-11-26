/*
 * JDBCImportExecutor.java
 *
 * Created on 28. Oktober 2003, 11:40
 */
package de.cismet.jpresso.core.finalizer;

import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;
import de.cismet.jpresso.core.kernel.Finalizer;
import de.cismet.jpresso.core.kernel.IntermedTable;
import java.io.File;
import java.io.FileWriter;

/**
 *
 * @author  srichter
 * @author  hell
 */
public final class SQLScriptGenerator extends Finalizer {

    public static final int MAX_LOG_ERROR = 20;
    /** Logger */
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private File target = null;

    /** Holds value of property rollback. */
    /** Creates a new instance of JDBCImportExecutor */
    public SQLScriptGenerator() {
    }
    
    /**
     * 
     * @param file
     */
    public void setFile(String file) {
        this.target = new File(file);
    }

    /** Die Methode die die ganze Arbeit erledigt ;-)
     *
     */
    public long finalise() throws Exception {
        log.debug("finalise");
        long errorCounter = 0;
        String stmnt;
        if(target == null) {
            throw new IllegalArgumentException("No Output file set. Please use Parameter \"File=/Path/to/file\"!");
        }
        System.out.println("Writing SQL Script to: " + target.getAbsolutePath());
        FileWriter fw = new FileWriter(target);

//        for (int i = 0; i < getIntermedTables().getNumberOfTargetTables(); ++i) {
//            String tableName = (String) getIntermedTables().getMetaInfo().getTopologicalTableSequence().get(i);
        for (final String tableName : getIntermedTables().getMetaInfo().getTopologicalTableSequence()) {
            log.info("Import in Tabelle: " + tableName + "\n");
            logs += "\nImport in Tabelle:" + tableName + "\n\n";
            final IntermedTable itab = getIntermedTables().getIntermedTable(tableName);
            for (int j = 0; j < itab.getRowCount(); ++j) {
                if (isCanceled()) {
                    return errorCounter;
                }
                setProgressValue(tableName, j + 1);
                stmnt = getFixedPartOfInsertStatement(itab) + getValuesForInsert(itab, j);
                fw.write(stmnt + ";\n");
            }
        }
        log.info("Import abgeschlossen");
        logs += "\n\n-----------------Import abgeschlossen";
        fw.flush();
        fw.close();
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
}
