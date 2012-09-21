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
 * $Id: Relations.java,v 1.1 2004/06/25 11:14:00 hell Exp $
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
 * Class Relations.
 *
 * @version  $Revision: 1.1 $ $Date: 2004/06/25 11:14:00 $
 */
public class Relations implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /** Field _relationList. */
    private java.util.Vector _relationList;

    //~ Constructors -----------------------------------------------------------

    /**
     * ----------------/ - Constructors -/ ----------------/
     */
    public Relations() {
        super();
        _relationList = new Vector();
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Relations()

    //~ Methods ----------------------------------------------------------------

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method addRelation.
     *
     * @param   vRelation  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     */

    public void addRelation(final de.cismet.jpresso.core.deprecated.castorGenerated.Relation vRelation)
            throws java.lang.IndexOutOfBoundsException {
        _relationList.addElement(vRelation);
    } // -- void addRelation(de.cismet.cids.admin.importAnt.castorGenerated.Relation)

    /**
     * Method addRelation.
     *
     * @param   index      DOCUMENT ME!
     * @param   vRelation  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     */

    public void addRelation(final int index, final de.cismet.jpresso.core.deprecated.castorGenerated.Relation vRelation)
            throws java.lang.IndexOutOfBoundsException {
        _relationList.insertElementAt(vRelation, index);
    } // -- void addRelation(int, de.cismet.cids.admin.importAnt.castorGenerated.Relation)

    /**
     * Method enumerateRelation.
     *
     * @return  DOCUMENT ME!
     */
    public java.util.Enumeration enumerateRelation() {
        return _relationList.elements();
    } // -- java.util.Enumeration enumerateRelation()

    /**
     * Method getRelation.
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     * @throws  IndexOutOfBoundsException            DOCUMENT ME!
     */
    public de.cismet.jpresso.core.deprecated.castorGenerated.Relation getRelation(final int index)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _relationList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (de.cismet.jpresso.core.deprecated.castorGenerated.Relation)_relationList.elementAt(index);
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Relation getRelation(int)

    /**
     * Method getRelation.
     *
     * @return  DOCUMENT ME!
     */
    public de.cismet.jpresso.core.deprecated.castorGenerated.Relation[] getRelation() {
        final int size = _relationList.size();
        final de.cismet.jpresso.core.deprecated.castorGenerated.Relation[] mArray =
            new de.cismet.jpresso.core.deprecated.castorGenerated.Relation[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.cismet.jpresso.core.deprecated.castorGenerated.Relation)_relationList.elementAt(index);
        }
        return mArray;
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Relation[] getRelation()

    /**
     * Method getRelationCount.
     *
     * @return  DOCUMENT ME!
     */
    public int getRelationCount() {
        return _relationList.size();
    } // -- int getRelationCount()

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
     * Method removeAllRelation.
     */
    public void removeAllRelation() {
        _relationList.removeAllElements();
    } // -- void removeAllRelation()

    /**
     * Method removeRelation.
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public de.cismet.jpresso.core.deprecated.castorGenerated.Relation removeRelation(final int index) {
        final java.lang.Object obj = _relationList.elementAt(index);
        _relationList.removeElementAt(index);
        return (de.cismet.jpresso.core.deprecated.castorGenerated.Relation)obj;
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Relation removeRelation(int)

    /**
     * Method setRelation.
     *
     * @param   index      DOCUMENT ME!
     * @param   vRelation  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     * @throws  IndexOutOfBoundsException            DOCUMENT ME!
     */

    public void setRelation(final int index, final de.cismet.jpresso.core.deprecated.castorGenerated.Relation vRelation)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _relationList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _relationList.setElementAt(vRelation, index);
    } // -- void setRelation(int, de.cismet.cids.admin.importAnt.castorGenerated.Relation)

    /**
     * Method setRelation.
     *
     * @param  relationArray  DOCUMENT ME!
     */

    public void setRelation(final de.cismet.jpresso.core.deprecated.castorGenerated.Relation[] relationArray) {
        // -- copy array
        _relationList.removeAllElements();
        for (int i = 0; i < relationArray.length; i++) {
            _relationList.addElement(relationArray[i]);
        }
    } // -- void setRelation(de.cismet.cids.admin.importAnt.castorGenerated.Relation)

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
    public static de.cismet.jpresso.core.deprecated.castorGenerated.Relations unmarshal(final java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (de.cismet.jpresso.core.deprecated.castorGenerated.Relations)Unmarshaller.unmarshal(
                de.cismet.jpresso.core.deprecated.castorGenerated.Relations.class,
                reader);
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Relations unmarshal(java.io.Reader)

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
