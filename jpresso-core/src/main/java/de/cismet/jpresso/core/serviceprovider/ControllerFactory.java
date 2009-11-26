/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.serviceprovider;

import de.cismet.jpresso.core.data.ImportRules;
import de.cismet.jpresso.core.data.SQLRun;
import de.cismet.jpresso.core.serviceprovider.exceptions.InitializingException;
import de.cismet.jpresso.core.kernel.Importer;
import de.cismet.jpresso.core.kernel.SQLScriptExecutorImpl;

/**
 * A facade to the import/table filling/finalizing functionalities in the kernel package.
 * This is the first step: It creates the ExtractAndTransformController, wich fills the
 * IntermedTables and can create a FinalizerCreator after being filled.
 * 
 * @author srichter
 */
public final class ControllerFactory {

    private ControllerFactory() {
    }

    /**
     * Creates a ExtractAndTransformController, which is the starting point of every
     * JPresso Import.
     * 
     * @param clp
     * @param rules
     * @param preview
     * @throws de.cismet.jpressocore.exceptions.InitializingException
     */
    public static ExtractAndTransformController createExtractAndTransformController(final ClassResourceProvider clp, final ImportRules rules) throws InitializingException {
        final Importer i = new Importer(rules, clp);
        i.setCheckMemory(true);
        return i;
    }
    
    /**
     * Creates a SQLScriptExecutionController, which is capable of executing SQLRuns
     * 
     * @param sqlRun
     * @param crp
     * @return
     * @throws de.cismet.jpressocore.serviceprovider.exceptions.InitializingException
     */
    public static SQLScriptExecutionController createSQLScriptExecutionController(final SQLRun sqlRun, final ClassResourceProvider crp) throws InitializingException {
        return new SQLScriptExecutorImpl(sqlRun, crp);
    }
}

