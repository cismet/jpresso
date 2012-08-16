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
 * $Id: Finalizer.java,v 1.1 2004/06/25 11:14:00 hell Exp $
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
 * Class Finalizer.
 *
 * @version  $Revision: 1.1 $ $Date: 2004/06/25 11:14:00 $
 */
public class Finalizer implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /** Field _className. */
    private java.lang.String _className;

    /** Field _propList. */
    private java.util.Vector _propList;

    //~ Constructors -----------------------------------------------------------

    /**
     * ----------------/ - Constructors -/ ----------------/
     */
    public Finalizer() {
        super();
        _propList = new Vector();
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Finalizer()

    //~ Methods ----------------------------------------------------------------

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method addProp.
     *
     * @param   vProp  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     */

    public void addProp(final de.cismet.jpresso.core.deprecated.castorGenerated.Prop vProp)
            throws java.lang.IndexOutOfBoundsException {
        _propList.addElement(vProp);
    } // -- void addProp(de.cismet.cids.admin.importAnt.castorGenerated.Prop)

    /**
     * Method addProp.
     *
     * @param   index  DOCUMENT ME!
     * @param   vProp  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     */

    public void addProp(final int index, final de.cismet.jpresso.core.deprecated.castorGenerated.Prop vProp)
            throws java.lang.IndexOutOfBoundsException {
        _propList.insertElementAt(vProp, index);
    } // -- void addProp(int, de.cismet.cids.admin.importAnt.castorGenerated.Prop)

    /**
     * Method enumerateProp.
     *
     * @return  DOCUMENT ME!
     */
    public java.util.Enumeration enumerateProp() {
        return _propList.elements();
    } // -- java.util.Enumeration enumerateProp()

    /**
     * Returns the value of field 'className'.
     *
     * @return  the value of field 'className'.
     */
    public java.lang.String getClassName() {
        return this._className;
    } // -- java.lang.String getClassName()

    /**
     * Method getProp.
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     * @throws  IndexOutOfBoundsException            DOCUMENT ME!
     */
    public de.cismet.jpresso.core.deprecated.castorGenerated.Prop getProp(final int index)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _propList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (de.cismet.jpresso.core.deprecated.castorGenerated.Prop)_propList.elementAt(index);
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Prop getProp(int)

    /**
     * Method getProp.
     *
     * @return  DOCUMENT ME!
     */
    public de.cismet.jpresso.core.deprecated.castorGenerated.Prop[] getProp() {
        final int size = _propList.size();
        final de.cismet.jpresso.core.deprecated.castorGenerated.Prop[] mArray =
            new de.cismet.jpresso.core.deprecated.castorGenerated.Prop[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.cismet.jpresso.core.deprecated.castorGenerated.Prop)_propList.elementAt(index);
        }
        return mArray;
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Prop[] getProp()

    /**
     * Method getPropCount.
     *
     * @return  DOCUMENT ME!
     */
    public int getPropCount() {
        return _propList.size();
    } // -- int getPropCount()

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
     * Method removeAllProp.
     */
    public void removeAllProp() {
        _propList.removeAllElements();
    } // -- void removeAllProp()

    /**
     * Method removeProp.
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public de.cismet.jpresso.core.deprecated.castorGenerated.Prop removeProp(final int index) {
        final java.lang.Object obj = _propList.elementAt(index);
        _propList.removeElementAt(index);
        return (de.cismet.jpresso.core.deprecated.castorGenerated.Prop)obj;
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Prop removeProp(int)

    /**
     * Sets the value of field 'className'.
     *
     * @param  className  the value of field 'className'.
     */
    public void setClassName(final java.lang.String className) {
        this._className = className;
    } // -- void setClassName(java.lang.String)

    /**
     * Method setProp.
     *
     * @param   index  DOCUMENT ME!
     * @param   vProp  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     * @throws  IndexOutOfBoundsException            DOCUMENT ME!
     */

    public void setProp(final int index, final de.cismet.jpresso.core.deprecated.castorGenerated.Prop vProp)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _propList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _propList.setElementAt(vProp, index);
    } // -- void setProp(int, de.cismet.cids.admin.importAnt.castorGenerated.Prop)

    /**
     * Method setProp.
     *
     * @param  propArray  DOCUMENT ME!
     */

    public void setProp(final de.cismet.jpresso.core.deprecated.castorGenerated.Prop[] propArray) {
        // -- copy array
        _propList.removeAllElements();
        for (int i = 0; i < propArray.length; i++) {
            _propList.addElement(propArray[i]);
        }
    } // -- void setProp(de.cismet.cids.admin.importAnt.castorGenerated.Prop)

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
    public static de.cismet.jpresso.core.deprecated.castorGenerated.Finalizer unmarshal(final java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (de.cismet.jpresso.core.deprecated.castorGenerated.Finalizer)Unmarshaller.unmarshal(
                de.cismet.jpresso.core.deprecated.castorGenerated.Finalizer.class,
                reader);
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Finalizer unmarshal(java.io.Reader)

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
