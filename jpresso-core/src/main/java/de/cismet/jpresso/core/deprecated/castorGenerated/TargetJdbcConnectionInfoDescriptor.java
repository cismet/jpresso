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
 * $Id: TargetJdbcConnectionInfoDescriptor.java,v 1.1 2003/11/04 10:00:00 Hell Exp $
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
 * Class TargetJdbcConnectionInfoDescriptor.
 *
 * @version  $Revision: 1.1 $ $Date: 2003/11/04 10:00:00 $
 */
public class TargetJdbcConnectionInfoDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

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
    public TargetJdbcConnectionInfoDescriptor() {
        super();
        nsURI = "http://www.cismet.de/cids";
        xmlName = "targetJdbcConnectionInfo";

        // -- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.xml.XMLFieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        // -- initialize attribute descriptors

        // -- _driverClass
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.String.class,
                "_driverClass",
                "DriverClass",
                org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final TargetJdbcConnectionInfo target = (TargetJdbcConnectionInfo)object;
                        return target.getDriverClass();
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final TargetJdbcConnectionInfo target = (TargetJdbcConnectionInfo)object;
                            target.setDriverClass((java.lang.String)value);
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

        // -- validation code for: _driverClass
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
            final StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _url
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.String.class,
                "_url",
                "Url",
                org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final TargetJdbcConnectionInfo target = (TargetJdbcConnectionInfo)object;
                        return target.getUrl();
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final TargetJdbcConnectionInfo target = (TargetJdbcConnectionInfo)object;
                            target.setUrl((java.lang.String)value);
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

        // -- validation code for: _url
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
            final StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _user
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.String.class,
                "_user",
                "User",
                org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final TargetJdbcConnectionInfo target = (TargetJdbcConnectionInfo)object;
                        return target.getUser();
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final TargetJdbcConnectionInfo target = (TargetJdbcConnectionInfo)object;
                            target.setUser((java.lang.String)value);
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

        // -- validation code for: _user
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            final StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _pass
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.String.class,
                "_pass",
                "Pass",
                org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final TargetJdbcConnectionInfo target = (TargetJdbcConnectionInfo)object;
                        return target.getPass();
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final TargetJdbcConnectionInfo target = (TargetJdbcConnectionInfo)object;
                            target.setPass((java.lang.String)value);
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

        // -- validation code for: _pass
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            final StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- initialize element descriptors

        // -- _propList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                de.cismet.jpresso.core.deprecated.castorGenerated.Prop.class,
                "_propList",
                "Prop",
                org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final TargetJdbcConnectionInfo target = (TargetJdbcConnectionInfo)object;
                        return target.getProp();
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final TargetJdbcConnectionInfo target = (TargetJdbcConnectionInfo)object;
                            target.addProp((de.cismet.jpresso.core.deprecated.castorGenerated.Prop)value);
                        } catch (java.lang.Exception ex) {
                            throw new IllegalStateException(ex.toString());
                        }
                    }
                    @Override
                    public java.lang.Object newInstance(final java.lang.Object parent) {
                        return new de.cismet.jpresso.core.deprecated.castorGenerated.Prop();
                    }
                });
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.cismet.de/cids");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);

        // -- validation code for: _propList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { // -- local scope
        }
        desc.setValidator(fieldValidator);
    }     // -- de.cismet.cids.admin.importAnt.castorGenerated.TargetJdbcConnectionInfoDescriptor()

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
        return de.cismet.jpresso.core.deprecated.castorGenerated.TargetJdbcConnectionInfo.class;
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
