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

import java.io.File;

import java.util.Set;

import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * DOCUMENT ME!
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
@XStreamAlias("DriverJar")
public class DriverJar {

    //~ Constructors -----------------------------------------------------------

    /**
     * <editor-fold defaultstate="collapsed" desc="Constructors">.
     *
     * @param   jarFile           DOCUMENT ME!
     * @param   driverClassNames  DOCUMENT ME!
     *
     * @throws  NullPointerException  DOCUMENT ME!
     */
    public DriverJar(final File jarFile, final Set<String> driverClassNames) {
        if (jarFile == null) {
            throw new NullPointerException("JarFile can not be null!");
        }
        this.jarFile = jarFile;
        setDriverClassNames(driverClassNames);
    }

    /**
     * Creates a new DriverJar object.
     *
     * @param  jarFile  DOCUMENT ME!
     */
    public DriverJar(final File jarFile) {
        this(jarFile, null);
    }

// </editor-fold>

    /**
     * <editor-fold defaultstate="collapsed" desc="Getter & Setter">.
     *
     * @return  DOCUMENT ME!
     */
    public File getJarFile() {
        return jarFile;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Set<String> getDriverClassNames() {
        return driverClassNames;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  driverClassNames  DOCUMENT ME!
     */
    public void setDriverClassNames(final Set<String> driverClassNames) {
        if (driverClassNames != null) {
            this.driverClassNames = driverClassNames;
        } else {
            this.driverClassNames = TypeSafeCollections.newHashSet();
        }
    }

// </editor-fold>

    //~ Instance fields --------------------------------------------------------

    @XStreamAlias("file")
    @XStreamAsAttribute
    private final File jarFile;
    @XStreamAlias("DriverClasses")
    private Set<String> driverClassNames;
    // is the jar file valid/existing/a file

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isValid() {
        if (getJarFile().isFile()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof DriverJar) {
            return (this.getJarFile().equals(((DriverJar)obj).getJarFile()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int hash = 203 + ((this.jarFile != null) ? this.jarFile.hashCode() : 0);
        return hash;
    }
}
