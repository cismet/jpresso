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
 * $Id: MappingDescriptor.java,v 1.2 2004/05/10 10:17:00 Hell Exp $
 */
package de.cismet.jpresso.core.deprecated.castorGenerated;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;

/**
 * Class MappingDescriptor.
 *
 * @version  $Revision: 1.2 $ $Date: 2004/05/10 10:17:00 $
 */
public class MappingDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

    //~ Instance fields --------------------------------------------------------

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /** Field nsPrefix. */
    private java.lang.String nsPrefix;

    /** Field nsURI. */
    private java.lang.String nsURI;

    /** Field xmlName. */
    private java.lang.String xmlName;

    /** Field identity. */
    private org.exolab.castor.xml.XMLFieldDescriptor identity;

    //~ Constructors -----------------------------------------------------------

    /**
     * ----------------/ - Constructors -/ ----------------/
     */
    public MappingDescriptor() {
        super();
        nsURI = "http://www.cismet.de/cids";
        xmlName = "mapping";
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.xml.XMLFieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        // -- _content
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.String.class,
                "_content",
                "PCDATA",
                org.exolab.castor.xml.NodeType.Text);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final Mapping target = (Mapping)object;
                        return target.getContent();
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final Mapping target = (Mapping)object;
                            target.setContent((java.lang.String)value);
                        } catch (java.lang.Exception ex) {
                            throw new IllegalStateException(ex.toString());
                        }
                    }
                    @Override
                    public java.lang.Object newInstance(final java.lang.Object parent) {
                        return null;
                    }
                });
        desc.setHandler(handler);
        addFieldDescriptor(desc);

        // -- validation code for: _content
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            final StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- initialize attribute descriptors

        // -- _path
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.String.class,
                "_path",
                "path",
                org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final Mapping target = (Mapping)object;
                        return target.getPath();
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final Mapping target = (Mapping)object;
                            target.setPath((java.lang.String)value);
                        } catch (java.lang.Exception ex) {
                            throw new IllegalStateException(ex.toString());
                        }
                    }
                    @Override
                    public java.lang.Object newInstance(final java.lang.Object parent) {
                        return null;
                    }
                });
        desc.setHandler(handler);
        addFieldDescriptor(desc);

        // -- validation code for: _path
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            final StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _targetTable
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.String.class,
                "_targetTable",
                "targetTable",
                org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final Mapping target = (Mapping)object;
                        return target.getTargetTable();
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final Mapping target = (Mapping)object;
                            target.setTargetTable((java.lang.String)value);
                        } catch (java.lang.Exception ex) {
                            throw new IllegalStateException(ex.toString());
                        }
                    }
                    @Override
                    public java.lang.Object newInstance(final java.lang.Object parent) {
                        return null;
                    }
                });
        desc.setHandler(handler);
        desc.setRequired(true);
        addFieldDescriptor(desc);

        // -- validation code for: _targetTable
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
            final StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _targetField
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.String.class,
                "_targetField",
                "targetField",
                org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final Mapping target = (Mapping)object;
                        return target.getTargetField();
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final Mapping target = (Mapping)object;
                            target.setTargetField((java.lang.String)value);
                        } catch (java.lang.Exception ex) {
                            throw new IllegalStateException(ex.toString());
                        }
                    }
                    @Override
                    public java.lang.Object newInstance(final java.lang.Object parent) {
                        return null;
                    }
                });
        desc.setHandler(handler);
        desc.setRequired(true);
        addFieldDescriptor(desc);

        // -- validation code for: _targetField
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
            final StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _autoIncrement
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.Boolean.TYPE,
                "_autoIncrement",
                "autoIncrement",
                org.exolab.castor.xml.NodeType.Attribute);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final Mapping target = (Mapping)object;
                        if (!target.hasAutoIncrement()) {
                            return null;
                        }
                        return new Boolean(target.getAutoIncrement());
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final Mapping target = (Mapping)object;
                            // if null, use delete method for optional primitives
                            if (value == null) {
                                target.deleteAutoIncrement();
                                return;
                            }
                            target.setAutoIncrement(((Boolean)value).booleanValue());
                        } catch (java.lang.Exception ex) {
                            throw new IllegalStateException(ex.toString());
                        }
                    }
                    @Override
                    public java.lang.Object newInstance(final java.lang.Object parent) {
                        return null;
                    }
                });
        desc.setHandler(handler);
        addFieldDescriptor(desc);

        // -- validation code for: _autoIncrement
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            final BooleanValidator typeValidator = new BooleanValidator();
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _comparing
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.Boolean.TYPE,
                "_comparing",
                "comparing",
                org.exolab.castor.xml.NodeType.Attribute);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final Mapping target = (Mapping)object;
                        if (!target.hasComparing()) {
                            return null;
                        }
                        return new Boolean(target.getComparing());
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final Mapping target = (Mapping)object;
                            // if null, use delete method for optional primitives
                            if (value == null) {
                                target.deleteComparing();
                                return;
                            }
                            target.setComparing(((Boolean)value).booleanValue());
                        } catch (java.lang.Exception ex) {
                            throw new IllegalStateException(ex.toString());
                        }
                    }
                    @Override
                    public java.lang.Object newInstance(final java.lang.Object parent) {
                        return null;
                    }
                });
        desc.setHandler(handler);
        addFieldDescriptor(desc);

        // -- validation code for: _comparing
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            final BooleanValidator typeValidator = new BooleanValidator();
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _enclosingChar
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.String.class,
                "_enclosingChar",
                "enclosingChar",
                org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final Mapping target = (Mapping)object;
                        return target.getEnclosingChar();
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final Mapping target = (Mapping)object;
                            target.setEnclosingChar((java.lang.String)value);
                        } catch (java.lang.Exception ex) {
                            throw new IllegalStateException(ex.toString());
                        }
                    }
                    @Override
                    public java.lang.Object newInstance(final java.lang.Object parent) {
                        return null;
                    }
                });
        desc.setHandler(handler);
        addFieldDescriptor(desc);

        // -- validation code for: _enclosingChar
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            final StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- initialize element descriptors
    }     // -- de.cismet.cids.admin.importAnt.castorGenerated.MappingDescriptor()

    //~ Methods ----------------------------------------------------------------

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method getAccessMode.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public org.exolab.castor.mapping.AccessMode getAccessMode() {
        return null;
    } // -- org.exolab.castor.mapping.AccessMode getAccessMode()

    /**
     * Method getExtends.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public org.exolab.castor.mapping.ClassDescriptor getExtends() {
        return null;
    } // -- org.exolab.castor.mapping.ClassDescriptor getExtends()

    /**
     * Method getIdentity.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        return identity;
    } // -- org.exolab.castor.mapping.FieldDescriptor getIdentity()

    /**
     * Method getJavaClass.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public java.lang.Class getJavaClass() {
        return de.cismet.jpresso.core.deprecated.castorGenerated.Mapping.class;
    } // -- java.lang.Class getJavaClass()

    /**
     * Method getNameSpacePrefix.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public java.lang.String getNameSpacePrefix() {
        return nsPrefix;
    } // -- java.lang.String getNameSpacePrefix()

    /**
     * Method getNameSpaceURI.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public java.lang.String getNameSpaceURI() {
        return nsURI;
    } // -- java.lang.String getNameSpaceURI()

    /**
     * Method getValidator.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public org.exolab.castor.xml.TypeValidator getValidator() {
        return this;
    } // -- org.exolab.castor.xml.TypeValidator getValidator()

    /**
     * Method getXMLName.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public java.lang.String getXMLName() {
        return xmlName;
    } // -- java.lang.String getXMLName()
}
