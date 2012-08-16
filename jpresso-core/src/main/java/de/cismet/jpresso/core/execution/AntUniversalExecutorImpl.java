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
package de.cismet.jpresso.core.execution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.cismet.jpresso.core.data.DatabaseConnection;
import de.cismet.jpresso.core.data.ImportRules;
import de.cismet.jpresso.core.data.JPLoadable;
import de.cismet.jpresso.core.data.SQLRun;
import de.cismet.jpresso.core.kernel.ImportFinalizer;
import de.cismet.jpresso.core.kernel.Importer;
import de.cismet.jpresso.core.kernel.SQLScriptExecutorImpl;
import de.cismet.jpresso.core.serviceprovider.AntUniversalExecutor;
import de.cismet.jpresso.core.serviceprovider.ClassResourceProvider;
import de.cismet.jpresso.core.serviceprovider.ClassResourceProviderFactory;
import de.cismet.jpresso.core.serviceprovider.exceptions.FinalizerException;
import de.cismet.jpresso.core.serviceprovider.exceptions.InitializingException;
import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;

/**
 * Facade that executes imports and sql scripts, returns errorcount and connections. Used in the Ant Tasks.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class AntUniversalExecutorImpl implements AntUniversalExecutor {

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final String projDir;
    private final Map<String, JPLoadable> cache;
    private final AntUniversalLoader loader;
    private final ClassResourceProvider clp;
    private Connection latestSourceCon;
    private Connection latestTargetCon;
    private boolean canCloseCurrentSource;
    private boolean canCloseCurrentTarget;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AntUniversalExecutorImpl object.
     *
     * @param   projDir  DOCUMENT ME!
     *
     * @throws  NullPointerException  DOCUMENT ME!
     */
    public AntUniversalExecutorImpl(final String projDir) {
        if (projDir == null) {
            throw new NullPointerException("Project directory can not be null!");
        }
        this.projDir = projDir;
        clp = ClassResourceProviderFactory.createClassRessourceProvider((new File(projDir)));
        loader = new AntUniversalLoader(projDir);
        latestSourceCon = null;
        latestTargetCon = null;
        cache = new HashMap<String, JPLoadable>();
        canCloseCurrentSource = true;
        canCloseCurrentTarget = true;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Close the current target- and sourceconnection if they were not retrieved by calling getXXXConnection().
     */
    private void processClosingPreviouseConnections() {
        String msg = "closing source";
        if (canCloseCurrentSource && (latestSourceCon != null)) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug(msg);
                }
                latestSourceCon.close();
            } catch (SQLException ex) {
                log.error("Can not close source", ex);
            }
        }
        if (canCloseCurrentTarget && (latestTargetCon != null)) {
            msg = "closing target";
            try {
                if (log.isDebugEnabled()) {
                    log.debug(msg);
                }
                latestTargetCon.close();
            } catch (SQLException ex) {
                log.error("Can not close target", ex);
            }
        }
        canCloseCurrentSource = true;
        canCloseCurrentTarget = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   names  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  JPressoException  de.cismet.jpressocore.kernel.InitializingException
     */
    @Override
    public long test(final String... names) throws JPressoException {
        long errorCountSum = 0;
        for (final String s : names) {
            String msg = "\nStart Testing: " + s;
            log.info(msg);
            System.out.println(msg);
            final long currentErrors;
            try {
                currentErrors = execute(s, true);
            } catch (Exception ex) {
                msg = "Error on executing " + s;
                throw new JPressoException(msg, ex);
            }
            errorCountSum += currentErrors;
            msg = "Testing " + s + " finished with " + currentErrors + " Error(s)!\n";
            log.info(msg);
            System.out.println(msg);
        }
        return errorCountSum;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   names  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  JPressoException  de.cismet.jpressocore.kernel.InitializingException
     */
    @Override
    public long execute(final String... names) throws JPressoException {
        long errorCountSum = 0;
        for (final String s : names) {
            String msg = "\nStart Executing: " + s;
            log.info(msg);
            System.out.println(msg);
            final long currentErrors;
            try {
                currentErrors = execute(s, false);
            } catch (Exception ex) {
                throw new JPressoException("Error on executing " + s, ex);
            }
            errorCountSum += currentErrors;
            msg = "Executing " + s + " finished with " + currentErrors + " Error(s)!\n";
            System.out.println(msg);
            log.info(msg);
        }
        return errorCountSum;
    }

    @Override
    public Connection openConnection(final String name) throws JPressoException {
        try {
            final JPLoadable obj = loader.load(name);
            if (obj instanceof DatabaseConnection) {
                final DatabaseConnection dbc = (DatabaseConnection)obj;
                return clp.getDriverManager().getConnection(dbc.getDriverClass(), dbc.getUrl(), dbc.getProps());
            }
        } catch (Exception e) {
            throw new JPressoException("Exception when trying to open connection described in " + name, e);
        }
        throw new IllegalArgumentException("File " + name + " does not represent a database connection!");
    }

    @Override
    public JPLoadable openJPressoFile(final String name) throws IOException {
        JPLoadable exec = cache.get(name);
        if (exec == null) {
            exec = loader.load(name);
            cache.put(name, exec);
        }
        return exec;
    }

    @Override
    public long execute(final JPLoadable exec, final boolean testOnly) throws JPressoException {
        long errorCount;
        if (exec instanceof ImportRules) {
            final ImportRules impRules = (ImportRules)exec;
//            impRules.setFileName(name.substring(0, name.length() - 4));
//            final ClassResourceProvider clp = ClassResourceProviderFactory.createClassRessourceProvider(new File(getProjDir()));
            final Importer importer = new Importer(impRules, clp);
            String msg;
            msg = "- Extracting and transforming data ...";
            log.info(msg);
            System.out.println(msg);
            importer.runImport();
            msg = "- Writing to Database, using Finalizer:" + impRules.getRuntimeProperties().getFinalizerClass()
                        + " ...";
            log.info(msg);
            System.out.println(msg);
            final String rb = impRules.getRuntimeProperties().getFinalizerProperties().getProperty("Rollback");
            if ((rb != null) && (testOnly != rb.equals("true"))) {
                msg = "! WARNING: This import run is executed with ROLLBACK = " + testOnly
                            + "! RuntimeProperties are overridden!";
                log.warn(msg);
                final StringBuffer marker = new StringBuffer();
                for (int i = 0; i < msg.length(); i++) {
                    marker.append("!");
                }
                System.out.println("\n" + marker);
                System.out.println(msg);
                System.out.println(marker + "\n");
            }
            if (testOnly) {
                impRules.getRuntimeProperties().getFinalizerProperties().setProperty("Rollback", "true");
            } else {
                impRules.getRuntimeProperties().getFinalizerProperties().setProperty("Rollback", "false");
            }
            final ImportFinalizer finalizer = new ImportFinalizer(impRules.getRuntimeProperties().getFinalizerClass(),
                    importer.getIntermedTablesContainer(),
                    impRules.getRuntimeProperties().getFinalizerProperties());
            errorCount = finalizer.finalise();
            setLatestSourceCon(importer.getSourceConn());
            setLatestTargetCon(importer.getTargetConn());
            return errorCount;
        } else if (exec instanceof SQLRun) {
            final SQLRun sqlRun = (SQLRun)exec;
//            final ClassResourceProvider clp = ClassResourceProviderFactory.createClassRessourceProvider((new File(getProjDir())));
            final SQLScriptExecutorImpl sqlexec = new SQLScriptExecutorImpl(sqlRun, clp);
            sqlexec.setTest(testOnly);
            errorCount = sqlexec.execute();
            this.setLatestSourceCon(null);
            this.setLatestTargetCon(sqlexec.getTargetConn());
        } else {
            throw new InitializingException("Argument was not an executable run or did not exist!", "");
        }
        return errorCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   name      DOCUMENT ME!
     * @param   testOnly  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  InitializingException     de.cismet.jpressocore.kernel.InitializingException
     * @throws  FinalizerException        de.cismet.jpressocore.kernel.FinalizerException
     * @throws  FileNotFoundException     DOCUMENT ME!
     * @throws  IOException               DOCUMENT ME!
     * @throws  JPressoException          de.cismet.jpressocore.kernel.JPressoException
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    private long execute(final String name, final boolean testOnly) throws InitializingException,
        FinalizerException,
        FileNotFoundException,
        IOException,
        JPressoException {
        if ((name == null) || (name.length() < 5)) {
            throw new IllegalArgumentException("Illegal filename argument on AntUniversalExecutor.");
        }
        processClosingPreviouseConnections();
        final JPLoadable exec = openJPressoFile(name);
        return execute(exec, testOnly);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Connection getLatestSourceCon() {
        canCloseCurrentSource = false;
        return latestSourceCon;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  source  DOCUMENT ME!
     */
    private void setLatestSourceCon(final Connection source) {
        canCloseCurrentSource = true;
        this.latestSourceCon = source;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Connection getLatestTargetCon() {
        canCloseCurrentTarget = false;
        return latestTargetCon;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  target  DOCUMENT ME!
     */
    private void setLatestTargetCon(final Connection target) {
        canCloseCurrentTarget = true;
        this.latestTargetCon = target;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  Throwable  java.lang.Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        processClosingPreviouseConnections();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String getProjDir() {
        return projDir;
    }

    @Override
    public AntUniversalExecutor copy() {
        return new AntUniversalExecutorImpl(projDir);
    }

    @Override
    public int startExternalCommand(final Map<String, String> env, final File cwd, final String... command)
            throws JPressoException {
        ProcessBuilder pcb = new ProcessBuilder(command).redirectErrorStream(false);
        if ((cwd != null) && cwd.isDirectory()) {
            pcb = pcb.directory(cwd);
        }
        if ((env != null) && !env.isEmpty()) {
            final Map<String, String> environment = pcb.environment();
            environment.putAll(env);
        }

        try {
            log.info("Starting external command(s): " + Arrays.deepToString(command));
            final Process proc = pcb.start();
            final BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            final BufferedReader errInput = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            final Thread stdOutConsumer = new Thread() {

                    @Override
                    public void run() {
                        String msg;
                        try {
                            while ((msg = stdInput.readLine()) != null) {
                                System.out.println(msg);
                            }
                        } catch (IOException ex) {
                        }
                    }
                };

            final Thread stdErrConsumer = new Thread() {

                    @Override
                    public void run() {
                        String msg;
                        try {
                            while ((msg = errInput.readLine()) != null) {
                                System.out.println(msg);
                            }
                        } catch (IOException ex) {
                        }
                    }
                };
            stdOutConsumer.start();
            stdErrConsumer.start();
            final int exitCode = proc.waitFor();
            // read the output from the command

            return exitCode;
        } catch (Exception ex) {
            throw new JPressoException("Error on executing external commands!", ex);
        }
    }

    @Override
    public int startExternalCommand(final String... command) throws JPressoException {
        return startExternalCommand(null, null, command);
    }
}
