/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.io.File;
import java.util.Set;

/**
 *
 * @author stefan
 */
@XStreamAlias("DriverJar")
public class DriverJar {

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    public DriverJar(File jarFile, Set<String> driverClassNames) {
        if (jarFile == null) {
            throw new NullPointerException("JarFile can not be null!");
        }
        this.jarFile = jarFile;
        setDriverClassNames(driverClassNames);
    }

    public DriverJar(File jarFile) {
        this(jarFile, null);
    }

// </editor-fold>
    @XStreamAlias("file")
    @XStreamAsAttribute
    private final File jarFile;
    @XStreamAlias("DriverClasses")
    private Set<String> driverClassNames;
    //is the jar file valid/existing/a file

    // <editor-fold defaultstate="collapsed" desc="Getter & Setter">
    public File getJarFile() {
        return jarFile;
    }

    public Set<String> getDriverClassNames() {
        return driverClassNames;
    }

    public void setDriverClassNames(Set<String> driverClassNames) {
        if (driverClassNames != null) {
            this.driverClassNames = driverClassNames;
        } else {
            this.driverClassNames = TypeSafeCollections.newHashSet();
        }
    }

// </editor-fold>
    public boolean isValid() {
        if (getJarFile().isFile()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof DriverJar) {
            return (this.getJarFile().equals(((DriverJar) obj).getJarFile()));
        }
        return false;
    }

    @Override
    public int hashCode() {        
        final int hash = 203 + (this.jarFile != null ? this.jarFile.hashCode() : 0);
        return hash;
    }
}
