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
package de.cismet.jpresso.core.serviceprovider;

import java.io.File;

import de.cismet.jpresso.core.drivermanager.loading.DynamicDriverClassLoader;
import de.cismet.jpresso.core.exceptions.DriverLoadException;
import de.cismet.jpresso.core.serviceprovider.exceptions.DriverLoaderCreateException;

/**
 * A scanner that looks for implemtentations for the java Driver-Interface in a jar.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class JarDriverScanner {

    //~ Static fields/initializers ---------------------------------------------

    // Number of max. scanned classes per classloader to avoid out-of-permgen memory errors
    private static final int CLASS_MAX = 2500;

    //~ Instance fields --------------------------------------------------------

    private DynamicDriverClassLoader loader;
    private int loadCounter;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new JarDriverScanner object.
     *
     * @param   jars  DOCUMENT ME!
     *
     * @throws  DriverLoaderCreateException  de.cismet.jpressocore.exceptions.DriverLoaderCreateException
     */
    public JarDriverScanner(final File... jars) throws DriverLoaderCreateException {
        loadCounter = 0;
        loader = new DynamicDriverClassLoader(jars);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   ressourceName  the jar entrys String representation (the classname with "/" instead of ".")
     *
     * @return  the classes cananical name if it was a jdbc driver, null if not.
     */
    public String scanResourceForDriverClass(final String ressourceName) {
        if (++loadCounter > CLASS_MAX) {
            // if the maximum amount of classes is loaded, replace the loader with a fresh copy of it. Avoids PermGen
            // Space errors.
            loader = loader.copy();
            loadCounter = 0;
        }
        final Class c;
        try {
            c = loader.loadDriverClassFromFile(ressourceName);
        } catch (DriverLoadException ex) {
            return null;
        }
        return c.getCanonicalName();
    }
}
