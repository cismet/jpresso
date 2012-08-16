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

import javax.tools.JavaCompiler;

import de.cismet.jpresso.core.data.JavaClassDefinition;

/**
 * Interface for a DynamicCompiler.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface DynamicCompiler {

    //~ Methods ----------------------------------------------------------------

    /**
     * Compiles JavaClassDefinitions using the given compiler-classpath.
     *
     * @param   compileClassPath  DOCUMENT ME!
     * @param   sources           DOCUMENT ME!
     *
     * @return  the CompilerResult, containing the compiled classes and/or error diagnostics,
     */
    @SuppressWarnings(value = "unchecked")
    CompilerResult compile(final String compileClassPath, final JavaClassDefinition... sources);

    /**
     * Sets the compiler to delegate to.
     *
     * @param  compiler  DOCUMENT ME!
     */
    void setCompiler(JavaCompiler compiler);
}
