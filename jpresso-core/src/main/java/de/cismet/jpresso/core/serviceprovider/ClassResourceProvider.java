/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.serviceprovider;

import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.core.serviceprovider.exceptions.DuplicateEntryException;
import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Provides important resources to obtain certain classes like drivers.
 * 
 * @author srichter
 */
public interface ClassResourceProvider {

    /**
     * 
     * @return a drivermanager
     */
    public DynamicDriverManager getDriverManager();

    /**
     * 
     * @return a dynamic classloader
     */
    public DynamicCompileClassLoader getDynClassLoader();

    /**
     * 
     * @return read-only set of classpath files.
     */
    public Set<File> getProjectClasspath();

    /**
     * 
     * @return read-only list of driver descriptions.
     */
    public List<DriverDescription> getDriverDescriptions();

    /**
     * 
     * @return the project's directory
     */
    public File getProjectDir();
    
    /**
     * 
     * @return the project's code directory
     */
    public File getCodeDir();

    /**
     * Change the project's driver despription list.
     * 
     * @param driverDescriptions
     * @throws de.cismet.jpressocore.exceptions.DuplicateEntryException
     */
    public void changeProjectDriverList(List<DriverDescription> driverDescriptions) throws DuplicateEntryException;

    /**
     * Change the project's classpath.
     * 
     * @param newClassPath
     */
    public void changeProjectClassPath(List<File> newClassPath);

    /**
     * 
     * @return the delegation filter suggested for classloading.
     */
    public String getDelegationFilter();
    
    /**
     * 
     * @return
     */
    public File getProjectPlanFile();
}
