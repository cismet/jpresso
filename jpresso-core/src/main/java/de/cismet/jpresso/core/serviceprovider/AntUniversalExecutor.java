/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.serviceprovider;

import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;
import java.io.File;
import java.sql.Connection;
import java.util.Map;

/**
 *
 * @author srichter
 */
public interface AntUniversalExecutor {

    /**
     * Executes the given jobs
     * 
     * @param names
     * @return the errorcount of the jobs
     * @throws de.cismet.jpressocore.kernel.InitializingException
     * @throws de.cismet.jpressocore.kernel.FinalizerException
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     * @throws de.cismet.jpressocore.kernel.JPressoException
     */
    public long execute(final String... names) throws JPressoException;

    /**
     * Starts the given command on an external shell with the given environment
     * and cwd and waits until finish.
     *
     * @param env - map of environment variables
     * @param cwd - the process current working directory
     * @param command - the commands to execute
     * @return the exit code
     * @throws de.cismet.jpressocore.serviceprovider.exceptions.JPressoException
     */
    public int startExternalCommand(final Map<String, String> env, final File cwd, final String... command) throws JPressoException;

    /**
     * Starts the given command on an external shell and waits until finish.
     *
     * @param command
     * @return the exit code
     * @throws de.cismet.jpressocore.serviceprovider.exceptions.JPressoException
     */
    public int startExternalCommand(final String... command) throws JPressoException;

    /**
     *
     * @return the source connection used by the latest job
     */
    public Connection getLatestSourceCon();

    /**
     *
     * @return the target connection used by the latest job
     */
    public Connection getLatestTargetCon();

    /**
     *
     * @return the project directories absolut path as string
     */
    public String getProjDir();

    /**
     * 
     * Tests the given jobs
     * 
     * @param names
     * @return the errorcount of the jobs
     * @throws de.cismet.jpressocore.kernel.InitializingException
     * @throws de.cismet.jpressocore.kernel.FinalizerException
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     * @throws de.cismet.jpressocore.kernel.JPressoException
     */
    public long test(final String... names) throws JPressoException;

    /**
     * 
     * 
     * 
     * @param name
     * @return the connection described in the parameters
     * @throws de.cismet.jpressocore.serviceprovider.exceptions.JPressoException
     */
    public Connection openConnection(final String name) throws JPressoException;

    /**
     * 
     * @return a copy of the executor instance, e.g. for multithreading
     */
    public AntUniversalExecutor copy();
}
