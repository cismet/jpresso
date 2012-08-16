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
package de.cismet.jpresso.project.serviceprovider;

import java.io.File;

import java.util.List;

import de.cismet.jpresso.core.data.DriverDescription;

/**
 * A listener for changes on the projects known-driver list or classpaths.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface ClassResourceListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * Call if the DriverDescriptionList for the DriverManager has changed. List entries must be unique!
     *
     * @param   driverPath  DOCUMENT ME!
     *
     * @throws  Exception  de.cismet.drivermanager.exception.DuplicateEntryException
     */
    void projectDriverListChanged(List<DriverDescription> driverPath) throws Exception;

    /**
     * Call if the list of external classpath jars has changed.
     *
     * @param  newClassPath  DOCUMENT ME!
     */
    void projectClassPathChanged(List<File> newClassPath);
}
