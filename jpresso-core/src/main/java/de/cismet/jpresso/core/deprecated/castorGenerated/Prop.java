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
 * $Id: Prop.java,v 1.1 2003/11/04 10:00:00 Hell Exp $
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
 * Class Prop.
 *
 * @version  $Revision: 1.1 $ $Date: 2003/11/04 10:00:00 $
 */
public class Prop implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /** internal content storage. */
    private java.lang.String _content = "";

    /** Field _key. */
    private java.lang.String _key;

    //~ Constructors -----------------------------------------------------------

    /**
     * ----------------/ - Constructors -/ ----------------/
     */
    public Prop() {
        super();
        setContent("");
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Prop()

    //~ Methods ----------------------------------------------------------------

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Returns the value of field 'content'. The field 'content' has the following description: internal content storage
     *
     * @return  the value of field 'content'.
     */
    public java.lang.String getContent() {
        return this._content;
    } // -- java.lang.String getContent()

    /**
     * Returns the value of field 'key'.
     *
     * @return  the value of field 'key'.
     */
    public java.lang.String getKey() {
        return this._key;
    } // -- java.lang.String getKey()

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
     * Sets the value of field 'content'. The field 'content' has the following description: internal content storage
     *
     * @param  content  the value of field 'content'.
     */
    public void setContent(final java.lang.String content) {
        this._content = content;
    } // -- void setContent(java.lang.String)

    /**
     * Sets the value of field 'key'.
     *
     * @param  key  the value of field 'key'.
     */
    public void setKey(final java.lang.String key) {
        this._key = key;
    } // -- void setKey(java.lang.String)

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
    public static de.cismet.jpresso.core.deprecated.castorGenerated.Prop unmarshal(final java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (de.cismet.jpresso.core.deprecated.castorGenerated.Prop)Unmarshaller.unmarshal(
                de.cismet.jpresso.core.deprecated.castorGenerated.Prop.class,
                reader);
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Prop unmarshal(java.io.Reader)

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
