/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.starter;

import de.cismet.jpresso.core.log4j.config.Log4jEasyConfigurator;
import de.cismet.jpresso.core.serviceprovider.ImporterExporter;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author srichter
 */
public class StartImport {

    public static void main(String[] args) throws IOException {
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
