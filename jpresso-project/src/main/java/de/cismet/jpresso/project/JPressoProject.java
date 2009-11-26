/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project;

import de.cismet.jpresso.project.serviceprovider.ClassResourceListener;
import de.cismet.jpresso.project.serviceprovider.ProjectClassResourceProvider;
import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.project.codescan.AutoCompleteMethodProvider;
import de.cismet.jpresso.core.utils.URLTools;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import de.cismet.jpresso.project.filetypes.cookies.RunCookie;
import de.cismet.jpresso.project.filetypes.AntHandler;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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

/**
 * This class defines the Project itself, providing a lot of information and
 * actions (like close, move, rename, start run, paths for autocomplete,...)
 * in the nested classes that are available over the project's lookup.
 * 
 * @author srichter
 */
public final class JPressoProject implements Project, DeleteOperationImplementation, CopyOperationImplementation, MoveOperationImplementation {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final FileObject projectDir;
    private final Lookup lkp;
    private final LogicalViewProvider logicalView = new JPressoLogicalView(this);
    private final ProjectState state;
    private final ProjectClassResourceProvider classResProvider;
    private final JPClassPathProviderImpl classPathProvider;
    private FileChangeAdapter projectDeletedListener;
    private static final String[] SUPPORTED_ACTIONS = new String[]{ActionProvider.COMMAND_RUN, ActionProvider.COMMAND_DELETE, ActionProvider.COMMAND_COPY, ActionProvider.COMMAND_MOVE, ActionProvider.COMMAND_RUN_SINGLE, ActionProvider.COMMAND_COMPILE_SINGLE, ActionProvider.COMMAND_RENAME};

    public JPressoProject(final FileObject projectDir, final ProjectState state) {
        this.projectDir = projectDir;
        this.state = state;
        initProjectDirs();
        this.classResProvider = new ProjectClassResourceProvider(FileUtil.toFile(projectDir));
        this.classPathProvider = new JPClassPathProviderImpl();
        this.classResProvider.setClassResourceListener(classPathProvider);
        this.lkp = Lookups.fixed(new Object[]{
                    this, //Project spec requires a project be in its own lookup
                    state, //Allow outside code to mark the project as needing saving
                    loadProperties(), //The project properties
                    logicalView, //Logical view of project implementation
                    classResProvider,//Provides the classloaders for dynamic compilation and the dynamic jdbc drivermanager
                    classPathProvider,//Provides paths for autocomplete etc
                    new ActionProviderImpl(), //Can provide project-wide standard actions like Run, Build and Clean
                    new JPInfo(), //Project information implementation
                    new AuxiliaryConfigurationImpl(),//only here because the spec requires it
                    new JPProjectOpenedHook(),//Describes actions on opening/closing this project
                    new AutoCompleteMethodProvider(projectDir.getFileObject(JPressoFileManager.DIR_CDE, ""))//Contains a list of the public-static methods detected in classes in the code-directory
                //,...
                });
    }
    // <editor-fold defaultstate="collapsed" desc="Directory Getter">

    public FileObject getProjectDirectory() {
        return projectDir;
    }

    // </editor-fold>
    public Lookup getLookup() {
        return lkp;
    }

    public String getName() {
        return getProjectDirectory().getName();
    }

    public String getDisplayName() {
        return getName();
    }

    public FileObject getBuildXML() {
        return projectDir.getFileObject("/" + JPressoFileManager.BUILD_XML);
    }

    /**
     * Check if all project directories exist and create missing directories.
     */
    private void initProjectDirs() {
        final String pd = FileUtil.toFile(projectDir).getAbsolutePath() + File.separator;
        final File con = new File(pd + JPressoFileManager.DIR_CON);
        //FileUtil.createFolder(con);
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
        //-----
    }

    private Properties loadProperties() {
        final FileObject fob = projectDir.getFileObject(JPressoFileManager.DIR_JPP + "/" + JPressoFileManager.PROJECT_PROPS);
        final Properties properties = new NotifyProperties(state);
        if (fob != null) {
            try {
                properties.load(fob.getInputStream());
            } catch (Exception e) {
                ErrorManager.getDefault().notify(e);
            }
        }
        //wenn nicht da, erzeugen!!!
        return properties;
    }

    /**
     * Subclass Properties to notify and set Project status changed if "put" is called
     * TODO needed??
     */
    private static class NotifyProperties extends Properties {

        private final ProjectState state;

        NotifyProperties(final ProjectState state) {
            this.state = state;
        }

        @Override
        public Object put(final Object key, final Object val) {
            final Object result = super.put(key, val);
            return result;
        }
    }

    /**
     * this project's supported actions
     */
    private final class ActionProviderImpl implements ActionProvider {

        public String[] getSupportedActions() {
            return SUPPORTED_ACTIONS;
        }

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
                final Collection<? extends FilterNode> activeNodes = Utilities.actionsGlobalContext().lookupAll(FilterNode.class);
                for (final FilterNode activeNode : activeNodes) {
                    if (activeNode != null && activeNode.getLookup() != null) {
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
                final Collection<? extends FilterNode> activeNodes = Utilities.actionsGlobalContext().lookupAll(FilterNode.class);
                for (final FilterNode activeNode : activeNodes) {
                    if (activeNode != null && activeNode.getLookup() != null) {
                        final Lookup l = activeNode.getLookup();
                        final Collection<? extends DataObject> selection = l.lookupAll(DataObject.class);
                        if (selection != null) {
                            final FileObject codeDir = projectDir.getFileObject(JPressoFileManager.DIR_CDE);
                            for (final DataObject cur : selection) {
                                if (cur.getPrimaryFile().getParent().equals(codeDir) && cur.getPrimaryFile().getExt().toLowerCase().equals(JPressoFileManager.END_JAVA)) {
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

        public boolean isActionEnabled(final String action, final Lookup lookup) throws IllegalArgumentException {
            if (COMMAND_RUN_SINGLE.equals(action)) {
                final Collection<? extends FilterNode> activeNodes = Utilities.actionsGlobalContext().lookupAll(FilterNode.class);
                if (activeNodes != null && activeNodes.size() == 1) {
                    FilterNode node = activeNodes.iterator().next();
                    final RunCookie rc = node.getCookie(RunCookie.class);
                    if (rc != null) {
                        return true;
                    } else {
                        final DataObject dob = node.getLookup().lookup(DataObject.class);
                        if (dob != null && dob.getCookie(EditCookie.class) != null) {
                            final FileObject fo = dob.getPrimaryFile();
                            if (fo != null && fo.isValid() && !fo.isVirtual()) {
                                try {
                                    return !SourceUtils.getMainClasses(fo).isEmpty();
                                } catch (IllegalArgumentException iex) {
                                    log.debug(iex, iex);
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
     * Implementation of project system's ProjectInformation class 
     */
    private final class JPInfo implements ProjectInformation {

        public Icon getIcon() {
            return new ImageIcon(Utilities.loadImage(
                    "de/cismet/jpresso/project/res/launch.png"));
        }

        public String getName() {
            return getProjectDirectory().getName();
        }

        public String getDisplayName() {
            return getName();
        }

        public void addPropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        public void removePropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        public Project getProject() {
            return JPressoProject.this;
        }
    }

    /**
     * never needed, but the specs demand it in project lookups!
     */
    private static final class AuxiliaryConfigurationImpl implements AuxiliaryConfiguration {

        public Element getConfigurationFragment(String elementName, String namespace, boolean shared) {
            //dummy impl
            return null;
        }

        public void putConfigurationFragment(Element fragment, boolean shared) throws IllegalArgumentException {
            //dummy impl
        }

        public boolean removeConfigurationFragment(String elementName, String namespace, boolean shared) throws IllegalArgumentException {
            //dummy impl
            return false;
        }
    }

    /**
     * Provides a classpaths for overall full project java editor support :-)
     */
    private final class JPClassPathProviderImpl implements ClassPathProvider, ClassResourceListener {

        //Boot Classpath
        private final ClassPath boot;
        //Source Classpath
        private final ClassPath source;
        //Compile Classpath
        private final ClassPath compile;
        //Holds reference for a changeable source CP
        private final JPClassPathImplementation jpCompile;

        public JPClassPathProviderImpl() {
            this.boot = JavaPlatformManager.getDefault().getDefaultPlatform().getBootstrapLibraries();
//            this.source = ClassPathFactory.createClassPath(new JPClassPathImplementation(getProjectDirectory()));
            this.source = ClassPathSupport.createClassPath(getProjectDirectory());
            this.jpCompile = new JPClassPathImplementation(processClassPathFiles(classResProvider.getProjectClasspath()));
            this.compile = ClassPathFactory.createClassPath(jpCompile);
        }

        /**
         * 
         * @return changeable source classpath
         */
        public JPClassPathImplementation getCompileClassPath() {
            return jpCompile;
        }

        /**
         * @see org.netbeans.spi.java.classpath.ClassPathProvider
         */
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
         * Converts a collection of files to an array of file objects and
         * takes care of putting some important resources on the classpath.
         * (like the jpressocore.jar)
         * 
         * @param additional external classpath files
         * @return prepared and complete classpath as FileObject[]
         */
        public FileObject[] processClassPathFiles(final Collection<File> ext) {
            //----------Find the jpressocore.jar for all mandatory classes------
            final Class<?> cl = JPressoFileManager.class;
            final URL u = JPressoFileManager.locateJarForClass(cl);
//            final String path = u.getFile();
//            final String path = u.getPath();
            //----------Create FileObjects for the classpath files--------------
            final List<FileObject> extFo = TypeSafeCollections.newArrayList(ext.size());
            for (final File cur : ext) {
                //if resource is a jar...
                if (cur.isFile()) {
                    extFo.add(FileUtil.getArchiveRoot(FileUtil.toFileObject(cur)));
                    //if resource is a directory...
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
            //-----------unite the resources in an array------------------------
            final FileObject[] res = extFo.toArray(new FileObject[extFo.size() + 1]);
            res[extFo.size()] = FileUtil.getArchiveRoot(FileUtil.toFileObject(URLTools.convertURLToFile(u)));
//            res[extFo.size()] = FileUtil.getArchiveRoot(FileUtil.toFileObject(new File(path)));
            return res;
        }

        /**
         * @see de.cismet.serviceprovider.ClassResourceListener
         */
        public void projectDriverListChanged(final List<DriverDescription> driverPath) throws Exception {
            //not interesting here...
        }

        /**
         * @see de.cismet.serviceprovider.ClassResourceListener      
         */
        public void projectClassPathChanged(final List<File> newClassPath) {
            jpCompile.setRoots(processClassPathFiles(newClassPath));
        }
    }

    /**
     * @see org.netbeans.spi.project.DeleteOperationImplementation
     */
    public void notifyDeleting() throws IOException {
        if (projectDeletedListener != null) {
            getProjectDirectory().getParent().removeFileChangeListener(this.projectDeletedListener);
        }
        ProjectManager.getDefault().saveProject(this);
    }

    /**
     * @see org.netbeans.spi.project.DeleteOperationImplementation
     */
    public void notifyDeleted() throws IOException {
        state.notifyDeleted();
    }

    /**
     * @see org.netbeans.spi.project.DataFilesProviderImplementation
     * dummy impl of mandatory function.
     */
    public List<FileObject> getMetadataFiles() {
        final List<FileObject> fl = TypeSafeCollections.newArrayList();
        return fl;
    }

    /**
     * @see org.netbeans.spi.project.DataFilesProviderImplementation
     */
    public List<FileObject> getDataFiles() {
        final List<FileObject> files = TypeSafeCollections.newArrayList();
        for (final FileObject fo : projectDir.getChildren()) {
            files.add(fo);
        }
        return files;
    }

    public void notifyCopying() throws IOException {
        //ignore
    }

    public void notifyCopied(Project arg0, File arg1, String arg2) throws IOException {
        //ignore
    }

    public void notifyMoving() throws IOException {
        //ignore
    }

    public void notifyMoved(Project arg0, File arg1, String arg2) throws IOException {
        //ignore
    }

    /**
     * Tasks to perform on opening/closing the project: register Classpaths.
     * (Registered classpaths e.g. used by "fix imports")
     */
    private final class JPProjectOpenedHook extends ProjectOpenedHook {

        /**
         * @see org.netbeans.spi.project.ui.ProjectOpenedHook
         */
        @Override
        protected void projectOpened() {
            //register classpaths
            GlobalPathRegistry.getDefault().register(ClassPath.SOURCE, new ClassPath[]{classPathProvider.findClassPath(getProjectDirectory(), ClassPath.SOURCE)});
            GlobalPathRegistry.getDefault().register(ClassPath.COMPILE, new ClassPath[]{classPathProvider.findClassPath(getProjectDirectory(), ClassPath.COMPILE)});
            getLookup().lookup(AutoCompleteMethodProvider.class).refresh();
            //add delete listener to project dir
            JPressoProject.this.projectDeletedListener = new FileChangeAdapter() {

                @Override
                public void fileDeleted(final FileEvent fe) {
                    //close project, if project directory is deleted or renamed
                    if (fe.getFile().getPath().equals(projectDir.getPath())) {
                        final String message = ("Project directory " + fe.getFile().getPath() + " deleted!\nClosing project.");
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
         * @see org.netbeans.spi.project.ui.ProjectOpenedHook
         */
        @Override
        protected void projectClosed() {
            //deregister classpaths
            GlobalPathRegistry.getDefault().unregister(ClassPath.SOURCE, new ClassPath[]{classPathProvider.findClassPath(getProjectDirectory(), ClassPath.SOURCE)});
            GlobalPathRegistry.getDefault().unregister(ClassPath.COMPILE, new ClassPath[]{classPathProvider.findClassPath(getProjectDirectory(), ClassPath.COMPILE)});
            //remove delete listener from project dir
            getLookup().lookup(AutoCompleteMethodProvider.class).unregisterListeners();
            if (JPressoProject.this.projectDeletedListener != null) {
                getProjectDirectory().getParent().removeFileChangeListener(JPressoProject.this.projectDeletedListener);
            }
        }
    }
}
