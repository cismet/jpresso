/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * This class was automatically generated with
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Relation.java,v 1.1 2003/11/04 10:00:00 Hell Exp $
 */
package de.cismet.jpresso.core.deprecated.castorGenerated;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import org.xml.sax.ContentHandler;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class Relation.
 *
 * @version  $Revision: 1.1 $ $Date: 2003/11/04 10:00:00 $
 */
public class Relation implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /** Field _masterTable. */
    private java.lang.String _masterTable;

    /** Field _masterTableForeignKey. */
    private java.lang.String _masterTableForeignKey;

    /** Field _foreignKeyComparing. */
    private boolean _foreignKeyComparing;

    /** keeps track of state for field: _foreignKeyComparing */
    private boolean _has_foreignKeyComparing;

    /** Field _enclosingChar. */
    private java.lang.String _enclosingChar;

    /** Field _detailTable. */
    private java.lang.String _detailTable;

    /** Field _detailTableKey. */
    private java.lang.String _detailTableKey;

    //~ Constructors -----------------------------------------------------------

    /**
     * ----------------/ - Constructors -/ ----------------/
     */
    public Relation() {
        super();
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Relation()

    //~ Methods ----------------------------------------------------------------

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method deleteForeignKeyComparing.
     */
    public void deleteForeignKeyComparing() {
        this._has_foreignKeyComparing = false;
    } // -- void deleteForeignKeyComparing()

    /**
     * Returns the value of field 'detailTable'.
     *
     * @return  the value of field 'detailTable'.
     */
    public java.lang.String getDetailTable() {
        return this._detailTable;
    } // -- java.lang.String getDetailTable()

    /**
     * Returns the value of field 'detailTableKey'.
     *
     * @return  the value of field 'detailTableKey'.
     */
    public java.lang.String getDetailTableKey() {
        return this._detailTableKey;
    } // -- java.lang.String getDetailTableKey()

    /**
     * Returns the value of field 'enclosingChar'.
     *
     * @return  the value of field 'enclosingChar'.
     */
    public java.lang.String getEnclosingChar() {
        return this._enclosingChar;
    } // -- java.lang.String getEnclosingChar()

    /**
     * Returns the value of field 'foreignKeyComparing'.
     *
     * @return  the value of field 'foreignKeyComparing'.
     */
    public boolean getForeignKeyComparing() {
        return this._foreignKeyComparing;
    } // -- boolean getForeignKeyComparing()

    /**
     * Returns the value of field 'masterTable'.
     *
     * @return  the value of field 'masterTable'.
     */
    public java.lang.String getMasterTable() {
        return this._masterTable;
    } // -- java.lang.String getMasterTable()

    /**
     * Returns the value of field 'masterTableForeignKey'.
     *
     * @return  the value of field 'masterTableForeignKey'.
     */
    public java.lang.String getMasterTableForeignKey() {
        return this._masterTableForeignKey;
    } // -- java.lang.String getMasterTableForeignKey()

    /**
     * Method hasForeignKeyComparing.
     *
     * @return  DOCUMENT ME!
     */
    public boolean hasForeignKeyComparing() {
        return this._has_foreignKeyComparing;
    } // -- boolean hasForeignKeyComparing()

    /**
     * Method isValid.
     *
     * @return  DOCUMENT ME!
     */
    public boolean isValid() {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } // -- boolean isValid()

    /**
     * Method marshal.
     *
     * @param   out  DOCUMENT ME!
     *
     * @throws  org.exolab.castor.xml.MarshalException     DOCUMENT ME!
     * @throws  org.exolab.castor.xml.ValidationException  DOCUMENT ME!
     */
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException,
        org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    } // -- void marshal(java.io.Writer)

    /**
     * Method marshal.
     *
     * @param   handler  DOCUMENT ME!
     *
     * @throws  java.io.IOException                        DOCUMENT ME!
     * @throws  org.exolab.castor.xml.MarshalException     DOCUMENT ME!
     * @throws  org.exolab.castor.xml.ValidationException  DOCUMENT ME!
     */
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException,
        org.exolab.castor.xml.MarshalException,
        org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    } // -- void marshal(org.xml.sax.ContentHandler)

    /**
     * Sets the value of field 'detailTable'.
     *
     * @param  detailTable  the value of field 'detailTable'.
     */
    public void setDetailTable(final java.lang.String detailTable) {
        this._detailTable = detailTable;
    } // -- void setDetailTable(java.lang.String)

    /**
     * Sets the value of field 'detailTableKey'.
     *
     * @param  detailTableKey  the value of field 'detailTableKey'.
     */
    public void setDetailTableKey(final java.lang.String detailTableKey) {
        this._detailTableKey = detailTableKey;
    } // -- void setDetailTableKey(java.lang.String)

    /**
     * Sets the value of field 'enclosingChar'.
     *
     * @param  enclosingChar  the value of field 'enclosingChar'.
     */
    public void setEnclosingChar(final java.lang.String enclosingChar) {
        this._enclosingChar = enclosingChar;
    } // -- void setEnclosingChar(java.lang.String)

    /**
     * Sets the value of field 'foreignKeyComparing'.
     *
     * @param  foreignKeyComparing  the value of field 'foreignKeyComparing'.
     */
    public void setForeignKeyComparing(final boolean foreignKeyComparing) {
        this._foreignKeyComparing = foreignKeyComparing;
        this._has_foreignKeyComparing = true;
    } // -- void setForeignKeyComparing(boolean)

    /**
     * Sets the value of field 'masterTable'.
     *
     * @param  masterTable  the value of field 'masterTable'.
     */
    public void setMasterTable(final java.lang.String masterTable) {
        this._masterTable = masterTable;
    } // -- void setMasterTable(java.lang.String)

    /**
     * Sets the value of field 'masterTableForeignKey'.
     *
     * @param  masterTableForeignKey  the value of field 'masterTableForeignKey'.
     */
    public void setMasterTableForeignKey(final java.lang.String masterTableForeignKey) {
        this._masterTableForeignKey = masterTableForeignKey;
    } // -- void setMasterTableForeignKey(java.lang.String)

    /**
     * Method unmarshal.
     *
     * @param   reader  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  org.exolab.castor.xml.MarshalException     DOCUMENT ME!
     * @throws  org.exolab.castor.xml.ValidationException  DOCUMENT ME!
     */
    public static de.cismet.jpresso.core.deprecated.castorGenerated.Relation unmarshal(final java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (de.cismet.jpresso.core.deprecated.castorGenerated.Relation)Unmarshaller.unmarshal(
                de.cismet.jpresso.core.deprecated.castorGenerated.Relation.class,
                reader);
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Relation unmarshal(java.io.Reader)

    /**
     * Method validate.
     *
     * @throws  org.exolab.castor.xml.ValidationException  DOCUMENT ME!
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        final org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } // -- void validate()
}
