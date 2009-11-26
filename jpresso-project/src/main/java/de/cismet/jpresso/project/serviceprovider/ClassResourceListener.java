/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.serviceprovider;

import de.cismet.jpresso.core.data.DriverDescription;
import java.io.File;
import java.util.List;

/**
 * A listener for changes on the projects known-driver list or classpaths
 * 
 * @author srichter
 */
public interface ClassResourceListener {

    /**
     * Call if the DriverDescriptionList for the DriverManager has changed. List entries must be unique!
     * 
     * @param driverPath
     * @throws de.cismet.drivermanager.exception.DuplicateEntryException
     */
    public void projectDriverListChanged(List<DriverDescription> driverPath) throws Exception;
    
    /**
     * Call if the list of external classpath jars has changed.
     * 
     * @param newClassPath
     */
    public void projectClassPathChanged(List<File> newClassPath);
}
