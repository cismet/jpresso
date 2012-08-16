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
package de.cismet.jpresso.core.starter;

import java.io.File;
import java.io.IOException;

import de.cismet.jpresso.core.serviceprovider.ImporterExporter;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class RestoreChecker {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  projDir  DOCUMENT ME!
     */
    public static void checkAndRestore(final String projDir) {
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
