/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.Properties;

/**
 *
 * @author srichter
 */
public final class RuntimeProperties {

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    public RuntimeProperties(String finalizerClass, Properties finalizerProperties, String importFileName) {
        this.finalizerClass = finalizerClass;
        this.finalizerProperties = finalizerProperties;
    }

    public RuntimeProperties() {
        finalizerProperties = new Properties();
    }
    // </editor-fold>
    /** Holds value of property finalizerClass. */
    @XStreamAlias("FinalizerClass")
    private String finalizerClass = "";
    /** Holds value of property finalizerProperties. */
    @XStreamAlias("FinalizerProperties")
    private java.util.Properties finalizerProperties;
    // <editor-fold defaultstate="collapsed" desc="Setters & Getters">
    public String getFinalizerClass() {
        return finalizerClass;
    }

    public void setFinalizerClass(String finalizerClass) {
        if (finalizerClass != null) {
            this.finalizerClass = finalizerClass;
        } else {
            this.finalizerClass = "";
        }

    }

    public java.util.Properties getFinalizerProperties() {
        return finalizerProperties;
    }

    public void setFinalizerProperties(java.util.Properties finalizerProperties) {
        if (finalizerProperties != null) {
            this.finalizerProperties = finalizerProperties;
        } else {
            this.finalizerProperties = new Properties();
        }
    }
// </editor-fold>
}
