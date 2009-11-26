/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.serviceprovider;

import de.cismet.jpresso.core.classloading.compilation.DynamicCompilerImpl;
import javax.tools.JavaCompiler;

/**
 * Factory for DynamicCompilers
 * @author srichter
 */
public abstract class DynamicCompilerFactory {
    
    /**
     * 
     * @return DynamicCompilerImpl delegating to StandardJavaCompiler
     */
    public static DynamicCompiler createDynamicCompiler() {
        return new DynamicCompilerImpl();
    }
    
    /**
     * 
     * @param compiler
     * @return DynamicCompilerImpl delegating to the given compiler instance
     */
    public static DynamicCompiler createDynamicCompiler(final JavaCompiler compiler) {
        return new DynamicCompilerImpl(compiler);
    }
}
