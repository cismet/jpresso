/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ImportRulesDescriptor.java,v 1.2 2003/12/01 12:41:00 Hell Exp $
 */

package de.cismet.jpresso.core.deprecated.castorGenerated;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;

/**
 * Class ImportRulesDescriptor.
 * 
 * @version $Revision: 1.2 $ $Date: 2003/12/01 12:41:00 $
 */
public class ImportRulesDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field nsPrefix
     */
    private java.lang.String nsPrefix;

    /**
     * Field nsURI
     */
    private java.lang.String nsURI;

    /**
     * Field xmlName
     */
    private java.lang.String xmlName;

    /**
     * Field identity
     */
    private org.exolab.castor.xml.XMLFieldDescriptor identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public ImportRulesDescriptor() {
        super();
        nsURI = "http://www.cismet.de/cids";
        xmlName = "importRules";
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.xml.XMLFieldHandler              handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _connectionInfo
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(de.cismet.jpresso.core.deprecated.castorGenerated.ConnectionInfo.class, "_connectionInfo", "connectionInfo", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ImportRules target = (ImportRules) object;
                return target.getConnectionInfo();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ImportRules target = (ImportRules) object;
                    target.setConnectionInfo( (de.cismet.jpresso.core.deprecated.castorGenerated.ConnectionInfo) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new de.cismet.jpresso.core.deprecated.castorGenerated.ConnectionInfo();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.cismet.de/cids");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _connectionInfo
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _preProcessingAndMapping
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(de.cismet.jpresso.core.deprecated.castorGenerated.PreProcessingAndMapping.class, "_preProcessingAndMapping", "preProcessingAndMapping", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ImportRules target = (ImportRules) object;
                return target.getPreProcessingAndMapping();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ImportRules target = (ImportRules) object;
                    target.setPreProcessingAndMapping( (de.cismet.jpresso.core.deprecated.castorGenerated.PreProcessingAndMapping) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new de.cismet.jpresso.core.deprecated.castorGenerated.PreProcessingAndMapping();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.cismet.de/cids");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _preProcessingAndMapping
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _relations
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(de.cismet.jpresso.core.deprecated.castorGenerated.Relations.class, "_relations", "relations", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ImportRules target = (ImportRules) object;
                return target.getRelations();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ImportRules target = (ImportRules) object;
                    target.setRelations( (de.cismet.jpresso.core.deprecated.castorGenerated.Relations) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new de.cismet.jpresso.core.deprecated.castorGenerated.Relations();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.cismet.de/cids");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _relations
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _options
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(de.cismet.jpresso.core.deprecated.castorGenerated.Options.class, "_options", "options", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ImportRules target = (ImportRules) object;
                return target.getOptions();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ImportRules target = (ImportRules) object;
                    target.setOptions( (de.cismet.jpresso.core.deprecated.castorGenerated.Options) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new de.cismet.jpresso.core.deprecated.castorGenerated.Options();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.cismet.de/cids");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _options
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _code
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(de.cismet.jpresso.core.deprecated.castorGenerated.Code.class, "_code", "code", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ImportRules target = (ImportRules) object;
                return target.getCode();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ImportRules target = (ImportRules) object;
                    target.setCode( (de.cismet.jpresso.core.deprecated.castorGenerated.Code) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new de.cismet.jpresso.core.deprecated.castorGenerated.Code();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.cismet.de/cids");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _code
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _runtimeProps
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(de.cismet.jpresso.core.deprecated.castorGenerated.RuntimeProps.class, "_runtimeProps", "runtimeProps", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ImportRules target = (ImportRules) object;
                return target.getRuntimeProps();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ImportRules target = (ImportRules) object;
                    target.setRuntimeProps( (de.cismet.jpresso.core.deprecated.castorGenerated.RuntimeProps) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new de.cismet.jpresso.core.deprecated.castorGenerated.RuntimeProps();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.cismet.de/cids");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _runtimeProps
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.ImportRulesDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAccessMode
     */
    public org.exolab.castor.mapping.AccessMode getAccessMode()
    {
        return null;
    } //-- org.exolab.castor.mapping.AccessMode getAccessMode() 

    /**
     * Method getExtends
     */
    public org.exolab.castor.mapping.ClassDescriptor getExtends()
    {
        return null;
    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends() 

    /**
     * Method getIdentity
     */
    public org.exolab.castor.mapping.FieldDescriptor getIdentity()
    {
        return identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
     * Method getJavaClass
     */
    public java.lang.Class getJavaClass()
    {
        return de.cismet.jpresso.core.deprecated.castorGenerated.ImportRules.class;
    } //-- java.lang.Class getJavaClass() 

    /**
     * Method getNameSpacePrefix
     */
    public java.lang.String getNameSpacePrefix()
    {
        return nsPrefix;
    } //-- java.lang.String getNameSpacePrefix() 

    /**
     * Method getNameSpaceURI
     */
    public java.lang.String getNameSpaceURI()
    {
        return nsURI;
    } //-- java.lang.String getNameSpaceURI() 

    /**
     * Method getValidator
     */
    public org.exolab.castor.xml.TypeValidator getValidator()
    {
        return this;
    } //-- org.exolab.castor.xml.TypeValidator getValidator() 

    /**
     * Method getXMLName
     */
    public java.lang.String getXMLName()
    {
        return xmlName;
    } //-- java.lang.String getXMLName() 

}
