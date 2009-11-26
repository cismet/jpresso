
package de.cismet.jpresso.core.data;

/**
 * Definition for a Java class. Consists of classname and sourcecode.
 * 
 * @author srichter
 */
public class JavaClassDefinition {
    
    /**
     * 
     * @param className
     * @param sourceCode
     */
    public JavaClassDefinition(String className, String sourceCode) {
        this.className = className;
        this.sourceCode = sourceCode;
    }

    private final String className;
    private final String sourceCode;
    
    /**
     * 
     * @return the class name
     */
    public String getClassName() {
        return className;
    }
    
    /**
     * 
     * @return the class source code
     */
    public String getSourceCode() {
        return sourceCode;
    }
}
