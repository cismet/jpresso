/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.starter;

import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;
import de.cismet.jpresso.core.exceptions.InvalidFormatFileException;
import de.cismet.jpresso.core.log4j.config.Log4jEasyConfigurator;
import de.cismet.jpresso.core.execution.AntUniversalExecutorImpl;
import de.cismet.jpresso.core.serviceprovider.AntUniversalExecutor;
import java.io.IOException;

/**
 *
 * @author srichter
 */
public class StartSingle {

    private final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StartSingle.class);

    public static void main(String[] args) throws IOException, InvalidFormatFileException, ClassNotFoundException {
        if (args.length == 2) {
            Log4jEasyConfigurator.configLog4j();
            String projDir = args[0];
            String runFile = args[1];
            RestoreChecker.checkAndRestore(projDir);
            startRun(projDir, runFile);
        } else {
            System.err.println("Wrong argument count. Need exactly 2 argument: String:ProjectDirectory, String:Run-File!");
        }
    }

    public static boolean startRun(String projDir, String runFile) {
        try {
            AntUniversalExecutor executor = new AntUniversalExecutorImpl(projDir);
            long errors = executor.execute(runFile);
            return (errors == 0);

        } catch (JPressoException ex) {
            log.error("JPressoException", ex);
            ex.printStackTrace();
            return false;
        }
    }
}
