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

import de.cismet.jpresso.core.serviceprovider.exceptions.DynamicCompilingException;

/**
 * An interface for dynamic runtime compilation and classloading.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface DynamicCompileClassLoader {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  a deep copy instance
     */
    DynamicCompileClassLoader copy();

    /**
     * Loads the source code from file, compiles it to the apropriate .class file and loads the class into the VM.
     *
     * <p>IMPORTANT: packages and folder structures must match! (baseDir == default package root directory)</p>
     *
     * @param   <T>         keepClassFile - keep the created class?
     * @param   sourceFile  - the source futureCodeFile to compile and load
     * @param   clazz       DOCUMENT ME!
     *
     * @return  the compiled and loaded class defined in the source
     *
     * @throws  DynamicCompilingException  de.cismet.jpressocore.classloading.DynamicCompilingException
     */
    <T> Class<? extends T> compileAndLoadClass(final File sourceFile, final Class<T> clazz)
            throws DynamicCompilingException;

    /**
     * DOCUMENT ME!
     *
     * @param   <T>        DOCUMENT ME!
     * @param   className  DOCUMENT ME!
     * @param   code       DOCUMENT ME!
     * @param   clazz      DOCUMENT ME!
     *
     * @return  the compiled and loaded class defined in the string with the given name
     *
     * @throws  DynamicCompilingException  de.cismet.jpressocore.serviceprovider.exceptions.DynamicCompilingException
     */
    <T> Class<? extends T> compileAndLoadClass(final String className, final String code, final Class<T> clazz)
            throws DynamicCompilingException;

    /**
     * DOCUMENT ME!
     *
     * @return  the baseDir which acts as the default package root directory
     */
    String getBaseDir();

    /**
     * DOCUMENT ME!
     *
     * @return  this classloaders additional classpath as String-representation
     */
    String getCompileClasspath();

    /**
     * DOCUMENT ME!
     *
     * @return  the delegation filter regex for classnames, inverting the classloader's standard delegation
     */
    String getFilter();

    /**
     * DOCUMENT ME!
     *
     * @return  true if the classloader's standard delegation is parent-first
     */
    boolean isParentFirstDelegation();
}
