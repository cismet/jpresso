/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.kernel;

import de.cismet.jpresso.core.classloading.DynamicCompileClassLoaderFactory;
import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.core.serviceprovider.exceptions.DuplicateEntryException;
import de.cismet.jpresso.core.drivermanager.DynamicDriverManagerImpl;
import de.cismet.jpresso.core.data.ProjectOptions;
import de.cismet.jpresso.core.serviceprovider.ClassResourceProvider;
import de.cismet.jpresso.core.serviceprovider.DynamicCompileClassLoader;
import de.cismet.jpresso.core.serviceprovider.DynamicDriverManager;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Provides general important resources like dynamic classloader, drivermanager 
 * and the project directory.
 * 
 * @author srichter
 */
public class ClassResourceProviderIml implements ClassResourceProvider {
    //regular expression to find the assigner base's classname
//    public static final String ASSIGNER_BASE_FILTER = "^" + code.AssignerBase.class.getCanonicalName().replaceAll("\\.", "\\\\.") + "$";

    public static final String ASSIGNER_BASE_FILTER = "^" + code.AssignerBase.class.getCanonicalName().replace(".", "\\.") + "$";
    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    //the drivermanager and the classloader
    private final DynamicDriverManager drivermanager;
    private final File projectDir;
    private final File codeDir;
    private final File projectPlan;
    private DynamicCompileClassLoader extClasspathLoader;
    //the current driver descriptions and jars
    private Set<File> projectClasspath;
    private List<DriverDescription> driverDescriptions;

    /**
     * 
     * @param projDir
     */
    public ClassResourceProviderIml(final File projDir) {
        projectPlan = new File(projDir.getAbsolutePath() + File.separator + JPressoFileManager.PROJECT_PLAN);
        projectClasspath = TypeSafeCollections.newConcurrentSkipListSet();
        DynamicDriverManagerImpl tmp = null;
        ProjectOptions po;
        try {
            po = JPressoFileManager.getDefault().load(new File(projDir.getAbsolutePath() + File.separator + JPressoFileManager.PROJECT_OPTIONS), ProjectOptions.class);
        } catch (FileNotFoundException ex) {
            log.error("Can not find file " + JPressoFileManager.PROJECT_OPTIONS + "! Creating new, empty ProjectOptions!", ex);
            po = new ProjectOptions();
        } catch (IOException ex) {
            po = new ProjectOptions();
            log.error("Error while parsing file " + JPressoFileManager.PROJECT_OPTIONS + "! Creating new, empty ProjectOptions!", ex);
        }
        projectClasspath.addAll(po.getAddClassPath());
        //@errorfinder
        projectClasspath.add(projDir);
        driverDescriptions = po.getDriver();
        try {
            tmp = new DynamicDriverManagerImpl(getDriverDescriptions());
        } catch (DuplicateEntryException ex) {
            //should never happen, but handled below...
        }

        if (tmp == null) {
            tmp = new DynamicDriverManagerImpl();
        }
        drivermanager = tmp;
        extClasspathLoader = DynamicCompileClassLoaderFactory.createDynamicClassLoader(projectClasspath, projDir.getAbsolutePath(), true, ASSIGNER_BASE_FILTER);
        this.projectDir = projDir;
        this.codeDir = new File(projDir, JPressoFileManager.DIR_CDE);
    }

    /**
     * 
     * @return a "clean" DynamicClassLoader instance with the current classpath.
     */
    public DynamicCompileClassLoader getDynClassLoader() {
//        never return extClasspathLoader directly! A class can be modified an loaded more than one time.
//        Using the same classloader for different versions of one class would end in a LinkageError.
        return extClasspathLoader.copy();
    }

    /**
     * 
     * @return the dynamic DriverManager.
     */
    public DynamicDriverManager getDriverManager() {
        return this.drivermanager;
    }

    /**
     * 
     * @param newClassPath
     */
    public void changeProjectClassPath(final List<File> newClassPath) {
        final Set<File> tmp = TypeSafeCollections.newConcurrentSkipListSet();
        //@errorfinder
        tmp.add(projectDir);
        for (final File f : newClassPath) {
            tmp.add(f);
        }
        log.debug("Project classpath changed to " + tmp);
        projectClasspath = tmp;
        extClasspathLoader = DynamicCompileClassLoaderFactory.createDynamicClassLoader(tmp, projectDir.getAbsolutePath(), true, ASSIGNER_BASE_FILTER);
    }

    /**
     * 
     * @param driverDescriptions
     * @throws de.cismet.drivermanager.exception.DuplicateEntryException
     */
    public void changeProjectDriverList(final List<DriverDescription> driverDescriptions) throws DuplicateEntryException {
        log.debug("Project driver list changed to " + driverDescriptions);
        this.driverDescriptions = driverDescriptions;
        drivermanager.setDriverDescriptionList(driverDescriptions);
    }

    /**
     * 
     * @return read-only set of classpath files.
     */
    public Set<File> getProjectClasspath() {
        return Collections.unmodifiableSet(projectClasspath);
    }

    /**
     * 
     * @return read-only list of driver descriptions.
     */
    public List<DriverDescription> getDriverDescriptions() {
        return Collections.unmodifiableList(driverDescriptions);
    }

    /**
     * 
     * @return the project directory.
     */
    public File getProjectDir() {
        return projectDir;
    }

    /**
     * 
     * @return a delegation filter for the assignbase
     */
    public String getDelegationFilter() {
        return ASSIGNER_BASE_FILTER;
    }

    /**
     * 
     * @return the project plan file
     */
    public File getProjectPlanFile() {
        return projectPlan;
    }

    public File getCodeDir() {
        return codeDir;
    }
}
