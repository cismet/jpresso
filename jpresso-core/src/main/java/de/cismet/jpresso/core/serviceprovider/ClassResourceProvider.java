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

import java.util.List;
import java.util.Set;

import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.core.serviceprovider.exceptions.DuplicateEntryException;

/**
 * Provides important resources to obtain certain classes like drivers.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface ClassResourceProvider {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  a drivermanager
     */
    DynamicDriverManager getDriverManager();

    /**
     * DOCUMENT ME!
     *
     * @return  a dynamic classloader
     */
    DynamicCompileClassLoader getDynClassLoader();

    /**
     * DOCUMENT ME!
     *
     * @return  read-only set of classpath files.
     */
    Set<File> getProjectClasspath();

    /**
     * DOCUMENT ME!
     *
     * @return  read-only list of driver descriptions.
     */
    List<DriverDescription> getDriverDescriptions();

    /**
     * DOCUMENT ME!
     *
     * @return  the project's directory
     */
    File getProjectDir();

    /**
     * DOCUMENT ME!
     *
     * @return  the project's code directory
     */
    File getCodeDir();

    /**
     * Change the project's driver despription list.
     *
     * @param   driverDescriptions  DOCUMENT ME!
     *
     * @throws  DuplicateEntryException  de.cismet.jpressocore.exceptions.DuplicateEntryException
     */
    void changeProjectDriverList(List<DriverDescription> driverDescriptions) throws DuplicateEntryException;

    /**
     * Change the project's classpath.
     *
     * @param  newClassPath  DOCUMENT ME!
     */
    void changeProjectClassPath(List<File> newClassPath);

    /**
     * DOCUMENT ME!
     *
     * @return  the delegation filter suggested for classloading.
     */
    String getDelegationFilter();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    File getProjectPlanFile();
}
