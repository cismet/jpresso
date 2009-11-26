/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.drivermanager.loading;

import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.core.data.DriverJar;
import de.cismet.jpresso.core.exceptions.DriverLoadException;
import de.cismet.jpresso.core.serviceprovider.exceptions.DriverLoaderCreateException;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.io.File;
import java.lang.reflect.Field;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Maps (driverDescr)File and Classname to Drivers.
 * @author stefan
 */
public class DynamicDriverLoaderManager {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    //a cache, that hits if a driver with a specific name, from a specific DriverDescription is loaded more than once.
    private final Map<DriverDescription, Map<String, Driver>> cache;

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public DynamicDriverLoaderManager() {
        this.cache = TypeSafeCollections.newConcurrentHashMap();
    }

    // </editor-fold>
    /**
     * 
     * @param driverDescr
     * @param name
     * @return
     * @throws de.cismet.drivermanager.exception.DriverLoadException
     * @throws de.cismet.drivermanager.exception.DriverLoaderCreateException
     */
    public Driver loadDriver(final DriverDescription driverDescr, final String name) throws DriverLoadException, DriverLoaderCreateException {
        Map<String, Driver> drivers = cache.get(driverDescr);
        Driver ret = null;
        if (drivers != null) {
            ret = drivers.get(name);
            if (ret != null) {
                log.debug("Cache hit: returning " + ret);
                return ret;
            }
        }
        final DynamicDriverClassLoader loader = createLoaderForDriverDescription(driverDescr);
        if (loader != null) {
            ret = loader.loadDriver(name);
            if (ret != null) {
                if (drivers != null) {
                    log.debug("Driver cache miss, but File there: returning " + ret);
                    drivers.put(name, ret);
                } else {
                    drivers = TypeSafeCollections.newHashMap();
                    drivers.put(name, ret);
                    cache.put(driverDescr, drivers);
                    log.debug("Driver cache miss, and File not there: returning " + ret);
                }
                return ret;
            }
        }
        throw new DriverLoadException("Driver not found!");
    }

    public void clearLoaderManagerCache() {
        //XXX: error-prone perm-gen-leak fix against drivers that load classes on they own against our will...
        try {
            final Field writeDrivers = DriverManager.class.getDeclaredField("writeDrivers");
            final Field readDrivers = DriverManager.class.getDeclaredField("readDrivers");
            writeDrivers.setAccessible(true);
            readDrivers.setAccessible(true);
            synchronized (DriverManager.class) {
                //get write-copy of driver-infos
                final Vector v = (Vector) writeDrivers.get(null);
                boolean done = false;
                boolean modified = false;
                //find all drivers in driver list that are loaded by on of our driverclassloader and delete them
                //(loop on concurrent modifications - which should never happen)
                while (!done) {
                    try {
                        final Iterator<?> it = v.iterator();
                        while (it.hasNext()) {
                            final Object o = it.next();
                            //extract driver-class from driver-info
                            final Field drvClassField = o.getClass().getDeclaredField("driverClass");
                            drvClassField.setAccessible(true);
                            ClassLoader cl = ((Class<?>) drvClassField.get(o)).getClassLoader();
                            //check if there is a driverclassloader in the cl-hierarchy...
                            while (cl != null && !cl.equals(getClass().getClassLoader())) {
                                //if yes: delete
                                if (DynamicDriverClassLoader.instanceOf(cl)) {
                                    if (log.isDebugEnabled()) {
                                        log.debug("Removing driver: " + o);
                                    }
                                    it.remove();
                                    modified = true;
                                    cl = null;
                                    //else: ignore and check further upward in hierachy
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
                //if we modified the write-copy, we now have to adjust the read-copy
                if (modified) {
                    readDrivers.set(null, v.clone());
                }
            }
            writeDrivers.setAccessible(false);
            readDrivers.setAccessible(false);

        } catch (Exception ex) {
            log.error(ex, ex);
        }
        //--------end fix
        cache.clear();
    }

    /**
     * 
     * @param url
     */
    public void removeDriverDescription(final DriverDescription toRemove) {
        cache.remove(toRemove);
    }

    /**
     * 
     * @return
     */
    public Set<DriverDescription> getDriverDescriptions() {
        return this.cache.keySet();
    }

    /**
     * Per default creates a new classloader for every single driver for isolation.
     * Maybe it could cache one classloader for every driverDescr one day?
     * 
     * @param driverDescr
     * @return
     * @throws de.cismet.drivermanager.exception.DriverLoaderCreateException
     */
    private DynamicDriverClassLoader createLoaderForDriverDescription(final DriverDescription driverDesc) throws DriverLoaderCreateException {
        final Set<File> files = TypeSafeCollections.newHashSet(driverDesc.getJarFiles().size());
        for (final DriverJar dj : driverDesc.getJarFiles()) {
            files.add(dj.getJarFile());
        }
        return new DynamicDriverClassLoader(files.toArray(new File[]{}));
    }
}
