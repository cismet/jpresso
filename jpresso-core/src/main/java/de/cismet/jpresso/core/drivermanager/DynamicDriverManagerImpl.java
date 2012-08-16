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
package de.cismet.jpresso.core.drivermanager;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.core.data.DriverJar;
import de.cismet.jpresso.core.drivermanager.loading.DynamicDriverLoaderManager;
import de.cismet.jpresso.core.exceptions.DriverLoadException;
import de.cismet.jpresso.core.serviceprovider.*;
import de.cismet.jpresso.core.serviceprovider.exceptions.DriverLoaderCreateException;
import de.cismet.jpresso.core.serviceprovider.exceptions.DuplicateEntryException;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * Maps DriverDescriptions to Driver with the help of a DynamicDriverLoaderManager:
 *
 * <p>X-Caller-X --[DriverDescription|Alias]--> DynamicDriverManagerImpl --[DriverDescription, DefaultClassName]-->
 * DynamicDriverLoaderManager --[Driver]--> X-Caller-X</p>
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public final class DynamicDriverManagerImpl implements DynamicDriverManager {

    //~ Instance fields --------------------------------------------------------

    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final List<DriverDescription> driverDescriptionList;
    private final DynamicDriverLoaderManager loaderManager;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DynamicDriverManagerImpl object.
     */
    public DynamicDriverManagerImpl() {
        loaderManager = new DynamicDriverLoaderManager();
        this.driverDescriptionList = TypeSafeCollections.newArrayList();
        try {
            setDriverDescriptionList(new ArrayList<DriverDescription>());
        } catch (DuplicateEntryException ex) {
        }
    }

    /**
     * Creates a new DynamicDriverManagerImpl object.
     *
     * @param   ddl  DOCUMENT ME!
     *
     * @throws  DuplicateEntryException  DOCUMENT ME!
     */
    public DynamicDriverManagerImpl(List<DriverDescription> ddl) throws DuplicateEntryException {
        loaderManager = new DynamicDriverLoaderManager();
        if (ddl == null) {
            ddl = TypeSafeCollections.newArrayList();
        }
        this.driverDescriptionList = TypeSafeCollections.newArrayList();
        setDriverDescriptionList(ddl);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   driverDescriptionAlias  DOCUMENT ME!
     * @param   url                     DOCUMENT ME!
     * @param   props                   DOCUMENT ME!
     *
     * @return  a jdbc connection
     *
     * @throws  SQLException            DOCUMENT ME!
     * @throws  ClassNotFoundException  DOCUMENT ME!
     */
    @Override
    public Connection getConnection(final String driverDescriptionAlias, final String url, final Properties props)
            throws SQLException, ClassNotFoundException {
        // First of all...give old DriverManager a try...
        SQLException ex = null;
        Connection ret = null;
        try {
//            for (final Enumeration<Driver> drv = DriverManager.getDrivers(); drv.hasMoreElements() || driver != null;) {
            for (final Enumeration<Driver> drv = DriverManager.getDrivers(); drv.hasMoreElements();) {
                final Driver cur = drv.nextElement();
                if (cur.getClass().getCanonicalName().equals(driverDescriptionAlias)) {
                    if (log.isDebugEnabled()) {
                        log.debug("DynamicDriverManager delegated getDriver() to sql.DriverManager returning driver "
                                    + cur);
                    }
                    ret = cur.connect(url, props);
                    if (log.isDebugEnabled()) {
                        log.debug(
                            "DynamicDriverManager delegated getConnection() to Driver from sql.DriverManager returning connection "
                                    + ret);
                    }
                    return ret;
                }
            }
        } catch (SQLException sqlEx) {
            ex = sqlEx;
        } catch (Throwable t) {
            log.error(t, t);
        }

        Driver driver = null;
        try {
            driver = loadDriver(driverDescriptionAlias);
            if (log.isDebugEnabled()) {
                log.debug("DynamicDriverManager loaded driver" + driver.getClass() + " version: "
                            + driver.getMajorVersion() + "." + driver.getMinorVersion());
            }
            ret = driver.connect(url, props);
            if (log.isDebugEnabled()) {
                log.debug("DynamicDriverManager returns connection: " + ret);
            }
            if (ret == null) {
                throw new IllegalArgumentException("The provided URL '" + url + "' has wrong format for Driver "
                            + driver);
            }
            return ret;
        } catch (DriverLoadException e) {
            if (driver == null) {
                if (ex == null) {
                    throw new ClassNotFoundException(e.toString(), e);
                } else {
                    throw ex;
                }
            } else {
                throw new SQLException("URL has wrong Format for Driver " + driver.getClass());
            }
        } catch (DriverLoaderCreateException e) {
            if (driver == null) {
                throw new ClassNotFoundException("Can not load driver " + driverDescriptionAlias + "!", e);
            } else {
                throw new SQLException("URL has wrong Format for Driver " + driver.getClass(), ex);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   alias  driverDescriptionAlias
     *
     * @return  if found: the driverDescriptionAlias described by a DriverDescription with the passed
     *          driverDescriptionAlias.
     *
     * @throws  DriverLoadException          de.cismet.drivermanager.exception.DriverLoadException
     * @throws  DriverLoaderCreateException  de.cismet.drivermanager.exception.DriverLoaderCreateException
     */
    private Driver loadDriver(final String alias) throws DriverLoadException, DriverLoaderCreateException {
        final DriverDescription dd = findDriverDescriptionForAlias(alias);
        return loadDriver(dd);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   alias  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  DriverLoadException  DOCUMENT ME!
     */
    private DriverDescription findDriverDescriptionForAlias(final String alias) throws DriverLoadException {
        for (final DriverDescription dd : driverDescriptionList) {
            if (dd.getName().equals(alias)) {
                return dd;
            }
        }
        throw new DriverLoadException("Unknown driver alias! Please select a known driver or create a driver for alias "
                    + alias + "!");
    }

    /**
     * DOCUMENT ME!
     *
     * @param   driverDescription  DOCUMENT ME!
     *
     * @return  if found: the driverDescriptionAlias described by driverDescription
     *
     * @throws  DriverLoadException          de.cismet.drivermanager.exception.DriverLoadException
     * @throws  DriverLoaderCreateException  de.cismet.drivermanager.exception.DriverLoaderCreateException
     */
    private Driver loadDriver(final DriverDescription driverDescription) throws DriverLoadException,
        DriverLoaderCreateException {
        // check if the driverDescriptionAlias description is valid...
        Exception e = null;
        if ((driverDescription != null) && driverDescription.isValid()) {
            Driver ret = null;
            // look through all jars until...
            final Iterator<DriverJar> it = driverDescription.getJarFiles().iterator();
            while ((ret == null) && it.hasNext()) {
                try {
                    final DriverJar dJar = it.next();
                    if (dJar.getDriverClassNames().contains(driverDescription.getDefaultClass())) {
                        ret = loaderManager.loadDriver(driverDescription, driverDescription.getDefaultClass());
                    }
                } catch (Exception ex) {
                    e = ex;
                }
            }
            if (ret != null) {
                // ...the searched driverDescriptionAlias is found
                return ret;
            }
        } else {
            throw new DriverLoadException(
                "Invalid driver selected! Please select a valid jar and default driverclass!");
        }
        // ...no driverDescriptionAlias is found
        final String exc = (e == null) ? "" : e.toString();
        throw new DriverLoadException(
            "Can not load driver! Maybe not the real driver class was set as default class, but an unloadable proxy?\n"
                    + exc);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  read-only list of all driver (no matter if valid or not)
     */
    @Override
    public List<DriverDescription> getDriverDescriptionList() {
        return Collections.unmodifiableList(driverDescriptionList);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   driverDescriptionList  DOCUMENT ME!
     *
     * @throws  DuplicateEntryException   de.cismet.drivermanager.manager.DuplicateEntryException
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    @Override
    public void setDriverDescriptionList(final List<DriverDescription> driverDescriptionList)
            throws DuplicateEntryException {
        if (driverDescriptionList == null) {
            throw new IllegalArgumentException("Null value not allowed as DriverDescriptionList!");
        }
        // look for duplicate entries...
        final Set<String> nameCheck = TypeSafeCollections.newHashSet();
        for (final DriverDescription dd : driverDescriptionList) {
            if (!nameCheck.add(dd.getName())) {
                throw new DuplicateEntryException(dd.getName());
            }
        }

//        //look for all jar files known
//        HashSet<File> fileSet = new HashSet<File>();
//        for (DriverDescription dd : driverDescriptionList) {
//            for (DriverJar jar : dd.getJarFiles()) {
//                fileSet.add(jar.getJarFile());
//            }
//        }
//
//        //remove all classloader for no longer existing jars from the classloader manager
//        Collection<File> urls = loaderManager.getDriverDescriptions();
//        for (File cur : urls) {
//            if (!fileSet.contains(cur)) {
//                loaderManager.removeDriverDescription();
//            }
//        }
//        for (final DriverDescription dd : loaderManager.getDriverDescriptions()) {
//            loaderManager.removeDriverDescription(dd);
//        }
        loaderManager.clearLoaderManagerCache();
        this.driverDescriptionList.clear();
        this.driverDescriptionList.addAll(driverDescriptionList);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  read-only list of all valid driver descriptions.
     */
    @Override
    public List<DriverDescription> getValidDrivers() {
        final List<DriverDescription> ret = TypeSafeCollections.newArrayList();
        for (final DriverDescription dd : getDriverDescriptionList()) {
            if ((dd != null) && dd.isValid()) {
                ret.add(dd);
            }
        }
        return Collections.unmodifiableList(ret);
    }
}
