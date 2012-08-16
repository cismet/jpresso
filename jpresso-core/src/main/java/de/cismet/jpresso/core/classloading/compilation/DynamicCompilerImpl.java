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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URI;

import java.nio.charset.Charset;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import de.cismet.jpresso.core.data.JavaClassDefinition;
import de.cismet.jpresso.core.serviceprovider.CompilerResult;
import de.cismet.jpresso.core.serviceprovider.DynamicCompiler;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * Class for programmatically code compilation. Uses JavaCompiler API. (JDK >= 1.6 !!)
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public class DynamicCompilerImpl implements DynamicCompiler {

    //~ Instance fields --------------------------------------------------------

    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DynamicCompilerImpl.class);
    private final JavaCompiler defaultCompiler = ToolProvider.getSystemJavaCompiler();
    private JavaCompiler compiler;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DynamicCompilerImpl object.
     */
    public DynamicCompilerImpl() {
        setCompiler(defaultCompiler);
    }

    /**
     * Creates a new DynamicCompilerImpl object.
     *
     * @param  compiler  DOCUMENT ME!
     */
    public DynamicCompilerImpl(final JavaCompiler compiler) {
        setCompiler(compiler);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   compileClassPath  DOCUMENT ME!
     * @param   sources           DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    private CompilerResult compile(final String compileClassPath, final JavaFileObject... sources) {
        if (compiler == null) {
            final String msg =
                "Can not find the default java compiler! ToolProvider.getSystemJavaCompiler() returned null - is tools.jar (jdk >= 1.6!) on the classpath?";
            // HINT if you have problems with class code levels, assert that tools.jar used by this programm is the
            // same version as your jdk (>= 1.6)!
            log.error(msg);
            throw new IllegalStateException(msg);
        }

        final List<JavaFileObject> toCompile = Arrays.asList(sources);
        final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        final MemoryFileManager memoryFileManager = new MemoryFileManager(diagnostics, Locale.getDefault(), null);
        final String[] options = new String[] { "-cp", compileClassPath };
        final JavaCompiler.CompilationTask task = compiler.getTask(
                null,
                memoryFileManager,
                diagnostics,
                Arrays.asList(options),
                null,
                toCompile);
        task.setLocale(Locale.getDefault());
        final boolean success = task.call();
        if (success) {
            log.info("Dynamic compilation successful!");
        } else {
            log.warn("Dynamic compilation completed with errors!");
            for (final Diagnostic<?> d : diagnostics.getDiagnostics()) {
                log.error(d);
            }
        }
        try {
            memoryFileManager.flush();
            memoryFileManager.close();
        } catch (IOException ex) {
            log.warn(ex.getMessage(), ex);
        }
        return new CompilerResultImpl(memoryFileManager.getClassByteCodes(), success, diagnostics);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   compileClassPath  DOCUMENT ME!
     * @param   sources           DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public CompilerResult compile(final String compileClassPath, final JavaClassDefinition... sources) {
        final JavaFileObject[] jfos = new JavaFileObject[sources.length];
        int i = -1;
        for (final JavaClassDefinition jcd : sources) {
            jfos[++i] = MemoryFileManager.createFileObjectForSource(jcd);
        }
        return compile(compileClassPath, jfos);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  compiler  DOCUMENT ME!
     */
    @Override
    public void setCompiler(final JavaCompiler compiler) {
        if (compiler != null) {
            this.compiler = compiler;
        } else {
            this.compiler = defaultCompiler;
            log.warn("Try to set compiler == null. Setting to SystemJavaCompiler instead!");
        }
    }
}

/**
 * FileManager that supports a memory filesystem. Used to execute compilation tasks in memory.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
final class MemoryFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    //~ Static fields/initializers ---------------------------------------------

    public static final String PREFIX = "mem:///";
    public static final String SLASH = "/";
//    public static final String MASKED_DOT = "\\.";
    public static final String DOT = ".";

    //~ Instance fields --------------------------------------------------------

    private final Map<String, ClassByteCode> classesHash = TypeSafeCollections.newHashMap();
    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MemoryFileManager.class);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MemoryFileManager object.
     */
    public MemoryFileManager() {
        super(ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null));
    }

    /**
     * Creates a new MemoryFileManager object.
     *
     * @param  dListener  DOCUMENT ME!
     * @param  locale     DOCUMENT ME!
     * @param  charSet    DOCUMENT ME!
     */
    public MemoryFileManager(final DiagnosticListener<JavaFileObject> dListener,
            final Locale locale,
            final Charset charSet) {
        super(ToolProvider.getSystemJavaCompiler().getStandardFileManager(dListener, locale, charSet));
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   location   DOCUMENT ME!
     * @param   className  DOCUMENT ME!
     * @param   kind       DOCUMENT ME!
     * @param   sibling    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @Override
    public JavaFileObject getJavaFileForOutput(final Location location,
            final String className,
            final Kind kind,
            final FileObject sibling) throws IOException {
        if ((StandardLocation.CLASS_OUTPUT == location) && (JavaFileObject.Kind.CLASS == kind)
                    && (sibling instanceof MemorySourceFileObject)) {
            final MemoryClassFileObject clazz = new MemoryClassFileObject(className);
            classesHash.put(className, clazz);
            return clazz;
        } else {
            return super.getJavaFileForOutput(location, className, kind, sibling);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   className  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public byte[] getClassBytes(final String className) {
        if (classesHash.containsKey(className)) {
            return classesHash.get(className).getBytes();
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Map<String, ClassByteCode> getClassByteCodes() {
        return classesHash;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   jcd  className
     *
     * @return  DOCUMENT ME!
     */
    public static JavaFileObject createFileObjectForSource(final JavaClassDefinition jcd) {
        return new MemorySourceFileObject(jcd.getClassName(), jcd.getSourceCode());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Set<String> getAvailableClasses() {
        return classesHash.keySet();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * An in-memory-class-file.
     *
     * @version  $Revision$, $Date$
     */
    static final class MemoryClassFileObject extends SimpleJavaFileObject implements ClassByteCode {

        //~ Instance fields ----------------------------------------------------

        private final ByteArrayOutputStream os = new ByteArrayOutputStream();

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new MemoryClassFileObject object.
         *
         * @param  className  DOCUMENT ME!
         */
        private MemoryClassFileObject(final String className) {
            super(URI.create(PREFIX + (className.replace(DOT, SLASH)) + Kind.CLASS.extension), Kind.CLASS);
//            super(URI.create(PREFIX + (className.replaceAll(MASKED_DOT, SLASH)) + Kind.CLASS.extension), Kind.CLASS);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public byte[] getBytes() {
            return os.toByteArray();
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            return os;
        }
    }

    /**
     * An in-memory-source-file.
     *
     * @version  $Revision$, $Date$
     */
    static final class MemorySourceFileObject extends SimpleJavaFileObject {

        //~ Instance fields ----------------------------------------------------

        private final String code;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new MemorySourceFileObject object.
         *
         * @param  className  DOCUMENT ME!
         * @param  code       DOCUMENT ME!
         */
        MemorySourceFileObject(final String className, final String code) {
            super(URI.create(PREFIX + (className.replace(DOT, SLASH)) + Kind.SOURCE.extension), Kind.SOURCE);
//            super(URI.create(PREFIX + (className.replaceAll(MASKED_DOT, SLASH)) + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public InputStream openInputStream() throws IOException {
            return new ByteArrayInputStream(code.getBytes());
        }

        @Override
        public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws IOException {
            return code;
        }
    }
}
