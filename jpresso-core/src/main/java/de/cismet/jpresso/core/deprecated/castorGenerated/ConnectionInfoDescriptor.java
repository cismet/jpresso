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
 * $Id: ConnectionInfoDescriptor.java,v 1.2 2004/05/10 10:17:00 Hell Exp $
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
 * Class ConnectionInfoDescriptor.
 *
 * @version  $Revision: 1.2 $ $Date: 2004/05/10 10:17:00 $
 */
public class ConnectionInfoDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

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
    public ConnectionInfoDescriptor() {
        super();
        nsURI = "http://www.cismet.de/cids";
        xmlName = "connectionInfo";

        // -- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.xml.XMLFieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        // -- initialize attribute descriptors

        // -- initialize element descriptors

        // -- _sourceJdbcConnectionInfo
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                de.cismet.jpresso.core.deprecated.castorGenerated.SourceJdbcConnectionInfo.class,
                "_sourceJdbcConnectionInfo",
                "sourceJdbcConnectionInfo",
                org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final ConnectionInfo target = (ConnectionInfo)object;
                        return target.getSourceJdbcConnectionInfo();
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final ConnectionInfo target = (ConnectionInfo)object;
                            target.setSourceJdbcConnectionInfo(
                                (de.cismet.jpresso.core.deprecated.castorGenerated.SourceJdbcConnectionInfo)value);
                        } catch (java.lang.Exception ex) {
                            throw new IllegalStateException(ex.toString());
                        }
                    }
                    @Override
                    public java.lang.Object newInstance(final java.lang.Object parent) {
                        return new de.cismet.jpresso.core.deprecated.castorGenerated.SourceJdbcConnectionInfo();
                    }
                });
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.cismet.de/cids");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _sourceJdbcConnectionInfo
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
        }
        desc.setValidator(fieldValidator);
        // -- _targetJdbcConnectionInfo
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                de.cismet.jpresso.core.deprecated.castorGenerated.TargetJdbcConnectionInfo.class,
                "_targetJdbcConnectionInfo",
                "targetJdbcConnectionInfo",
                org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

                    @Override
                    public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                        final ConnectionInfo target = (ConnectionInfo)object;
                        return target.getTargetJdbcConnectionInfo();
                    }
                    @Override
                    public void setValue(final java.lang.Object object, final java.lang.Object value)
                            throws IllegalStateException, IllegalArgumentException {
                        try {
                            final ConnectionInfo target = (ConnectionInfo)object;
                            target.setTargetJdbcConnectionInfo(
                                (de.cismet.jpresso.core.deprecated.castorGenerated.TargetJdbcConnectionInfo)value);
                        } catch (java.lang.Exception ex) {
                            throw new IllegalStateException(ex.toString());
                        }
                    }
                    @Override
                    public java.lang.Object newInstance(final java.lang.Object parent) {
                        return new de.cismet.jpresso.core.deprecated.castorGenerated.TargetJdbcConnectionInfo();
                    }
                });
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.cismet.de/cids");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _targetJdbcConnectionInfo
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
        }
        desc.setValidator(fieldValidator);
    }     // -- de.cismet.cids.admin.importAnt.castorGenerated.ConnectionInfoDescriptor()

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
        return de.cismet.jpresso.core.deprecated.castorGenerated.ConnectionInfo.class;
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
