/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.classloading.compilation;

import de.cismet.jpresso.core.serviceprovider.CompilerResult;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 * Encapsulates important information about compval finished code-compilation,
 * like errors, sourcefiles, classfiles, success-flag.
 * 
 * @author stefan
 */
public class CompilerResultImpl implements CompilerResult {

    public CompilerResultImpl(final Map<String, ClassByteCode> byteCodeMap, final boolean successful, final DiagnosticCollector<JavaFileObject> diagnostics) {
        if (diagnostics == null || byteCodeMap == null) {
            throw new IllegalArgumentException("Arguments for CompilerArtifacts can not be null!!!");
        }
        this.successful = successful;
        this.diagnostics = diagnostics;
        this.byteCodeMap = byteCodeMap;
        this.sortedClassNames = TypeSafeCollections.newArrayList(byteCodeMap.keySet());
        //sort the classnames to an appropriate order -> nested/inner classes first
        Collections.sort(this.sortedClassNames, new Comparator<String>() {

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
    private final Map<String, ClassByteCode> byteCodeMap;
    private final List<String> sortedClassNames;
    private final DiagnosticCollector<JavaFileObject> diagnostics;
    private boolean successful;

    public boolean isSuccessful() {
        return successful;
    }

    public DiagnosticCollector<JavaFileObject> getDiagnostics() {
        return diagnostics;
    }

    public byte[] getByteCodeForClass(final String className) {
        final ClassByteCode bca = byteCodeMap.get(className);
        if (bca != null) {
            return bca.getBytes();
        }
        return null;
    }

    public List<String> getAvailableClasses() {
        return sortedClassNames;
    }
}
