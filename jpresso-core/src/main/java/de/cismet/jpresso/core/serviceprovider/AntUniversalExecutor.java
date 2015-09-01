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
package de.cismet.jpresso.core.serviceprovider;

import java.io.File;
import java.io.IOException;

import java.sql.Connection;

import java.util.Map;

import de.cismet.jpresso.core.data.JPLoadable;
import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface AntUniversalExecutor {

    //~ Methods ----------------------------------------------------------------

    /**
     * Executes the given jobs.
     *
     * @param   names  DOCUMENT ME!
     *
     * @return  the errorcount of the jobs
     *
     * @throws  JPressoException  de.cismet.jpressocore.kernel.InitializingException
     */
    long execute(final String... names) throws JPressoException;

    /**
     * Starts the given command on an external shell with the given environment and cwd and waits until finish.
     *
     * @param   env      - map of environment variables
     * @param   cwd      - the process current working directory
     * @param   command  - the commands to execute
     *
     * @return  the exit code
     *
     * @throws  JPressoException  de.cismet.jpressocore.serviceprovider.exceptions.JPressoException
     */
    int startExternalCommand(final Map<String, String> env, final File cwd, final String... command)
            throws JPressoException;

    /**
     * Starts the given command on an external shell and waits until finish.
     *
     * @param   command  DOCUMENT ME!
     *
     * @return  the exit code
     *
     * @throws  JPressoException  de.cismet.jpressocore.serviceprovider.exceptions.JPressoException
     */
    int startExternalCommand(final String... command) throws JPressoException;

    /**
     * DOCUMENT ME!
     *
     * @return  the source connection used by the latest job
     */
    Connection getLatestSourceCon();

    /**
     * DOCUMENT ME!
     *
     * @return  the target connection used by the latest job
     */
    Connection getLatestTargetCon();

    /**
     * DOCUMENT ME!
     *
     * @return  the project directories absolut path as string
     */
    String getProjDir();

    /**
     * Tests the given jobs.
     *
     * @param   names  DOCUMENT ME!
     *
     * @return  the errorcount of the jobs
     *
     * @throws  JPressoException  de.cismet.jpressocore.kernel.InitializingException
     */
    long test(final String... names) throws JPressoException;

    /**
     * DOCUMENT ME!
     *
     * @param   name  DOCUMENT ME!
     *
     * @return  the connection described in the parameters
     *
     * @throws  JPressoException  de.cismet.jpressocore.serviceprovider.exceptions.JPressoException
     */
    Connection openConnection(final String name) throws JPressoException;

    /**
     * DOCUMENT ME!
     *
     * @return  a copy of the executor instance, e.g. for multithreading
     */
    AntUniversalExecutor copy();

    /**
     * DOCUMENT ME!
     *
     * @param   name  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    JPLoadable openJPressoFile(String name) throws IOException;

    /**
     * DOCUMENT ME!
     *
     * @param   exec      DOCUMENT ME!
     * @param   testOnly  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  JPressoException  DOCUMENT ME!
     */
    long execute(final JPLoadable exec, final boolean testOnly) throws JPressoException;
}
