/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.jpresso.core.serviceprovider;

import de.cismet.jpresso.core.data.JavaClassDefinition;
import javax.tools.JavaCompiler;

/**
 * Interface for a DynamicCompiler.
 * 
 * @author srichter
 */
public interface DynamicCompiler {

    /**
     * Compiles JavaClassDefinitions using the given compiler-classpath.
     * 
     * @param compileClassPath
     * @param sources
     * @return the CompilerResult, containing the compiled classes and/or error diagnostics,
     */
    @SuppressWarnings(value = "unchecked")
    public CompilerResult compile(final String compileClassPath, final JavaClassDefinition... sources);
    
    /**
     * Sets the compiler to delegate to.
     * 
     * @param compiler
     */
    public void setCompiler(JavaCompiler compiler);

}
