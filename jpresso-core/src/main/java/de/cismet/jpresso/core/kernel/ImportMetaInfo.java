/*
 * ImportMetaInfo.java
 *
 * Created on 16. September 2003, 15:06
 */
package de.cismet.jpresso.core.kernel;

import code.AssignerBase;
import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;
import de.cismet.jpresso.core.exceptions.WrongNameException;
import de.cismet.jpresso.core.exceptions.ImportMetaInfoException;
import de.cismet.jpresso.core.data.ImportRules;
import de.cismet.jpresso.core.data.Mapping;
import de.cismet.jpresso.core.data.Reference;
import de.cismet.jpresso.core.kernel.TableMetaInfo.Builder;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.core.utils.EscapeUtil;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * ImportMetaInfo haelt die notwendigen Metainformationen zu einem Import.
 * Dazu gehoeren neben dem Zugriffsobjekt der Importdate auch noch weitere
 * Convenience Funktionen.
 * 
 * @author srichter
 * @author hell
 */
public final class ImportMetaInfo {

    //prevents naming mappingErrors if tablename == a field name
    private static final String FILTER_ERROR_FINDER = "by-cids-filter-error-finder:";
    private static final String OPENING_BRACKET = "[";
    private static final String COUNT_STMNT = "SELECT count(*) FROM ";
    private static final String EMPTY = "";
    private static final String KOMMA = ",";
    private static final String FROM = " FROM ";
    private static final String SELECT = "SELECT ";
    private static final String WHERE_TRUE = " WHERE 0 = 1";
    private static final String BREAK_IDENTIFIER = "CIDS-BREAK";
    public static final String TABLE_TAG = "TBL$";
    public static final String FIELD_TAG = "FLD$";
    public static final String ERROR_FINDER = "by-cids-error-finder:";
    public static final String NORMALIZE_MEM_ONLY = "#";
    public static final String NORMALIZE_WITH_DB = "!";
    //regex describing a valid java variable name
    public static final Pattern VALID_JAVA_VARIABLE_NAME = Pattern.compile("^[_\\$A-Za-zÄäÜüÖöß][_\\$\\p{Alnum}ÄäÜüÖöß]*$");
    /** Logger */
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    /** Spaltennamen der Datenquelle z.B um zu pruefen ob ein Label doppelt vorhanden ist*/
    private final List<String> sourceFields = TypeSafeCollections.newArrayList();
    /** Die realen Zieltabellen(Strukturen) (ohne Beziehungspfad) : Tabellenname -> Liste Feldnamen*/
    private final Map<String, List<String>> targetTables = TypeSafeCollections.newLinkedHashMap();
    /** Enclosing Character der einzelnen Felder der Zieltabellen: Zieltabelle -> Feldname -> Umschliessende Characters*/
    private final Map<String, Map<String, String>> enclosingChars = TypeSafeCollections.newLinkedHashMap();
    /** Normalisierungscontainer: Enthaelt die Namen der zu normalisierenden Tabellen */
    private final Map<String, String> normalizeHM = TypeSafeCollections.newLinkedHashMap();
    /** Welche Felder sind fuer einen Vergleich (Normalisierung) relevant ? : Tabellenname -> Liste vergleichsrelevanter Felder */
    private final Map<String, List<String>> compareRelevant = TypeSafeCollections.newLinkedHashMap();
    /** RelationenContainer : String(DetailTabelle.Feld) -> List: String(MasterTabelle.Feld)*/
    private final Map<FieldDescription, List<FieldDescription>> referencesHashMap = TypeSafeCollections.newLinkedHashMap();
    /** Mastertabellen, mit Pfaden */
    private final Map<String, String> tableIsMaster = TypeSafeCollections.newLinkedHashMap();
    /** Detailtabellen (Tabellen die irgendwo referenziert werden, mit Pfaden): Tabellenname -> Referenziertes Feld*/
    private final Map<String, List<String>> tableIsDetail = TypeSafeCollections.newLinkedHashMap();
    /** Topologisch sortierte Reihenfolge der Zieltabellen zur Abarbeitung, ohne Pfade */
    private final Set<String> topologicalTableSequence = TypeSafeCollections.newLinkedHashSet();
    /** Topologisch sortierte Reihenfolge der Zieltabellen zur Abarbeitung, mit Pfaden */
    private final Set<String> topologicalTableSequenceWithPath = TypeSafeCollections.newLinkedHashSet();
    /** Sammlung aller Tabellen mit Pfad zu einem Tabellenname ohne Pfad: Tabellenname ohne Pfad -> Liste zugehoeriger Tabellennamen mit Pfad **/
    private final Map<String, List<String>> tableToAppropriateTablesWithPaths = TypeSafeCollections.newLinkedHashMap();
    /** Umgekehrt mappt dieser Hash alle Tabellennamen mit Pfad auf ihren jeweilen Tabellenname ohne Pfad. Dies dient nur der Performancesteigerung **/
    private final Map<String, String> pureTableNameHash = TypeSafeCollections.newHashMap();
    /** Ein Hash der einer FieldDescription einen eine Map zuordnet, die fue eine MasterTable einen zirkularen Felditerator liefert **/
    private final Map<FieldDescription, Map<String, ListCirculator<String>>> referencingFields = TypeSafeCollections.newHashMap();
    /** Cache der Anzeigt ob zwischen 2 Tabellen ueber ein gegebenes Feld eine Master-Detail-Beziehung ebsteht **/
    private final Map<String, Map<String, Map<String, Boolean>>> rightMasterTableCache = TypeSafeCollections.newHashMap();
    /** Tabellenname -> Nummern der referenzierten Felder **/
    private final Map<String, int[]> detailKeyFields = TypeSafeCollections.newHashMap();
    /** Tabellenname -> Nummern der referenzierten Felder **/
    private final Map<String, List<Integer>> autoIncFieldNos = TypeSafeCollections.newHashMap();
    /** ImportKonfiguration */
    private final ImportRules rules;
    /** Tabellenname -> gueltiger Java Variablenname der die Tablle im Assigner als String[] repräsentiert**/
    private final Map<String, String> tableVariableNames = TypeSafeCollections.newHashMap();
    /** **/
    private final Iterable<Reference> sortedReferences;
    private final List<TableMetaInfo> tableInfo;

    public final List<TableMetaInfo> getTableInfo() {
        return tableInfo;
    }

    /** 
     * Konstruiert die Metainformationen aus der entsprechenden Konfigurationsdatei
     * 
     * @param rules ImportRules
     * @throws JPressoException wird geworfen wenn ein schwerer Fehler auftritt ;-)
     */
    public ImportMetaInfo(final ImportRules rules) throws ImportMetaInfoException {
        this.rules = rules;
        try {
            processMappings();
            if (rules.getReferences() != null) {
                //Sortieren der References, so dass Beziehungen innerhalb einer Tabelle vor Fremdschlüsselbeziehungen stehen
                //-> Felder die z.B. Kopie eines AutoInc-Feldes sind können sicher referenziert werden
                final List<Reference> references = TypeSafeCollections.newArrayList(rules.getReferences());
                //topologisches Sortieren der Tabellen und Referenzen (~Felder)
                final ImportTopology importTopology = new ImportTopology(references);
                sortedReferences = importTopology.getTopologicalSortedReferences();
                for (final String tabN : importTopology.getTopologicalSortedTables()) {
                    topologicalTableSequenceWithPath.add(tabN);
                }
                processReferences();
            } else {
                sortedReferences = Collections.emptyList();
            }
            processNormalization();
            tableInfo = createTableInfos();
        } catch (JPressoException ex) {
            throw new ImportMetaInfoException("Error creating import meta information: " + ex.getMessage(), ex);
        }
    }

    private final void processNormalization() throws WrongNameException {
        //Zu normalisierende Tabellen merken
        if (rules.getOptions() != null) {
            for (final String tableToNormalizeName : rules.getOptions().getNormalize()) {
                normalizeHM.put(tableToNormalizeName, NORMALIZE_WITH_DB);
            }
        }

        for (final String tabN : getTargetTableNames()) {
            if (topologicalTableSequence.add(tabN)) {
                topologicalTableSequenceWithPath.addAll(getTablesWithPathFromPureTableName(tabN));
            }
        }
        for (final String tabN : topologicalTableSequenceWithPath) {
            if (!(topologicalTableSequence.add(getPureTabName(tabN)))) {
                topologicalTableSequence.remove(getPureTabName(tabN));
                topologicalTableSequence.add(getPureTabName(tabN));
            }
        }
        log.info("Table ordering sequence is " + topologicalTableSequence);
        for (final String tab : topologicalTableSequence) {
            log.info("Table " + tab + " := " + getTargetFields(tab) + " with enclosing characters " + enclosingChars.get(getPureTabName(tab)));
        }
    }

    private final void processReferences() throws ImportMetaInfoException {
        for (final Reference ref : sortedReferences) {
            String pureReferencingTabName = getPureTabName(ref.getReferencingTable());
            final String referencingTable = ref.getReferencingTable().trim();
            final String referencedTable = ref.getReferencedTable().trim();
            final String referencingField = ref.getReferencingField().trim();
            final String referencedField = ref.getReferencedField().trim();
            final String eChar = ref.getEnclosingChar().trim();
            //Hinzufuegen der Foreign Keys Fields zu den einzelnen Tabellen
            if (!targetTables.containsKey(pureReferencingTabName)) {
                if (pureReferencingTabName == null) {
                    pureReferencingTabName = referencingTable;
                } else if (pureReferencingTabName.trim().length() < 1) {
                    pureReferencingTabName = "<Empty/only Whitspaces>";
                }
                throw new ImportMetaInfoException("Mastertable " + pureReferencingTabName + " does not exist!\nForeign-Key relies on a non existant Table (" + pureReferencingTabName + ")!!!");
            } else {
                final List<String> targetTabsList = targetTables.get(pureReferencingTabName);
                if (!targetTabsList.contains(referencingField)) {
                    targetTabsList.add(referencingField);
                }
            }
            //Fuellen der References-HashMap
            final FieldDescription detailField = new FieldDescription(referencedTable, referencedField);
            final FieldDescription masterField = new FieldDescription(referencingTable, referencingField);
            final List<FieldDescription> foreignFields;
            if (!referencesHashMap.containsKey(detailField)) {
                foreignFields = TypeSafeCollections.newArrayList();
                foreignFields.add(masterField);
                referencesHashMap.put(detailField, foreignFields);
            } else {
                foreignFields = referencesHashMap.get(detailField);
                if (!foreignFields.contains(masterField)) {
                    foreignFields.add(masterField);
                }
            }

            //Rollen der Tabellen abspeichern
            tableIsMaster.put(referencingTable, "-");
            List<String> detailKeys = tableIsDetail.get(referencedTable);
            if (detailKeys == null) {
                detailKeys = TypeSafeCollections.newArrayList();
                tableIsDetail.put(referencedTable, detailKeys);
            }
            if (!detailKeys.contains(referencedField)) {
                detailKeys.add(referencedField);
            }
            if (ref.isComparing()) {
                if (!compareRelevant.containsKey(pureReferencingTabName)) {
                    final List<String> fields = TypeSafeCollections.newArrayList();
                    fields.add(referencingField);
                    compareRelevant.put(pureReferencingTabName, fields);
                } else {
                    final List<String> compareFieldNamesList = compareRelevant.get(pureReferencingTabName);
                    if (!compareFieldNamesList.contains(referencingField)) {
                        compareFieldNamesList.add(referencingField);
                    }
                }
            }

            //Enclosing-chars des foreign-Key Feldes rausfinden
            if (!enclosingChars.containsKey(pureReferencingTabName)) {
                final Map<String, String> fields = TypeSafeCollections.newLinkedHashMap();
                fields.put(ref.getReferencingField(), eChar);
                enclosingChars.put(pureReferencingTabName, fields);
            } else {
                final Map<String, String> lhm = ((enclosingChars.get(pureReferencingTabName)));
                lhm.put(ref.getReferencingField(), eChar);
            }
        }
        log.info("Reference ordering sequence is " + sortedReferences);

        if (log.isDebugEnabled()) {
            log.debug("Relations created");
            log.debug(referencesHashMap);
        }
    }

    private final void processMappings() throws WrongNameException {
        for (final Mapping m : rules.getMappings()) {
            final String tableName = m.getTargetTable().trim();
            final String tableNameWithPath = m.getTargetTableWithPath().trim();
            final String eChar = m.getEnclosingChar().trim();
            final String targetField = m.getTargetField().trim();

            pureTableNameHash.put(tableName, tableName);
            //Wenn die Tabelle noch nicht gemerkt ist...
            List<String> fieldNames = targetTables.get(tableName);
            if (fieldNames == null) {
                fieldNames = TypeSafeCollections.newArrayList();
                targetTables.put(tableName, fieldNames);
            }
            if (!fieldNames.contains(targetField)) {
                fieldNames.add(targetField);
            }

            if (m.isAutoIncrement()) {
                List<Integer> fields = autoIncFieldNos.get(tableName);
                if (fields == null) {
                    fields = TypeSafeCollections.newArrayList();
                    autoIncFieldNos.put(tableName, fields);
                }
                int pos = getPositionInTable(tableName, targetField);
                if (!fields.contains(pos)) {
                    fields.add(getPositionInTable(tableName, targetField));
                }
            }

            if (m.isComparing()) {
                final List<String> fields;
                if (!compareRelevant.containsKey(tableName)) {
                    fields = TypeSafeCollections.newArrayList();
                    fields.add(targetField);
                    compareRelevant.put(tableName, fields);
                } else {
                    fields = compareRelevant.get(tableName);
                    if (!fields.contains(targetField)) {
                        fields.add(targetField);
                    }
                }
            }

            if (!enclosingChars.containsKey(tableName)) {
                final Map<String, String> fields = TypeSafeCollections.newLinkedHashMap();
                fields.put(m.getTargetField(), eChar);
                enclosingChars.put(tableName, fields);
            } else {
                final Map<String, String> lhm = ((enclosingChars.get(tableName)));
                lhm.put(m.getTargetField(), eChar);
            }

            if (m.getPath() != null && m.getPath().length() > 0) {
                pureTableNameHash.put(tableNameWithPath, tableName);
                if (!tableToAppropriateTablesWithPaths.containsKey(tableName)) {
                    final List<String> paths = TypeSafeCollections.newArrayList();
                    paths.add(tableNameWithPath);
                    tableToAppropriateTablesWithPaths.put(tableName, paths);
                } else {
                    final List<String> tabsWithPath = tableToAppropriateTablesWithPaths.get(tableName);
                    if (!tabsWithPath.contains(tableNameWithPath)) {
                        tabsWithPath.add(tableNameWithPath);
                    }
                }
            } else {
                //TODO abschliessender check ob sich eine Tabelle mit UND ohne path darin befindet -> fehler!
                final List<String> ownName = TypeSafeCollections.newArrayList();
                ownName.add(tableName);
                tableToAppropriateTablesWithPaths.put(tableName, ownName);
            }
        }//Ende Mapping Verarbeitung
        if (log.isDebugEnabled()) {
            log.debug("Mappings created");
            log.debug("Paths:\n" + tableToAppropriateTablesWithPaths);
        }
    }

    private final List<TableMetaInfo> createTableInfos() throws WrongNameException {
//        final Map<String, TableMetaInfo> tableMetaInfoMap = TypeSafeCollections.newHashMap(topologicalTableSequenceWithPath.size());
        final List<TableMetaInfo> result = TypeSafeCollections.newArrayList();
        final Map<String, Integer> posMap = TypeSafeCollections.newHashMap(topologicalTableSequenceWithPath.size());

        for (final String tab : topologicalTableSequenceWithPath) {
            posMap.put(tab, posMap.size());
        }
        for (final String tabNameWithPath : topologicalTableSequenceWithPath) {
            final Builder builder = TableMetaInfo.getBuilder();
            final List<Integer> autoIncFieldList = getAutoIncFieldNos(tabNameWithPath);
            int[] autoIncFieldArray;
            if (autoIncFieldList != null) {
                autoIncFieldArray = new int[autoIncFieldList.size()];
                for (int i = 0; i < autoIncFieldList.size(); ++i) {
                    autoIncFieldArray[i] = autoIncFieldList.get(i);
                }
            } else {
                autoIncFieldArray = new int[0];
            }
            builder.setCompareFields(getRelevantFieldNamesForComparing(tabNameWithPath));
            builder.setAutoIncrementFields(autoIncFieldArray);
            builder.setDetailKeyFields(getDetailedKeyFieldNos(tabNameWithPath));
            builder.setEnclosingCharacters(getTargetEnclosingCharsAsStringArray(tabNameWithPath));
            builder.setMasterTable(tableIsMaster.get(tabNameWithPath) != null);
            builder.setNormalizationType(getNormalizationType(tabNameWithPath));
            builder.setTableNameWithPath(tabNameWithPath);
            builder.setTableName(getPureTabName(tabNameWithPath));

            for (final Reference ref : sortedReferences) {
                if (ref.getReferencedTable().equals(tabNameWithPath)) {
                    try {
                        final int masterTableIndex = posMap.get(ref.getReferencingTable());
                        final int detailFieldPos = getPositionInTable(tabNameWithPath, ref.getReferencedField());
                        final int masterFieldPos = getPositionInTable(ref.getReferencingTable(), ref.getReferencingField());
                        builder.addStoreOffsets(masterTableIndex, masterFieldPos, detailFieldPos);
                    } catch (WrongNameException ex) {
                        log.warn(ex, ex);
                    }
                }
            }

            result.add(builder.build());
//            tableMetaInfoMap.put(tabNameWithPath, builder.build());
        }
//        final List<TableMetaInfo> result = TypeSafeCollections.newArrayList(tableMetaInfoMap.size());
//        for (final String tabNameWithPath : topologicalTableSequenceWithPath) {
//            result.add(tableMetaInfoMap.get(tabNameWithPath));
//        }
        return result;
    }

//    public ImportMetaInfo(final ImportRules rules) throws ImportMetaInfoException {
//        this.rules = rules;
//        try {
//            final Set<String> prepareTableSequence = TypeSafeCollections.newLinkedHashSet();
//            final Set<String> prepareTableSequenceWithPath = TypeSafeCollections.newLinkedHashSet();
//            //process mappings
//            for (final Mapping m : rules.getMappings()) {
//                final String tableName = m.getTargetTable();
//                pureTableNameHash.put(tableName, tableName);
//                //Wenn die Tabelle noch nicht gemerkt ist...
//                if (!targetTables.containsKey(tableName)) {
//                    //Neue List die sich die Felder merkt
//                    final List<String> fields = TypeSafeCollections.newArrayList();
//                    //Feld des aktuellen Mappings dazu
//                    fields.add(m.getTargetField());
//                    targetTables.put(tableName, fields);
//                } else {
//                    //sonst den Vector der Felder geben lassen
//                    final List<String> v = (targetTables.get(tableName));
//                    //und wenn das feld noch nicht drin ist, hinzufuegen
//                    if (!v.contains(m.getTargetField())) {
//                        v.add(m.getTargetField());
//                    }
//                }
//                if (m.isAutoIncrement()) {
//                    List<Integer> fields = autoIncFieldNos.get(tableName);
//                    if (fields == null) {
//                        fields = TypeSafeCollections.newArrayList();
//                        autoIncFieldNos.put(tableName, fields);
//                    }
//                    int pos = getPositionInTable(tableName, m.getTargetField());
//                    if (!fields.contains(pos)) {
//                        fields.add(getPositionInTable(tableName, m.getTargetField()));
//                    }
//                }
//
//                if (m.isComparing()) {
//                    final List<String> fields;
//                    if (!compareRelevant.containsKey(tableName)) {
//                        fields = TypeSafeCollections.newArrayList();
//                        fields.add(m.getTargetField());
//                        compareRelevant.put(tableName, fields);
//                    } else {
//                        fields = compareRelevant.get(tableName);
//                        if (!fields.contains(m.getTargetField())) {
//                            fields.add(m.getTargetField());
//                        }
//                    }
//                }
//                String eChar = m.getEnclosingChar();
//                if (eChar == null) {
//                    eChar = EMPTY;
//                }
//                if (!enclosingChars.containsKey(tableName)) {
//                    final Map<String, String> fields = TypeSafeCollections.newLinkedHashMap();
//                    fields.put(m.getTargetField(), eChar);
//                    enclosingChars.put(tableName, fields);
//                } else {
//                    final Map<String, String> lhm = ((enclosingChars.get(tableName)));
//                    lhm.put(m.getTargetField(), eChar);
//                }
//
//                if (m.getPath() != null && m.getPath().length() > 0) {
//                    final String tableNameWithPath = tableName + getBracketAppenderFromPath(m.getPath());
//                    pureTableNameHash.put(tableNameWithPath, tableName);
//                    if (!tableToAppropriateTablesWithPaths.containsKey(tableName)) {
//                        final List<String> paths = TypeSafeCollections.newArrayList();
//                        paths.add(tableNameWithPath);
//                        tableToAppropriateTablesWithPaths.put(tableName, paths);
//                    } else {
//                        final List<String> tabsWithPath = tableToAppropriateTablesWithPaths.get(tableName);
//                        if (!tabsWithPath.contains(tableNameWithPath)) {
//                            tabsWithPath.add(tableNameWithPath);
//                        }
//                    }
//
//                } else {
//                    //TODO abschliessender check ob sich eine Tabelle mit UND ohne path darin befindet -> fehler!
//                    final List<String> ownName = TypeSafeCollections.newArrayList();
//                    ownName.add(tableName);
//                    tableToAppropriateTablesWithPaths.put(tableName, ownName);
//                }
//            }//End Mapping Loop
//
//            log.debug("Mappings created");
//            log.debug("Paths:\n" + tableToAppropriateTablesWithPaths);
//            //Analysieren des Relations-Abschnitt
//            Iterable<Reference> sortedRefs = null;
//            if (rules.getReferences() != null) {
//                //Sortieren der References, so dass Beziehungen innerhalb einer Tabelle vor Fremdschlüsselbeziehungen stehen
//                //-> Felder die z.B. Kopie eines AutoInc-Feldes sind können sicher referenziert werden
//                final List<Reference> references = TypeSafeCollections.newArrayList(rules.getReferences());
//                //topologisches Sortieren der Tabellen und Referenzen (~Felder)
//                final ImportTopology importTopology = new ImportTopology(references);
//                sortedRefs = importTopology.getTopologicalSortedReferences();
//                for (final String tabN : importTopology.getTopologicalSortedTables()) {
//                    prepareTableSequenceWithPath.add(tabN);
//                }
//                for (final Reference ref : sortedRefs) {
//                    String pureReferencingTabName = getPureTabName(ref.getReferencingTable());
//                    //Hinzufuegen der Foreign Keys Fields zu den einzelnen Tabellen
//                    if (!targetTables.containsKey(pureReferencingTabName)) {
//                        if (pureReferencingTabName == null) {
//                            pureReferencingTabName = ref.getReferencingTable();
//                        } else if (pureReferencingTabName.trim().length() < 1) {
//                            pureReferencingTabName = "<Empty/only Whitspaces>";
//                        }
//                        throw new ImportMetaInfoException("Mastertable " + pureReferencingTabName + " does not exist!\nForeign-Key relies on a non existant Table (" + pureReferencingTabName + ")!!!");
//                    } else {
//                        final List<String> v = ((targetTables.get(pureReferencingTabName)));
//                        if (!v.contains(ref.getReferencingField())) {
//                            v.add(ref.getReferencingField());
//                        }
//                    }
//                    //Fuellen der References-HashMap
//                    final String detailTab = ref.getReferencedTable().trim();
//                    final FieldDescription detailField = new FieldDescription(detailTab, ref.getReferencedField().trim());
//                    final FieldDescription masterField = new FieldDescription(ref.getReferencingTable(), ref.getReferencingField());
//                    final List<FieldDescription> foreignFields;
//                    if (!referencesHashMap.containsKey(detailField)) {
//                        foreignFields = TypeSafeCollections.newArrayList();
//                        foreignFields.add(masterField);
//                        referencesHashMap.put(detailField, foreignFields);
//                    } else {
//                        foreignFields = referencesHashMap.get(detailField);
//                        if (!foreignFields.contains(masterField)) {
//                            foreignFields.add(masterField);
//                        }
//                    }
//
//                    //Rollen der Tabellen abspeichern
//                    tableIsMaster.put(ref.getReferencingTable(), "-");
//                    List<String> detailKeys = tableIsDetail.get(ref.getReferencedTable());
//                    if (detailKeys == null) {
//                        detailKeys = TypeSafeCollections.newArrayList();
//                        tableIsDetail.put(ref.getReferencedTable(), detailKeys);
//                    }
//                    final String detKey = ref.getReferencedField();
//                    if (!detailKeys.contains(detKey)) {
//                        detailKeys.add(detKey);
//                    }
//                    if (ref.isComparing()) {
//                        if (!compareRelevant.containsKey(pureReferencingTabName)) {
//                            final List<String> fields = TypeSafeCollections.newArrayList();
//                            fields.add(ref.getReferencingField());
//                            compareRelevant.put(pureReferencingTabName, fields);
//                        } else {
//                            final List<String> v = ((compareRelevant.get(pureReferencingTabName)));
//                            if (!v.contains(ref.getReferencingField())) {
//                                v.add(ref.getReferencingField());
//                            }
//                        }
//                    }
//
//                    //Enclosing-chars des foreign-Key Feldes rausfinden
//                    String eChar = ref.getEnclosingChar();
//                    if (eChar == null) {
//                        eChar = EMPTY;
//                    }
//                    if (!enclosingChars.containsKey(pureReferencingTabName)) {
//                        final Map<String, String> fields = TypeSafeCollections.newLinkedHashMap();
//                        fields.put(ref.getReferencingField(), eChar);
//                        enclosingChars.put(pureReferencingTabName, fields);
//                    } else {
//                        final Map<String, String> lhm = ((enclosingChars.get(pureReferencingTabName)));
//                        lhm.put(ref.getReferencingField(), eChar);
//                    }
//                }
//                log.info("Reference ordering sequence is " + sortedRefs);
//            }//End References Loop
//            if (log.isDebugEnabled()) {
//                log.debug("Relations created");
//                log.debug(referencesHashMap);
//            }
//            //Zu normalisierende Tabellen merken
//            if (rules.getOptions() != null) {
//                for (final String tableToNormalizeName : rules.getOptions().getNormalize()) {
//                    normalizeHM.put(tableToNormalizeName, NORMALIZE_WITH_DB);
//                }
//            }
//
//
//            for (final String tabN : getTargetTableNames()) {
//                if (prepareTableSequence.add(tabN)) {
//                    prepareTableSequenceWithPath.addAll(getTablesWithPathFromPureTableName(tabN));
//                }
//            }
//            for (final String tabN : prepareTableSequenceWithPath) {
//                if (!(prepareTableSequence.add(getPureTabName(tabN)))) {
//                    prepareTableSequence.remove(getPureTabName(tabN));
//                    prepareTableSequence.add(getPureTabName(tabN));
//                }
//            }
//            topologicalTableSequenceWithPath = prepareTableSequenceWithPath;
//            topologicalTableSequence = prepareTableSequence;
//            log.info("Table ordering sequence is " + topologicalTableSequence);
//            for (final String tab : topologicalTableSequence) {
//                log.info("Table " + tab + " := " + getTargetFields(tab) + " with enclosing characters " + enclosingChars.get(getPureTabName(tab)));
//            }
//            //-----------------
////            final Map<String, Integer> posMap = TypeSafeCollections.newHashMap(topologicalTableWithPathSequence.size());
////            for (final String tab : topologicalTableWithPathSequence) {
////                posMap.put(tab, posMap.size());
////            }
////            log.fatal(posMap);
////            for (final String s : posMap.keySet()) {
////                log.fatal(s + " " + testCreatesReferences(posMap, s, sortedRefs));
////            }
//            //-----------------
//        } catch (JPressoException ex) {
//            throw new ImportMetaInfoException("Error creating import meta information: " + ex.getMessage(), ex);
//        }
//    }
    /** 
     * Liefert die Felder der Datenquelle
     * @return List: Felder der Datenquelle
     */
    public final Iterable<String> getSourceFields() {
        return sourceFields;
    }

    /** 
     * Setzt die Felder der Datenquelle. Analysiert das Source ResultSet und setzt dann
     * die entsprechenden Spaltennamen
     * 
     * @param rs ResultSet das analysiert wird
     * @throws JPressoException wird geworfen, wenn ein SQL Fehler auftritt
     */
    public final void setSourceFields(final DataSource dataSource) throws ImportMetaInfoException {
        boolean lower = rules.getSourceQuery().labelsToLowerCase();
        boolean upper = rules.getSourceQuery().labelsToUpperCase();
        for (int i = 0; i < dataSource.getColumnCount(); ++i) {
            String sField = dataSource.getColumnLabel(i);
            if (sField != null) {
                if (lower) {
                    sField = sField.toLowerCase();
                } else if (upper) {
                    sField = sField.toUpperCase();
                }
                sourceFields.add(sField);
            }
        }

        final Set<String> findDuplicates = TypeSafeCollections.newHashSet();
        for (final String cur : sourceFields) {
            final Matcher javaVarNameValidator = VALID_JAVA_VARIABLE_NAME.matcher(cur);
            if (!javaVarNameValidator.matches()) {
                throw new ImportMetaInfoException("Columnlabel " + cur + " is not a valid Java-variable name!\n Please rename the column/label (with: SELECT invalidName AS validName).");
            }
            if (!findDuplicates.add(cur)) {
                throw new ImportMetaInfoException("Query creates duplicated source fieldnames!\nPlease use UNIQUE labels for the fields to assure well-defined mapping!");
            }
        }

        if(log.isDebugEnabled()) {
            log.debug("Found the follwing source fields: "+sourceFields);
        }
    }

    /** 
     * Liefert einen Iterator der ueber die Ziehltabellennamen laeuft
     * 
     * @return Iterator der ueber die Ziehltabellennamen laeuft
     */
    public final Iterable<String> getTargetTableNames() {
        return targetTables.keySet();
    }

    /** 
     * Liefert die Spaltennamen der uebergebenen Zieltabelle
     * 
     * @return Spaltennamen der uebergebenen Zieltabelle als List
     * @param pureReferencingTabName Zieltabelle
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     *
     */
    private final List<String> getTargetFields(String tableName) throws WrongNameException {
        tableName = getPureTabName(tableName);
        final List<String> ret = (targetTables.get(tableName));
        if (ret == null) {
            throw new WrongNameException("Wrong TableName: " + tableName);
        }
        return ret;
    }

    /** 
     * Liefert alle im Konfigurationsfile angegebenen Enclosing Characters fuer eine
     * bestimmte Tabelle
     * 
     * @param tabN Tabellennamen
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     * @return String[]: Kopie des der enclosing Characters fuer eine bestimmte Tabelle
     */
    public final String[] getTargetEnclosingCharsAsStringArray(String tabName) throws WrongNameException {
        tabName = getPureTabName(tabName);
        final Map<String, String> lhm = enclosingChars.get(tabName);
        if (lhm == null) {
            throw new WrongNameException("Wrong TableName: " + tabName);
        }
        final List<String> v = TypeSafeCollections.newArrayList();
        for (final String encChar : lhm.values()) {
            v.add(encChar);
        }
        return v.toArray(new String[v.size()]);
    }

    /** 
     * Liefert die Spaltennamen der uebergebenen Zieltabelle
     * @return Spaltennamen der uebergebenen Zieltabelle als String[]
     * 
     * @param pureReferencingTabName Zieltabelle
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     */
    public final String[] getTargetFieldsAsStringArray(String tableName) throws WrongNameException {
        final List<String> v = getTargetFields(tableName);
        return v.toArray(new String[v.size()]);
//        final String[] s = ;
//        for (int i = 0; i < s.length; ++i) {
//            s[i] = v.get(i);
//        }
//        return s;
    }

    /** 
     * Erzeugt aus einem Zieltabellenname ein SQL-Statement. Wenn dieses SQL-Statement
     * (Select .... ) ohne Fehler ausgefuehrt werden kann, existiert die Tablle im
     * Zielsystem und der Import kann durchgefuehrt werden.
     * 
     * @return String: SQL-Statement
     * @param pureReferencingTabName Zieltabellenname
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     */
    public final String getControlStatement(String tableName) throws WrongNameException {
        tableName = getPureTabName(tableName);
        final StringBuilder stmnt = new StringBuilder(SELECT);
        final List<String> fields = (targetTables.get(tableName));
        if (fields != null) {
            final Iterator<String> it = fields.iterator();
            while (it.hasNext()) {
                String field = it.next();
                stmnt.append(field);
                if (it.hasNext()) {
                    stmnt.append(KOMMA);
                }
            }
            stmnt.append(FROM).append(tableName);
            stmnt.append(WHERE_TRUE);
            return stmnt.toString();
        } else {
            throw new WrongNameException("Wrong TableName: " + tableName);
        }
    }

    /** 
     * Liefert eine Stringrepraesentation des Objektes
     * 
     * @return Stringrepraesentation des Objektes
     */
    @Override
    public final String toString() {
        return targetTables.toString() + "\n\n" + sourceFields.toString();
    }

    /** 
     * Erzeugt aus den Metadaten der import-Konfiguration eine Java-Klasse. Diese
     * Klasse wird dazu verwendet den entsprechenden Felder zuzuweisen und einfache
     * Datenkonvertierungsaufgaben zu uebernehmen. Spaeter im Workflow wird diese Klasse
     * waehrend der Laufzeit kompiliert und aufgerufen.
     *
     * @param canonicalAssignerName Dateiname der Zukuenftigen Java-Klasse
     * @throws JPressoException wird bei einem Fehler geworfen
     * @return String: Java-Quelltext der Klasse
     */
    public final String generateAssignerJavaClassCode(final String canonicalAssignerName) throws JPressoException {
        int splitPoint = canonicalAssignerName.lastIndexOf(".");
        final String packageName = splitPoint > 0 ? canonicalAssignerName.substring(0, splitPoint) : null;
        final String className = splitPoint > 0 ? canonicalAssignerName.substring(splitPoint + 1) : canonicalAssignerName;
        final Map<String, String> topologicalTablesWithPathSequenceEscaped = TypeSafeCollections.newLinkedHashMap();
        for (final String tabName : topologicalTableSequenceWithPath) {
            topologicalTablesWithPathSequenceEscaped.put(tabName, EscapeUtil.escapeJava(tabName));
        }
        final StringBuilder buf = new StringBuilder("/**\n * cids ImportAnt mapping class (DO NOT CHANGE)\n");
        buf.append(" * Source created @ " + System.currentTimeMillis() + "\n");
        if (packageName != null) {
            buf.append(" */\npackage " + packageName + ";\n\n");
        }
        buf.append("import " + JPressoFileManager.DIR_CDE + ".*;\n");
        buf.append("import java.util.Map;\n");
        buf.append("import java.util.HashMap;\n");
        //buf.append("import java.util.LinkedHashMap;\n");
        buf.append("import de.cismet.jpressocore.kernel.UniversalContainer;\n\n");
        buf.append(" public final class " + className + " extends " + AssignerBase.class.getCanonicalName() + " implements " + Assigner.class.getCanonicalName() + " {\n\n");
        buf.append("    private final Map<String, int[]> lookupMap;\n");
        buf.append("    private String[][] currentRows;\n\n");
        buf.append("    public " + className + "() {\n");
        buf.append("        lookupMap = new HashMap<String, int[]>();\n");
//        buf.append("        //currentRows = new LinkedHashMap<String, String[]>();\n\n");
        int i = -1;
        for (final Map.Entry<String, String> tabUnescapedToEscaped : topologicalTablesWithPathSequenceEscaped.entrySet()) {
            ++i;
            final List<String> fields = targetTables.get(getPureTabName(tabUnescapedToEscaped.getKey()));
            if (fields != null) {
                for (int j = 0; j < fields.size(); ++j) {
                    buf.append("        lookupMap.put(\"").append(tabUnescapedToEscaped.getValue());
                    buf.append(".").append(EscapeUtil.escapeJava(fields.get(j))).append("\", new int[]{").append(i).append(", ").append(j).append("});\n");
                }
            } else {
                log.error("Can not find field entries for table " + tabUnescapedToEscaped.getKey());
            }
        }
//        for (final Mapping m : rules.getMappings()) {
//
//            if (!m.isAutoIncrement()) {
//                buf.append("        lookupMap.put(\"").append(EscapeUtil.escapeJava(m.getTargetTable()));
//                final String path = m.getPath();
//                if (path != null && path.length() > 0) {
//                    buf.append("[").append(path).append("]");
//                }
//                buf.append(".").append(EscapeUtil.escapeJava(m.getTargetField()) + "\", new int[]{" + getPositionInTable(m.getTargetTable(), m.getTargetField()) + "});\n");
//            }
//        }
        buf.append("    }\n\n");
        buf.append("    public final String[][] assign(final java.sql.Connection $tc, final String[] $args, " + UniversalContainer.class.getCanonicalName() + " $universalC) {\n");
        buf.append("        // init\n");
        buf.append("        " + FIELD_TAG + "TargetConnection = $tc;\n");
        buf.append("        " + FIELD_TAG + "UniversalContainer = $universalC;\n");
        i = 0;
        for (final String fieldName : getSourceFields()) {
            buf.append("        final String " + fieldName + " = $args[" + i + "];\n");
            ++i;
        }
        buf.append("\n");
        for (final String tabName : getTopologicalTableSequenceWithPath()) {
            log.debug("getClass: TargetTables: ->" + tabName);
            buf.append("        final String[] " + (getScriptVariableName(tabName)) + " = new String[" + getFieldCount(tabName) + "];\n");
        }
        buf.append("\n        // Assigning Section\n");
        for (final Mapping m : rules.getMappings()) {
            if (!m.isAutoIncrement()) {
                buf.append("        " + getScriptVariableNameWithIndex(m.getTargetTable(), m.getTargetField(), m.getPath()) + " = " + m.getContent() + ";" + "//" + ERROR_FINDER + m.getContent() + "\n");
            }
        }
        //preparing the return value
        buf.append("\n        // Preparing the return value");
        buf.append("\n        currentRows = new String[" + topologicalTableSequenceWithPath.size() + "][];\n");
        final Properties filters = rules.getOptions().getFilter();
        i = -1;
        for (final String tName : topologicalTableSequenceWithPath) {
            buf.append("        currentRows[" + (++i) + "] = " + getScriptVariableName(tName) + ";\n");
        }
        if (!filters.isEmpty()) {
            buf.append("\n    // Filter expressions\n");
        }
        i = -1;
        for (final String tabName : topologicalTableSequenceWithPath) {
            ++i;
            final String filterString = filters.getProperty(tabName);
            boolean isFilterSet = filterString != null && filterString.length() > 0;
            if (isFilterSet) {
                log.info("Filter for " + tabName + " is set as \"" + filterString + "\".");
                buf.append("    if(!(" + filterString + ")) { //" + FILTER_ERROR_FINDER + filterString + "\n        ");
                buf.append("        currentRows[" + i + "] = null;\n");
                buf.append("    }\n");
            }
        }
        buf.append("        return currentRows;\n");
        buf.append("    }\n");
        //inhereted Methods
        buf.append("    // Predefined Methods");
        buf.append("\n    public final java.sql.Connection getTargetConnection() {");
        buf.append("\n        return " + FIELD_TAG + "TargetConnection;");
        buf.append("\n    }\n");
        buf.append("\n    public final void setTargetConnection(final java.sql.Connection targetConnection) {");
        buf.append("\n        " + FIELD_TAG + "TargetConnection = targetConnection;");
        buf.append("\n    }\n");
        buf.append("\n    public final java.sql.Connection getSourceConnection() {");
        buf.append("\n        return " + FIELD_TAG + "SourceConnection;");
        buf.append("\n    }\n");
        buf.append("\n    public final void setSourceConnection(final java.sql.Connection sourceConnection) {");
        buf.append("\n        " + FIELD_TAG + "SourceConnection = sourceConnection;");
        buf.append("\n    }\n");
        buf.append("\n    public final UniversalContainer getUniversalContainer() {");
        buf.append("\n        return " + FIELD_TAG + "UniversalContainer;");
        buf.append("\n    }\n");
        buf.append("\n    public final void setUniversalContainer(final UniversalContainer universalContainer) {");
        buf.append("\n        " + FIELD_TAG + "UniversalContainer = universalContainer;");
        buf.append("\n    }\n");
        buf.append("\n    public final String lookup(final String table, String field) {");
        buf.append("\n        return lookup(table + \".\" + field);");
        buf.append("\n    }\n");
        buf.append("\n");
        buf.append("\n    public final String lookup(final String tableAndField) {");
        buf.append("\n        final int[] pos = lookupMap.get(tableAndField);");
        buf.append("\n        if (pos == null) {");
        buf.append("\n             return null;");
        buf.append("\n        }");
        buf.append("\n        final String[] row = currentRows[pos[0]];");
        buf.append("\n        if (row == null) {");
        buf.append("\n             return null;");
        buf.append("\n        }");
        buf.append("\n        return row[pos[1]];");
        buf.append("\n    }\n");
        buf.append("\n");
        // break if Funktion
        buf.append("    public final String cidsBreakIf(boolean breakIt, final String defaultValue) {\n");
        buf.append("        if (breakIt) { return \"" + ImportMetaInfo.BREAK_IDENTIFIER + "\";} else { return defaultValue; }\n");
        buf.append("    }\n");
        buf.append("\n}\n");
        return buf.toString();
    }

    /** 
     * Methode zur Erzeugung des Java-Codes zum Zugriff auf eine Spalte einer
     * Zieltabelle.
     * 
     * @param targetTable Zieltabelle
     * @param targetField Zielspalte
     * @throws WrongNameException wird geworfen, wenn der Tabellenname oder der Spaltenname nicht stimmt
     * @return Java-Code zum Zugriff auf eine Spalte einer Zieltabelle
     */
    private final String getScriptVariableNameWithIndex(final String targetTable, final String targetField, String path) throws WrongNameException {
        return new StringBuffer(getScriptVariableName(targetTable, path)).append("[").append(getPositionInTable(targetTable, targetField)).append("]").toString();
    }

    private final String getScriptVariableName(final String targetTable, String path) throws WrongNameException {
        if (path == null) {
            path = EMPTY;
        }
        path = getBracketAppenderFromPath(path);
        return getScriptVariableName(new StringBuffer(targetTable).append(path).toString());
    }

    private final String getScriptVariableName(final String targetTableWithPath) throws WrongNameException {
        String ret = tableVariableNames.get(targetTableWithPath);
        if (ret == null) {
            ret = EscapeUtil.escapeJavaVariableName(
                    new StringBuffer(TABLE_TAG).append(tableVariableNames.size()).append("$").append(EscapeUtil.escapeJavaVariableName(targetTableWithPath.replace('[', '$').replace(']', '$'))).toString());
            tableVariableNames.put(targetTableWithPath, ret);
        }
        return ret;
    }

    /** 
     * Liefert den Spaltenindex der Spalte einer Zieltabelle.
     * 
     * @param pureReferencingTabName Zieltabelle
     * @param fieldName Zielspalte
     * @throws WrongNameException wird geworfen, wenn der Tabellenname oder der Spaltenname nicht stimmt
     * @return Spaltenindex
     */
    public final int getPositionInTable(String tableName, final String fieldName) throws WrongNameException {
        tableName = getPureTabName(tableName);
        final List<String> fields = targetTables.get(tableName);
        if (fields == null) {
            throw new WrongNameException("Malformed or not existing Table Name (" + tableName + ")");
        }
        final int index = fields.indexOf(fieldName);
        if (index == -1) {
            throw new WrongNameException("Malformed or not existing FieldName (" + fieldName + ")", fieldName);
        }
        return index;
    }

    /** 
     * Gibt die Anzahl der Felder einer Zieltabelle aus
     * 
     * @param pureReferencingTabName Zieltabellenname
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     * @return Anzahl der Felder der Zieltabelle
     */
    private int getFieldCount(final String tableName) throws WrongNameException {
        final String pureTabName = getPureTabName(tableName);
        final List<String> fields = targetTables.get(pureTabName);
        if (fields == null) {
            throw new WrongNameException("Malformed Table Name: " + tableName);
        }
        return fields.size();
    }

    // Methoden die fuers ReferenceManagement benoetigt werden
    /** 
     * Gibt eine List aus, in der die Reihenfolge der Zieltabellen mit den Beziehungspfaden abgelegt ist.
     * Die Reihenfolge der Zieltabellen ist nicht frei waehlbar, da es Beziehungen
     * zwischen den einzelnen Zieltabellen gibt.
     * Unterschied zu getTopologicalTableSequence(): Beziehungspfade explizit einzeln enthalten.
     * 
     * Wird in addCurrentRow benoetigt um in der richtigen Reihenfolge ueber alle
     * Tabellenpfade zu iterieren.
     * 
     * @return List mit den Namen der Zieltabellen (Strings) in der richtigen Reihenfolge.
     */
    public Iterable<String> getTopologicalTableSequenceWithPath() {
        return topologicalTableSequenceWithPath;
    }

    /** 
     * Gibt eine List aus, in dem die Reihenfolge der Zieltabellen abgelegt ist.
     * Die Reihenfolge der Zieltabellen ist nicht frei waehlbar, da es Beziehungen
     * zwischen den einzelnen Zieltabellen gibt.
     * 
     * Unterschied zu getTopologicalTableSequenceWithPath(): Beziehungspfade zusammengefasst,
     * nur die eigentlichen Tabellen enthalten.
     * 
     * Wird im Finalizer benoetigt um zu wissen, welche Tabellen zuerst geschrieben 
     * werden muessen (weil es z.B. constraints gibt, die verlangen, dass gewisse 
     * Werte bereits in der datenbank existieren.)
     * 
     * @return List mit den Namen der Zieltabellen (Strings) in der richtigen Reihenfolge.
     */
    public Iterable<String> getTopologicalTableSequence() {
        return topologicalTableSequence;
    }

    /** 
     * Liefert alle Tabellennamen, die das uebergebene Feld der uebergebenen Tabelle als
     * Fremdschluessel referenzieren.
     * <br>
     * Bsp.:<br>
     * ein <CODE>getMasterTables("Url","UrlID")</CODE> koennte als Ergebniss liefern:
     * <br>
     * <CODE>"Altlast",""Fluss"</CODE> weil beide Tabellen auf die Tabelle Url
     * verweisen und dort ihre Beschreibung ablegen
     * @return alle Tabellennamen, die das uebergebene Feld der uebergebenen Tabelle als
     * Fremdschluessel referenzieren
     * @param pureReferencingTabName Tabellenname
     * @param fieldName Feldname
     * @throws WrongNameException wird geworfen, wenn der Tabellenname oder Spaltenname nicht stimmt
     */
    public String[] getMasterTables(final String tableName, final String fieldName) throws WrongNameException {
        final List<FieldDescription> masterTabList = referencesHashMap.get(new FieldDescription(tableName, fieldName));
        if (masterTabList == null) {
            throw new WrongNameException("Wrong TableName.FieldName: " + tableName + "." + fieldName);
        }
        final String[] arr = new String[masterTabList.size()];
        for (int i = 0; i < masterTabList.size(); ++i) {
            arr[i] = masterTabList.get(i).getTableName();
        }
        return arr;
    }

    /** 
     * Liefert den Feldnamen des Feldes, das auf das
     * uebergebene Feld der uebergebenen Tabelle referenziert (<B>Fremdschluessel</B>). Zusaetzlich muss noch die
     * Tabelle angegeben werden. 
     * Es ist zu beachten, dass ein Feld mehrfach referenziert werden kann. Darum wird mittels eines zirkularen Iterators
     * immer wieder ueber die alle gefundenen Master-Felder iteriert.
     *
     * <br>
     * Bsp.:
     * <br>
     * erster Schritt:
     * <br>
     * ein <CODE>getMasterTables("Url","UrlID")</CODE> koennte als Ergebniss liefern:
     * <br>
     * <CODE>"Altlast",""Fluss"</CODE> weil beide Tabellen auf die Tabelle Url
     * verweisen und dort ihre Beschreibung ablegen
     * <br>
     * wenn man nun <CODE>getReferencingField("Url","UrlID","Altlast")</CODE> aufruft,
     * koennte die Methode folgendes Ergebniss liefern:
     *    <CODE>DescrId</CODE>
     * @return Fremdschluesselfeldnamen
     * @param pureReferencingTabName Tabellenname der Detailtabelle
     * @param fieldName Feldname des Detailschluessels (Primaerschluessel der Detailtabelle)
     * @param masterTable Tabellenname der Tabelle die auf die Detailtabelle referenziert
     * @throws WrongNameException wird geworfen, wenn der Tabellenname oder Spaletnname nicht stimmt
     */
    public final String getReferencingField(final String tableName, final String fieldName, final String masterTable) throws WrongNameException {
        final FieldDescription detailFieldDescription = new FieldDescription(tableName, fieldName);
        Map<String, ListCirculator<String>> masterTableFieldIterators = referencingFields.get(detailFieldDescription);
        if (masterTableFieldIterators == null) {
            masterTableFieldIterators = TypeSafeCollections.newHashMap();
            referencingFields.put(detailFieldDescription, masterTableFieldIterators);
        }
        ListCirculator<String> circ = masterTableFieldIterators.get(masterTable);
        if (circ != null) {
            //Zirkularer Iterator gefunden, liefere naechstes Element
            return circ.next();
        } else {
            final List<FieldDescription> refFields = referencesHashMap.get(detailFieldDescription);
            if (refFields == null) {
                throw new WrongNameException("Wrong TableName.FieldName: " + tableName + "." + fieldName);
            }
            //Liste der richtigen Master-Tabellen Felder erstellen
            final List<String> fields = TypeSafeCollections.newArrayList();
            for (int i = 0; i < refFields.size(); ++i) {
                if (refFields.get(i).getTableName().equals(masterTable)) {
                    fields.add(refFields.get(i).getFieldName());
                }
            }
            if (fields.isEmpty()) {
                throw new WrongNameException("Wrong ForeignTableName:" + masterTable);
            }
            //Neuen zirkularen Iterator für diese Liste anlegen und abspeichern
            circ = new ListCirculator<String>(fields);
            masterTableFieldIterators.put(masterTable, circ);
            return circ.next();
        }
    }

    /** 
     * Liefert die Nummer des Feldes, das auf das
     * uebergebene Feld der uebergebenen Tabelle referenziert (<B>Fremdschluessel</B>). Zusaetzlich muss noch die
     * Tabelle angegeben werden.
     *
     *<br>
     * Bsp.:
     *<br>
     * erster Schritt:
     *<br>
     * ein <CODE>getForeignTables("Url","UrlID")</CODE> koennte als Ergebniss liefern:
     *<br>
     * <CODE>"Altlast",""Fluss"</CODE> weil beide Tabellen auf die Tabelle Url
     * verweisen und dort ihre Beschreibung ablegen
     *<br>
     * wenn man nun <CODE>getReferencingField("Url","UrlID","Altlast")</CODE> aufruft,
     * koennte die Methode folgendes Ergebniss liefern:
     *    <CODE>DescrId</CODE>
     * @return Fremdschluesselfeldnummer
     * @param pureReferencingTabName Tabellenname der Detailtabelle
     * @param fieldName Feldname des Detailschluessels (Primaerschlurssel der Detailtabelle)
     * @param masterTable Tabellenname der Tabelle die auf die Detailtabelle referenziert
     * @throws WrongNameException wird geworfen, wenn der Tabellenname oder der Spaltenname nicht stimmt
     */
    public final int getReferencingFieldNo(final String tableName, final String fieldName, final String masterTable) throws WrongNameException {
        return getPositionInTable(masterTable, getReferencingField(tableName, fieldName, masterTable));
    }

    /** 
     * Gibt an ob das angegebene Feld der angegebenen Tabelle in einer Reference
     * verwendet wird.
     * 
     * @param pureReferencingTabName Tabellenname
     * @param fieldName Feldname
     * @return boolean: <CODE>true</CODE> wenn in Reference vorhanden, <CODE>false</CODE> sonst
     */
    public final boolean isInRelation(final String tableName, final String fieldName) {
        return referencesHashMap.containsKey(new FieldDescription(tableName, fieldName));
    }

    /**
     * Schaut nach ob wir gemaess Beziehungspfad beim Einfügen der 
     * DetailTable-Referenzen in die MasterTable an der richtigen Tabelle werkeln
     * 
     * @param detailTable
     * @param foreignTable
     * @return
     */
    public final boolean isTheRightMasterTable(final String detailTable, final String masterTable, final String refField) {
        Map<String, Map<String, Boolean>> masterField = rightMasterTableCache.get(detailTable);
        Map<String, Boolean> fieldBoolean;
        Boolean result;
        if (masterField != null) {
            fieldBoolean = masterField.get(masterTable);
            if (fieldBoolean != null) {
                result = fieldBoolean.get(refField);
                if (result != null) {
                    return result;
                }
            } else {
                fieldBoolean = TypeSafeCollections.newHashMap();
                masterField.put(masterTable, fieldBoolean);
            }
        } else {
            masterField = TypeSafeCollections.newHashMap();
            fieldBoolean = TypeSafeCollections.newHashMap();
            masterField.put(masterTable, fieldBoolean);
            rightMasterTableCache.put(detailTable, masterField);

        }
        final int ftIndex = masterTable.lastIndexOf(OPENING_BRACKET);
        if (ftIndex == -1) {
            fieldBoolean.put(refField, true);
            return true;
        }
        result = testDetailMasterRelationship(detailTable, masterTable, refField);
        fieldBoolean.put(refField, result);
        return result;
    }

    /**
     * Checkt die Reference um festzustellen ob wir gemaess Beziehungspfad beim Einfügen der 
     * DetailTable-Referenzen in die MasterTable an der richtigen Tabelle werkeln.
     * 
     * @param dTab
     * @param mTab
     * @param refField
     * @return
     */
    private final boolean testDetailMasterRelationship(final String dTab, final String mTab, final String refField) {
        for (final Reference r : rules.getReferences()) {
            if (r.getReferencedTable().equals(dTab)) {
                if (r.getReferencedField().equals(refField)) {
                    if (r.getReferencingTable().equals(mTab)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** 
     * Liefert die Feldnummern einer Tabelle,  bei denen in der Importkonfiguration
     * festgelegt wurde, dass sie AutoInkrement sind.
     * 
     * @param pureReferencingTabName Tabellenname
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     * @return List mit den Feldnummern der AutoInkrementfelder
     */
    public final List<Integer> getAutoIncFieldNos(String tableName) throws WrongNameException {
        tableName = getPureTabName(tableName);
        return autoIncFieldNos.get(tableName);
    }

    /** 
     * Gibt die Feldnamen an, die fuer das Vergleichen beim Normalisieren notwendig
     * sind
     * 
     * @param pureReferencingTabName Tabellenname
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     * @return List mit den Feldnamen, die fuer das Vergleichen beim Normalisieren noptwendig
     * sind
     */
    private final String[] getRelevantFieldNamesForComparing(String tableName) {
        tableName = getPureTabName(tableName);
        final List<String> compFieldNamesList = compareRelevant.get(tableName);
        if (compFieldNamesList == null) {
            return new String[0];
        }
        final String[] arr = new String[compFieldNamesList.size()];
        for (int i = 0; i < compFieldNamesList.size(); ++i) {
            arr[i] = compFieldNamesList.get(i);
        }
        return arr;
    }

    /** 
     * Gibt die Feldnummern an, die fuer das Vergleichen beim Normalisieren notwendig
     * sind
     * 
     * @param pureReferencingTabName Tabellenname
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     * @return int[] mit den Feldnummern, die fuer das Vergleichen beim Normalisieren noptwendig
     * sind
     */
    public final int[] getRelevantFieldNosForComparing(String tableName) throws WrongNameException {
        tableName = getPureTabName(tableName);
        if (normalizeHM.get(tableName) != null) {
            final List<String> compareFieldNamesList = compareRelevant.get(tableName);
            if (compareFieldNamesList == null) {
                throw new WrongNameException("Activated normalization, but no field(s) for comparison declared:\n" + tableName);
            }
            final int[] arr = new int[compareFieldNamesList.size()];
            for (int i = 0; i < compareFieldNamesList.size(); ++i) {
                arr[i] = getPositionInTable(tableName, compareFieldNamesList.get(i));
            }
            return arr;
        } else {
            return new int[]{};
        }
    }

    /** 
     * Gibt an ob eine Tabelle normalisiert werden soll, oder nicht. Beim normalisieren
     * werden doppelte Datensaetze identifiziert und durch Verwendung der
     * entsprechenden vorhandenen Datensaetze wird Speicherplatz gespart.
     * <br>
     * (Erklaerung fuer Laien)
     * @param pureReferencingTabName Tabellenname
     * @return <CODE>true</CODE> wenn Tabelle normalisiert werden soll, <CODE>false</CODE> sonst
     */
    public final String getNormalizationType(String tableName) {
        tableName = getPureTabName(tableName);
        return normalizeHM.get(tableName);
    }

    /**
     * Optimizes normalization by checking if the according tables are ampty first,
     * switching to memory-only normalization otherwise.
     * 
     * @param tc - the target connection
     */
    public final void optimizeNormalization(final Connection tc) {
        try {
            final Statement stmnt = tc.createStatement();
            final List<String> items = TypeSafeCollections.newArrayList(normalizeHM.keySet().size());
            for (final String tab : normalizeHM.keySet()) {
                try {
                    final ResultSet rs = stmnt.executeQuery(COUNT_STMNT + tab);
                    if (rs.next()) {
                        final int count = rs.getInt(1);
                        if (count < 1) {
                            items.add(tab);
                        }
                    }
                } catch (SQLException ex) {
                    log.warn(ex);
                }
            }
            stmnt.close();
            for (final String change : items) {
                normalizeHM.put(change, NORMALIZE_MEM_ONLY);
            }
        } catch (SQLException ex) {
            log.warn(ex);
        }
        log.info("Normalization optimized to: " + normalizeHM);
    }

    /**
     * Returns all Table.Fields as FieldDescriptions, that reference the given Table
     *
     * @param detailTabName
     * @return
     */
    public final Iterable<FieldDescription> getAllMasterFields(final FieldDescription detailFD) {
        final Set<FieldDescription> result = TypeSafeCollections.newHashSet();
        for (final FieldDescription fd : referencesHashMap.keySet()) {
            if (fd.getTableName().equals(detailFD.getTableName()) || fd.getTableName().startsWith(detailFD.getTableName() + "[")) {
                result.addAll(referencesHashMap.get(fd));
            }
        }
        return result;
    }

    /** 
     * Liefert die Schluesselfelder in einer Detail-Tabelle.
     * <br>
     * :-( maybe buggy
     * <br>
     * z.B.:
     * <CODE>Url_ID</CODE> in der Tabelle <CODE>Url</CODE>
     * @return Schluesselfeld in einer Detail-Tabelle
     * @param detailTable Tabellenname
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     */
    public final List<String> getDetailKeyFields(final String detailTable) {
        return tableIsDetail.get(detailTable);
    }

    /** Liefert die Nummer des Schluesselfeldes in einer Detail-Tabelle.
     * <br>
     * :-( maybe buggy<br>
     * z.B.:
     * <CODE>Url_ID</CODE> in der Tabelle <CODE>Url</CODE>
     * @param detailTable Tabellenname
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     * @return Schluesselfeldnummer in einer Detail-Tabelle
     */
    public final int[] getDetailedKeyFieldNos(final String detailTable) throws WrongNameException {
        int[] ret = detailKeyFields.get(detailTable);
        if (ret == null) {
            final List<String> fields = getDetailKeyFields(detailTable);
            if (fields != null) {
                ret = new int[fields.size()];
                for (int i = 0; i < ret.length; ++i) {
                    ret[i] = getPositionInTable(detailTable, fields.get(i));
                }
                detailKeyFields.put(detailTable, ret);
            }
        }
        return ret;
    }

    /**
     * Entfernt das []-relationPath aus dem Tabellennamen
     */
    public final String getPureTabName(final String tabName) {
        return pureTableNameHash.get(tabName);
    }

    /**
     *Erzeugt aus dem Beziehungspfad das $FROM$-Format zur Verwendung in Variablennamen
     *
     **/
    private final String getBracketAppenderFromPath(String path) {
        if (path != null) {
            path = path.trim();
            if (path.length() > 0) {
                return "[" + path + "]";
            }
        }
        return EMPTY;
    }

    /**
     * 
     * @param pureReferencingTabName
     * @return
     */
    private final List<String> getTablesWithPathFromPureTableName(String tableName) {
        tableName = getPureTabName(tableName);
        return Collections.unmodifiableList(tableToAppropriateTablesWithPaths.get(tableName));
    }
}
