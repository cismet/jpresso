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
import java.util.Set;

import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.core.serviceprovider.ClassResourceProvider;
import de.cismet.jpresso.core.serviceprovider.ClassResourceProviderFactory;
import de.cismet.jpresso.core.serviceprovider.DynamicCompileClassLoader;
import de.cismet.jpresso.core.serviceprovider.DynamicDriverManager;
import de.cismet.jpresso.core.serviceprovider.exceptions.DuplicateEntryException;

/**
 * A ClassResourceProvider that provides resources like dynamic classloader and drivermanager and listens to DriverList-
 * or ExtClassPath-changes and can forward them to other ClassResourceListener.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class ProjectClassResourceProvider implements ClassResourceProvider, ClassResourceListener {

    //~ Instance fields --------------------------------------------------------

    private ClassResourceListener classResListener;
    private final ClassResourceProvider delegate;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProjectClassResourceProvider object.
     *
     * @param  projDir  DOCUMENT ME!
     */
    public ProjectClassResourceProvider(final File projDir) {
        delegate = ClassResourceProviderFactory.createClassRessourceProvider(projDir);
        classResListener = null;
    }

    /**
     * Creates a new ProjectClassResourceProvider object.
     *
     * @param  projDir  DOCUMENT ME!
     * @param  crl      DOCUMENT ME!
     */
    public ProjectClassResourceProvider(final File projDir, final ClassResourceListener crl) {
        delegate = ClassResourceProviderFactory.createClassRessourceProvider(projDir);
        classResListener = crl;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   driverPath  DOCUMENT ME!
     *
     * @throws  DuplicateEntryException  de.cismet.jpressocore.serviceprovider.exceptions.DuplicateEntryException
     *
     * @see     de.cismet.serviceprovider.ClassResourceListener
     */
    @Override
    public void projectDriverListChanged(final List<DriverDescription> driverPath) throws DuplicateEntryException {
        changeProjectDriverList(driverPath);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  newClassPath  DOCUMENT ME!
     *
     * @see    de.cismet.serviceprovider.ClassResourceListener
     */
    @Override
    public void projectClassPathChanged(final List<File> newClassPath) {
        changeProjectClassPath(newClassPath);
        if (classResListener != null) {
            classResListener.projectClassPathChanged(newClassPath);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  classResListener  DOCUMENT ME!
     */
    public void setClassResourceListener(final ClassResourceListener classResListener) {
        this.classResListener = classResListener;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Set<File> getProjectClasspath() {
        return delegate.getProjectClasspath();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public DynamicDriverManager getDriverManager() {
        return delegate.getDriverManager();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public DynamicCompileClassLoader getDynClassLoader() {
        return delegate.getDynClassLoader();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public File getProjectDir() {
        return delegate.getProjectDir();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   driverDescriptions  DOCUMENT ME!
     *
     * @throws  DuplicateEntryException  de.cismet.jpressocore.serviceprovider.exceptions.DuplicateEntryException
     */
    @Override
    public void changeProjectDriverList(final List<DriverDescription> driverDescriptions)
            throws DuplicateEntryException {
        delegate.changeProjectDriverList(driverDescriptions);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  newClassPath  DOCUMENT ME!
     */
    @Override
    public void changeProjectClassPath(final List<File> newClassPath) {
        delegate.changeProjectClassPath(newClassPath);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public List<DriverDescription> getDriverDescriptions() {
        return delegate.getDriverDescriptions();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String getDelegationFilter() {
        return delegate.getDelegationFilter();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public File getProjectPlanFile() {
        return delegate.getProjectPlanFile();
    }

    @Override
    public File getCodeDir() {
        return delegate.getCodeDir();
    }
}
