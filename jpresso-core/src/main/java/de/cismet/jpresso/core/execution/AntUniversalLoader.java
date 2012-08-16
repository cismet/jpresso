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
package de.cismet.jpresso.core.execution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.cismet.jpresso.core.data.DatabaseConnection;
import de.cismet.jpresso.core.data.JPLoadable;
import de.cismet.jpresso.core.data.JPressoRun;
import de.cismet.jpresso.core.data.SQLRun;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;

/**
 * A class that loads ant executable objects from file.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
final class AntUniversalLoader {

    //~ Static fields/initializers ---------------------------------------------

    public static final JPressoFileManager FILEMANAGER = JPressoFileManager.getDefault();

    //~ Instance fields --------------------------------------------------------

    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(getClass());
    private final String projectDir;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AntUniversalLoader object.
     *
     * @param  projectDir  DOCUMENT ME!
     */
    public AntUniversalLoader(final String projectDir) {
        this.projectDir = projectDir;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Loads an AntExecutableInterface implementing objects from the file named like the parameter in the project known
     * to this.
     *
     * @param   pureFileNameWithExt  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  FileNotFoundException     DOCUMENT ME!
     * @throws  IOException               DOCUMENT ME!
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public JPLoadable load(final String pureFileNameWithExt) throws FileNotFoundException, IOException {
        final String extension = pureFileNameWithExt.substring(pureFileNameWithExt.length() - 3);
        final String pathElement = JPressoFileManager.DIRECTORY_LOOKUP.get(extension);
        if (pathElement == null) {
            throw new IllegalArgumentException("Illegal file-extension: " + pureFileNameWithExt);
        }
        final String absPath = projectDir + File.separator + pathElement + File.separator + pureFileNameWithExt;
        final File toLoad = new File(absPath);
        JPLoadable current = null;
        if (extension.equalsIgnoreCase(JPressoFileManager.END_RUN)) {
            final JPressoRun currentRun = FILEMANAGER.load(toLoad, JPressoRun.class);
            if (currentRun != null) {
                current = FILEMANAGER.importRulesFromRunData(currentRun, projectDir);
            }
        } else if (extension.equalsIgnoreCase(JPressoFileManager.END_SQL)) {
            current = FILEMANAGER.load(toLoad, SQLRun.class);
        } else if (extension.equalsIgnoreCase(JPressoFileManager.END_CONNECTION)) {
            current = FILEMANAGER.load(toLoad, DatabaseConnection.class);
        }
        return current;
    }

    /**
     * Loads multiple AntExecutableInterface implementing objects from multiple files.
     *
     * @param   files  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  FileNotFoundException  DOCUMENT ME!
     * @throws  IOException            DOCUMENT ME!
     */
    public JPLoadable[] load(final String... files) throws FileNotFoundException, IOException {
        final JPLoadable[] result = new JPLoadable[files.length];
        int i = 0;
        for (final String s : files) {
            result[i++] = load(s);
        }
        return result;
    }
}
