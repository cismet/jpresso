/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.serviceprovider;

import de.cismet.jpresso.core.serviceprovider.exceptions.DynamicCompilingException;
import java.io.File;

/**
 * An interface for dynamic runtime compilation and classloading.
 * 
 * @author srichter
 */
public interface DynamicCompileClassLoader {

    /**
     * @return a deep copy instance
     */
    public DynamicCompileClassLoader copy();

    /**
     * Loads the source code from file, compiles it to the apropriate .class file
     * and loads the class into the VM.
     *
     * IMPORTANT: packages and folder structures must match! (baseDir == default
     * package root directory)
     *
     * @param sourceFile - the source futureCodeFile to compile and load
     * @param keepClassFile - keep the created class?
     * @return the compiled and loaded class defined in the source
     * @throws de.cismet.jpressocore.classloading.DynamicCompilingException
     */
    public <T> Class<? extends T> compileAndLoadClass(final File sourceFile, final Class<T> clazz) throws DynamicCompilingException;

    /**
     * 
     * @param className
     * @param code
     * @return the compiled and loaded class defined in the string with the given name
     * @throws de.cismet.jpressocore.serviceprovider.exceptions.DynamicCompilingException
     */
    public <T> Class<? extends T> compileAndLoadClass(final String className, final String code, final Class<T> clazz) throws DynamicCompilingException;

    /**
     *
     * @return the baseDir which acts as the default package root directory
     */
    public String getBaseDir();

    /**
     *
     * @return this classloaders additional classpath as String-representation
     */
    public String getCompileClasspath();

    /**
     * 
     * @return the delegation filter regex for classnames, inverting the classloader's standard delegation
     */
    public String getFilter();

    /**
     * 
     * @return true if the classloader's standard delegation is parent-first
     */
    public boolean isParentFirstDelegation();
}
