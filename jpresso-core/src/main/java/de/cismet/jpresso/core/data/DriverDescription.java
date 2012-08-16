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
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;

import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * Represents all data logically belonging to one driver:
 *
 * <p>a) an alias name. b) a driver class to load. c) an optional default url format to be more userfriendly. d) jar
 * files to contain driver classes.</p>
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
@XStreamAlias("DriverDescription")
public class DriverDescription implements Comparable<DriverDescription> {

    //~ Constructors -----------------------------------------------------------

    /**
     * <editor-fold defaultstate="collapsed" desc="Constuctors">.
     *
     * @param  name          DOCUMENT ME!
     * @param  defaultClass  DOCUMENT ME!
     * @param  urlFormat     DOCUMENT ME!
     * @param  jarFiles      DOCUMENT ME!
     */
    public DriverDescription(final String name,
            final String defaultClass,
            final String urlFormat,
            final List<DriverJar> jarFiles) {
        setName(name);
        setDefaultClass(defaultClass);
        setUrlFormat(urlFormat);
        setJarFiles(jarFiles);
    }

    /**
     * Creates a new DriverDescription object.
     */
    public DriverDescription() {
        this.name = "";
        this.urlFormat = "";
        this.defaultClass = "";
        this.jarFiles = TypeSafeCollections.newArrayList();
    } // </editor-fold>

    /**
     * <editor-fold defaultstate="collapsed" desc="Getter & Setter">.
     *
     * @return  DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  name  DOCUMENT ME!
     */
    public void setName(final String name) {
        if (name != null) {
            this.name = name;
        } else {
            this.name = "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getDefaultClass() {
        return defaultClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  defaultClass  DOCUMENT ME!
     */
    public void setDefaultClass(final String defaultClass) {
        if (defaultClass != null) {
            this.defaultClass = defaultClass;
        } else {
            this.defaultClass = "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getUrlFormat() {
        return urlFormat;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  urlFormat  DOCUMENT ME!
     */
    public void setUrlFormat(final String urlFormat) {
        if (urlFormat != null) {
            this.urlFormat = urlFormat;
        } else {
            this.urlFormat = "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<DriverJar> getJarFiles() {
        return jarFiles;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  jarFiles  DOCUMENT ME!
     */
    public void setJarFiles(final List<DriverJar> jarFiles) {
        if (jarFiles != null) {
            this.jarFiles = jarFiles;
        } else {
            this.jarFiles = TypeSafeCollections.newArrayList();
        }
    }
// </editor-fold>

    //~ Instance fields --------------------------------------------------------

    @XStreamAlias("name")
    @XStreamAsAttribute
    private String name;
    @XStreamAlias("defaultclass")
    @XStreamAsAttribute
    private String defaultClass;
    @XStreamAlias("urlformat")
    @XStreamAsAttribute
    private String urlFormat;
    @XStreamAlias("DriverJars")
    private List<DriverJar> jarFiles;

    //~ Methods ----------------------------------------------------------------

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Returns true if there is at last one jar entry, a default class set and the jar entries contain at last one
     * existing jar file known to possibly contain the default class.
     *
     * @return  DOCUMENT ME!
     */
    public boolean isValid() {
        // is valid: if there is at last one jar entry and a default class set...
        if ((getJarFiles().size() > 0) && (getDefaultClass() != null) && (defaultClass.length() > 0)) {
            for (final DriverJar cur : getJarFiles()) {
                // ...and  at last one jar file is an existing file known to possibly contain the default class
                if ((cur != null) && cur.isValid() && cur.getDriverClassNames().contains(getDefaultClass())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Compare method for alphabetic sort.
     *
     * @param   o  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
// @Override
// public int compareTo(DriverDescription o) {
// return name.compareToIgnoreCase(o.getName());
// }
    @Override
    public int compareTo(final DriverDescription o) {
        return name.compareTo(o.getName());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof DriverDescription) {
            return name.equals(((DriverDescription)obj).getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = (97 * hash) + ((this.name != null) ? this.name.hashCode() : 0);
        return hash;
    }
}
