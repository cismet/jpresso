/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 *  Encapsulates all the project option data.
 * 
 * @author srichter
 */
@XStreamAlias("ProjectOptions")
public final class ProjectOptions implements JPLoadable {

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    public ProjectOptions() {
        this.addClassPath = TypeSafeCollections.newHashSet();
        this.driver = TypeSafeCollections.newArrayList();
        this.defaultFinalizerClass = "";
        this.defaultFinalizerProperties = new Properties();
    }

    public ProjectOptions(Set<File> addClassPath, List<DriverDescription> driver, final String defFinClass, final Properties defFinProps) {
        if (addClassPath != null) {
            this.addClassPath = addClassPath;
        } else {
            this.addClassPath = TypeSafeCollections.newHashSet();
        }
        if (driver != null) {
            this.driver = driver;
        } else {
            this.driver = TypeSafeCollections.newArrayList();
        }
        if (defFinClass != null) {
            this.defaultFinalizerClass = defFinClass;
        } else {
            this.defaultFinalizerClass = "";
        }
        if (defFinProps != null) {
            this.defaultFinalizerProperties = defFinProps;
        } else {
            this.defaultFinalizerProperties = new Properties();
        }
    }
// </editor-fold>
    //private String name;
    @XStreamAlias("AdditionalClassPath")
    private Set<File> addClassPath;
    @XStreamAlias("Driver")
    private List<DriverDescription> driver;
    @XStreamAlias("DefaultFinalizerClass")
    private String defaultFinalizerClass;
    @XStreamAlias("DefaultFinalizerProperties")
    private Properties defaultFinalizerProperties;

    // <editor-fold defaultstate="collapsed" desc="Setters & Getters">
    public Set<File> getAddClassPath() {
        return addClassPath;
    }

    public void setAddClassPath(final Set<File> addClassPath) {
        if (addClassPath != null) {
            this.addClassPath = addClassPath;
        } else {
            this.addClassPath = TypeSafeCollections.newHashSet();
        }
    }

    public List<DriverDescription> getDriver() {
        return driver;
    }

    public void setDriver(final List<DriverDescription> driver) {
        if (driver != null) {
            this.driver = driver;
        } else {
            this.driver = TypeSafeCollections.newArrayList();
        }
    }

    public String getDefaultFinalizerClass() {
        return defaultFinalizerClass;
    }

    public void setDefaultFinalizerClass(String defaultFinalizerClass) {
        if (defaultFinalizerClass != null) {
            this.defaultFinalizerClass = defaultFinalizerClass;
        }
    }

    public Properties getDefaultFinalizerProperties() {
        return defaultFinalizerProperties;
    }

    public void setDefaultFinalizerProperties(final Properties defaultFinalizerProperties) {
        if (defaultFinalizerProperties != null) {
            this.defaultFinalizerProperties = defaultFinalizerProperties;
        }
    }
// </editor-fold>
}
