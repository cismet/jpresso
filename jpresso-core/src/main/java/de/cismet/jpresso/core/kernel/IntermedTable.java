/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.kernel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.List;
import java.util.Map;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import de.cismet.jpresso.core.exceptions.NoValuesException;
import de.cismet.jpresso.core.exceptions.WrongNameException;
import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * A class that defines all the functions a temporary buffer store for an imported table must have.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public abstract class IntermedTable implements TableModel {

    //~ Static fields/initializers ---------------------------------------------

    public static final String SELECT = "SELECT ";
    public static final String FROM = " FROM ";
    public static final String WHERE = " WHERE ";
    public static final String AND = " AND ";
    public static final String IS_NULL = " IS NULL";
    public static final String EQUALS = "=";
    public static final String WRONG_COLUMN_NAME = "Wrong columnName ";
    public static final String EMPTY_STRING = "";
    public static final String ZERO = "0";
    public static final String BACKSLASH_N = "\n";
    public static final String KOMMA = ",";
    static final String STAT_PART_1 = "Select max(";
    static final String STAT_PART_2 = "), count(*) from ";

    //~ Instance fields --------------------------------------------------------

    /** Logger. */
    final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    /** Spaltenname. */
    final String[] columnNames;
    /** Enclosing Characters. */
    final String[] enclChars;
    /** Map in der alle Spaltennamen einer Spaltennummer zugeordnet sind. */
    final Map<String, Integer> columnNameToNumberMapping;
    /** Tabellenname der Zieltabelle. */
    final String tableName;
    /** mit dieser Variable wird das Problem der Mehrfachzugriffe bei MultiReferences und AutoInkrement geloest.* */
    final int[] counterValues;
    /** der erste autoinc wert jeder spalte.* */
    final int[] offsets;
    /** Fields for nomralization comparison.* */
    final int[] compareFields;
    /** Connection zum Zielsystem. */
    Connection targetConn;
    /** .*DOCUMENT ME! */
    final boolean deleteOrphaned;
    /** .*DOCUMENT ME! */
    final int[] autoIncFields;
    /** debug flag.* */
    final boolean debug = log.isDebugEnabled();
    final EventListenerList listenerList = new EventListenerList();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new IntermedTable object.
     *
     * @param  columnNames     DOCUMENT ME!
     * @param  enclChars       DOCUMENT ME!
     * @param  compareFields   DOCUMENT ME!
     * @param  tableName       DOCUMENT ME!
     * @param  targetConn      DOCUMENT ME!
     * @param  autoIncFields   DOCUMENT ME!
     * @param  deleteOrphaned  DOCUMENT ME!
     */
    public IntermedTable(final String[] columnNames,
            final String[] enclChars,
            final int[] compareFields,
            final String tableName,
            final Connection targetConn,
            final int[] autoIncFields,
            final boolean deleteOrphaned) {
        this.tableName = tableName;
        this.columnNames = columnNames.clone();
        this.autoIncFields = autoIncFields.clone();
        this.enclChars = enclChars.clone();
        this.columnNameToNumberMapping = TypeSafeCollections.newHashMap();
        this.counterValues = new int[columnNames.length];
        this.offsets = new int[columnNames.length];
        this.targetConn = targetConn;
        this.deleteOrphaned = deleteOrphaned;
        for (int i = 0; i < columnNames.length; ++i) {
            counterValues[i] = 0;
            offsets[i] = -1;
        }
        if (compareFields != null) {
            this.compareFields = compareFields.clone();
        } else {
            this.compareFields = new int[] {};
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Liefert den Namen der Tabelle.
     *
     * @return  DOCUMENT ME!
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isDeleteOrphaned() {
        return deleteOrphaned;
    }

    /**
     * DOCUMENT ME!
     */
    public abstract void adjustCountersToActualValues();

    /**
     * Fuegt der IntermedTable eine neue Zeile an.
     *
     * @param  rowData  String[] der neuen Zeile
     */
    public abstract void addRow(final String[] rowData);

    /**
     * Liefert den neuesten Wert in der angegebenen Spalte.
     *
     * @return  Wert der Zelle als String
     */
    public abstract String[] getNextAutogeneratedValues();

    /**
     * Ueberprueft ob der uebergebene Datensatz schon in der Datenstruktur vorkommt. Entscheidende Methode zur
     * Realisierung der Normalisierungsfunktionalitaet. Diese Methode untersucht nur die Hauptspeicherstruktur. Es
     * werden nur Spalten zum Vergleich herangezogen die in fields[] angegeben sind.
     *
     * @param   data            <B>komplette</B> Zeile die ueberprueft werden soll
     * @param   alsoInDatabase  ist dieses Flag auf true gesetzt, wird nicht nur der momentane Import- datenbestand fuer
     *                          einen Vergleich ueberprueft, sondern auch die echte Zieltabelle (Datenbank) ueberprueft.
     *
     * @return  Falls eine uebereinstimmung gefunden wurde wird die entsprechende Zeile zurueckgegeben, um z.B. die
     *          entsprechende ID zu erhalten.
     *
     *          <p>Genutzt vom IntermedTablesContainer.</p>
     */
    public abstract String[] searchForRow(final String[] data, final boolean alsoInDatabase);

    /**
     * DOCUMENT ME!
     */
    public abstract void clearAllCaches();

    /**
     * DOCUMENT ME!
     *
     * @param   rowNo  DOCUMENT ME!
     * @param   sep    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  NoValuesException  de.cismet.jpressocore.exceptions.NoValuesException
     */
    public String getRowStringWithGivenEnclosingChar(final int rowNo, final String sep) throws NoValuesException {
        final StringBuilder buf = new StringBuilder();
        String eChar;
        String value;
        for (int i = 0; i < (columnNames.length - 1); ++i) {
            eChar = enclChars[i];
            value = getValueAt(rowNo, i);
            if ((eChar == null) || (value == null)) {
                eChar = EMPTY_STRING;
            }
            buf.append(eChar);
            buf.append(value);
            buf.append(eChar);
            buf.append(sep);
        }
        eChar = enclChars[columnNames.length - 1];
        value = getValueAt(rowNo, columnNames.length - 1);
        if ((eChar == null) || (value == null)) {
            eChar = EMPTY_STRING;
        }
        buf.append(eChar);
        buf.append(value);
        buf.append(eChar);
        return buf.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected final int[] getCompareFields() {
        return compareFields;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   rowNo  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  NoValuesException  de.cismet.jpressocore.exceptions.NoValuesException
     */
    public List<String> getValueListWithGivenEnclosingChar(final int rowNo) throws NoValuesException {
        final List<String> ret = TypeSafeCollections.newArrayList();
        String eChar;
        String value;
        StringBuilder buf;
        for (int i = 0; i < columnNames.length; ++i) {
            buf = new StringBuilder();
            eChar = enclChars[i];
            value = getValueAt(rowNo, i);
            if ((eChar == null) || (value == null)) {
                eChar = EMPTY_STRING;
            }
            buf.append(eChar);
            buf.append(value);
            buf.append(eChar);
            ret.add(buf.toString());
        }
        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   columnName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  WrongNameException  de.cismet.jpressocore.exceptions.WrongNameException
     */
    public final int getColumnNumber(final String columnName) throws WrongNameException {
        final Integer ret = columnNameToNumberMapping.get(columnName);
        if (ret == null) {
            throw new WrongNameException(columnName);
        }
        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   columnName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  WrongNameException  de.cismet.jpressocore.exceptions.WrongNameException
     */
    public final int getColumnNumberIgnoreCase(final String columnName) throws WrongNameException {
        Integer ret = columnNameToNumberMapping.get(columnName);
        if (ret == null) {
            for (final String s : columnNameToNumberMapping.keySet()) {
                if (s.equalsIgnoreCase(columnName)) {
                    ret = columnNameToNumberMapping.get(s);
                    return ret;
                }
            }
        } else {
            return ret;
        }
        return -1;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public abstract int getRowCount();

    @Override
    public String getColumnName(final int columnNo) {
        return columnNames[columnNo];
    }

    @Override
    public boolean isCellEditable(final int row, final int column) {
        return true;
    }

    @Override
    public abstract String getValueAt(final int row, final int column);

    /**
     * DOCUMENT ME!
     */
    public final void close() {
        this.targetConn = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public final int getAutoIncOffsetForColumn(final int column) {
        if (column < offsets.length) {
            return offsets[column];
        }
        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  column  DOCUMENT ME!
     * @param  value   DOCUMENT ME!
     */
    public final void setAutoIncOffsetForColumn(final int column, final int value) {
        if (column < offsets.length) {
            offsets[column] = value;
        }
    }

    /**
     * Liefert den neuesten Wert in der angegebenen Spalte aus der Ziel Datenbank. Wird benutzt, wenn
     * getNextAutogeneratedValue nichts in der Datenstruktur findet um herauszufinden, wie der letzte Wert ist.
     *
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    final String getNextValueFromDB(final int column) {
        try {
            if (debug) {
                if (log.isDebugEnabled()) {
                    log.debug("getNextValue: search in database");
                }
            }
            final Statement stmnt = targetConn.createStatement();
            final StringBuilder sb = new StringBuilder(STAT_PART_1);
            sb.append(this.columnNames[column]);
            sb.append(STAT_PART_2);
            sb.append(tableName);
            final ResultSet rs = stmnt.executeQuery(sb.toString());
            rs.next();
            final String res = eval(rs.getString(1), rs.getInt(2), column);
            final int latestDbValue = Integer.parseInt(res);
            final int nextDbValue = latestDbValue + 1;
            if (offsets[column] == -1) {
                offsets[column] = nextDbValue;
            }
            counterValues[column] = nextDbValue;
            return String.valueOf(nextDbValue);
        } catch (Exception ex) {
//            ++counterValues[column];
            log.warn("AutoValue", ex);
            if (offsets[column] == -1) {
                offsets[column] = 1;
            }
            counterValues[column] = 1;
            return "1";
        }
    }

    /**
     * Utility function.
     *
     * @param   maxVal    ret
     * @param   countVal  DOCUMENT ME!
     * @param   column    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    final String eval(final String maxVal, final int countVal, final int column) {
        if ((countVal == 0) && (counterValues[column] == 0)) {
            ++counterValues[column];
            return ZERO;
        } else if ((countVal == 0) && (counterValues[column] > 0)) {
            final String result = String.valueOf(counterValues[column]);
            ++counterValues[column];
            return result;
        }
        // ----
        if ((maxVal == null) && (counterValues[column] == 0)) {
            ++counterValues[column];
            return ZERO;
        }
        final String result = stringInc(maxVal, counterValues[column]);
        ++counterValues[column];
        return result;
    }

    /**
     * Utility function to increase a string-represented Integer.
     *
     * @param   s    DOCUMENT ME!
     * @param   inc  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    final String stringInc(final String s, final int inc) {
        if (inc == 0) {
            return s;
        }
        try {
            return String.valueOf(Integer.parseInt(s) + inc);
        } catch (Exception e) {
            final String msg = "Error at String-Incrementing: " + e;
            log.error(msg);
            if (log.isDebugEnabled()) {
                log.debug(msg);
            }
            return ZERO;
        }
    }

    @Override
    public final String toString() {
        return getTableName();
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        return String.class;
    }

    @Override
    public void addTableModelListener(final TableModelListener l) {
        listenerList.add(TableModelListener.class, l);
    }

    @Override
    public void removeTableModelListener(final TableModelListener l) {
        listenerList.remove(TableModelListener.class, l);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    public void fireTableChanged(final TableModelEvent e) {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TableModelListener.class) {
                ((TableModelListener)listeners[i + 1]).tableChanged(e);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public final String[] getEnclosingCharacters() {
        return enclChars;
    }

    /**
     * Escapes all occurences of the character in the String by duplication.
     *
     * @param   input     DOCUMENT ME!
     * @param   toEscape  DOCUMENT ME!
     *
     * @return  escapes the given character in the string by duplication
     */
    public final String escapeCharacter(final String input, final char toEscape) {
        if ((input != null) && (input.length() > 0)) {
            // All SQL-Escaping is done here - using no library methods for performance
            final char[] chars = input.toCharArray();
            final char[] res = new char[chars.length * 2];
            int ptr = -1;
            char c;
            for (int j = 0; j < chars.length; ++j) {
                c = chars[j];
                if (c == toEscape) {
                    res[++ptr] = c;
                }
                res[++ptr] = c;
            }
            return new String(res, 0, ++ptr);
        }
        return input;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the autoIncFields
     */
    public int[] getAutoIncFields() {
        return autoIncFields;
    }
}
