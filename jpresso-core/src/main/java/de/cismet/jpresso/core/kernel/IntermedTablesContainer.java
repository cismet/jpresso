/*
 * IntermedTablesContainer.java
 *
 * Created on 19. September 2003, 13:31
 */
package de.cismet.jpresso.core.kernel;

import de.cismet.jpresso.core.kernel.*;
import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;
import de.cismet.jpresso.core.exceptions.WrongNameException;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 
 * Diese Klasse stellt einen Container fuer alle IntermedTables dar und bietet
 * Methoden zum Fuellen an.
 * 
 * @author srichter
 */
public final class IntermedTablesContainer {

    /** Logger */
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    public final boolean debug = log.isDebugEnabled();
    /** Container fuer die IntermedTables */
    private final Map<String, IntermedTable> tables = TypeSafeCollections.newLinkedHashMap();
    private final List<IntermedTable> sortedIntermedTableList = TypeSafeCollections.newArrayList();
    /** Metainformationsobjekt */
    private final ImportMetaInfo metaInfo;
    private static final String KOMMA = ",";
    private static final String NULL = "NULL";
    private Connection targetConn;

    /** 
     * Creates a new instance of IntermedTablesContainer
     * 
     * @param metaInfo Metainformationsobjekt
     */
    public IntermedTablesContainer(final ImportMetaInfo metaInf, final Connection targetConn) throws WrongNameException {
        this.metaInfo = metaInf;
        this.targetConn = targetConn;
        for (final String tabName : metaInf.getTopologicalTableSequence()) {
            log.debug("-----------Anlegen von IntermedTables");
            log.debug("-tabName:" + tabName);
            log.debug("-getTargetFieldsAsStringArray:" + getStARR(metaInf.getTargetFieldsAsStringArray(tabName)));
            log.debug("-getTargetEnclosingCharsAsStringArray:" + getStARR(metaInf.getTargetEnclosingCharsAsStringArray(tabName)) + "[" + metaInf.getTargetEnclosingCharsAsStringArray(tabName).length + "]");
            log.debug("-targetConn:" + targetConn);
            log.debug("-----------Anlegen von IntermedTables fertig ------");
            final List<Integer> autoIncFieldsList = metaInf.getAutoIncFieldNos(tabName);
            final int[] autoIncFieldsArray;
            if (autoIncFieldsList != null) {
                autoIncFieldsArray = new int[autoIncFieldsList.size()];
                for (int i = 0; i < autoIncFieldsList.size(); ++i) {
                    autoIncFieldsArray[i] = autoIncFieldsList.get(i);
                }
            } else {
                autoIncFieldsArray = new int[0];
            }
            final IntermedTable tab = new IntermedTableMemoryImpl(tabName, metaInf.getTargetFieldsAsStringArray(tabName), metaInf.getTargetEnclosingCharsAsStringArray(tabName), metaInf.getRelevantFieldNosForComparing(tabName), targetConn, autoIncFieldsArray, false);
            tables.put(tabName, tab);
        }
        for (final String tabName : metaInf.getTopologicalTableSequenceWithPath()) {
            sortedIntermedTableList.add(getIntermedTable(tabName));
        }
        log.debug("IntermedTablesContainer:\n" + tables);
    }

    /**
     * 
     * @return the import meta information.
     */
    public ImportMetaInfo getMetaInfo() {
        return metaInfo;
    }

    /** 
     * Fuegt die uebergebenen Daten (EINE ZEILE) in die temporaeren Tabellen ein. <br>
     * Beachtet wird dabei: Normalisierung, automatische Inkrementierung,
     * Relationenmanagement.
     * 
     * @param rowData Daten die eingefuegt werden
     * @throws JPressoException wenn Fehler
     */
    public final void addCurrentRow(final String[][] rowData) throws JPressoException {
        //Ziel-Tabellennamen mit Pfaden in der richtigen Reihenfolge besorgen       
        for (int currentRowNumber = 0; currentRowNumber < rowData.length; ++currentRowNumber) {
            final String[] values = rowData[currentRowNumber];
            final IntermedTable iTab = sortedIntermedTableList.get(currentRowNumber);
            if (values != null) {
                final String[] encChars = iTab.getEnclosingCharacters();
                final TableMetaInfo tableInfo = metaInfo.getTableInfo().get(currentRowNumber);
                String[] normalizationResult = null;
                //Normalization Indikator
                boolean normalizedRow = false;
                //Escape SQL
                for (int i = 0; i < values.length; ++i) {
                    final String encChar = encChars[i];
                    if (encChar.length() == 1) {
                        values[i] = iTab.escapeCharacter(values[i], encChar.charAt(0));
                    }
                }
                //Schauen, ob normalisiert werden soll
                final String normalizationType = tableInfo.getNormalizationType();
                if (normalizationType != null) {
                    //Wenn ja, checken ob der Datensatz schon vorhanden ist
                    //Überprüfen, ob der angegebene Datensatz schon in der Zwischentabelle steht (doppelt drin?)
                    //Auch die Ziel-Tabelle wird betrachtet, ob ein solcher Wert bereits in ihr steht.
                    //Dazu zuerst schauen ob er in einer anderen, bereits ausgelesenen Row oder - letztlich - in der DB steht
                    normalizationResult = iTab.searchForRow(values, (normalizationType.equals(ImportMetaInfo.NORMALIZE_WITH_DB)));
                    if (normalizationResult != null) {
                        // Treffer
                        //rowData[currentRow] = null;
                        normalizedRow = true;
                        //=> kein autoInkrement mehr
                    }
                }
                //Ansonsten...
                if (!normalizedRow) {
                    //Die zu inkrementierenden Zaehler/ID-Felder geben lassen
                    final int[] autoInc = iTab.autoIncFields;
                    if (autoInc != null) {
                        //die naechsten Werte fuer die aktuellen autoInc Felder geben lassen
                        final String[] generatedValues = iTab.getNextAutogeneratedValues();
                        for (int j = 0; j < autoInc.length; ++j) {
                            values[autoInc[j]] = generatedValues[j];
                        }
                    }
                }
                //ReferenceManagement wird nur fuer referenzierte Tabellen gemacht
                final List<StoreOffset> references = tableInfo.getReferences();
                for (final StoreOffset storeOffset : references) {
                    String refVal = null;
                    if (normalizedRow) {
                        //wenn beim normalisieren etwas gefunden wurde, stehen hier die Werte, die nun uebernommen werden!
                        refVal = normalizationResult[storeOffset.detailFieldPosition];
                    } else {
                        //wenn die spalte nicht wegnormalisiert wurde...
                        //steht der wert ganz normal in den rowdatas
                        refVal = values[storeOffset.detailFieldPosition];//Wert des Schlüsselfeldes
                        }
                    final String[] masterRowToManipulate = rowData[storeOffset.masterTabIndex];
                    if (masterRowToManipulate != null) {
                        masterRowToManipulate[storeOffset.masterFieldPosition] = refVal;
                    }
                }
                if (!normalizedRow) {
                    //Hier werden die Ergebnisse in die IntermedTables geschrieben
                    iTab.addRow(values);
                }
            }
        }
    }

    /**
     * 
     * @param rowData
     */
    private final void filterOrphanedRows(final String[][] rowData) {
        //reversed order - going from masters to details....
        final Set<IntermedTable> counterNeedRefreshHere = TypeSafeCollections.newHashSet(rowData.length);
        for (int currentRow = rowData.length - 1; currentRow >= 0; --currentRow) {
            if (rowData[currentRow] != null) {
                final TableMetaInfo tmi = metaInfo.getTableInfo().get(currentRow);
                if (tmi.isDeleteOrphaned()) {
                    final List<StoreOffset> offsets = tmi.getReferences();
                    boolean toDelete = true;
                    for (final StoreOffset offset : offsets) {
                        if (rowData[offset.masterTabIndex] != null && offset.masterTabIndex != currentRow) {
                            //at least one referencing row from another table there, so this row is not orphaned
                            toDelete = false;
                            break;
                        }
                    }
                    if (toDelete) {
                        rowData[currentRow] = null;
                        counterNeedRefreshHere.add(sortedIntermedTableList.get(currentRow));
                    }
                }
            }
        }
        for (final IntermedTable tableToRefresh : counterNeedRefreshHere) {
        }
    }

    /** 
     * Methode zum Setzen eines Wertes in einer bestimmten InterMedTable
     * 
     * @param rowData Hastable Container
     * @param tabname Zieltabellenname
     * @param pos Spaltennummer
     * @param value Wert
     */
    private final void setValueInRowData(final Map<String, String[]> rowData, String tabname, int pos, String value) {
        final String[] sa = rowData.get(tabname);
//        if (sa != null && sa[pos] == null) {
        if (sa != null) {
            sa[pos] = value;
        }
    }

    /** 
     * Methode zur Ausgabe einer IntermedTable aus der LinkedHashMap
     * 
     * @param tabNa Tabellenname
     * @throws WrongNameException wird geworfen wenn der Tabellenname falsch ist.
     * @return IntermedTable
     */
    public IntermedTable getIntermedTable(String tabNa) throws WrongNameException {
        tabNa = metaInfo.getPureTabName(tabNa);
        final IntermedTable iTab = tables.get(tabNa);
        if (tabNa == null) {
            throw new WrongNameException("Malformed TableName !!!");
        } else {
            return iTab;
        }
    }

    /** 
     * Erzeugt eine Stringrepraesentation von einem IntermedTablesContainer-Objekt
     * 
     * @return Stringrepraesentation des IntermedTablesContainer-Objekts
     */
    @Override
    public String toString() {
        return tables.toString();
    }

    /**
     * Returns the number of target tables hold in the container.
     */
    public int getNumberOfTargetTables() {
        return tables.size();
    }

    /**
     * 
     * @return intermediate tables. returns actual internals, no defense copy!
     */
    public Collection<IntermedTable> getIntermedTables() {
        return tables.values();
    }

    /**
     * 
     * @return the connection to the import target
     */
    public Connection getTargetConn() {
        return targetConn;
    }

    /** 
     * DebugMethode
     * @param s StringArray
     */
    private final String getStARR(final String[] strs) {
        if (strs == null) {
            return NULL;
        } else {
            final StringBuilder sB = new StringBuilder();
            for (final String s : strs) {
                sB.append(s);
                sB.append(KOMMA);
            }
            return sB.toString();
        }
    }

    /**
     * Release no longer used indices + caches (+in the internal IntermedTables)
     */
    public void clearAllCaches() {
        for (final IntermedTable iTab : tables.values()) {
            iTab.clearAllCaches();
            if (log.isDebugEnabled()) {
                log.debug("Cleared Caches for IntermedTable " + iTab.getTableName());
            }
        }
    }

    /**
     * Dispose all memory resources. does not close the target connection!
     */
    public void close() {
        targetConn = null;
        for (final IntermedTable iTab : tables.values()) {
            iTab.close();
            if (log.isDebugEnabled()) {
                log.debug("Closed IntermedTable " + iTab.getTableName());
            }
        }
        tables.clear();
    }
}
