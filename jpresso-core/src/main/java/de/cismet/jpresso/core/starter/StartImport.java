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

import de.cismet.jpresso.core.log4j.config.Log4jEasyConfigurator;
import de.cismet.jpresso.core.serviceprovider.ImporterExporter;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class StartImport {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public static void main(final String[] args) throws IOException {
        if (args.length == 2) {
            Log4jEasyConfigurator.configLog4j();
            ImporterExporter.importProjectFromZip(new File(args[0]), new File(args[1]));
        } else if (args.length == 1) {
            ImporterExporter.restoreProjectEnvironment(new File(args[0]));
        } else {
            System.err.println("Error: incorrect arguments. Need [inputfile.zip] [outputdirectory]");
        }
    }
}
