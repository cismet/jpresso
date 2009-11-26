/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author srichter
 */
@XStreamAlias("Reference")
public final class Reference {
    // <editor-fold defaultstate="collapsed" desc="Constructors">

    public Reference(Reference ref) {
        this.referencingTable = ref.referencingField;
        this.referencingField = ref.referencingField;
        this.referencedTable = ref.referencedTable;
        this.referencedField = ref.referencedField;
        this.comparing = ref.comparing;
        this.enclosingChar = ref.enclosingChar;
    }

    public Reference() {
        referencingTable = referencingField = referencedTable = referencedField = enclosingChar = "";
        comparing = false;
    }
    // </editor-fold>
    @XStreamAsAttribute
    private String referencingTable;
    @XStreamAsAttribute
    private String referencingField;
    @XStreamAsAttribute
    private String referencedTable;
    @XStreamAsAttribute
    private String referencedField;
    @XStreamAsAttribute
    private boolean comparing;
    @XStreamAsAttribute
    private String enclosingChar;
    private static final String DOT = ".";

    // <editor-fold defaultstate="collapsed" desc="Setters & Getters">
    public String getReferencingTable() {
        if (referencingTable == null) {
            referencingTable = "";
        }
        return referencingTable;
//        return referencingTable.toUpperCase();
    }

    public void setReferencingTable(final String table) {
        if (table != null) {
            this.referencingTable = table.trim();
        } else {
            this.referencingTable = "";
        }
    }

    public String getReferencingField() {
        if (referencingField == null) {
            referencingField = "";
        }
        return referencingField;
    }

    public void setReferencingField(final String field) {
        if (field != null) {
            this.referencingField = field.trim();
        } else {
            this.referencingField = "";
        }
    }

    public boolean isComparing() {
        return comparing;
    }

    public void setComparing(final boolean foreignKeyComparing) {
        this.comparing = foreignKeyComparing;
    }

    public String getEnclosingChar() {
        if (enclosingChar == null) {
            enclosingChar = "";
        }
        return enclosingChar;
    }

    public void setEnclosingChar(final String enclosingChar) {
        if (enclosingChar != null) {
            this.enclosingChar = enclosingChar;
        } else {
            this.enclosingChar = "";
        }
    }

    public String getReferencedTable() {
        if (referencedTable == null) {
            referencedTable = "";
        }
        return referencedTable;
//        return referencedTable.toUpperCase();
    }

    public void setReferencedTable(final String table) {
        if (table != null) {
            this.referencedTable = table.trim();
        } else {
            this.referencedTable = "";
        }
    }

    public String getReferencedField() {
        if (referencedField == null) {
            referencedField = "";
        }
        return referencedField;
    }

    public void setReferencedField(final String field) {
        if (field != null) {
            this.referencedField = field.trim();
        } else {
            this.referencedField = "";
        }
    }
// </editor-fold>

    public Reference copy() {
        return new Reference(this);
    }

    public final String getCompleteReferencing() {
        return referencingTable + DOT + referencingField;
    }

    public final String getCompleteReferenced() {
        return referencedTable + DOT + referencedField;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Reference) {
            final Reference other = (Reference) obj;
            return other.getCompleteReferenced().equals(this.getCompleteReferenced()) && other.getCompleteReferencing().equals(this.getCompleteReferencing());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 29 * getCompleteReferenced().hashCode() + 7 * getCompleteReferencing().hashCode();
    }

    @Override
    public String toString() {
        return getCompleteReferencing() + " -> " + getCompleteReferenced();
    }
}
