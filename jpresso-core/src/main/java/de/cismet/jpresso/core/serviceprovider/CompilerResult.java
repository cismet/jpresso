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

import java.util.List;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 * The result of a compilation process.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface CompilerResult {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the canonical names of the compiled classes
     */
    List<String> getAvailableClasses();

    /**
     * DOCUMENT ME!
     *
     * @param   className  DOCUMENT ME!
     *
     * @return  the bytecode for the given canonical classname
     */
    byte[] getByteCodeForClass(final String className);

    /**
     * DOCUMENT ME!
     *
     * @return  the diagnostics of the compilation process
     */
    DiagnosticCollector<JavaFileObject> getDiagnostics();

    /**
     * DOCUMENT ME!
     *
     * @return  true if the compilation was successful without errors
     */
    boolean isSuccessful();
}
