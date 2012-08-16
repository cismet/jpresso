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
 * $Id: PreProcessingAndMapping.java,v 1.1 2003/11/04 10:00:00 Hell Exp $
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
 * Class PreProcessingAndMapping.
 *
 * @version  $Revision: 1.1 $ $Date: 2003/11/04 10:00:00 $
 */
public class PreProcessingAndMapping implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /** Field _mappingList. */
    private java.util.Vector _mappingList;

    //~ Constructors -----------------------------------------------------------

    /**
     * ----------------/ - Constructors -/ ----------------/
     */
    public PreProcessingAndMapping() {
        super();
        _mappingList = new Vector();
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.PreProcessingAndMapping()

    //~ Methods ----------------------------------------------------------------

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method addMapping.
     *
     * @param   vMapping  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     */

    public void addMapping(final de.cismet.jpresso.core.deprecated.castorGenerated.Mapping vMapping)
            throws java.lang.IndexOutOfBoundsException {
        _mappingList.addElement(vMapping);
    } // -- void addMapping(de.cismet.cids.admin.importAnt.castorGenerated.Mapping)

    /**
     * Method addMapping.
     *
     * @param   index     DOCUMENT ME!
     * @param   vMapping  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     */

    public void addMapping(final int index, final de.cismet.jpresso.core.deprecated.castorGenerated.Mapping vMapping)
            throws java.lang.IndexOutOfBoundsException {
        _mappingList.insertElementAt(vMapping, index);
    } // -- void addMapping(int, de.cismet.cids.admin.importAnt.castorGenerated.Mapping)

    /**
     * Method enumerateMapping.
     *
     * @return  DOCUMENT ME!
     */
    public java.util.Enumeration enumerateMapping() {
        return _mappingList.elements();
    } // -- java.util.Enumeration enumerateMapping()

    /**
     * Method getMapping.
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     * @throws  IndexOutOfBoundsException            DOCUMENT ME!
     */
    public de.cismet.jpresso.core.deprecated.castorGenerated.Mapping getMapping(final int index)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _mappingList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (de.cismet.jpresso.core.deprecated.castorGenerated.Mapping)_mappingList.elementAt(index);
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Mapping getMapping(int)

    /**
     * Method getMapping.
     *
     * @return  DOCUMENT ME!
     */
    public de.cismet.jpresso.core.deprecated.castorGenerated.Mapping[] getMapping() {
        final int size = _mappingList.size();
        final de.cismet.jpresso.core.deprecated.castorGenerated.Mapping[] mArray =
            new de.cismet.jpresso.core.deprecated.castorGenerated.Mapping[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.cismet.jpresso.core.deprecated.castorGenerated.Mapping)_mappingList.elementAt(index);
        }
        return mArray;
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Mapping[] getMapping()

    /**
     * Method getMappingCount.
     *
     * @return  DOCUMENT ME!
     */
    public int getMappingCount() {
        return _mappingList.size();
    } // -- int getMappingCount()

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
     * Method removeAllMapping.
     */
    public void removeAllMapping() {
        _mappingList.removeAllElements();
    } // -- void removeAllMapping()

    /**
     * Method removeMapping.
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public de.cismet.jpresso.core.deprecated.castorGenerated.Mapping removeMapping(final int index) {
        final java.lang.Object obj = _mappingList.elementAt(index);
        _mappingList.removeElementAt(index);
        return (de.cismet.jpresso.core.deprecated.castorGenerated.Mapping)obj;
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Mapping removeMapping(int)

    /**
     * Method setMapping.
     *
     * @param   index     DOCUMENT ME!
     * @param   vMapping  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     * @throws  IndexOutOfBoundsException            DOCUMENT ME!
     */

    public void setMapping(final int index, final de.cismet.jpresso.core.deprecated.castorGenerated.Mapping vMapping)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _mappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _mappingList.setElementAt(vMapping, index);
    } // -- void setMapping(int, de.cismet.cids.admin.importAnt.castorGenerated.Mapping)

    /**
     * Method setMapping.
     *
     * @param  mappingArray  DOCUMENT ME!
     */

    public void setMapping(final de.cismet.jpresso.core.deprecated.castorGenerated.Mapping[] mappingArray) {
        // -- copy array
        _mappingList.removeAllElements();
        for (int i = 0; i < mappingArray.length; i++) {
            _mappingList.addElement(mappingArray[i]);
        }
    } // -- void setMapping(de.cismet.cids.admin.importAnt.castorGenerated.Mapping)

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
    public static de.cismet.jpresso.core.deprecated.castorGenerated.PreProcessingAndMapping unmarshal(
            final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException,
        org.exolab.castor.xml.ValidationException {
        return (de.cismet.jpresso.core.deprecated.castorGenerated.PreProcessingAndMapping)Unmarshaller.unmarshal(
                de.cismet.jpresso.core.deprecated.castorGenerated.PreProcessingAndMapping.class,
                reader);
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.PreProcessingAndMapping unmarshal(java.io.Reader)

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
