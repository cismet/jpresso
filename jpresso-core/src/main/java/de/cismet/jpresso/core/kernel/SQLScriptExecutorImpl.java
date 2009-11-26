/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.kernel;

import de.cismet.jpresso.core.serviceprovider.*;
import de.cismet.jpresso.core.serviceacceptor.ProgressListener;
import de.cismet.jpresso.core.serviceprovider.exceptions.InitializingException;
import de.cismet.jpresso.core.data.DatabaseConnection;
import de.cismet.jpresso.core.data.SQLRun;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author srichter
 */
public final class SQLScriptExecutorImpl implements SQLScriptExecutionController {

    public static final int MAX_LOG_ERROR = 20;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    boolean debug = log.isDebugEnabled();
    private final SQLRun sqlRun;
    private boolean test = false;
    private DatabaseConnection target;
    private Connection targetConn;
    private final ClassResourceProvider clp;
    private boolean canceled;
    private String logs = "";

    /**
     * 
     * @param sqlRun
     * @param clp
     * @throws de.cismet.jpressocore.exceptions.InitializingException
     */
    public SQLScriptExecutorImpl(final SQLRun sqlRun, final ClassResourceProvider clp) throws InitializingException {
        if (sqlRun == null || clp == null) {
            throw new NullPointerException();
        }
        this.clp = clp;
        this.canceled = false;
        this.targetConn = null;
        this.sqlRun = sqlRun;
        if (sqlRun.getConnection() != null) {
            target = sqlRun.getConnection();
        }
        try {
            final DynamicDriverManager dm = clp.getDriverManager();
            if (target != null && target.getProps() != null) {
                if (target.getProps().values() != null && target.getProps().values().size() > 0) {
                    final Connection c = dm.getConnection(target.getDriverClass(), target.getUrl(), target.getProps());
                    setTargetConn(c);

                } else {
                    final Connection c = dm.getConnection(target.getDriverClass(), target.getUrl(), new Properties());
                    setTargetConn(c);
                }
            } else {
                final String msg = "No valid DatabaseConnection !";
                logs += msg;
                throw new InitializingException(msg, logs);
            }
        } catch (Exception e) {
            log.error("Error connecting to target database.", e);
            //TODO PROPER ERROR HANDLING AND LOGGING!!!
            throw new InitializingException("Error connecting to target database.", "Connection Error", e);//"Fehler beim Verbindungsaufbau zum Script-Target.", initializeLog, e);

        }
    }

    /**
     * 
     * @return
     * @throws de.cismet.jpressocore.exceptions.InitializingException
     */
    public long execute() {
        return execute(null);
    }

    /**
     * 
     * @param progress
     * @return
     * @throws de.cismet.jpressocore.exceptions.InitializingException
     */
    public long execute(final ProgressListener progress) {
        boolean showProgress = (progress != null);
        int counter = 0;
        String msg;
        if (showProgress) {
            final int fcount = sqlRun.getScript().size();
            progress.start("SQL script is running...", fcount);
        }
        try {
            logs = "";
            int logErrorCounter = 0;
            long errorCount = 0;
            int line = 0;
            if (getTargetConn() != null) {
                msg = "Executing SQL Script... \n- Connecting to " + target.getUrl() + "\n";
                logs += msg;
                log.info(msg);
                if (!(sqlRun.getScript().size() > 0)) {
                    msg = "Script ist leer!\n";
                    log.info(msg);
                    logs += msg;
                    return 0;
                }
                try {
                    getTargetConn().setAutoCommit(false);
                    if (!test) {
                        msg = "- Autocommit is false. Will try automatic rollback ON ERRORS (useless you explicid call commit in script)!\n";
                        log.info(msg);
                        logs += msg;
                    } else {
                        msg = "- Test SQL run. Autocommit is false. Will try automatic rollback (useless you explicid call commit in script)!\n";
                        log.info(msg);
                        logs += msg;
                    }
                    for (final String s : sqlRun.getScript()) {
                        //Cancel procedure
                        if (canceled) {
                            logs += "\nSQL run canceled at Line " + line + "! Will try to rollback all uncommited Statements!\n";
                            getTargetConn().rollback();
                            logs += "Rollback successful. Done. ";
                            return errorCount;
                        }
                        if (s != null) {
                            ++line;
                            final Statement stmnt = getTargetConn().createStatement();
                            try {
                                if (debug) {
                                    msg = "Execute line " + line + ": " + s + " @ " + target.getUrl() + "\n";
                                    log.debug(msg);
                                }
                                stmnt.execute(s);
                                stmnt.close();
                                if (showProgress) {
                                    progress.progress(++counter);
                                }
                            } catch (SQLException ex) {
                                errorCount++;
                                logErrorCounter++;
                                getTargetConn().rollback();
                                log.error("Error in line " + line + ":" + stmnt + ": " + ex);

                                if (logErrorCounter < MAX_LOG_ERROR) {
                                    logs += "    Script erros in line " + line + ", statement: " + s + " (" + ex.toString() + ")\n";
                                } else if (logErrorCounter == MAX_LOG_ERROR) {
                                    logs += "    ************** more errors (output stopped)\n";
                                }
                            }
                        } else {
                            errorCount++;
                            logErrorCounter++;
                            logs += "   Script line " + line + " is null!\n";
                        }
                    }
                } catch (SQLException ex) {
                    log.error("SQL Exception during Script Execution", ex);
                //ex.printStackTrace();
                }
                if (errorCount > 0 || test) {
                    try {
                        if (!test) {
                            msg = "\nScript error --> Rollback\n";
                            log.info(msg);
                            logs += msg;
                        } else {
                            msg = "\n- Test executed with " + errorCount + " errors --> Rollback of all uncommited statements\n";
                            log.info(msg);
                            logs += msg;
                        }
                        getTargetConn().rollback();
                        logs += "Rollback successful. Done. ";
                    } catch (SQLException ex) {
                        log.error("Error at:ROLLBACK: " + ex);
                        if (logErrorCounter < MAX_LOG_ERROR) {
                            logs += "    Import error .. rollback of statement" + ex.toString() + ")\n";
                        } else if (logErrorCounter == MAX_LOG_ERROR) {
                            logs += "    ************** more errors (output stopped)\n";
                        }
                    }
                } else {
                    try {
                        msg = "\n- Script executet without errors --> Commit\n";
                        log.info(msg);
                        logs += msg;
                        getTargetConn().commit();
                    } catch (SQLException ex) {
                        log.error("Error at:COMMIT: " + ex);
                        if (logErrorCounter < MAX_LOG_ERROR) {
                            logs += "    Script error .. commit failed: " + ex.toString() + ")\n";
                        } else if (logErrorCounter == MAX_LOG_ERROR) {
                            logs += "    ************** more errors (output stopped)\n";
                        }
                    }
                }
            }
            msg = line + " Statements executed! " + errorCount + " errors";
            log.info(msg);
            logs += msg;
            System.out.println(getLogs());
            return errorCount;
        } finally {
            if (showProgress) {
                progress.switchToIndeterminate();
                progress.finish();
            }
        }
    }

    /**
     * 
     * @return the logs
     */
    public String getLogs() {
        return logs;
    }

    /**
     * 
     * @return
     */
    public Connection getTargetConn() {
        return targetConn;
    }

    /**
     * 
     * @param test
     */
    public void setTest(final boolean test) {
        this.test = test;
    }

    /**
     * 
     * @param targetConn
     */
    public void setTargetConn(final Connection targetConn) {
        this.targetConn = targetConn;
    }

    /**
     * 
     * @return
     */
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * 
     * @param canceled
     */
    public void setCanceled(final boolean canceled) {
        this.canceled = canceled;
    }
}
          

