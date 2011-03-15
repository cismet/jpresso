/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.starter;

import de.cismet.jpresso.core.serviceprovider.exceptions.DynamicCompilingException;
import de.cismet.jpresso.core.exceptions.InvalidFormatFileException;
import de.cismet.jpresso.core.log4j.config.Log4jEasyConfigurator;
import de.cismet.jpresso.core.serviceprovider.DynamicCompileClassLoader;
import de.cismet.jpresso.core.execution.AntUniversalExecutorImpl;
import de.cismet.jpresso.core.execution.ProjectPlanBase;
import de.cismet.jpresso.core.serviceprovider.ClassResourceProvider;
import de.cismet.jpresso.core.serviceprovider.ClassResourceProviderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author srichter
 */
public class StartProject {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StartProject.class);
    //public static final String PROJECT_PLAN_DIR = "jpproject";

    public static void main(String[] args) throws IOException, InvalidFormatFileException, FileNotFoundException, DynamicCompilingException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        if (args.length == 1) {
            Log4jEasyConfigurator.configLog4j();
            final String projDir = args[0];
            RestoreChecker.checkAndRestore(projDir);
            startProject(projDir);
        } else {
            System.err.println("Wrong argument count. Need exactly 1 argument: String:ProjectDirectory!");
        }
    }

    private static void startProject(final String projDir) throws FileNotFoundException, IOException, DynamicCompilingException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        final File projDirFile = new File(projDir);
        final ClassResourceProvider crp = ClassResourceProviderFactory.createClassRessourceProvider(projDirFile);
        final DynamicCompileClassLoader dl = crp.getDynClassLoader();
        final File source = crp.getProjectPlanFile();
        final Class<? extends ProjectPlanBase> c = dl.compileAndLoadClass(source, ProjectPlanBase.class);
        //"inject" dependency
        ProjectPlanBase.EXECUTOR = new AntUniversalExecutorImpl(projDir);
        final ProjectPlanBase pPB = c.newInstance();
        pPB.start();

    }
}
