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
import java.io.FileNotFoundException;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;

import de.cismet.jpresso.core.exceptions.InvalidFormatFileException;
import de.cismet.jpresso.core.execution.AntUniversalExecutorImpl;
import de.cismet.jpresso.core.execution.ProjectPlanBase;
import de.cismet.jpresso.core.log4j.config.Log4jEasyConfigurator;
import de.cismet.jpresso.core.serviceprovider.ClassResourceProvider;
import de.cismet.jpresso.core.serviceprovider.ClassResourceProviderFactory;
import de.cismet.jpresso.core.serviceprovider.DynamicCompileClassLoader;
import de.cismet.jpresso.core.serviceprovider.exceptions.DynamicCompilingException;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class StartProject {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StartProject.class);
    // public static final String PROJECT_PLAN_DIR = "jpproject";

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @throws  IOException                 DOCUMENT ME!
     * @throws  InvalidFormatFileException  DOCUMENT ME!
     * @throws  FileNotFoundException       DOCUMENT ME!
     * @throws  DynamicCompilingException   DOCUMENT ME!
     * @throws  NoSuchMethodException       DOCUMENT ME!
     * @throws  IllegalAccessException      DOCUMENT ME!
     * @throws  IllegalArgumentException    DOCUMENT ME!
     * @throws  InvocationTargetException   DOCUMENT ME!
     * @throws  InstantiationException      DOCUMENT ME!
     */
    public static void main(final String[] args) throws IOException,
        InvalidFormatFileException,
        FileNotFoundException,
        DynamicCompilingException,
        NoSuchMethodException,
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException,
        InstantiationException {
        if (args.length == 1) {
            Log4jEasyConfigurator.configLog4j();
            final String projDir = args[0];
            RestoreChecker.checkAndRestore(projDir);
            startProject(projDir);
        } else {
            System.err.println("Wrong argument count. Need exactly 1 argument: String:ProjectDirectory!");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   projDir  DOCUMENT ME!
     *
     * @throws  FileNotFoundException      DOCUMENT ME!
     * @throws  IOException                DOCUMENT ME!
     * @throws  DynamicCompilingException  DOCUMENT ME!
     * @throws  NoSuchMethodException      DOCUMENT ME!
     * @throws  IllegalAccessException     DOCUMENT ME!
     * @throws  IllegalArgumentException   DOCUMENT ME!
     * @throws  InvocationTargetException  DOCUMENT ME!
     * @throws  InstantiationException     DOCUMENT ME!
     */
    private static void startProject(final String projDir) throws FileNotFoundException,
        IOException,
        DynamicCompilingException,
        NoSuchMethodException,
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException,
        InstantiationException {
        final File projDirFile = new File(projDir);
        final ClassResourceProvider crp = ClassResourceProviderFactory.createClassRessourceProvider(projDirFile);
        final DynamicCompileClassLoader dl = crp.getDynClassLoader();
        final File source = crp.getProjectPlanFile();
        final Class<? extends ProjectPlanBase> c = dl.compileAndLoadClass(source, ProjectPlanBase.class);
        // "inject" dependency
        ProjectPlanBase.EXECUTOR = new AntUniversalExecutorImpl(projDir);
        final ProjectPlanBase pPB = c.newInstance();
        pPB.start();
    }
}
