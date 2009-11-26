/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.jpresso.core.serviceprovider;

import java.util.List;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 * The result of a compilation process.
 * 
 * @author srichter
 */
public interface CompilerResult {
    
    /**
     * 
     * @return the canonical names of the compiled classes
     */
    public List<String> getAvailableClasses();
    
    /**
     * 
     * @param className
     * @return the bytecode for the given canonical classname
     */
    public byte[] getByteCodeForClass(final String className);
    
    /**
     * 
     * @return the diagnostics of the compilation process
     */
    public DiagnosticCollector<JavaFileObject> getDiagnostics();
    
    /**
     * 
     * @return true if the compilation was successful without errors
     */
    public boolean isSuccessful();

}
