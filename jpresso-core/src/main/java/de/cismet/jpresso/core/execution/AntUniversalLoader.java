/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.execution;

import de.cismet.jpresso.core.data.DatabaseConnection;
import de.cismet.jpresso.core.data.JPLoadable;
import de.cismet.jpresso.core.data.JPressoRun;
import de.cismet.jpresso.core.data.SQLRun;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A class that loads ant executable objects from file.
 * 
 * @author srichter
 */
final class AntUniversalLoader {

    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(getClass());
    public static final JPressoFileManager FILEMANAGER = JPressoFileManager.getDefault();
    private final String projectDir;

    public AntUniversalLoader(final String projectDir) {
        this.projectDir = projectDir;
    }

    /**
     * Loads an AntExecutableInterface implementing objects from the file
     * named like the parameter in the project known to this.
     * 
     * @param pureFileNameWithExt
     * @return
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public final JPLoadable load(final String pureFileNameWithExt) throws FileNotFoundException, IOException {
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
     * Loads multiple AntExecutableInterface implementing objects from multiple
     * files.
     * 
     * @param files
     * @return
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public final JPLoadable[] load(final String... files) throws FileNotFoundException, IOException {
        final JPLoadable[] result = new JPLoadable[files.length];
        int i = 0;
        for (final String s : files) {
            result[i++] = load(s);
        }
        return result;
    }
}
