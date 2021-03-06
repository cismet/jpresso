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
package de.cismet.jpresso.core.drivermanager.loading;

import java.io.File;

import java.lang.reflect.Field;

import java.sql.Driver;
import java.sql.DriverManager;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.core.data.DriverJar;
import de.cismet.jpresso.core.exceptions.DriverLoadException;
import de.cismet.jpresso.core.serviceprovider.exceptions.DriverLoaderCreateException;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * Maps (driverDescr)File and Classname to Drivers.
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public class DynamicDriverLoaderManager {

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    // a cache, that hits if a driver with a specific name, from a specific DriverDescription is loaded more than once.
    private final Map<DriverDescription, Map<String, Driver>> cache;

    //~ Constructors -----------------------------------------------------------

    /**
     * <editor-fold defaultstate="collapsed" desc="Constructor">.
     */
    public DynamicDriverLoaderManager() {
        this.cache = TypeSafeCollections.newConcurrentHashMap();
    }

    // </editor-fold>

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   driverDescr  DOCUMENT ME!
     * @param   name         DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  DriverLoadException          de.cismet.drivermanager.exception.DriverLoadException
     * @throws  DriverLoaderCreateException  de.cismet.drivermanager.exception.DriverLoaderCreateException
     */
    public Driver loadDriver(final DriverDescription driverDescr, final String name) throws DriverLoadException,
        DriverLoaderCreateException {
        Map<String, Driver> drivers = cache.get(driverDescr);
        Driver ret = null;
        if (drivers != null) {
            ret = drivers.get(name);
            if (ret != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Cache hit: returning " + ret);
                }
                return ret;
            }
        }
        final DynamicDriverClassLoader loader = createLoaderForDriverDescription(driverDescr);
        if (loader != null) {
            ret = loader.loadDriver(name);
            if (ret != null) {
                if (drivers != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Driver cache miss, but File there: returning " + ret);
                    }
                    drivers.put(name, ret);
                } else {
                    drivers = TypeSafeCollections.newHashMap();
                    drivers.put(name, ret);
                    cache.put(driverDescr, drivers);
                    if (log.isDebugEnabled()) {
                        log.debug("Driver cache miss, and File not there: returning " + ret);
                    }
                }
                return ret;
            }
        }
        throw new DriverLoadException("Driver not found!");
    }

    /**
     * DOCUMENT ME!
     */
    public void clearLoaderManagerCache() {
        // XXX: error-prone perm-gen-leak fix against drivers that load classes on their own against our will...
        try {
            boolean java16 = false;
            boolean java17 = false;
            final Field[] fields = DriverManager.class.getDeclaredFields();
            for (final Field field : fields) {
                if (field.getName().equals("writeDrivers") || field.getName().equals("readDrivers")) {
                    java16 = true;
                    break;
                } else if (field.getName().equals("registeredDrivers")) {
                    java17 = true;
                    break;
                }
            }

            if (java16) {
                log.info(
                    "applying ugly driver manager hack and removing DynamicDriverClassLoader drivers for Java 1.6");
                final Field writeDrivers = DriverManager.class.getDeclaredField("writeDrivers");
                final Field readDrivers = DriverManager.class.getDeclaredField("readDrivers");
                writeDrivers.setAccessible(true);
                readDrivers.setAccessible(true);
                synchronized (DriverManager.class) {
                    // get write-copy of driver-infos
                    final Vector v = (Vector)writeDrivers.get(null);
                    boolean done = false;
                    boolean modified = false;
                    // find all drivers in driver list that are loaded by one of our driverclassloader and delete them
                    // (loop on concurrent modifications - which should never happen)
                    while (!done) {
                        try {
                            final Iterator<?> it = v.iterator();
                            while (it.hasNext()) {
                                final Object o = it.next();
                                // extract driver-class from driver-info
                                final Field drvClassField = o.getClass().getDeclaredField("driverClass");
                                drvClassField.setAccessible(true);
                                ClassLoader cl = ((Class<?>)drvClassField.get(o)).getClassLoader();
                                // check if there is a driverclassloader in the cl-hierarchy...
                                while ((cl != null) && !cl.equals(getClass().getClassLoader())) {
                                    // if yes: delete
                                    if (DynamicDriverClassLoader.instanceOf(cl)) {
                                        if (log.isDebugEnabled()) {
                                            log.debug("Removing driver: " + o);
                                        }
                                        it.remove();
                                        modified = true;
                                        cl = null;
                                        // else: ignore and check further upward in hierachy
                                    } else {
                                        cl = cl.getParent();
                                    }
                                }
                                drvClassField.setAccessible(false);
                            }
                            done = true;
                        } catch (ConcurrentModificationException cmex) {
                            log.warn(cmex, cmex);
                        }
                    }
                    // if we modified the write-copy, we now have to adjust the read-copy
                    if (modified) {
                        readDrivers.set(null, v.clone());
                    }
                }
                writeDrivers.setAccessible(false);
                readDrivers.setAccessible(false);
            } else if (java17) {
                log.info(
                    "applying ugly driver manager hack and removing DynamicDriverClassLoader drivers for Java 1.7+");

                final Field registeredDriversFiled = DriverManager.class.getDeclaredField("registeredDrivers");
                registeredDriversFiled.setAccessible(true);
                final CopyOnWriteArrayList registeredDrivers = (CopyOnWriteArrayList)registeredDriversFiled.get(null);

                final Iterator<?> it = registeredDrivers.iterator();
                while (it.hasNext()) {
                    final Object o = it.next();
                    // extract driver-class from driver-info
                    final Field drvField = o.getClass().getDeclaredField("driver");
                    drvField.setAccessible(true);
                    ClassLoader cl = ((Class<?>)drvField.get(o).getClass()).getClassLoader();
                    // check if there is a driverclassloader in the cl-hierarchy...
                    while ((cl != null) && !cl.equals(getClass().getClassLoader())) {
                        // if yes: delete
                        if (DynamicDriverClassLoader.instanceOf(cl)) {
                            if (log.isDebugEnabled()) {
                                log.debug("Removing driver: " + o);
                            }
                            it.remove();
                            cl = null;
                            // else: ignore and check further upward in hierachy
                        } else {
                            cl = cl.getParent();
                        }
                    }
                    drvField.setAccessible(false);
                }
            } else {
                log.warn(
                    "ugly driver manager hack not available in this java version, cannot remove DynamicDriverClassLoader drivers");
            }
        } catch (Exception ex) {
            log.error(ex, ex);
        }
        // --------end fix
        cache.clear();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  toRemove  url
     */
    public void removeDriverDescription(final DriverDescription toRemove) {
        cache.remove(toRemove);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Set<DriverDescription> getDriverDescriptions() {
        return this.cache.keySet();
    }

    /**
     * Per default creates a new classloader for every single driver for isolation. Maybe it could cache one classloader
     * for every driverDescr one day?
     *
     * @param   driverDesc  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  DriverLoaderCreateException  de.cismet.drivermanager.exception.DriverLoaderCreateException
     */
    private DynamicDriverClassLoader createLoaderForDriverDescription(final DriverDescription driverDesc)
            throws DriverLoaderCreateException {
        final Set<File> files = TypeSafeCollections.newHashSet(driverDesc.getJarFiles().size());
        for (final DriverJar dj : driverDesc.getJarFiles()) {
            files.add(dj.getJarFile());
        }
        return new DynamicDriverClassLoader(files.toArray(new File[] {}));
    }
}
