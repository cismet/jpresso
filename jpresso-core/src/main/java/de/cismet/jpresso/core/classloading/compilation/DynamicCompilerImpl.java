/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.classloading.compilation;

import de.cismet.jpresso.core.classloading.compilation.*;
import de.cismet.jpresso.core.data.JavaClassDefinition;
import de.cismet.jpresso.core.serviceprovider.DynamicCompiler;
import de.cismet.jpresso.core.serviceprovider.CompilerResult;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
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

/**
 * Class for programmatically code compilation. Uses JavaCompiler API.
 * (JDK >= 1.6 !!)
 * 
 * @author stefan
 */
public class DynamicCompilerImpl implements DynamicCompiler {

    public DynamicCompilerImpl(JavaCompiler compiler) {
        setCompiler(compiler);
    }
    
    public DynamicCompilerImpl() {
        setCompiler(defaultCompiler);
    }

    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DynamicCompilerImpl.class);
    private final JavaCompiler defaultCompiler = ToolProvider.getSystemJavaCompiler();
    private JavaCompiler compiler;

    /**
     * 
     * @param baseDir
     * @param compileClassPath
     * @param sources
     * @return
     */
    private CompilerResult compile(final String compileClassPath, final JavaFileObject... sources) {
        if (compiler == null) {
            String msg = "Can not find the default java compiler! ToolProvider.getSystemJavaCompiler() returned null - is tools.jar (jdk >= 1.6!) on the classpath?";
            //HINT if you have problems with class code levels, assert that tools.jar used by this programm is the same version as your jdk (>= 1.6)!
            log.error(msg);
            throw new IllegalStateException(msg);
        }

        final List<JavaFileObject> toCompile = Arrays.asList(sources);
        final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        final MemoryFileManager memoryFileManager = new MemoryFileManager(diagnostics, Locale.getDefault(), null);
        final String[] options = new String[]{"-cp", compileClassPath};
        final JavaCompiler.CompilationTask task = compiler.getTask(null, memoryFileManager, diagnostics, Arrays.asList(options), null, toCompile);
        task.setLocale(Locale.getDefault());
        boolean success = task.call();
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
     * 
     * @param compileClassPath
     * @param sources
     * @return
     */
    @Override
    public CompilerResult compile(final String compileClassPath, final JavaClassDefinition... sources) {
        final JavaFileObject[] jfos = new JavaFileObject[sources.length];
        int i = -1;
        for (JavaClassDefinition jcd : sources) {
            jfos[++i] = MemoryFileManager.createFileObjectForSource(jcd);
        }
        return compile(compileClassPath, jfos);
    }

    /**
     * 
     * @param compiler
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
 * FileManager that supports a memory filesystem. Used to execute compilation
 * tasks in memory.
 * 
 * @author srichter
 */
final class MemoryFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    public static final String PREFIX = "mem:///";
    public static final String SLASH = "/";
//    public static final String MASKED_DOT = "\\.";
    public static final String DOT = ".";
    private final Map<String, ClassByteCode> classesHash = TypeSafeCollections.newHashMap();
    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MemoryFileManager.class);

    /**
     * An in-memory-class-file
     */
    static final class MemoryClassFileObject extends SimpleJavaFileObject implements ClassByteCode {

        private final ByteArrayOutputStream os = new ByteArrayOutputStream();

        private MemoryClassFileObject(final String className) {
            super(URI.create(PREFIX + (className.replace(DOT, SLASH)) + Kind.CLASS.extension), Kind.CLASS);
//            super(URI.create(PREFIX + (className.replaceAll(MASKED_DOT, SLASH)) + Kind.CLASS.extension), Kind.CLASS);
        }

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
     * An in-memory-source-file
     */
    static final class MemorySourceFileObject extends SimpleJavaFileObject {

        private final String code;

        MemorySourceFileObject(final String className, final String code) {
            super(URI.create(PREFIX + (className.replace(DOT, SLASH)) + Kind.SOURCE.extension), Kind.SOURCE);
//            super(URI.create(PREFIX + (className.replaceAll(MASKED_DOT, SLASH)) + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public InputStream openInputStream() throws IOException {
            return new ByteArrayInputStream(code.getBytes());
        }

        @Override
        public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws IOException {
            return code;
        }
    }

    /**
     * 
     */
    public MemoryFileManager() {
        super(ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null));
    }

    /**
     * 
     * @param dListener
     * @param locale
     * @param charSet
     */
    public MemoryFileManager(final DiagnosticListener<JavaFileObject> dListener, final Locale locale, final Charset charSet) {
        super(ToolProvider.getSystemJavaCompiler().getStandardFileManager(dListener, locale, charSet));
    }

    /**
     * 
     * @param location
     * @param className
     * @param kind
     * @param sibling
     * @return
     * @throws java.io.IOException
     */
    @Override
    public JavaFileObject getJavaFileForOutput(final Location location, final String className, final Kind kind, final FileObject sibling) throws IOException {
        if (StandardLocation.CLASS_OUTPUT == location && JavaFileObject.Kind.CLASS == kind && sibling instanceof MemorySourceFileObject) {
            final MemoryClassFileObject clazz = new MemoryClassFileObject(className);
            classesHash.put(className, clazz);
            return clazz;
        } else {
            return super.getJavaFileForOutput(location, className, kind, sibling);
        }
    }

    /**
     * 
     * @param className
     * @return
     */
    public byte[] getClassBytes(final String className) {
        if (classesHash.containsKey(className)) {
            return classesHash.get(className).getBytes();
        }
        return null;
    }

    /**
     * 
     * @return
     */
    public Map<String, ClassByteCode> getClassByteCodes() {
        return classesHash;
    }

    /**
     * 
     * @param className
     * @param source
     * @return
     */
    public static JavaFileObject createFileObjectForSource(final JavaClassDefinition jcd) {
        return new MemorySourceFileObject(jcd.getClassName(), jcd.getSourceCode());
    }

    /**
     * 
     * @return
     */
    public Set<String> getAvailableClasses() {
        return classesHash.keySet();
    }
}
