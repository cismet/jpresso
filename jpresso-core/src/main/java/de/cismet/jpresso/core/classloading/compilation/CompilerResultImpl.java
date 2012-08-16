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
package de.cismet.jpresso.core.classloading.compilation;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import de.cismet.jpresso.core.serviceprovider.CompilerResult;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * Encapsulates important information about compval finished code-compilation, like errors, sourcefiles, classfiles,
 * success-flag.
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public class CompilerResultImpl implements CompilerResult {

    //~ Instance fields --------------------------------------------------------

    private final Map<String, ClassByteCode> byteCodeMap;
    private final List<String> sortedClassNames;
    private final DiagnosticCollector<JavaFileObject> diagnostics;
    private boolean successful;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CompilerResultImpl object.
     *
     * @param   byteCodeMap  DOCUMENT ME!
     * @param   successful   DOCUMENT ME!
     * @param   diagnostics  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public CompilerResultImpl(final Map<String, ClassByteCode> byteCodeMap,
            final boolean successful,
            final DiagnosticCollector<JavaFileObject> diagnostics) {
        if ((diagnostics == null) || (byteCodeMap == null)) {
            throw new IllegalArgumentException("Arguments for CompilerArtifacts can not be null!!!");
        }
        this.successful = successful;
        this.diagnostics = diagnostics;
        this.byteCodeMap = byteCodeMap;
        this.sortedClassNames = TypeSafeCollections.newArrayList(byteCodeMap.keySet());
        // sort the classnames to an appropriate order -> nested/inner classes first
        Collections.sort(this.sortedClassNames, new Comparator<String>() {

                @Override
                public int compare(final String o1, final String o2) {
                    int compval = 0;
                    for (int i = 0; i < o1.length(); ++i) {
                        if (o1.charAt(i) == '$') {
                            --compval;
                        }
                    }
                    for (int i = 0; i < o2.length(); ++i) {
                        if (o2.charAt(i) == '$') {
                            ++compval;
                        }
                    }
                    return compval;
                }
            });
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean isSuccessful() {
        return successful;
    }

    @Override
    public DiagnosticCollector<JavaFileObject> getDiagnostics() {
        return diagnostics;
    }

    @Override
    public byte[] getByteCodeForClass(final String className) {
        final ClassByteCode bca = byteCodeMap.get(className);
        if (bca != null) {
            return bca.getBytes();
        }
        return null;
    }

    @Override
    public List<String> getAvailableClasses() {
        return sortedClassNames;
    }
}
