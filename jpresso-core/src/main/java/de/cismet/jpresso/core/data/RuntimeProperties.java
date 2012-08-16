/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Properties;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class RuntimeProperties {

    //~ Constructors -----------------------------------------------------------

    /**
     * <editor-fold defaultstate="collapsed" desc="Constructors">.
     *
     * @param  finalizerClass       DOCUMENT ME!
     * @param  finalizerProperties  DOCUMENT ME!
     * @param  importFileName       DOCUMENT ME!
     */
    public RuntimeProperties(final String finalizerClass,
            final Properties finalizerProperties,
            final String importFileName) {
        this.finalizerClass = finalizerClass;
        this.finalizerProperties = finalizerProperties;
    }

    /**
     * Creates a new RuntimeProperties object.
     */
    public RuntimeProperties() {
        finalizerProperties = new Properties();
    }
    // </editor-fold>

    /**
     * <editor-fold defaultstate="collapsed" desc="Setters & Getters">.
     *
     * @return  DOCUMENT ME!
     */
    public String getFinalizerClass() {
        return finalizerClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  finalizerClass  DOCUMENT ME!
     */
    public void setFinalizerClass(final String finalizerClass) {
        if (finalizerClass != null) {
            this.finalizerClass = finalizerClass;
        } else {
            this.finalizerClass = "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public java.util.Properties getFinalizerProperties() {
        return finalizerProperties;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  finalizerProperties  DOCUMENT ME!
     */
    public void setFinalizerProperties(final java.util.Properties finalizerProperties) {
        if (finalizerProperties != null) {
            this.finalizerProperties = finalizerProperties;
        } else {
            this.finalizerProperties = new Properties();
        }
    }
// </editor-fold>

    //~ Instance fields --------------------------------------------------------

    /** Holds value of property finalizerClass. */
    @XStreamAlias("FinalizerClass")
    private String finalizerClass = "";
    /** Holds value of property finalizerProperties. */
    @XStreamAlias("FinalizerProperties")
    private java.util.Properties finalizerProperties;
}
