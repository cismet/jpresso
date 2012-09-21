/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * Importer.java
 *
 * Created on 15. September 2003, 17:23
 */
package de.cismet.jpresso.core.kernel;

import java.awt.Point;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CancellationException;

import javax.swing.table.TableModel;

import de.cismet.jpresso.core.data.DatabaseConnection;
import de.cismet.jpresso.core.data.ImportRules;
import de.cismet.jpresso.core.data.Mapping;
import de.cismet.jpresso.core.data.Query;
import de.cismet.jpresso.core.data.Reference;
import de.cismet.jpresso.core.data.RuntimeProperties;
import de.cismet.jpresso.core.exceptions.ImportMetaInfoException;
import de.cismet.jpresso.core.exceptions.TargetTableProblemException;
import de.cismet.jpresso.core.exceptions.WrongNameException;
import de.cismet.jpresso.core.serviceacceptor.ProgressListener;
import de.cismet.jpresso.core.serviceprovider.ClassResourceProvider;
import de.cismet.jpresso.core.serviceprovider.DynamicCompileClassLoader;
import de.cismet.jpresso.core.serviceprovider.DynamicDriverManager;
import de.cismet.jpresso.core.serviceprovider.ExtractAndTransformController;
import de.cismet.jpresso.core.serviceprovider.FinalizerController;
import de.cismet.jpresso.core.serviceprovider.FinalizerCreator;
import de.cismet.jpresso.core.serviceprovider.exceptions.DynamicCompilingException;
import de.cismet.jpresso.core.serviceprovider.exceptions.FinalizerException;
import de.cismet.jpresso.core.serviceprovider.exceptions.InitializingException;
import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * .
 *
 * <p>Hauptklasse des ganzen Paketes. Hier laufen alle Faeden zusammen ;-)</p>
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 * @TODO     Exceptions bereinigen
 */
public final class Importer implements FinalizerCreator, ExtractAndTransformController {

    //~ Static fields/initializers ---------------------------------------------

    /** Threshold values for memory to run an import. */
    public static final long MEM_THRESHOLD_START = 10485761;
    public static final long MEM_THRESHOLD_ASSIGN = 5242881;
    public static final int CHK_INTERVALL = 20;
    private static final String MSG_1_BEGIN = "Init importer...\n";
    private static final String MSG_2_SYNTAX = "Import description syntax check: ";
    private static final String MSG_3_SRC_CON = "Import source connection check: ";
    private static final String MSG_4_TRG_CON = "Import target connection check: ";
    private static final String MSG_5_META_CREA = "Import metainformation creation: ";
    private static final String MSG_6_CHK_DBTBL = "Import target table check: ";
    private static final String MSG_7_CREA_IMTBL = "Intermediate table structure creation: ";
    private static final String MSG_OK = "OK\n";
    public static final String CANONICAL_ASSIGNER_CLASSNAME = "assigner.GeneratedAssigner";

    //~ Instance fields --------------------------------------------------------

    /** Logger. */
    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    /** CastorObjekt der ImportKonfigurationsDatei (XML). */
    private final ImportRules impRules;
    /** Quellen Recordset. */
    private final DataSource importDataSource;
    /** Quell-Datenbankverbindung. */
    private Connection sourceConn;
    /** Ziel-Datenbankverbindung. */
    private Connection targetConn;
    /** Metainformation die aus impRules erzeugt wurden. */
    private final ImportMetaInfo importMetaInfo;
    /** Datenstruktur in die die Zieltabellen gespeichert werden. */
    private final IntermedTablesContainer intermedTablesContainer;
    /** indicates if there should be memory supervision during import.* */
    private boolean checkMemory = false;
    private final StringBuilder initializeLog = new StringBuilder();
    private final Assigner assigner;
    private final ClassResourceProvider clp;
    private boolean intermedFilled;
    private boolean canceled;
    private boolean closed;
    private String assignerJavaCode = "";

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Importer object.
     *
     * @param   impRules  DOCUMENT ME!
     * @param   clp       DOCUMENT ME!
     *
     * @throws  InitializingException  de.cismet.cids.admin.importAnt.InitializingException
     */
    public Importer(final ImportRules impRules, final ClassResourceProvider clp) throws InitializingException {
        this.closed = false;
        this.clp = clp;
        intermedFilled = false;
        initializeLog.append(MSG_1_BEGIN);

        try {
            this.impRules = impRules;
            initializeLog.append(MSG_2_SYNTAX);
            // analyse the import rules
            validateImportRules(impRules);
            initializeLog.append(MSG_OK);
            // connect to source db and get resultset
            log.info("Starting import with ID " + System.currentTimeMillis());
            importDataSource = openDataSource();
            initializeLog.append(MSG_3_SRC_CON);
            initializeLog.append(MSG_OK);

            // create connection to target db system
            setTargetConn(getTargetDbConnection());
            initializeLog.append(MSG_4_TRG_CON);
            initializeLog.append(MSG_OK);
            importMetaInfo = new ImportMetaInfo(impRules);
            importMetaInfo.optimizeNormalization(targetConn);
            // setzt die SourceFields in den MetaInformationen
            if (importDataSource != null) {
                importMetaInfo.setSourceFields(importDataSource);
            }
            if (log.isDebugEnabled()) {
                log.debug("Import metainformation created\n" + importMetaInfo.toString());
            }
            initializeLog.append(MSG_5_META_CREA);
            initializeLog.append(MSG_OK);
            // check target db system
            checkTargetDbSystem();
            initializeLog.append(MSG_6_CHK_DBTBL);
            initializeLog.append(MSG_OK);
            // create intermed structure
            intermedTablesContainer = new IntermedTablesContainer(importMetaInfo, getTargetConn());
            initializeLog.append(MSG_7_CREA_IMTBL);
            initializeLog.append(MSG_OK);

            // create the Assigner
            try {
                final DynamicCompileClassLoader loader = clp.getDynClassLoader();
                assignerJavaCode = importMetaInfo.generateAssignerJavaClassCode(CANONICAL_ASSIGNER_CLASSNAME);
                final Class<? extends Assigner> assignerClass = loader.compileAndLoadClass(
                        CANONICAL_ASSIGNER_CLASSNAME,
                        assignerJavaCode,
                        Assigner.class);
                assigner = assignerClass.newInstance();
                assigner.setSourceConnection(sourceConn);
                assigner.setDriverManager(findDriverManager());
            } catch (DynamicCompilingException ex) {
                throw ex;
            } catch (Exception ex) {
                log.error("Fehler", ex);
                throw new DynamicCompilingException("!internal Bug!" + ex.toString(), "");
            }
            initializeLog.append("Init successful!\n");
        } catch (ImportMetaInfoException miEx) {
            throw new InitializingException("Error while creating meta information! (" + miEx.getMessage() + ")",
                initializeLog.toString(),
                miEx);
        } catch (TargetTableProblemException ttEx) {
            throw new InitializingException(ttEx.getMessage(), initializeLog.toString(), ttEx);
        } catch (WrongNameException wnEx) {
            throw new InitializingException("Error creating internal datastructure! " + wnEx.getMessage(),
                initializeLog.toString(),
                wnEx);
        } catch (DynamicCompilingException dcEx) {
            throw new InitializingException("Error creating the mapping class! " + dcEx.getMessage(),
                initializeLog.toString(),
                dcEx);
        } catch (InitializingException ie) {
            throw new InitializingException(ie.getMessage(),
                ie.getInitializeLog(),
                ie.getMappingErrorFields(),
                ie.getReferenceErrorFields());
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  JPressoException  DOCUMENT ME!
     */
    public FinalizerCreator runImport() throws JPressoException {
        return runImport(null);
    }

    /**
     * Test ImportRules for the following errors:
     *
     * <p>- Empty mapping content - Relationpath errors - AutoInc-, Compare- an Enclosingcharacter issues</p>
     *
     * @param   rules  DOCUMENT ME!
     *
     * @throws  InitializingException  DOCUMENT ME!
     */
    private void validateImportRules(final ImportRules rules) throws InitializingException {
        final StringBuilder sb = new StringBuilder();
        String msg = "";
        final List<Point> mappingErrors = TypeSafeCollections.newArrayList();
        final List<Point> referenceErrors = TypeSafeCollections.newArrayList();
        final List<Mapping> mappings = rules.getMappings();
        final List<Reference> references = rules.getReferences();
//        final Set<String> possibleRelationTables = new HashSet<String>();
        final Map<String, Set<String>> possibleRelationFieldsForTable = TypeSafeCollections.newHashMap();
        final Set<String> autoInc = TypeSafeCollections.newHashSet();
        final Set<String> compares = TypeSafeCollections.newHashSet();
        final Map<String, String> encChars = TypeSafeCollections.newHashMap();
        final Set<String> seenWithPath = TypeSafeCollections.newHashSet();
        // test mappings for correct targets and paths
        int row = -1;
        for (final Mapping m : mappings) {
            ++row;
            if ((m.getContent().length() < 1) && !m.isAutoIncrement()) {
                // leerer inhalt
                msg = "Mapping-Error: Emtpy mapping for " + m.getCompleteTarget() + "! Tablepos.: Row " + row
                            + ", Column " + 1;
                log.error(msg);
                sb.append(msg).append("\n");
                mappingErrors.add(new Point(row, 1));
            }
            if ((m.getTargetField().length() > 0) && (m.getTargetTable().length() > 0)) {
                if (m.isAutoIncrement()) {
                    autoInc.add(m.getCompleteTarget());
                }
                if (m.isComparing()) {
                    compares.add(m.getCompleteTarget());
                }
                if (m.isAutoIncrement() && m.isComparing()) {
                    // auto-inkrement und comparing = schlecht
                    msg = "Mapping-Error: AutoIncrement AND Comparing activated for " + m.getCompleteTarget()
                                + "! Tablepos.: Row " + row + ", Column " + 3 + " and " + 4;
                    log.error(msg);
                    sb.append(msg).append("\n");
                    mappingErrors.add(new Point(row, 3));
                    mappingErrors.add(new Point(row, 4));
                }
                encChars.put(m.getCompleteTarget(), m.getEnclosingChar());
                final String relPath = m.getPath();
                try {
                    // testen ob der pfad eine zahl ist
                    final String key;
                    if (relPath.length() > 0) {
                        Integer.parseInt(relPath);
                        key = m.getTargetTable() + "[" + relPath + "]";
                        seenWithPath.add(m.getTargetTable());
                    } else {
                        key = m.getTargetTable();
                    }
                    Set<String> fields = possibleRelationFieldsForTable.get(key);
                    if (fields == null) {
                        fields = TypeSafeCollections.newHashSet();
                        possibleRelationFieldsForTable.put(key, fields);
                    }
                    if (!fields.add(m.getTargetField())) {
                        // das feld wurde schonmal unter dem gleichen pfad benutzt!
                        msg = "Mapping-Error: Target " + m.getCompleteTarget() + " used more than once! Tablepos.: Row "
                                    + row + ", Column " + 2;
                        log.error(msg);
                        mappingErrors.add(new Point(row, 2));
                        sb.append(msg).append("\n");
                    }
                } catch (NumberFormatException e) {
                    // relationpath ist keine zahl
                    msg = "Mapping-Error: Relationpath " + m.getPath() + " on target " + m.getCompleteTarget()
                                + " is not a number! Tablepos.: Row " + row + ", Column " + 2;
                    log.error(msg);
                    mappingErrors.add(new Point(row, 2));
                    sb.append(msg).append("\n");
                }
            } else {
                // leeres/unvollständiges target
                msg = "Mapping-Error: Target missing or incomplete! Tablepos.: Row " + row + ", Column " + 0;
                log.error(msg);
                sb.append(msg).append("\n");
                mappingErrors.add(new Point(row, 0));
            }
        }
        row = -1;
        for (final Mapping m : mappings) {
            ++row;
            final String key = m.getCompleteTarget();
            if (seenWithPath.contains(m.getTargetTable()) && (m.getPath().length() < 1)) {
                // es gab dieses target mit pfad, dennoch wurde es ohne pfad gefunden!
                msg = "Mapping-Error: Target " + m.getCompleteTarget()
                            + " found with and without paths! Tablepos.: Row " + row + ", Column " + 2;
                log.error(msg);
                sb.append(msg).append("\n");
                mappingErrors.add(new Point(row, 2));
            }
            if (autoInc.contains(key) && !m.isAutoIncrement()) {
                msg = "Mapping-Error: Target " + m.getCompleteTarget()
                            + " has different Auto-Increment-settings among it's paths! Tablepos.: Row " + row
                            + ", Column " + 3;
                log.error(msg);
                sb.append(msg).append("\n");
                mappingErrors.add(new Point(row, 3));
            }
            if (compares.contains(key) && !m.isComparing()) {
                msg = "Mapping-Error: Target " + m.getCompleteTarget()
                            + " has different Comparing-settings among it's paths! Tablepos.: Row " + row + ", Column "
                            + 4;
                log.error(msg);
                sb.append(msg).append("\n");
                mappingErrors.add(new Point(row, 4));
            }
            final String encChar = encChars.get(key);
            if ((encChar != null) && !encChar.equals(m.getEnclosingChar())) {
                msg = "Mapping-Error: Target " + m.getCompleteTarget()
                            + " has different Enclosing-Character-settings among it's paths! Tablepos.: Row " + row
                            + ", Column " + 5;
                log.error(msg);
                sb.append(msg).append("\n");
                mappingErrors.add(new Point(row, 5));
            }
        }
        // -----------

        for (final Reference ref : references) {
            if (ref.isComparing()) {
                final int cut = ref.getReferencingTable().lastIndexOf("[");
                final String pureCompleteReferencing = ((cut > 0) ? ref.getReferencingTable().substring(0, cut)
                                                                  : ref.getReferencingTable()) + "."
                            + ref.getReferencingField();
                compares.add(pureCompleteReferencing);
            }
        }

        row = -1;
        final Set<String> findDuplicates = TypeSafeCollections.newHashSet();
        for (final Reference ref : references) {
            final Set<String> fromFields = possibleRelationFieldsForTable.get(ref.getReferencingTable());
            final Set<String> toFields = possibleRelationFieldsForTable.get(ref.getReferencedTable());
            ++row;

            if (!findDuplicates.add(ref.getCompleteReferencing())) {
                msg = "Referencing-Error: Target " + ref.getCompleteReferencing()
                            + " is already assigned in another Relation! Tablepos.: Row " + row + ", Column " + 0
                            + " and " + 1;
                log.error(msg);
                sb.append(msg).append("\n");
                referenceErrors.add(new Point(row, 0));
                referenceErrors.add(new Point(row, 1));
            }
            final Set<String> mappingTestSet = possibleRelationFieldsForTable.get(ref.getReferencingTable());
            if (mappingTestSet != null) {
                if (mappingTestSet.contains(ref.getReferencingField())) {
                    msg = "Mapping/Referencing-Error: Target " + ref.getCompleteReferencing()
                                + " is already assigned in a Mapping! Tablepos.: Row " + row + ", Column " + 0 + " and "
                                + 1;
                    log.error(msg);
                    sb.append(msg).append("\n");
                    referenceErrors.add(new Point(row, 0));
                    referenceErrors.add(new Point(row, 1));
                }
            }
            if (fromFields == null) {
                // referencing table nicht vorhanden
                msg = "Referencing-Error: Mapping does not contain the Referencing-Table " + ref.getReferencingTable()
                            + "! Tablepos.: Row " + row + ", Column " + 0;
                log.error(msg);
                sb.append(msg).append("\n");
                referenceErrors.add(new Point(row, 0));
            }
            if (toFields == null) {
                // referenced table nicht vorhanden
                msg = "Referencing-Error: Mapping does not contain the Referenced-Table " + ref.getReferencedTable()
                            + "! Tablepos.: Row " + row + ", Column " + 2;
                log.error(msg);
                sb.append(msg).append("\n");
                referenceErrors.add(new Point(row, 2));
            }
            final int cut = ref.getReferencingTable().lastIndexOf("[");
            final String pureCompleteReferencing = ((cut > 0) ? ref.getReferencingTable().substring(0, cut)
                                                              : ref.getReferencingTable()) + "."
                        + ref.getReferencingField();
            if (compares.contains(pureCompleteReferencing) && !ref.isComparing()) {
                // mismatch of comparing
                msg = "Mapping/Referencing-Error: Found different setting for Comparing on table "
                            + pureCompleteReferencing + " Tablepos.: Row " + row + ", Column " + 4;
                log.error(msg);
                sb.append(msg).append("\n");
                referenceErrors.add(new Point(row, 4));
            }
            final String encChar = encChars.get(pureCompleteReferencing);
            if ((encChar != null) && !encChar.equals(ref.getEnclosingChar())) {
                // mismatch of enclosing characters
                msg = "Mapping/Referencing-Error: Found different setting for Enclosing-Character on table "
                            + pureCompleteReferencing + "! Tablepos.: Row " + row + ", Column " + 5;
                log.error(msg);
                sb.append(msg).append("\n");
                referenceErrors.add(new Point(row, 5));
            } else {
                encChars.put(pureCompleteReferencing, ref.getEnclosingChar());
            }
        }
        if ((mappingErrors.size() > 0) || (referenceErrors.size() > 0)) {
            throw new InitializingException(
                "Found errors when prechecking Mappings and References! Task canceled.",
                sb.toString(),
                mappingErrors,
                referenceErrors);
        }
    }

    /**
     * Checks if there is more JVM memory available that the given value.
     *
     * <p>Else throws a RuntimeException.</p>
     *
     * @param   min  memory value
     *
     * @throws  RuntimeException  DOCUMENT ME!
     */
    private void memCheck(final long min) {
        if (Runtime.getRuntime().freeMemory() < min) {
            System.gc();
            final long mem = Runtime.getRuntime().freeMemory();
            if (mem < min) {
                final long memMB = mem / 1048576;
                final long memMBthresh = min / (1048576);
                final long memOverall = Runtime.getRuntime().maxMemory() / (1048576);
                throw new RuntimeException("Low memory detected!\nOnly " + memMB + " MB of " + memOverall + " MB left, "
                            + memMBthresh
                            + " MB is minimum.\nStopping import.\nProvide more memory to the JVM or splitt your import into smaller parts!");
            }
        }
    }

    /**
     * Der eigentliche Import wird mit Hilfe der dynamischen Klasse durchgefuehrt.
     *
     * @param   progress  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  JPressoException  bei Fehler
     */
    @Override
    public FinalizerCreator runImport(final ProgressListener progress) throws JPressoException {
        if (closed) {
            throw new JPressoException("Can not run import. This Importer is already closed!");
        }
        if (intermedFilled) {
            throw new JPressoException("Can not run import. This Importer is already filled with data!");
        }
        if (checkMemory) {
            memCheck(MEM_THRESHOLD_START);
        }
        final boolean showProgress = (progress != null);
        if (showProgress) {
            progress.start("Filling InterMedTables...");
        }
        try {
            if (log.isDebugEnabled()) {
                log.debug("RulesMetaInfo\n" + this.importMetaInfo.toString());
            }
            final long start = System.currentTimeMillis();
            if (showProgress) {
                final int rowCount = importDataSource.getRowCount();
                log.info("The data source has " + rowCount + " row(s) to process.");
                progress.switchToDeterminate(rowCount);
            }
            final UniversalContainer universalContainer = new UniversalContainer();
            int counter = 0;
            int checkIntervall = 0;
            final Connection tc = getTargetConn();
            for (final String[] current : importDataSource) {
                if (canceled) {
                    break;
                }
                if (checkMemory) {
                    if (++checkIntervall > CHK_INTERVALL) {
                        checkIntervall = 0;
                        memCheck(MEM_THRESHOLD_ASSIGN);
                    }
                }
                // Zuordnung durch die dynamische Klasse:
                // eine Zeile der Quelltabelle in je eine Zeile möglicherweise mehrerer Zieltabellen
                final String[][] result;
                // TODO: performance steigerung: ArrayList statt LHM: groesse bekannt, reihenfolge aus metaInf bekannt!
                try {
                    result = assigner.assign(tc, current, universalContainer);
                    if (assigner.isStopped()) {
                        break;
                    }
                } catch (RuntimeException ex) {
                    log.warn("Runtime import error in assigner class! Please check your code: \n" + assignerJavaCode,
                        ex);
                    throw new JPressoException("Runtime import error in assigner class! Please check your code.", ex);
                }

                // Die aktuelle Zeile wird in die intermed Tables geschrieben
                try {
                    intermedTablesContainer.addCurrentRow(result);
                } catch (RuntimeException ex) {
                    log.warn("Import error when adding current row!", ex);
                    throw new RuntimeException("Import error when adding current row!", ex);
                }
                if (showProgress) {
                    progress.progress(++counter);
                }
            }
            if (!canceled) {
                intermedFilled = true;
            } else {
                throw new CancellationException("Import canceled!");
            }
            final long diff = System.currentTimeMillis() - start;
            log.info("Filling internal datastructure took (time [ms]):" + diff);
            return this;
        } catch (Exception ex) {
            if (canceled) {
                throw new CancellationException("Import canceled!");
            } else {
                throw new JPressoException("Error during import!", ex);
            }
        } finally {
            // Release resources as far as possible yet.
            // (Keeping those that are relevant for Finalizers)
            intermedTablesContainer.clearAllCaches();
            if (showProgress) {
                progress.finish();
            }
            if (importDataSource != null) {
                importDataSource.close();
            }
            if (!isIntermedFilled()) {
                close();
            }
        }
    }

    /**
     * Legt die JDBC Verbindung zur Datenquelle an.
     *
     * @return  DOCUMENT ME!
     *
     * @throws  InitializingException  Exception bei Fehler
     */
    private DataSource openDataSource() throws InitializingException {
        try {
            final Query source = impRules.getSourceQuery();
            final String fetchSizeProp = source.getProps().getProperty(DataSource.FETCH_SIZE_PROPERTY);
            int fetchSize;
            try {
                fetchSize = Integer.parseInt(fetchSizeProp);
            } catch (Exception ex) {
                fetchSize = DataSource.NO_FETCH_SIZE;
            }
            if (fetchSize != DataSource.NO_FETCH_SIZE) {
                log.info("FetchSize = " + fetchSize);
            }
            if ((getSourceConn() == null) || getSourceConn().isClosed()) {
                final DynamicDriverManager dm = findDriverManager();
                if (source.getProps().values().size() > 0) {
                    final Connection c = dm.getConnection(source.getDriverClass(), source.getUrl(), source.getProps());
                    setSourceConn(c);
                } else {
                    final Connection c = dm.getConnection(source.getDriverClass(), source.getUrl(), new Properties());
                    setSourceConn(c);
                }
            }

            return new DatabaseDataSource(sourceConn, source.getQueryStatement(), fetchSize);
        } catch (Exception e) {
            throw new InitializingException(e.toString(), initializeLog.toString(), e);
        }
    }

    /**
     * Legt die Verbindung zur Zieldatenbank an.
     *
     * @return  DOCUMENT ME!
     *
     * @throws  InitializingException  Exception bei Fehler
     */
    private Connection getTargetDbConnection() throws InitializingException {
        try {
            final DatabaseConnection target = impRules.getTargetConnection();
            Connection conn;
            final DynamicDriverManager dm = findDriverManager();
            if ((target.getProps() != null) && (target.getProps().values().size() > 0)) {
                conn = dm.getConnection(target.getDriverClass(), target.getUrl(), target.getProps());
            } else {
                conn = dm.getConnection(target.getDriverClass(), target.getUrl(), new Properties());
            }
            return conn;
        } catch (Throwable e) {
            throw new InitializingException(e.getMessage(), initializeLog.toString(), e);
        }
    }

    /**
     * Fuehrt auf der Zieldatenbank Kontroll-Statements aus, um zu ueberpruefen ob die Zieltabellen auch vorhanden sind,
     *
     * @throws  TargetTableProblemException  Exception bei Fehler
     */
    private void checkTargetDbSystem() throws TargetTableProblemException {
        String tabControlStatement = "";
        try {
            final Statement stmnt = getTargetConn().createStatement();
            for (final String table : importMetaInfo.getTargetTableNames()) {
                try {
                    tabControlStatement = importMetaInfo.getControlStatement(table);
                    if (log.isDebugEnabled()) {
                        log.debug("checkTargetDbSystem:Query:" + tabControlStatement);
                    }
                    stmnt.setMaxRows(1);
                    stmnt.executeQuery(tabControlStatement);
                } catch (Throwable e) {
                    if (log.isDebugEnabled()) {
                        log.debug("Error on statement:" + tabControlStatement, e);
                    }
                    throw new TargetTableProblemException("@ " + table + "\nSTATEMENT: " + tabControlStatement
                                + "\nCAUSE: " + e.getMessage());
                }
            }
        } catch (Throwable e) {
            throw new TargetTableProblemException(e.getMessage());
        }
    }

    /**
     * Getter for property intermedTables.
     *
     * @return  Value of property intermedTables.
     */
    public de.cismet.jpresso.core.kernel.IntermedTablesContainer getIntermedTablesContainer() {
        return intermedTablesContainer;
    }

    /**
     * Getter for property initializeLog.
     *
     * @return  Value of property initializeLog.
     */
    @Override
    public java.lang.String getInitializeLog() {
        return initializeLog.toString();
    }

    // close all connections
    @Override
    public void close() {
        try {
            if ((importDataSource != null) && !importDataSource.isClosed()) {
                if (log.isDebugEnabled()) {
                    log.debug("Source CLOSED");
                }
                this.getSourceConn().close();
                this.setSourceConn(null);
            }
            if ((getTargetConn() != null) && !getTargetConn().isClosed()) {
                if (log.isDebugEnabled()) {
                    log.debug("Target CLOSED");
                }
                this.getTargetConn().close();
                this.setTargetConn(null);
            }
            if (getIntermedTablesContainer() != null) {
                getIntermedTablesContainer().close();
            }
        } catch (SQLException ex) {
            // ignore
            log.warn("Error closing connection!", ex);
        }
        this.closed = true;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (!closed) {
            close();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Connection getSourceConn() {
        return sourceConn;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sourceConn  DOCUMENT ME!
     */
    public void setSourceConn(final Connection sourceConn) {
        this.sourceConn = sourceConn;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Connection getTargetConn() {
        return targetConn;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  targetConn  DOCUMENT ME!
     */
    public void setTargetConn(final Connection targetConn) {
        this.targetConn = targetConn;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public DynamicDriverManager findDriverManager() {
        return clp.getDriverManager();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isIntermedFilled() {
        return intermedFilled;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isCheckMemory() {
        return checkMemory;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  checkMemory  DOCUMENT ME!
     */
    public void setCheckMemory(final boolean checkMemory) {
        this.checkMemory = checkMemory;
    }

    @Override
    public FinalizerController createFinalizer(final RuntimeProperties rP, final ProgressListener pl)
            throws FinalizerException {
        if ((rP == null) || (rP.getFinalizerClass() == null)) {
            throw new FinalizerException("RuntimeProperties are null or FinalizerClass entry is null!");
        }
        if (!isIntermedFilled()) {
            throw new FinalizerException(
                "Can not create finalizer with emtpy IntermediateTables! Please execute Importer first!");
        }
        if (closed) {
            throw new FinalizerException("Can not create finalizer from closed Importer!");
        }
        final String finalizerClass = rP.getFinalizerClass();
        return new ImportFinalizer(finalizerClass, getIntermedTablesContainer(), rP.getFinalizerProperties(), pl);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  closed  DOCUMENT ME!
     */
    public void setClosed(final boolean closed) {
        this.closed = closed;
    }

    @Override
    public void cancel() {
        this.canceled = true;
    }

    @Override
    public Collection<? extends TableModel> getIntermedTableModels() {
        return getIntermedTablesContainer().getIntermedTables();
    }

    @Override
    public String[] getEnclosingCharacters(final TableModel table) {
        if (table instanceof IntermedTable) {
            final IntermedTable iTab = (IntermedTable)table;
            return iTab.getEnclosingCharacters().clone();
        }
        return new String[] {};
    }
}
