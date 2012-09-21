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
 * $Id: Function.java,v 1.1 2003/11/04 09:59:00 Hell Exp $
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
 * Class Function.
 *
 * @version  $Revision: 1.1 $ $Date: 2003/11/04 09:59:00 $
 */
public class Function implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /** internal content storage. */
    private java.lang.String _content = "";

    /** Field _comment. */
    private java.lang.String _comment;

    //~ Constructors -----------------------------------------------------------

    /**
     * ----------------/ - Constructors -/ ----------------/
     */
    public Function() {
        super();
        setContent("");
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Function()

    //~ Methods ----------------------------------------------------------------

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Returns the value of field 'comment'.
     *
     * @return  the value of field 'comment'.
     */
    public java.lang.String getComment() {
        return this._comment;
    } // -- java.lang.String getComment()

    /**
     * Returns the value of field 'content'. The field 'content' has the following description: internal content storage
     *
     * @return  the value of field 'content'.
     */
    public java.lang.String getContent() {
        return this._content;
    } // -- java.lang.String getContent()

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
     * Sets the value of field 'comment'.
     *
     * @param  comment  the value of field 'comment'.
     */
    public void setComment(final java.lang.String comment) {
        this._comment = comment;
    } // -- void setComment(java.lang.String)

    /**
     * Sets the value of field 'content'. The field 'content' has the following description: internal content storage
     *
     * @param  content  the value of field 'content'.
     */
    public void setContent(final java.lang.String content) {
        this._content = content;
    } // -- void setContent(java.lang.String)

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
    public static de.cismet.jpresso.core.deprecated.castorGenerated.Function unmarshal(final java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (de.cismet.jpresso.core.deprecated.castorGenerated.Function)Unmarshaller.unmarshal(
                de.cismet.jpresso.core.deprecated.castorGenerated.Function.class,
                reader);
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Function unmarshal(java.io.Reader)

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
