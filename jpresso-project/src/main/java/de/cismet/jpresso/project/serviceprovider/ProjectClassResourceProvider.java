/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.serviceprovider;

import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.core.serviceprovider.exceptions.DuplicateEntryException;
import de.cismet.jpresso.core.serviceprovider.ClassResourceProvider;
import de.cismet.jpresso.core.serviceprovider.ClassResourceProviderFactory;
import de.cismet.jpresso.core.serviceprovider.DynamicCompileClassLoader;
import de.cismet.jpresso.core.serviceprovider.DynamicDriverManager;
import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * A ClassResourceProvider that provides resources like dynamic classloader and drivermanager
 * and listens to DriverList- or ExtClassPath-changes and can forward them to other ClassResourceListener.
 * 
 * @author srichter
 */
public final class ProjectClassResourceProvider implements ClassResourceProvider, ClassResourceListener {

    private ClassResourceListener classResListener;
    private final ClassResourceProvider delegate;

    public ProjectClassResourceProvider(final File projDir) {
        delegate = ClassResourceProviderFactory.createClassRessourceProvider(projDir);
        classResListener = null;
    }

    public ProjectClassResourceProvider(final File projDir, final ClassResourceListener crl) {
        delegate = ClassResourceProviderFactory.createClassRessourceProvider(projDir);
        classResListener = crl;
    }

    /**
     * @see de.cismet.serviceprovider.ClassResourceListener
     * 
     * @param driverPath
     * @throws de.cismet.jpressocore.serviceprovider.exceptions.DuplicateEntryException
     */
    public void projectDriverListChanged(final List<DriverDescription> driverPath) throws DuplicateEntryException {
        changeProjectDriverList(driverPath);
    }

    /**
     * @see de.cismet.serviceprovider.ClassResourceListener
     * 
     * @param newClassPath
     */
    public void projectClassPathChanged(final List<File> newClassPath) {
        changeProjectClassPath(newClassPath);
        if (classResListener != null) {
            classResListener.projectClassPathChanged(newClassPath);
        }
    }

    /**
     * 
     * @param classResListener
     */
    public void setClassResourceListener(final ClassResourceListener classResListener) {
        this.classResListener = classResListener;
    }

    /**
     * 
     * @return
     */
    public Set<File> getProjectClasspath() {
        return delegate.getProjectClasspath();
    }
    
    /**
     * 
     * @return
     */
    public DynamicDriverManager getDriverManager() {
        return delegate.getDriverManager();
    }
    
    /**
     * 
     * @return
     */
    public DynamicCompileClassLoader getDynClassLoader() {
        return delegate.getDynClassLoader();
    }
    
    /**
     * 
     * @return
     */
    public File getProjectDir() {
        return delegate.getProjectDir();
    }
    
    /**
     * 
     * @param driverDescriptions
     * @throws de.cismet.jpressocore.serviceprovider.exceptions.DuplicateEntryException
     */
    public void changeProjectDriverList(final List<DriverDescription> driverDescriptions) throws DuplicateEntryException {
        delegate.changeProjectDriverList(driverDescriptions);
    }
    
    /**
     * 
     * @param newClassPath
     */
    public void changeProjectClassPath(final List<File> newClassPath) {
        delegate.changeProjectClassPath(newClassPath);
    }
    
    /**
     * 
     * @return
     */
    public List<DriverDescription> getDriverDescriptions() {
        return delegate.getDriverDescriptions();
    }
    
    /**
     * 
     * @return
     */
    public String getDelegationFilter() {
        return delegate.getDelegationFilter();
    }
    
    /**
     * 
     * @return
     */
    public File getProjectPlanFile() {
        return delegate.getProjectPlanFile();
    }

    public File getCodeDir() {
        return delegate.getCodeDir();
    }
}
