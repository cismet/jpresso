/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.util.List;

/**
 * Represents all data logically belonging to one driver:
 * 
 * a) an alias name.
 * b) a driver class to load.
 * c) an optional default url format to be more userfriendly.
 * d) jar files to contain driver classes.
 * 
 * @author stefan
 */
@XStreamAlias("DriverDescription")
public class DriverDescription implements Comparable<DriverDescription> {

    // <editor-fold defaultstate="collapsed" desc="Constuctors">
    public DriverDescription(String name, String defaultClass, String urlFormat, List<DriverJar> jarFiles) {
        setName(name);
        setDefaultClass(defaultClass);
        setUrlFormat(urlFormat);
        setJarFiles(jarFiles);
    }

    public DriverDescription() {
        this.name = "";
        this.urlFormat = "";
        this.defaultClass = "";
        this.jarFiles = TypeSafeCollections.newArrayList();
    }// </editor-fold>
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

    // <editor-fold defaultstate="collapsed" desc="Getter & Setter">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        } else {
            this.name = "";
        }
    }

    public String getDefaultClass() {
        return defaultClass;
    }

    public void setDefaultClass(String defaultClass) {
        if (defaultClass != null) {
            this.defaultClass = defaultClass;
        } else {
            this.defaultClass = "";
        }
    }

    public String getUrlFormat() {
        return urlFormat;
    }

    public void setUrlFormat(String urlFormat) {
        if (urlFormat != null) {
            this.urlFormat = urlFormat;
        } else {
            this.urlFormat = "";
        }
    }

    public List<DriverJar> getJarFiles() {
        return jarFiles;
    }

    public void setJarFiles(List<DriverJar> jarFiles) {
        if (jarFiles != null) {
            this.jarFiles = jarFiles;
        } else {
            this.jarFiles = TypeSafeCollections.newArrayList();
        }
    }
// </editor-fold>

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Returns true if there is at last one jar entry, a default class set
     * and the jar entries contain at last one existing jar file known to possibly contain 
     * the default class.
     * 
     * @return
     */
    public boolean isValid() {
        //is valid: if there is at last one jar entry and a default class set...
        if (getJarFiles().size() > 0 && getDefaultClass() != null && defaultClass.length() > 0) {
            for (DriverJar cur : getJarFiles()) {
                //...and  at last one jar file is an existing file known to possibly contain the default class
                if (cur != null && cur.isValid() && cur.getDriverClassNames().contains(getDefaultClass())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Compare method for alphabetic sort.
     * 
     * @param o
     * @return
     */
//    @Override
//    public int compareTo(DriverDescription o) {
//        return name.compareToIgnoreCase(o.getName());
//    }
    @Override
    public int compareTo(DriverDescription o) {
        return name.compareTo(o.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DriverDescription) {
            return name.equals(((DriverDescription) obj).getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
