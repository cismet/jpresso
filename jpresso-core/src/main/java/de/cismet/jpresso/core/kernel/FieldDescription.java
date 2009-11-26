/*
 * Field.java
 *
 * Created on 19. September 2003, 14:26
 */
package de.cismet.jpresso.core.kernel;

/** 
 * Bean zur Beschreibung eines Feldes einer Zeiltabelle
 * @author srichter
 * @author hell
 */
public final class FieldDescription {

    private static final String DOT = ".";
    /** Tabellenname */
    private final String tableName;
    /** Feldname */
    private final String fieldName;
    /** String Repraesentation**/
    private final String toString;

    /** Konstruktor der die Bean anlegt (Tabellenname,Feldname)
     * @param table Tabellenname
     * @param field Feldname
     */
    public FieldDescription(final String table, final String field) {
        tableName = table;
        fieldName = field;
        toString = tableName + DOT + fieldName;
    }

    /** Getter for property tableName.
     * @return Value of property tableName.
     *
     */
    public String getTableName() {
        return this.tableName;
    }

    /** Getter for property fieldName.
     * @return Value of property fieldName.
     *
     */
    public String getFieldName() {
        return this.fieldName;
    }

    /** 
     * Stringrepraesentation der Bean FieldDescription
     * @return Stringwert der Bean
     */
    @Override
    public final String toString() {
        return toString;
    }

    /** 
     * ueberladene Equals - Methode
     * @param o zu vergleichendes Objekt
     * @return true wenn gleich
     * false sonst
     */
    @Override
    public final boolean equals(final Object o) {
        if (o instanceof FieldDescription) {
            final FieldDescription tmp = (FieldDescription) o;
            return tableName.equals(tmp.getTableName()) && fieldName.equals(tmp.getFieldName());
        }
        return false;
    }

    /**
     * ueberladene HashCode Methode der Bean
     * Wird benoetigt, da die Bean Key in HashMaps sein kann
     * @return Hash-Wert der Bean
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
