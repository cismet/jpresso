/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project;

import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import java.io.IOException;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;

/**
 * Creates instances of JPressoProjects
 * 
 * @author srichter
 */
public final class JPressoProjectFactory implements org.netbeans.spi.project.ProjectFactory {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    public JPressoProjectFactory() {

    }
    
    /**
     * 
     * @param projectDirectory
     * @return
     */
    public final boolean isProject(final FileObject projectDirectory) {
        return projectDirectory.getFileObject(JPressoFileManager.DIR_JPP) != null;
    }

    /**
     * 
     * @param dir
     * @param state
     * @return
     * @throws java.io.IOException
     */
    public Project loadProject(final FileObject dir, final ProjectState state) throws IOException {
        return isProject(dir) ? new JPressoProject(dir, state) : null;
    }
    
    /**
     * 
     * @param project
     * @throws java.io.IOException
     * @throws java.lang.ClassCastException
     */
    public void saveProject(final Project project) throws IOException, ClassCastException {
//        FileObject projectRoot = project.getProjectDirectory();
//        if (projectRoot.getFileObject(PROJECT_DIR) == null) {
//            throw new IOException("Project dir " + projectRoot.getPath() + " deleted," +
//                    " cannot save project");
//        }
//        //Force creation of the folder dirs if deleted
//        JPressoProject jpro = (JPressoProject) project;
//        jpro.getConnectionFolder(true);
//        jpro.getQueryFolder(true);
//        jpro.getRunFolder(true);
//        jpro.getCodeFolder(true);
//        jpro.getSQLFolder(true);
//
//        //Find the properties file jpproject/project.properties,
//        //creating it if necessary
//        String propsPath = PROJECT_DIR + "/" + PROJECT_PROPFILE;
//        FileObject propertiesFile = projectRoot.getFileObject(propsPath);
//        if (propertiesFile == null) {
//            //Recreate the properties file if needed
//            propertiesFile = projectRoot.createData(propsPath);
//        }
//
//        Properties properties = project.getLookup().lookup(Properties.class);
//
//        File f = FileUtil.toFile(propertiesFile);
//        properties.store(new FileOutputStream(f), "NetBeans JPresso Project Properties");
//    TODO ...more
    }
}
