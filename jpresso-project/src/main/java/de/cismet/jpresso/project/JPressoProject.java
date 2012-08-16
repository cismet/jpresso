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
package de.cismet.jpresso.project;

import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.classpath.GlobalPathRegistry;
import org.netbeans.api.java.platform.JavaPlatformManager;
import org.netbeans.api.java.source.SourceUtils;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.java.classpath.ClassPathFactory;
import org.netbeans.spi.java.classpath.ClassPathProvider;
import org.netbeans.spi.java.classpath.support.ClassPathSupport;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.AuxiliaryConfiguration;
import org.netbeans.spi.project.CopyOperationImplementation;
import org.netbeans.spi.project.DeleteOperationImplementation;
import org.netbeans.spi.project.MoveOperationImplementation;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.support.ProjectOperations;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.netbeans.spi.project.ui.support.DefaultProjectOperations;

import org.openide.ErrorManager;
import org.openide.cookies.EditCookie;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.FilterNode;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

import org.w3c.dom.Element;

import java.beans.PropertyChangeListener;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import de.cismet.jpresso.core.utils.URLTools;

import de.cismet.jpresso.project.codescan.AutoCompleteMethodProvider;
import de.cismet.jpresso.project.filetypes.AntHandler;
import de.cismet.jpresso.project.filetypes.cookies.RunCookie;
import de.cismet.jpresso.project.serviceprovider.ClassResourceListener;
import de.cismet.jpresso.project.serviceprovider.ProjectClassResourceProvider;

/**
 * This class defines the Project itself, providing a lot of information and actions (like close, move, rename, start
 * run, paths for autocomplete,...) in the nested classes that are available over the project's lookup.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class JPressoProject implements Project,
    DeleteOperationImplementation,
    CopyOperationImplementation,
    MoveOperationImplementation {

    //~ Static fields/initializers ---------------------------------------------

    private static final String[] SUPPORTED_ACTIONS = new String[] {
            ActionProvider.COMMAND_RUN,
            ActionProvider.COMMAND_DELETE,
            ActionProvider.COMMAND_COPY,
            ActionProvider.COMMAND_MOVE,
            ActionProvider.COMMAND_RUN_SINGLE,
            ActionProvider.COMMAND_COMPILE_SINGLE,
            ActionProvider.COMMAND_RENAME
        };

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final FileObject projectDir;
    private final Lookup lkp;
    private final LogicalViewProvider logicalView = new JPressoLogicalView(this);
    private final ProjectState state;
    private final ProjectClassResourceProvider classResProvider;
    private final JPClassPathProviderImpl classPathProvider;
    private FileChangeAdapter projectDeletedListener;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new JPressoProject object.
     *
     * @param  projectDir  DOCUMENT ME!
     * @param  state       DOCUMENT ME!
     */
    public JPressoProject(final FileObject projectDir, final ProjectState state) {
        this.projectDir = projectDir;
        this.state = state;
        initProjectDirs();
        this.classResProvider = new ProjectClassResourceProvider(FileUtil.toFile(projectDir));
        this.classPathProvider = new JPClassPathProviderImpl();
        this.classResProvider.setClassResourceListener(classPathProvider);
        this.lkp = Lookups.fixed(
                new Object[] {
                    this,                                                                                    // Project spec requires a project be in its own lookup
                    state,                                                                                   // Allow outside code to mark the project as needing saving
                    loadProperties(),                                                                        // The project properties
                    logicalView,                                                                             // Logical view of project implementation
                    classResProvider,                                                                        // Provides the classloaders for dynamic compilation and the dynamic jdbc drivermanager
                    classPathProvider,                                                                       // Provides paths for autocomplete etc
                    new ActionProviderImpl(),                                                                // Can provide project-wide standard actions like Run, Build and Clean
                    new JPInfo(),                                                                            // Project information implementation
                    new AuxiliaryConfigurationImpl(),                                                        // only here because the spec requires it
                    new JPProjectOpenedHook(),                                                               // Describes actions on opening/closing this project
                    new AutoCompleteMethodProvider(projectDir.getFileObject(JPressoFileManager.DIR_CDE, "")) // Contains a list of the public-static methods detected in classes in the code-directory
                    // ,...
                });
    }
    // <editor-fold defaultstate="collapsed" desc="Directory Getter">

    //~ Methods ----------------------------------------------------------------

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    // </editor-fold>
    @Override
    public Lookup getLookup() {
        return lkp;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getName() {
        return getProjectDirectory().getName();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getDisplayName() {
        return getName();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public FileObject getBuildXML() {
        return projectDir.getFileObject("/" + JPressoFileManager.BUILD_XML);
    }

    /**
     * Check if all project directories exist and create missing directories.
     */
    private void initProjectDirs() {
        final String pd = FileUtil.toFile(projectDir).getAbsolutePath() + File.separator;
        final File con = new File(pd + JPressoFileManager.DIR_CON);
        // FileUtil.createFolder(con);
        con.mkdirs();
        final File qry = new File(pd + JPressoFileManager.DIR_QRY);
        qry.mkdirs();
        final File run = new File(pd + JPressoFileManager.DIR_RUN);
        run.mkdirs();
        final File sql = new File(pd + JPressoFileManager.DIR_SQL);
        sql.mkdirs();
        final File cde = new File(pd + JPressoFileManager.DIR_CDE);
        cde.mkdirs();
        final File prj = new File(pd + JPressoFileManager.DIR_JPP);
        prj.mkdirs();
        // -----
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Properties loadProperties() {
        final FileObject fob = projectDir.getFileObject(JPressoFileManager.DIR_JPP + "/"
                        + JPressoFileManager.PROJECT_PROPS);
        final Properties properties = new NotifyProperties(state);
        if (fob != null) {
            try {
                properties.load(fob.getInputStream());
            } catch (Exception e) {
                ErrorManager.getDefault().notify(e);
            }
        }
        // wenn nicht da, erzeugen!!!
        return properties;
    }

    /**
     * @see  org.netbeans.spi.project.DeleteOperationImplementation
     */
    @Override
    public void notifyDeleting() throws IOException {
        if (projectDeletedListener != null) {
            getProjectDirectory().getParent().removeFileChangeListener(this.projectDeletedListener);
        }
        ProjectManager.getDefault().saveProject(this);
    }

    /**
     * @see  org.netbeans.spi.project.DeleteOperationImplementation
     */
    @Override
    public void notifyDeleted() throws IOException {
        state.notifyDeleted();
    }

    /**
     * @see  org.netbeans.spi.project.DataFilesProviderImplementation dummy impl of mandatory function.
     */
    @Override
    public List<FileObject> getMetadataFiles() {
        final List<FileObject> fl = TypeSafeCollections.newArrayList();
        return fl;
    }

    /**
     * @see  org.netbeans.spi.project.DataFilesProviderImplementation
     */
    @Override
    public List<FileObject> getDataFiles() {
        final List<FileObject> files = TypeSafeCollections.newArrayList();
        for (final FileObject fo : projectDir.getChildren()) {
            files.add(fo);
        }
        return files;
    }

    @Override
    public void notifyCopying() throws IOException {
        // ignore
    }

    @Override
    public void notifyCopied(final Project arg0, final File arg1, final String arg2) throws IOException {
        // ignore
    }

    @Override
    public void notifyMoving() throws IOException {
        // ignore
    }

    @Override
    public void notifyMoved(final Project arg0, final File arg1, final String arg2) throws IOException {
        // ignore
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Subclass Properties to notify and set Project status changed if "put" is called TODO needed??
     *
     * @version  $Revision$, $Date$
     */
    private static class NotifyProperties extends Properties {

        //~ Instance fields ----------------------------------------------------

        private final ProjectState state;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new NotifyProperties object.
         *
         * @param  state  DOCUMENT ME!
         */
        NotifyProperties(final ProjectState state) {
            this.state = state;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public Object put(final Object key, final Object val) {
            final Object result = super.put(key, val);
            return result;
        }
    }

    /**
     * this project's supported actions.
     *
     * @version  $Revision$, $Date$
     */
    private final class ActionProviderImpl implements ActionProvider {

        //~ Methods ------------------------------------------------------------

        @Override
        public String[] getSupportedActions() {
            return SUPPORTED_ACTIONS;
        }

        @Override
        public void invokeAction(final String action, final Lookup lookup) throws IllegalArgumentException {
            if (COMMAND_DELETE.equals(action)) {
                DefaultProjectOperations.performDefaultDeleteOperation(JPressoProject.this);
            } else if (COMMAND_COPY.equals(action)) {
                DefaultProjectOperations.performDefaultCopyOperation(JPressoProject.this);
            } else if (COMMAND_RENAME.equals(action)) {
                DefaultProjectOperations.performDefaultRenameOperation(JPressoProject.this, getName());
            } else if (COMMAND_MOVE.equals(action)) {
                DefaultProjectOperations.performDefaultMoveOperation(JPressoProject.this);
            } else if (COMMAND_RUN.equals(action)) {
                AntHandler.startProject(getBuildXML());
            } else if (COMMAND_RUN_SINGLE.equals(action)) {
                final Collection<? extends FilterNode> activeNodes = Utilities.actionsGlobalContext()
                            .lookupAll(FilterNode.class);
                for (final FilterNode activeNode : activeNodes) {
                    if ((activeNode != null) && (activeNode.getLookup() != null)) {
                        final Lookup l = activeNode.getLookup();
                        final DataObject dob = l.lookup(DataObject.class);
                        if (dob != null) {
                            final RunCookie rc = dob.getCookie(RunCookie.class);
                            if (rc != null) {
                                AntHandler.startSingleRun(getBuildXML(), dob);
                            } else {
                                if (dob.getPrimaryFile().getExt().equalsIgnoreCase(JPressoFileManager.END_JAVA)) {
                                    AntHandler.startJava(getBuildXML(), dob);
                                }
                            }
                        }
                    }
                }
            } else if (COMMAND_COMPILE_SINGLE.equals(action)) {
                final Collection<DataObject> filtered = TypeSafeCollections.newHashSet();
                final Collection<? extends FilterNode> activeNodes = Utilities.actionsGlobalContext()
                            .lookupAll(FilterNode.class);
                for (final FilterNode activeNode : activeNodes) {
                    if ((activeNode != null) && (activeNode.getLookup() != null)) {
                        final Lookup l = activeNode.getLookup();
                        final Collection<? extends DataObject> selection = l.lookupAll(DataObject.class);
                        if (selection != null) {
                            final FileObject codeDir = projectDir.getFileObject(JPressoFileManager.DIR_CDE);
                            for (final DataObject cur : selection) {
                                if (cur.getPrimaryFile().getParent().equals(codeDir)
                                            && cur.getPrimaryFile().getExt().toLowerCase().equals(
                                                JPressoFileManager.END_JAVA)) {
                                    filtered.add(cur);
                                }
                            }
                        }
                    }
                }
                if (!filtered.isEmpty()) {
                    AntHandler.startCompile(getBuildXML(), filtered);
                }
            }
        }

        @Override
        public boolean isActionEnabled(final String action, final Lookup lookup) throws IllegalArgumentException {
            if (COMMAND_RUN_SINGLE.equals(action)) {
                final Collection<? extends FilterNode> activeNodes = Utilities.actionsGlobalContext()
                            .lookupAll(FilterNode.class);
                if ((activeNodes != null) && (activeNodes.size() == 1)) {
                    final FilterNode node = activeNodes.iterator().next();
                    final RunCookie rc = node.getCookie(RunCookie.class);
                    if (rc != null) {
                        return true;
                    } else {
                        final DataObject dob = node.getLookup().lookup(DataObject.class);
                        if ((dob != null) && (dob.getCookie(EditCookie.class) != null)) {
                            final FileObject fo = dob.getPrimaryFile();
                            if ((fo != null) && fo.isValid() && !fo.isVirtual()) {
                                try {
                                    return !SourceUtils.getMainClasses(fo).isEmpty();
                                } catch (IllegalArgumentException iex) {
                                    if (log.isDebugEnabled()) {
                                        log.debug(iex, iex);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Implementation of project system's ProjectInformation class.
     *
     * @version  $Revision$, $Date$
     */
    private final class JPInfo implements ProjectInformation {

        //~ Methods ------------------------------------------------------------

        @Override
        public Icon getIcon() {
            return new ImageIcon(Utilities.loadImage(
                        "de/cismet/jpresso/project/res/launch.png"));
        }

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public void addPropertyChangeListener(final PropertyChangeListener pcl) {
            // do nothing, won't change
        }

        @Override
        public void removePropertyChangeListener(final PropertyChangeListener pcl) {
            // do nothing, won't change
        }

        @Override
        public Project getProject() {
            return JPressoProject.this;
        }
    }

    /**
     * never needed, but the specs demand it in project lookups!
     *
     * @version  $Revision$, $Date$
     */
    private static final class AuxiliaryConfigurationImpl implements AuxiliaryConfiguration {

        //~ Methods ------------------------------------------------------------

        @Override
        public Element getConfigurationFragment(final String elementName,
                final String namespace,
                final boolean shared) {
            // dummy impl
            return null;
        }

        @Override
        public void putConfigurationFragment(final Element fragment, final boolean shared)
                throws IllegalArgumentException {
            // dummy impl
        }

        @Override
        public boolean removeConfigurationFragment(final String elementName,
                final String namespace,
                final boolean shared) throws IllegalArgumentException {
            // dummy impl
            return false;
        }
    }

    /**
     * Provides a classpaths for overall full project java editor support :-).
     *
     * @version  $Revision$, $Date$
     */
    private final class JPClassPathProviderImpl implements ClassPathProvider, ClassResourceListener {

        //~ Instance fields ----------------------------------------------------

        // Boot Classpath
        private final ClassPath boot;
        // Source Classpath
        private final ClassPath source;
        // Compile Classpath
        private final ClassPath compile;
        // Holds reference for a changeable source CP
        private final JPClassPathImplementation jpCompile;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new JPClassPathProviderImpl object.
         */
        public JPClassPathProviderImpl() {
            this.boot = JavaPlatformManager.getDefault().getDefaultPlatform().getBootstrapLibraries();
//            this.source = ClassPathFactory.createClassPath(new JPClassPathImplementation(getProjectDirectory()));
            this.source = ClassPathSupport.createClassPath(getProjectDirectory());
            this.jpCompile = new JPClassPathImplementation(processClassPathFiles(
                        classResProvider.getProjectClasspath()));
            this.compile = ClassPathFactory.createClassPath(jpCompile);
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  changeable source classpath
         */
        public JPClassPathImplementation getCompileClassPath() {
            return jpCompile;
        }

        /**
         * @see  org.netbeans.spi.java.classpath.ClassPathProvider
         */
        @Override
        public org.netbeans.api.java.classpath.ClassPath findClassPath(final FileObject file, final String type) {
            if (type.equals(ClassPath.BOOT)) {
                return boot;
            }
            if (type.equals(ClassPath.SOURCE)) {
                return source;
            } else if (type.equals(ClassPath.COMPILE)) {
                return compile;
            } else {
                return null;
            }
        }

        /**
         * Converts a collection of files to an array of file objects and takes care of putting some important resources
         * on the classpath. (like the jpressocore.jar)
         *
         * @param   ext  additional external classpath files
         *
         * @return  prepared and complete classpath as FileObject[]
         */
        public FileObject[] processClassPathFiles(final Collection<File> ext) {
            // ----------Find the jpressocore.jar for all mandatory classes------
            final Class<?> cl = JPressoFileManager.class;
            final URL u = JPressoFileManager.locateJarForClass(cl);
//            final String path = u.getFile();
//            final String path = u.getPath();
            // ----------Create FileObjects for the classpath files--------------
            final List<FileObject> extFo = TypeSafeCollections.newArrayList(ext.size());
            for (final File cur : ext) {
                // if resource is a jar...
                if (cur.isFile()) {
                    extFo.add(FileUtil.getArchiveRoot(FileUtil.toFileObject(cur)));
                    // if resource is a directory...
                } else if (cur.isDirectory()) {
                    extFo.add(FileUtil.toFileObject(cur));
                }
            }
//            File f;
//            URI uri;
//            try {
//                uri = new URI(path.toString().substring(0,path.toString().length()-2));
//            } catch (URISyntaxException ex) {
//                uri = null;
//            }
//            f = new File(uri);
//            System.out.println(">>> " + f + "  " + f.exists());
            // -----------unite the resources in an array------------------------
            final FileObject[] res = extFo.toArray(new FileObject[extFo.size() + 1]);
            res[extFo.size()] = FileUtil.getArchiveRoot(FileUtil.toFileObject(URLTools.convertURLToFile(u)));
//            res[extFo.size()] = FileUtil.getArchiveRoot(FileUtil.toFileObject(new File(path)));
            return res;
        }

        /**
         * @see  de.cismet.serviceprovider.ClassResourceListener
         */
        @Override
        public void projectDriverListChanged(final List<DriverDescription> driverPath) throws Exception {
            // not interesting here...
        }

        /**
         * @see  de.cismet.serviceprovider.ClassResourceListener
         */
        @Override
        public void projectClassPathChanged(final List<File> newClassPath) {
            jpCompile.setRoots(processClassPathFiles(newClassPath));
        }
    }

    /**
     * Tasks to perform on opening/closing the project: register Classpaths. (Registered classpaths e.g. used by "fix
     * imports")
     *
     * @version  $Revision$, $Date$
     */
    private final class JPProjectOpenedHook extends ProjectOpenedHook {

        //~ Methods ------------------------------------------------------------

        /**
         * @see  org.netbeans.spi.project.ui.ProjectOpenedHook
         */
        @Override
        protected void projectOpened() {
            // register classpaths
            GlobalPathRegistry.getDefault()
                    .register(
                        ClassPath.SOURCE,
                        new ClassPath[] { classPathProvider.findClassPath(getProjectDirectory(), ClassPath.SOURCE) });
            GlobalPathRegistry.getDefault()
                    .register(
                        ClassPath.COMPILE,
                        new ClassPath[] { classPathProvider.findClassPath(getProjectDirectory(), ClassPath.COMPILE) });
            getLookup().lookup(AutoCompleteMethodProvider.class).refresh();
            // add delete listener to project dir
            JPressoProject.this.projectDeletedListener = new FileChangeAdapter() {

                    @Override
                    public void fileDeleted(final FileEvent fe) {
                        // close project, if project directory is deleted or renamed
                        if (fe.getFile().getPath().equals(projectDir.getPath())) {
                            final String message = ("Project directory " + fe.getFile().getPath()
                                            + " deleted!\nClosing project.");
                            log.error(message);
                            try {
                                ProjectOperations.notifyDeleted(JPressoProject.this);
//                    final NotifyDescriptor err = new NotifyDescriptor.Exception(null, message);
//                    DialogDisplayer.getDefault().notify(err);
//                    OpenProjects.getDefault().close(new Project[]{JPressoProject.this});
                            } catch (IOException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        }
                    }
                };
            getProjectDirectory().getParent().addFileChangeListener(JPressoProject.this.projectDeletedListener);
        }

        /**
         * @see  org.netbeans.spi.project.ui.ProjectOpenedHook
         */
        @Override
        protected void projectClosed() {
            // deregister classpaths
            GlobalPathRegistry.getDefault()
                    .unregister(
                        ClassPath.SOURCE,
                        new ClassPath[] { classPathProvider.findClassPath(getProjectDirectory(), ClassPath.SOURCE) });
            GlobalPathRegistry.getDefault()
                    .unregister(
                        ClassPath.COMPILE,
                        new ClassPath[] { classPathProvider.findClassPath(getProjectDirectory(), ClassPath.COMPILE) });
            // remove delete listener from project dir
            getLookup().lookup(AutoCompleteMethodProvider.class).unregisterListeners();
            if (JPressoProject.this.projectDeletedListener != null) {
                getProjectDirectory().getParent().removeFileChangeListener(JPressoProject.this.projectDeletedListener);
            }
        }
    }
}
