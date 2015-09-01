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

import java.io.File;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * Encapsulates all the project option data.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
@XStreamAlias("ProjectOptions")
public final class ProjectOptions implements JPLoadable {

    //~ Constructors -----------------------------------------------------------

    /**
     * <editor-fold defaultstate="collapsed" desc="Constructors">.
     */
    public ProjectOptions() {
        this.addClassPath = TypeSafeCollections.newHashSet();
        this.driver = TypeSafeCollections.newArrayList();
        this.defaultFinalizerClass = "";
        this.defaultFinalizerProperties = new Properties();
    }

    /**
     * Creates a new ProjectOptions object.
     *
     * @param  addClassPath  DOCUMENT ME!
     * @param  driver        DOCUMENT ME!
     * @param  defFinClass   DOCUMENT ME!
     * @param  defFinProps   DOCUMENT ME!
     */
    public ProjectOptions(final Set<File> addClassPath,
            final List<DriverDescription> driver,
            final String defFinClass,
            final Properties defFinProps) {
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

    /**
     * <editor-fold defaultstate="collapsed" desc="Setters & Getters">.
     *
     * @return  DOCUMENT ME!
     */
    public Set<File> getAddClassPath() {
        return addClassPath;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  addClassPath  DOCUMENT ME!
     */
    public void setAddClassPath(final Set<File> addClassPath) {
        if (addClassPath != null) {
            this.addClassPath = addClassPath;
        } else {
            this.addClassPath = TypeSafeCollections.newHashSet();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<DriverDescription> getDriver() {
        return driver;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  driver  DOCUMENT ME!
     */
    public void setDriver(final List<DriverDescription> driver) {
        if (driver != null) {
            this.driver = driver;
        } else {
            this.driver = TypeSafeCollections.newArrayList();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getDefaultFinalizerClass() {
        return defaultFinalizerClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  defaultFinalizerClass  DOCUMENT ME!
     */
    public void setDefaultFinalizerClass(final String defaultFinalizerClass) {
        if (defaultFinalizerClass != null) {
            this.defaultFinalizerClass = defaultFinalizerClass;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Properties getDefaultFinalizerProperties() {
        return defaultFinalizerProperties;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  defaultFinalizerProperties  DOCUMENT ME!
     */
    public void setDefaultFinalizerProperties(final Properties defaultFinalizerProperties) {
        if (defaultFinalizerProperties != null) {
            this.defaultFinalizerProperties = defaultFinalizerProperties;
        }
    }
// </editor-fold>

    // private String name;

    //~ Instance fields --------------------------------------------------------

// </editor-fold>

    // private String name;
    @XStreamAlias("AdditionalClassPath")
    private Set<File> addClassPath;
    @XStreamAlias("Driver")
    private List<DriverDescription> driver;
    @XStreamAlias("DefaultFinalizerClass")
    private String defaultFinalizerClass;
    @XStreamAlias("DefaultFinalizerProperties")
    private Properties defaultFinalizerProperties;
}
