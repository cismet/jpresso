/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.jpresso.core.data;

/**
 * Definition for a Java class. Consists of classname and sourcecode.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class JavaClassDefinition {

    //~ Instance fields --------------------------------------------------------

    private final String className;
    private final String sourceCode;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new JavaClassDefinition object.
     *
     * @param  className   DOCUMENT ME!
     * @param  sourceCode  DOCUMENT ME!
     */
    public JavaClassDefinition(final String className, final String sourceCode) {
        this.className = className;
        this.sourceCode = sourceCode;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the class source code
     */
    public String getSourceCode() {
        return sourceCode;
    }
}
