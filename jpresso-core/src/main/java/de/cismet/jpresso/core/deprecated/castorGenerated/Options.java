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
 * $Id: Options.java,v 1.1 2003/11/04 10:00:00 Hell Exp $
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

import java.util.Enumeration;
import java.util.Vector;

/**
 * Class Options.
 *
 * @version  $Revision: 1.1 $ $Date: 2003/11/04 10:00:00 $
 */
public class Options implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /** Field _normalizeList. */
    private java.util.Vector _normalizeList;

    //~ Constructors -----------------------------------------------------------

    /**
     * ----------------/ - Constructors -/ ----------------/
     */
    public Options() {
        super();
        _normalizeList = new Vector();
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Options()

    //~ Methods ----------------------------------------------------------------

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method addNormalize.
     *
     * @param   vNormalize  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     */

    public void addNormalize(final java.lang.String vNormalize) throws java.lang.IndexOutOfBoundsException {
        _normalizeList.addElement(vNormalize);
    } // -- void addNormalize(java.lang.String)

    /**
     * Method addNormalize.
     *
     * @param   index       DOCUMENT ME!
     * @param   vNormalize  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     */

    public void addNormalize(final int index, final java.lang.String vNormalize)
            throws java.lang.IndexOutOfBoundsException {
        _normalizeList.insertElementAt(vNormalize, index);
    } // -- void addNormalize(int, java.lang.String)

    /**
     * Method enumerateNormalize.
     *
     * @return  DOCUMENT ME!
     */
    public java.util.Enumeration enumerateNormalize() {
        return _normalizeList.elements();
    } // -- java.util.Enumeration enumerateNormalize()

    /**
     * Method getNormalize.
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     * @throws  IndexOutOfBoundsException            DOCUMENT ME!
     */
    public java.lang.String getNormalize(final int index) throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _normalizeList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (String)_normalizeList.elementAt(index);
    } // -- java.lang.String getNormalize(int)

    /**
     * Method getNormalize.
     *
     * @return  DOCUMENT ME!
     */
    public java.lang.String[] getNormalize() {
        final int size = _normalizeList.size();
        final java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_normalizeList.elementAt(index);
        }
        return mArray;
    } // -- java.lang.String[] getNormalize()

    /**
     * Method getNormalizeCount.
     *
     * @return  DOCUMENT ME!
     */
    public int getNormalizeCount() {
        return _normalizeList.size();
    } // -- int getNormalizeCount()

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
     * Method removeAllNormalize.
     */
    public void removeAllNormalize() {
        _normalizeList.removeAllElements();
    } // -- void removeAllNormalize()

    /**
     * Method removeNormalize.
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public java.lang.String removeNormalize(final int index) {
        final java.lang.Object obj = _normalizeList.elementAt(index);
        _normalizeList.removeElementAt(index);
        return (String)obj;
    } // -- java.lang.String removeNormalize(int)

    /**
     * Method setNormalize.
     *
     * @param   index       DOCUMENT ME!
     * @param   vNormalize  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     * @throws  IndexOutOfBoundsException            DOCUMENT ME!
     */

    public void setNormalize(final int index, final java.lang.String vNormalize)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _normalizeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _normalizeList.setElementAt(vNormalize, index);
    } // -- void setNormalize(int, java.lang.String)

    /**
     * Method setNormalize.
     *
     * @param  normalizeArray  DOCUMENT ME!
     */

    public void setNormalize(final java.lang.String[] normalizeArray) {
        // -- copy array
        _normalizeList.removeAllElements();
        for (int i = 0; i < normalizeArray.length; i++) {
            _normalizeList.addElement(normalizeArray[i]);
        }
    } // -- void setNormalize(java.lang.String)

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
    public static de.cismet.jpresso.core.deprecated.castorGenerated.Options unmarshal(final java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (de.cismet.jpresso.core.deprecated.castorGenerated.Options)Unmarshaller.unmarshal(
                de.cismet.jpresso.core.deprecated.castorGenerated.Options.class,
                reader);
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Options unmarshal(java.io.Reader)

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
