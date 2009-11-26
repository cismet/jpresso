/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.drivermanager.loading;

import de.cismet.jpresso.core.drivermanager.loading.*;
import de.cismet.jpresso.core.exceptions.DriverLoadException;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.core.serviceprovider.exceptions.DriverLoaderCreateException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.Arrays;

/**
 * A wrapper around the DriverClassLoader, which should guarantee,
 * that our jar with Cleaner.class is always on the classpath of the loader.
 * It handels automatic driver deregistration, to avoid classloader memory leaks.
 * Furthermore, it provides methods to deal with jars, as well as
 * as driver scanning method, finding all JDBC drivers in a jar.
 * 
 * @author stefan
 */
public class DynamicDriverClassLoader {

    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    //our modified URLClassLoader, which we delegate to.
    private final DriverClassLoader driverLoader;
    //the helperclass for DriverManager deregistration
    private final Class cleaner;
    //the classloaders classpath
    private final URL[] path;
    private final File[] files;
    public static final String ENDING_DOT_CLASS = "." + JPressoFileManager.END_CLASS;
    public static final String JAR_SEPARATOR_SLASH = "/";
    public static final String DOT = ".";

    @SuppressWarnings("deprecation")
    public DynamicDriverClassLoader(final File... jar) throws DriverLoaderCreateException {
        //TODO DANGER !! (e.g. when starting a class with ant from the console).
        //In such cases, specify a path to Cleaner.class here!
        //Try to find jar with Cleaner Class in it
        this.files = jar.clone();
        final URL cleaningHelperClassURL = JPressoFileManager.locateJarForClass(Cleaner.class);
        final URL[] urls = new URL[files.length + 1];
        try {
            this.path = new URL[files.length];
            for (int i = 0; i < files.length; ++i) {
                //needs deprecated method to handle files with spaces which are %20 otherwise
                path[i] = files[i].toURL();
            }
            log.debug("DriverClassLoader path: " + Arrays.deepToString(urls));
        } catch (MalformedURLException ex) {
            throw new DriverLoaderCreateException(ex);
        }
        System.arraycopy(path, 0, urls, 0, jar.length);
//        for (int i = 0; i < jar.length; ++i) {
//            urls[i] = path[i];
//        }
        urls[files.length] = cleaningHelperClassURL;
        this.driverLoader = new DriverClassLoader(urls);
        Class cleanerClass = null;
        try {
            cleanerClass = Class.forName(Cleaner.class.getCanonicalName(), false, driverLoader);
        } catch (ClassNotFoundException ex) {
            throw new DriverLoaderCreateException(ex);
        }
        this.cleaner = cleanerClass;
        if (this.cleaner == null) {
            throw new DriverLoaderCreateException();
        }

    }

    /**
     * 
     * 
     * @param name
     * @return an instance of the class with the given canonical name in the classloaders path, if it is a java.sql.Driver
     * @throws de.cismet.drivermanager.exception.DriverLoadException
     */
    public Driver loadDriver(final String name) throws DriverLoadException {
        Class driverClass = null;
        Driver current = null;

        try {
            driverClass = Class.forName(name, false, driverLoader);
        } catch (ClassNotFoundException ex) {
            throw new DriverLoadException(ex);
        }
        if (driverClass != null && Driver.class.isAssignableFrom(driverClass) && !driverClass.isInterface()) {
            try {
                current = (Driver) driverClass.newInstance();
            } catch (InstantiationException ex) {
                throw new DriverLoadException(ex);
            } catch (IllegalAccessException ex) {
                throw new DriverLoadException(ex);
            }
            deregisterAll();
            return current;
        } else {
            throw new DriverLoadException("Class " + name + " is does not implemtent the java.sql.Driver interface or is an interface/abstract class itself!");
        }

    }

    /**
     * 
     * @param resourceName
     * @return a class with the given canonical name in the classloaders path, if it is a java.sql.Driver
     */
    public final Class loadDriverClassFromFile(final String resourceName) throws DriverLoadException {
        if (resourceName.endsWith(ENDING_DOT_CLASS)) {
            Class driverClass = null;
            try {
//                driverClass = Class.forName(resourceName.substring(0, resourceName.length() - 6).replaceAll(JAR_SEPARATOR_SLASH, DOT), false, driverLoader);
                driverClass = Class.forName(resourceName.substring(0, resourceName.length() - 6).replace(JAR_SEPARATOR_SLASH, DOT), false, driverLoader);
            } catch (Throwable ex) {
                //expected to happen -> ignore
            }
            if (driverClass != null && Driver.class.isAssignableFrom(driverClass) && !driverClass.isInterface()) {
                return driverClass;
            }
        }
        throw new DriverLoadException("Class " + resourceName + " is does not implemtent the java.sql.Driver interface or is an interface/abstract class itself!");
    }

    public DynamicDriverClassLoader copy() {
        try {
            return new DynamicDriverClassLoader(files);
        } catch (DriverLoaderCreateException ex) {
            throw new IllegalStateException("Unexpected Exception while cloning DynamicClassLoader with path " + Arrays.deepToString(path) + ". Files referenced still present?", ex);
        }
    }

    /**
     * 
     * @return the URL of this loaders classpath
     */
    public URL[] getPath() {
        return path.clone();
    }

    /**
     * 
     * @param cleaner
     * @param loader
     */
    private void deregisterAll(final Class cleaner, final ClassLoader loader) {
        if (loader == null) {
            throw new NullPointerException("Loader for deregistration is null");
        }
        try {
            if (cleaner == null) {
                //if there should be no cleaner, create one...
                final Class cleanerload = Class.forName(Cleaner.class.getCanonicalName(), true, loader);
                deregisterAll(cleanerload, loader);
            } else {
                //cleaner standard-constructor invokes cleaning-procedure
                cleaner.newInstance();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalStateException("Can not deregister drivers", ex);
        }
    }

    /**
     * 
     */
    private void deregisterAll() {
        deregisterAll(cleaner, driverLoader);
    }

    public static final boolean instanceOf(Object o) {
        return o instanceof DriverClassLoader;
    }
}

/**
 * The internal child-first-delegation URLClassLoader which does the actual loading.
 * (Otherwise the cache of parent-classloaders will be flooded on scanning large jars!)
 * 
 * @author stefan
 */
class DriverClassLoader extends URLClassLoader {

    protected DriverClassLoader(final URL[] urls) {
        super(urls, null);
    }

    @Override
    public final synchronized Class<?> loadClass(final String name) throws ClassNotFoundException {
        try {
            //if (name.equals(Cleaner.class.getCanonicalName())) {
            // First check whether it's already been loaded, if so use it
            Class loadedClass = findLoadedClass(name);
            // Not loaded, try to load it
            if (loadedClass == null) {
                // Ignore parent delegation try to load locally
                loadedClass = findClass(name);
                // If not found locally, use normal parent delegation in URLClassloader
                if (loadedClass == null) {
                    // throws ClassNotFoundException if not found in delegation hierarchy at all
                    loadedClass = super.loadClass(name);
                }
            }
            // will never return null (ClassNotFoundException will be thrown)
            return loadedClass;
//        }
        } catch (Exception e) {
            // If not found locally, use normal parent delegation in URLClassloader
            return super.loadClass(name);
        }
    }
}
