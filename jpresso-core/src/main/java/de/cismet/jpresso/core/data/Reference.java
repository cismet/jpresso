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
package de.cismet.jpresso.core.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
@XStreamAlias("Reference")
public final class Reference {

    //~ Static fields/initializers ---------------------------------------------

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    private static final String DOT = ".";

    /**
     * <editor-fold defaultstate="collapsed" desc="Setters & Getters">.
     *
     * @return  DOCUMENT ME!
     */
    public String getReferencingTable() {
        if (referencingTable == null) {
            referencingTable = "";
        }
        return referencingTable;
//        return referencingTable.toUpperCase();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  table  DOCUMENT ME!
     */
    public void setReferencingTable(final String table) {
        if (table != null) {
            this.referencingTable = table.trim();
        } else {
            this.referencingTable = "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getReferencingField() {
        if (referencingField == null) {
            referencingField = "";
        }
        return referencingField;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  field  DOCUMENT ME!
     */
    public void setReferencingField(final String field) {
        if (field != null) {
            this.referencingField = field.trim();
        } else {
            this.referencingField = "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isComparing() {
        return comparing;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  foreignKeyComparing  DOCUMENT ME!
     */
    public void setComparing(final boolean foreignKeyComparing) {
        this.comparing = foreignKeyComparing;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getEnclosingChar() {
        if (enclosingChar == null) {
            enclosingChar = "";
        }
        return enclosingChar;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  enclosingChar  DOCUMENT ME!
     */
    public void setEnclosingChar(final String enclosingChar) {
        if (enclosingChar != null) {
            this.enclosingChar = enclosingChar;
        } else {
            this.enclosingChar = "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getReferencedTable() {
        if (referencedTable == null) {
            referencedTable = "";
        }
        return referencedTable;
//        return referencedTable.toUpperCase();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  table  DOCUMENT ME!
     */
    public void setReferencedTable(final String table) {
        if (table != null) {
            this.referencedTable = table.trim();
        } else {
            this.referencedTable = "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getReferencedField() {
        if (referencedField == null) {
            referencedField = "";
        }
        return referencedField;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  field  DOCUMENT ME!
     */
    public void setReferencedField(final String field) {
        if (field != null) {
            this.referencedField = field.trim();
        } else {
            this.referencedField = "";
        }
    }
// </editor-fold>

    //~ Instance fields --------------------------------------------------------

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

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Reference object.
     */
    public Reference() {
        referencingTable = referencingField = referencedTable = referencedField = enclosingChar = "";
        comparing = false;
    }
    // </editor-fold>

    /**
     * Creates a new Reference object.
     *
     * @param  ref  DOCUMENT ME!
     */
    public Reference(final Reference ref) {
        this.referencingTable = ref.referencingField;
        this.referencingField = ref.referencingField;
        this.referencedTable = ref.referencedTable;
        this.referencedField = ref.referencedField;
        this.comparing = ref.comparing;
        this.enclosingChar = ref.enclosingChar;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Reference copy() {
        return new Reference(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getCompleteReferencing() {
        return referencingTable + DOT + referencingField;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getCompleteReferenced() {
        return referencedTable + DOT + referencedField;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Reference) {
            final Reference other = (Reference)obj;
            return other.getCompleteReferenced().equals(this.getCompleteReferenced())
                        && other.getCompleteReferencing().equals(this.getCompleteReferencing());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (29 * getCompleteReferenced().hashCode()) + (7 * getCompleteReferencing().hashCode());
    }

    @Override
    public String toString() {
        return getCompleteReferencing() + " -> " + getCompleteReferenced();
    }
}
