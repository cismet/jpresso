/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.execution;

import de.cismet.jpresso.core.data.DatabaseConnection;
import de.cismet.jpresso.core.serviceprovider.AntUniversalExecutor;
import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;
import de.cismet.jpresso.core.serviceprovider.exceptions.FinalizerException;
import de.cismet.jpresso.core.kernel.ImportFinalizer;
import de.cismet.jpresso.core.kernel.Importer;
import de.cismet.jpresso.core.serviceprovider.exceptions.InitializingException;
import de.cismet.jpresso.core.kernel.SQLScriptExecutorImpl;
import de.cismet.jpresso.core.data.ImportRules;
import de.cismet.jpresso.core.data.JPLoadable;
import de.cismet.jpresso.core.data.SQLRun;

import de.cismet.jpresso.core.serviceprovider.ClassResourceProvider;
import de.cismet.jpresso.core.serviceprovider.ClassResourceProviderFactory;
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

/**
 *  Facade that executes imports and sql scripts, returns errorcount and connections.
 *  Used in the Ant Tasks.
 *
 * @author srichter
 */
public final class AntUniversalExecutorImpl implements AntUniversalExecutor {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final String projDir;
    private final Map<String, JPLoadable> cache;
    private final AntUniversalLoader loader;
    private final ClassResourceProvider clp;
    private Connection latestSourceCon;
    private Connection latestTargetCon;
    private boolean canCloseCurrentSource;
    private boolean canCloseCurrentTarget;

    /**
     *
     * @param projDir
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

    /**
     * Close the current target- and sourceconnection if they were not
     * retrieved by calling getXXXConnection().
     */
    private void processClosingPreviouseConnections() {
        String msg = "closing source";
        if (canCloseCurrentSource && latestSourceCon != null) {
            try {
                log.debug(msg);
                latestSourceCon.close();
            } catch (SQLException ex) {
                log.error("Can not close source", ex);
            }
        }
        if (canCloseCurrentTarget && latestTargetCon != null) {
            msg = "closing target";
            try {
                log.debug(msg);
                latestTargetCon.close();
            } catch (SQLException ex) {
                log.error("Can not close target", ex);
            }
        }
        canCloseCurrentSource = true;
        canCloseCurrentTarget = true;
    }

    /**
     *
     * @param names
     * @return
     * @throws de.cismet.jpressocore.kernel.InitializingException
     * @throws de.cismet.jpressocore.kernel.FinalizerException
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     * @throws de.cismet.jpressocore.kernel.JPressoException
     */
    @Override
    public final long test(final String... names) throws JPressoException {
        long errorCountSum = 0;
        for (final String s : names) {
            String msg = "\nStart Testing: " + s;
            log.info(msg);
            System.out.println(msg);
            long currentErrors;
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
     *
     * @param names
     * @return
     * @throws de.cismet.jpressocore.kernel.InitializingException
     * @throws de.cismet.jpressocore.kernel.FinalizerException
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     * @throws de.cismet.jpressocore.kernel.JPressoException
     */
    @Override
    public final long execute(final String... names) throws JPressoException {
        long errorCountSum = 0;
        for (final String s : names) {
            String msg = "\nStart Executing: " + s;
            log.info(msg);
            System.out.println(msg);
            long currentErrors;
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
    public final Connection openConnection(final String name) throws JPressoException {
        try {
            final JPLoadable obj = loader.load(name);
            if (obj instanceof DatabaseConnection) {
                final DatabaseConnection dbc = (DatabaseConnection) obj;
                return clp.getDriverManager().getConnection(dbc.getDriverClass(), dbc.getUrl(), dbc.getProps());
            }
        } catch (Exception e) {
            throw new JPressoException("Exception when trying to open connection described in " + name, e);
        }
        throw new IllegalArgumentException("File " + name + " does not represent a database connection!");
    }

    /**
     *
     * @param name
     * @param testOnly
     * @return
     * @throws de.cismet.jpressocore.kernel.InitializingException
     * @throws de.cismet.jpressocore.kernel.FinalizerException
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     * @throws de.cismet.jpressocore.kernel.JPressoException
     */
    private final long execute(final String name, final boolean testOnly) throws InitializingException, FinalizerException, FileNotFoundException, IOException, JPressoException {
        if (name == null || name.length() < 5) {
            throw new IllegalArgumentException("Illegal filename argument on AntUniversalExecutor.");
        }
        processClosingPreviouseConnections();
        JPLoadable exec = cache.get(name);
        if (exec == null) {
            exec = loader.load(name);
            cache.put(name, exec);
        }
        long errorCount;
        if (exec instanceof ImportRules) {
            final ImportRules impRules = (ImportRules) exec;
            impRules.setFileName(name.substring(0, name.length() - 4));
//            final ClassResourceProvider clp = ClassResourceProviderFactory.createClassRessourceProvider(new File(getProjDir()));
            final Importer importer = new Importer(impRules, clp);
            String msg;
            msg = "- Extracting and transforming data ...";
            log.info(msg);
            System.out.println(msg);
            importer.runImport();
            msg = "- Writing to Database, using Finalizer:" + impRules.getRuntimeProperties().getFinalizerClass() + " ...";
            log.info(msg);
            System.out.println(msg);
            final String rb = impRules.getRuntimeProperties().getFinalizerProperties().getProperty("Rollback");
            if (rb != null && testOnly != rb.equals("true")) {
                msg = "! WARNING: This import run is executed with ROLLBACK = " + testOnly + "! RuntimeProperties are overridden!";
                log.warn(msg);
                StringBuffer marker = new StringBuffer();
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
            final ImportFinalizer finalizer = new ImportFinalizer(impRules.getRuntimeProperties().getFinalizerClass(), importer.getIntermedTablesContainer(), impRules.getRuntimeProperties().getFinalizerProperties());
            errorCount = finalizer.finalise();
            setLatestSourceCon(importer.getSourceConn());
            setLatestTargetCon(importer.getTargetConn());
            return errorCount;
        } else if (exec instanceof SQLRun) {
            final SQLRun sqlRun = (SQLRun) exec;
//            final ClassResourceProvider clp = ClassResourceProviderFactory.createClassRessourceProvider((new File(getProjDir())));
            final SQLScriptExecutorImpl sqlexec = new SQLScriptExecutorImpl(sqlRun, clp);
            sqlexec.setTest(testOnly);
            errorCount = sqlexec.execute();
            this.setLatestSourceCon(null);
            this.setLatestTargetCon(sqlexec.getTargetConn());
        } else {
            throw new InitializingException("Argument " + name + "was not an executable run or did not exist!", "");
        }
        return errorCount;
    }

    /**
     *
     * @return
     */
    @Override
    public Connection getLatestSourceCon() {
        canCloseCurrentSource = false;
        return latestSourceCon;
    }

    /**
     *
     * @param source
     */
    private void setLatestSourceCon(Connection source) {
        canCloseCurrentSource = true;
        this.latestSourceCon = source;
    }

    /**
     *
     * @return
     */
    @Override
    public Connection getLatestTargetCon() {
        canCloseCurrentTarget = false;
        return latestTargetCon;
    }

    /**
     *
     * @param target
     */
    private void setLatestTargetCon(Connection target) {
        canCloseCurrentTarget = true;
        this.latestTargetCon = target;
    }

    /**
     *
     * @throws java.lang.Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        processClosingPreviouseConnections();
    }

    /**
     *
     * @return
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
    public int startExternalCommand(final Map<String, String> env, final File cwd, final String... command) throws JPressoException {
        ProcessBuilder pcb = new ProcessBuilder(command).redirectErrorStream(false);
        if (cwd != null && cwd.isDirectory()) {
            pcb = pcb.directory(cwd);
        }
        if (env != null && !env.isEmpty()) {
            final Map<String, String> environment = pcb.environment();
            environment.putAll(env);
        }

        try {
            log.info("Starting external command(s): " + Arrays.deepToString(command));
            Process proc = pcb.start();
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
            int exitCode = proc.waitFor();
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
