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

import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectState;

import org.openide.filesystems.FileObject;

import java.io.IOException;

import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;

/**
 * Creates instances of JPressoProjects.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class JPressoProjectFactory implements org.netbeans.spi.project.ProjectFactory {

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new JPressoProjectFactory object.
     */
    public JPressoProjectFactory() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   projectDirectory  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean isProject(final FileObject projectDirectory) {
        return projectDirectory.getFileObject(JPressoFileManager.DIR_JPP) != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   dir    DOCUMENT ME!
     * @param   state  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @Override
    public Project loadProject(final FileObject dir, final ProjectState state) throws IOException {
        return isProject(dir) ? new JPressoProject(dir, state) : null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   project  DOCUMENT ME!
     *
     * @throws  IOException         DOCUMENT ME!
     * @throws  ClassCastException  DOCUMENT ME!
     */
    @Override
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
