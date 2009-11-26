/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.starter;

import de.cismet.jpresso.core.serviceprovider.ImporterExporter;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author srichter
 */
public class RestoreChecker {

    public static void checkAndRestore(String projDir) {
        try {
            final File importRestoreInf = new File(projDir, File.separator + JPressoFileManager.IMPORT_OPTIONS);
            if (importRestoreInf.isFile()) {
                ImporterExporter.restoreProjectEnvironment(new File(projDir));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
