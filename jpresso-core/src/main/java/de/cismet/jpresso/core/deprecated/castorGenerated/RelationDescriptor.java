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
 * $Id: RelationDescriptor.java,v 1.1 2003/11/04 10:00:00 Hell Exp $
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
 * Class RelationDescriptor.
 *
 * @version  $Revision: 1.1 $ $Date: 2003/11/04 10:00:00 $
 */
public class RelationDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

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
    public RelationDescriptor() {
        super();
        nsURI = "http://www.cismet.de/cids";
        xmlName = "relation";
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.xml.XMLFieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        // -- initialize attribute descriptors

        // -- _masterTable
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.String.class,
                "_masterTable",
                "masterTable",
                org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final Relation target = (Relation)object;
                        return target.getMasterTable();
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final Relation target = (Relation)object;
                            target.setMasterTable((java.lang.String)value);
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

        // -- validation code for: _masterTable
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
            final StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _masterTableForeignKey
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.String.class,
                "_masterTableForeignKey",
                "masterTableForeignKey",
                org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final Relation target = (Relation)object;
                        return target.getMasterTableForeignKey();
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final Relation target = (Relation)object;
                            target.setMasterTableForeignKey((java.lang.String)value);
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

        // -- validation code for: _masterTableForeignKey
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
            final StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _foreignKeyComparing
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.Boolean.TYPE,
                "_foreignKeyComparing",
                "foreignKeyComparing",
                org.exolab.castor.xml.NodeType.Attribute);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final Relation target = (Relation)object;
                        if (!target.hasForeignKeyComparing()) {
                            return null;
                        }
                        return new Boolean(target.getForeignKeyComparing());
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final Relation target = (Relation)object;
                            // if null, use delete method for optional primitives
                            if (value == null) {
                                target.deleteForeignKeyComparing();
                                return;
                            }
                            target.setForeignKeyComparing(((Boolean)value).booleanValue());
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

        // -- validation code for: _foreignKeyComparing
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
                        final Relation target = (Relation)object;
                        return target.getEnclosingChar();
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final Relation target = (Relation)object;
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
        // -- _detailTable
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.String.class,
                "_detailTable",
                "detailTable",
                org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final Relation target = (Relation)object;
                        return target.getDetailTable();
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final Relation target = (Relation)object;
                            target.setDetailTable((java.lang.String)value);
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

        // -- validation code for: _detailTable
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
            final StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _detailTableKey
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.String.class,
                "_detailTableKey",
                "detailTableKey",
                org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final Relation target = (Relation)object;
                        return target.getDetailTableKey();
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final Relation target = (Relation)object;
                            target.setDetailTableKey((java.lang.String)value);
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

        // -- validation code for: _detailTableKey
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
            final StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- initialize element descriptors
    }     // -- de.cismet.cids.admin.importAnt.castorGenerated.RelationDescriptor()

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
        return de.cismet.jpresso.core.deprecated.castorGenerated.Relation.class;
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
