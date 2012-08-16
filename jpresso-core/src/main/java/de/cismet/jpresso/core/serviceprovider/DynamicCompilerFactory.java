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

import de.cismet.jpresso.core.classloading.compilation.DynamicCompilerImpl;

/**
 * Factory for DynamicCompilers.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public abstract class DynamicCompilerFactory {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DynamicCompilerImpl delegating to StandardJavaCompiler
     */
    public static DynamicCompiler createDynamicCompiler() {
        return new DynamicCompilerImpl();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   compiler  DOCUMENT ME!
     *
     * @return  DynamicCompilerImpl delegating to the given compiler instance
     */
    public static DynamicCompiler createDynamicCompiler(final JavaCompiler compiler) {
        return new DynamicCompilerImpl(compiler);
    }
}
