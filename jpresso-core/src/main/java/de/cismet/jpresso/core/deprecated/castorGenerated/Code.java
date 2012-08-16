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
 * $Id: Code.java,v 1.1 2003/11/04 09:59:00 Hell Exp $
 */
package de.cismet.jpresso.core.deprecated.castorGenerated;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import java.util.Vector;

/**
 * Class Code.
 *
 * @version  $Revision: 1.1 $ $Date: 2003/11/04 09:59:00 $
 */
public class Code implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /** Field _import. */
    private java.lang.String _import;

    /** Field _functionList. */
    private java.util.Vector _functionList;

    //~ Constructors -----------------------------------------------------------

    /**
     * ----------------/ - Constructors -/ ----------------/
     */
    public Code() {
        super();
        _functionList = new Vector();
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Code()

    //~ Methods ----------------------------------------------------------------

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method addFunction.
     *
     * @param   vFunction  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     */

    public void addFunction(final de.cismet.jpresso.core.deprecated.castorGenerated.Function vFunction)
            throws java.lang.IndexOutOfBoundsException {
        _functionList.addElement(vFunction);
    } // -- void addFunction(de.cismet.cids.admin.importAnt.castorGenerated.Function)

    /**
     * Method addFunction.
     *
     * @param   index      DOCUMENT ME!
     * @param   vFunction  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     */

    public void addFunction(final int index, final de.cismet.jpresso.core.deprecated.castorGenerated.Function vFunction)
            throws java.lang.IndexOutOfBoundsException {
        _functionList.insertElementAt(vFunction, index);
    } // -- void addFunction(int, de.cismet.cids.admin.importAnt.castorGenerated.Function)

    /**
     * Method enumerateFunction.
     *
     * @return  DOCUMENT ME!
     */
    public java.util.Enumeration enumerateFunction() {
        return _functionList.elements();
    } // -- java.util.Enumeration enumerateFunction()

    /**
     * Method getFunction.
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     * @throws  IndexOutOfBoundsException            DOCUMENT ME!
     */
    public de.cismet.jpresso.core.deprecated.castorGenerated.Function getFunction(final int index)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _functionList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (de.cismet.jpresso.core.deprecated.castorGenerated.Function)_functionList.elementAt(index);
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Function getFunction(int)

    /**
     * Method getFunction.
     *
     * @return  DOCUMENT ME!
     */
    public de.cismet.jpresso.core.deprecated.castorGenerated.Function[] getFunction() {
        final int size = _functionList.size();
        final de.cismet.jpresso.core.deprecated.castorGenerated.Function[] mArray =
            new de.cismet.jpresso.core.deprecated.castorGenerated.Function[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.cismet.jpresso.core.deprecated.castorGenerated.Function)_functionList.elementAt(index);
        }
        return mArray;
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Function[] getFunction()

    /**
     * Method getFunctionCount.
     *
     * @return  DOCUMENT ME!
     */
    public int getFunctionCount() {
        return _functionList.size();
    } // -- int getFunctionCount()

    /**
     * Returns the value of field 'import'.
     *
     * @return  the value of field 'import'.
     */
    public java.lang.String getImport() {
        return this._import;
    } // -- java.lang.String getImport()

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
     * Method removeAllFunction.
     */
    public void removeAllFunction() {
        _functionList.removeAllElements();
    } // -- void removeAllFunction()

    /**
     * Method removeFunction.
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public de.cismet.jpresso.core.deprecated.castorGenerated.Function removeFunction(final int index) {
        final java.lang.Object obj = _functionList.elementAt(index);
        _functionList.removeElementAt(index);
        return (de.cismet.jpresso.core.deprecated.castorGenerated.Function)obj;
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Function removeFunction(int)

    /**
     * Method setFunction.
     *
     * @param   index      DOCUMENT ME!
     * @param   vFunction  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     * @throws  IndexOutOfBoundsException            DOCUMENT ME!
     */

    public void setFunction(final int index, final de.cismet.jpresso.core.deprecated.castorGenerated.Function vFunction)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _functionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _functionList.setElementAt(vFunction, index);
    } // -- void setFunction(int, de.cismet.cids.admin.importAnt.castorGenerated.Function)

    /**
     * Method setFunction.
     *
     * @param  functionArray  DOCUMENT ME!
     */

    public void setFunction(final de.cismet.jpresso.core.deprecated.castorGenerated.Function[] functionArray) {
        // -- copy array
        _functionList.removeAllElements();
        for (int i = 0; i < functionArray.length; i++) {
            _functionList.addElement(functionArray[i]);
        }
    } // -- void setFunction(de.cismet.cids.admin.importAnt.castorGenerated.Function)

    /**
     * Sets the value of field 'import'.
     *
     * @param  _import  DOCUMENT ME!
     */
    public void setImport(final java.lang.String _import) {
        this._import = _import;
    } // -- void setImport(java.lang.String)

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
    public static de.cismet.jpresso.core.deprecated.castorGenerated.Code unmarshal(final java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (de.cismet.jpresso.core.deprecated.castorGenerated.Code)Unmarshaller.unmarshal(
                de.cismet.jpresso.core.deprecated.castorGenerated.Code.class,
                reader);
    } // -- de.cismet.cids.admin.importAnt.castorGenerated.Code unmarshal(java.io.Reader)

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
