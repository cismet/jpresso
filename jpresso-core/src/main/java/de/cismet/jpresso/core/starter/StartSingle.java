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

import java.io.IOException;

import de.cismet.jpresso.core.exceptions.InvalidFormatFileException;
import de.cismet.jpresso.core.execution.AntUniversalExecutorImpl;
import de.cismet.jpresso.core.log4j.config.Log4jEasyConfigurator;
import de.cismet.jpresso.core.serviceprovider.AntUniversalExecutor;
import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class StartSingle {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StartSingle.class);

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @throws  IOException                 DOCUMENT ME!
     * @throws  InvalidFormatFileException  DOCUMENT ME!
     * @throws  ClassNotFoundException      DOCUMENT ME!
     */
    public static void main(final String[] args) throws IOException,
        InvalidFormatFileException,
        ClassNotFoundException {
        if (args.length == 2) {
            Log4jEasyConfigurator.configLog4j();
            final String projDir = args[0];
            final String runFile = args[1];
            RestoreChecker.checkAndRestore(projDir);
            startRun(projDir, runFile);
        } else {
            System.err.println(
                "Wrong argument count. Need exactly 2 argument: String:ProjectDirectory, String:Run-File!");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   projDir  DOCUMENT ME!
     * @param   runFile  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static boolean startRun(final String projDir, final String runFile) {
        try {
            final AntUniversalExecutor executor = new AntUniversalExecutorImpl(projDir);
            final long errors = executor.execute(runFile);
            return (errors == 0);
        } catch (JPressoException ex) {
            log.error("JPressoException", ex);
            ex.printStackTrace();
            return false;
        }
    }
}
