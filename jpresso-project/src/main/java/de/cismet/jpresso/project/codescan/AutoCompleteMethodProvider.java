/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.codescan;

import de.cismet.jpresso.project.serviceprovider.CodeFunctionProvider;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.JavaSource.Phase;
import org.netbeans.api.java.source.Task;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;

/**
 * Provides all available method public-static method names from the code folder
 * sources for selfmade autocomplete support in mapping table.
 * 
 * @author srichter
 */
public class AutoCompleteMethodProvider implements CodeFunctionProvider {

    /**
     * 
     * @param codeDir
     */
    public AutoCompleteMethodProvider(final FileObject codeDir) {
        //uses ConcurrentHashMap, as it is accessed from edt if a TopComponent 
        //asks for methods and non-edt, if a file changes an methods become updated.
        this.methodCache = TypeSafeCollections.newConcurrentHashMap();
        this.codeDir = codeDir;
        this.fcListener = new FileChangeAdapter() {

            @Override
            public void fileChanged(FileEvent fe) {
                //if a file was changed, refresh its method list.
                readMethodsFromSourceFiles(fe.getFile());
            }

            @Override
            public void fileDeleted(FileEvent fe) {
                //if a file was deleted, remove methods from cache.
                methodCache.remove(fe.getFile().getNameExt());
            }

            @Override
            public void fileRenamed(FileRenameEvent fe) {
                //if a file was renamed, delete the entry under the old name...
                final List<String> oldL = methodCache.remove(fe.getName() + "." + fe.getExt());
                //...then modify and store it under the new name
                if (oldL != null) {
                    final List<String> newL = TypeSafeCollections.newArrayList(oldL.size());
                    for (final String s : oldL) {
                        newL.add(s.replaceFirst("^.+\\.", fe.getFile().getName() + "."));
                    }
                    methodCache.put(fe.getFile().getNameExt(), newL);
                } else {
                    readMethodsFromSourceFiles(fe.getFile());
                }

            }
        };
        this.codeDir.addFileChangeListener(fcListener);
    }
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final FileObject codeDir;
    private final FileChangeAdapter fcListener;
    private final Map<String, List<String>> methodCache;

    /**
     * Scans java sourcefiles for public-static methods and adds their names
     * to the available-methods-cache.
     * 
     * @param sources to scan
     */
    public final void readMethodsFromSourceFiles(final FileObject... sources) {
        for (final FileObject fo : sources) {
            if (fo != null && fo.getExt().equals(JPressoFileManager.END_JAVA) && fo.isValid()) {
                if (log.isDebugEnabled()) {
                    log.debug("Start scanning java file " + fo.getPath() + " for public-static methods!");
                }
                final JavaSource javaSource = JavaSource.forFileObject(fo);
                try {
                    javaSource.runUserActionTask(new Task<CompilationController>() {

                        public void run(final CompilationController compilationController) throws Exception {
                            compilationController.toPhase(Phase.ELEMENTS_RESOLVED);
                            final List<String> classPSMethods = new MethodVisitor(compilationController).scan(compilationController.getCompilationUnit(), null);
                            if (classPSMethods != null) {
                                methodCache.put(fo.getNameExt(), classPSMethods);
                            }
                        }
                    }, true);
                } catch (IOException ex) {
                    log.error("IO Exception while scanning java source for methods.", ex);
                }
            }
        }
        if (log.isDebugEnabled()) {
            final StringBuilder sb = new StringBuilder();
            for (final String k : methodCache.keySet()) {
                sb.append("\t").append(k).append(" -> ").append(methodCache.get(k)).append("\n");
            }
            log.debug("Method Cache is now: \n" + sb.toString());
        }
    }

    /**
     * (re)initialise method list
     */
    public void refresh() {
        methodCache.clear();
        readMethodsFromSourceFiles(codeDir.getChildren());
    }

    /**
     * @see CodeFunctionProvider
     * 
     * @return list of all currently available public-static methods.
     */
    public List<String> getFunctionList() {
        final List<String> ret = TypeSafeCollections.newArrayList();
        for (final List<String> cur : methodCache.values()) {
            ret.addAll(cur);
        }
        return ret;
    }

    /**
     * unregisterListeners filechangelistener on code-directory
     */
    public final void unregisterListeners() {
        if (fcListener != null) {
            codeDir.removeFileChangeListener(fcListener);
        }
    }
//    private void notifyListeners() {
//        for (final ChangeListener l : listener) {
//            l.stateChanged(new ChangeEvent(this));
//        }
//    }
}
