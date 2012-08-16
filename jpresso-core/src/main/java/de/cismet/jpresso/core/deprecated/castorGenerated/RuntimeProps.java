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
 * $Id: RuntimeProps.java,v 1.1 2003/12/01 11:38:00 Hell Exp $
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
 * Class RuntimeProps.
 *
 * @version  $Revision: 1.1 $ $Date: 2003/12/01 11:38:00 $
 */
public class RuntimeProps implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /** Field _keepCode. */
    private boolean _keepCode;

    /** keeps track of state for field: _keepCode */
    private boolean _has_keepCode;

    /** Field _tmpDir. */
    private java.lang.String _tmpDir;

    /** Field _finalizer. */
    private de.cismet.jpresso.core.deprecated.castorGenerated.Finalizer _finalizer;

    //~ Constructors -----------------------------------------------------------

    /**
     * ----------------/ - Constructors -/ ----------------/
     */
    public RuntimeProps() {
        super();
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.RuntimeProps()

    //~ Methods ----------------------------------------------------------------

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method deleteKeepCode.
     */
    public void deleteKeepCode() {
        this._has_keepCode = false;
    } // -- void deleteKeepCode()

    /**
     * Returns the value of field 'finalizer'.
     *
     * @return  the value of field 'finalizer'.
     */
    public de.cismet.jpresso.core.deprecated.castorGenerated.Finalizer getFinalizer() {
        return this._finalizer;
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Finalizer getFinalizer()

    /**
     * Returns the value of field 'keepCode'.
     *
     * @return  the value of field 'keepCode'.
     */
    public boolean getKeepCode() {
        return this._keepCode;
    } // -- boolean getKeepCode()

    /**
     * Returns the value of field 'tmpDir'.
     *
     * @return  the value of field 'tmpDir'.
     */
    public java.lang.String getTmpDir() {
        return this._tmpDir;
    } // -- java.lang.String getTmpDir()

    /**
     * Method hasKeepCode.
     *
     * @return  DOCUMENT ME!
     */
    public boolean hasKeepCode() {
        return this._has_keepCode;
    } // -- boolean hasKeepCode()

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
     * Sets the value of field 'finalizer'.
     *
     * @param  finalizer  the value of field 'finalizer'.
     */
    public void setFinalizer(final de.cismet.jpresso.core.deprecated.castorGenerated.Finalizer finalizer) {
        this._finalizer = finalizer;
    } // -- void setFinalizer(de.cismet.cids.admin.importAnt.castorGenerated.Finalizer)

    /**
     * Sets the value of field 'keepCode'.
     *
     * @param  keepCode  the value of field 'keepCode'.
     */
    public void setKeepCode(final boolean keepCode) {
        this._keepCode = keepCode;
        this._has_keepCode = true;
    } // -- void setKeepCode(boolean)

    /**
     * Sets the value of field 'tmpDir'.
     *
     * @param  tmpDir  the value of field 'tmpDir'.
     */
    public void setTmpDir(final java.lang.String tmpDir) {
        this._tmpDir = tmpDir;
    } // -- void setTmpDir(java.lang.String)

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
    public static de.cismet.jpresso.core.deprecated.castorGenerated.RuntimeProps unmarshal(final java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (de.cismet.jpresso.core.deprecated.castorGenerated.RuntimeProps)Unmarshaller.unmarshal(
                de.cismet.jpresso.core.deprecated.castorGenerated.RuntimeProps.class,
                reader);
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.RuntimeProps unmarshal(java.io.Reader)

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
