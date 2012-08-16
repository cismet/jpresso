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
package de.cismet.jpresso.core.classloading;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintStream;

import java.net.URL;
import java.net.URLClassLoader;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;

import de.cismet.jpresso.core.data.JavaClassDefinition;
import de.cismet.jpresso.core.kernel.ImportMetaInfo;
import de.cismet.jpresso.core.serviceprovider.CompilerResult;
import de.cismet.jpresso.core.serviceprovider.DynamicCompileClassLoader;
import de.cismet.jpresso.core.serviceprovider.DynamicCompiler;
import de.cismet.jpresso.core.serviceprovider.DynamicCompilerFactory;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.core.serviceprovider.exceptions.DynamicCompilingException;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import de.cismet.jpresso.core.utils.URLTools;

/**
 * A classloader that can compile source to classes and load them into the jvm It uses an array of resource-urls as
 * classpath extention.
 *
 * <p>The delegation can be switched between child-first and parent-first delegation by setting the
 * parentFirstDelegation flag (default: is true).</p>
 *
 * <p>A filter can be set, to apply the opposite delegation on classes matching the filter.</p>
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
class DynamicCompileClassLoaderImpl extends URLClassLoader implements DynamicCompileClassLoader {

    //~ Static fields/initializers ---------------------------------------------

    private static final String DOT = ".";
//    private static final String MASKED_FILE_SEPERATOR = "\\" + File.separator;

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    // this classloaders additional (ext) classpath
    private final String compileClasspath;
    // this classloaders default package root directory
    private final String baseDir;
    private Pattern filter;
    private boolean parentFirstDelegation = true;
    private final DynamicCompiler dynCompiler = DynamicCompilerFactory.createDynamicCompiler();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DynamicCompileClassLoaderImpl object.
     *
     * @param  urls     DOCUMENT ME!
     * @param  baseDir  DOCUMENT ME!
     */
    DynamicCompileClassLoaderImpl(final URL[] urls, final String baseDir) {
        // Make the classloader child of the current ContextClassloader to make other loaded Classes visible
        // to the classes we are going to load!
        this(urls, Thread.currentThread().getContextClassLoader(), baseDir, true, null);
    }

    /**
     * Creates a new DynamicCompileClassLoaderImpl object.
     *
     * @param   urls                   DOCUMENT ME!
     * @param   parent                 DOCUMENT ME!
     * @param   baseDir                DOCUMENT ME!
     * @param   parentFirstDelegation  DOCUMENT ME!
     * @param   delegationFilterRegex  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    DynamicCompileClassLoaderImpl(final URL[] urls,
            final ClassLoader parent,
            final String baseDir,
            final boolean parentFirstDelegation,
            final String delegationFilterRegex) {
        super(urls, parent);
        // further checks...
        if (urls == null) {
            throw new IllegalArgumentException("No nullpointer allowed for URL[] urls in DynamicCompileClassLoader!");
        }
        if (baseDir == null) {
            throw new IllegalArgumentException(
                "No nullpointer allowed for String baseDir in DynamicCompileClassLoader!");
        }
        final File base = new File(baseDir);
        if (!base.isDirectory()) {
            throw new IllegalArgumentException(
                "String baseDir in DynamicCompileClassLoader does not represent a valid, existing directory!");
        }
        // checks done...process add. classpath...
        final StringBuilder classpathBuilder = new StringBuilder();
        for (final URL url : getURLs()) {
            final File f = URLTools.convertURLToFile(url);
            classpathBuilder.append(f.getAbsolutePath() + File.pathSeparator);
        }
        // finally set attributes
        if (delegationFilterRegex != null) {
            this.filter = Pattern.compile(delegationFilterRegex);
        } else {
            filter = null;
        }
        this.parentFirstDelegation = parentFirstDelegation;
        this.compileClasspath = classpathBuilder.toString();
        this.baseDir = base.getAbsolutePath();
    }

    /**
     * Copy constructor.
     *
     * @param  instanceToCopy  urls
     */
    private DynamicCompileClassLoaderImpl(final DynamicCompileClassLoaderImpl instanceToCopy) {
        // Make the classloader child of the current ContextClassloader to make other loaded Classes visible
        // to the classes we are going to load!
        this(instanceToCopy.getURLs(),
            instanceToCopy.getParent(),
            instanceToCopy.baseDir,
            instanceToCopy.parentFirstDelegation,
            (instanceToCopy.filter != null) ? instanceToCopy.filter.pattern() : null);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public DynamicCompileClassLoader copy() {
        return new DynamicCompileClassLoaderImpl(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   className  DOCUMENT ME!
     * @param   compRes    bytecode
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ClassNotFoundException  DOCUMENT ME!
     */
    private synchronized Class<?> getTheClass(final String className, final CompilerResult compRes)
            throws ClassNotFoundException {
        // load all classes in the compilerresult (important for nested classes!)
        for (final String currentClass : compRes.getAvailableClasses()) {
            final byte[] bytecode = compRes.getByteCodeForClass(currentClass);
            final Class<?> newClass = defineClass(currentClass, bytecode, 0, bytecode.length);
            if (currentClass.equals(className)) {
                // return the class to load
                return newClass;
            }
        }
        throw new ClassNotFoundException("Can not find Class " + className + " in the compiler result!");
    }

    /**
     * Changed delegation for Class AssignerBase. These are loaded with child-first, all others are loaded as
     * parent-first. This creates thread-safe static AssignerBases, which are only visible to the appropriate Assigner.
     *
     * <p>DANGER: EVIL HACK!</p>
     *
     * @param   name  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ClassNotFoundException  DOCUMENT ME!
     */
    @Override
    public final synchronized Class<?> loadClass(final String name) throws ClassNotFoundException {
        final boolean filtered = ((getFilter() != null) && filter.matcher(name).matches());
        if (isParentFirstDelegation() == filtered) {
            try {
                // First check whether it'currentClass already been loaded, if so use it
                Class loadedClass = findLoadedClass(name);

                // Not loaded, try to load it
                if (loadedClass == null) {
                    // Ignore parent delegation try to load locally
                    loadedClass = findClass(name);
                    // If not found locally, use normal parent delegation in URLClassloader
                    if (loadedClass == null) {
                        // throws ClassNotFoundException if not found in delegation hierarchy at all
                        loadedClass = super.loadClass(name);
                    }
                }
                // will never return null (ClassNotFoundException will be thrown instead)
                return loadedClass;
//        }
            } catch (Throwable e) {
                // If not found locally, use normal parent delegation in URLClassloader
                return super.getParent().loadClass(name);
            }
        }
        return super.loadClass(name);
    }

    /**
     * Loads the source code from file, compiles it to the apropriate .class file and loads the class into the VM.
     *
     * <p>IMPORTANT: packages and folder structures must match! (baseDir == default package root directory)</p>
     *
     * @param   <T>         keepClassFile - keep the created class?
     * @param   sourceFile  - the source futureCodeFile to compile and load
     * @param   clazz       DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  DynamicCompilingException  de.cismet.jpressocore.classloading.DynamicCompilingException
     */
    @Override
    public <T> Class<? extends T> compileAndLoadClass(final File sourceFile, final Class<T> clazz)
            throws DynamicCompilingException {
        try {
            final String sourceCode = JPressoFileManager.getDefault().readFileToString(sourceFile);
            // find the canonical name of the class
            final String className = sourceFile.getAbsolutePath()
                        .substring(baseDir.length() + 1,
                                sourceFile.getAbsolutePath().length()
                                - (JPressoFileManager.END_JAVA.length() + 1))
                        .replace(File.separator, DOT);
//            final String className = sourceFile.getAbsolutePath().substring(baseDir.length() + 1, sourceFile.getAbsolutePath().length() - (JPressoFileManager.END_JAVA.length() + 1)).replaceAll(MASKED_FILE_SEPERATOR, DOT);
            return compileAndLoadClass(className, sourceCode, clazz);
        } catch (Exception ex) {
            throw new DynamicCompilingException("Internal Compilation Bug: " + ex.toString(), "");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   <T>        DOCUMENT ME!
     * @param   className  DOCUMENT ME!
     * @param   code       DOCUMENT ME!
     * @param   clazz      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  DynamicCompilingException  de.cismet.jpressocore.serviceprovider.exceptions.DynamicCompilingException
     */
    @Override
    public <T> Class<? extends T> compileAndLoadClass(final String className, final String code, final Class<T> clazz)
            throws DynamicCompilingException {
        return compileAndLoadClass(new JavaClassDefinition(className, code)).asSubclass(clazz);
    }

    /**
     * Fix for strange behaviour of the Java Compiler API: Changed classes were no longer automatically recompiled, as
     * long as any class with a date newer than its source-file exists. So this fix sets the timestamps of all
     * java-sources in the project to the timestamp of the newest. WARNING: directories are JPresso-specific!
     */
    private void fixRecompile() {
        final File projDir = new File(baseDir);
        final File codeDir = new File(projDir, JPressoFileManager.DIR_CDE);
        final File[] baseSources = projDir.listFiles(new JavaFileFilter());
        final File[] codeSources = codeDir.listFiles(new JavaFileFilter());
        if (projDir.isDirectory() && codeDir.isDirectory()) {
            final File[] allSources = Arrays.copyOf(baseSources, baseSources.length + codeSources.length);
            System.arraycopy(codeSources, 0, allSources, baseSources.length, codeSources.length);
            long changeDate = 0L;
            for (final File cur : allSources) {
                if (cur.lastModified() > changeDate) {
                    changeDate = cur.lastModified();
                }
            }
            for (final File cur : allSources) {
                cur.setLastModified(changeDate);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   source  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  DynamicCompilingException  de.cismet.jpressocore.serviceprovider.exceptions.DynamicCompilingException
     */
    private synchronized Class<?> compileAndLoadClass(final JavaClassDefinition source)
            throws DynamicCompilingException {
        if (log.isDebugEnabled()) {
            log.debug("Additional Compiler Classpath: " + getCompileClasspath());
        }
        fixRecompile();
        try {
            final ByteArrayOutputStream errorOutStream = new ByteArrayOutputStream();
            System.setErr(new PrintStream(errorOutStream));
            final CompilerResult compRes = dynCompiler.compile(getCompileClasspath(), source);
            log.info("Classes to be loaded: " + compRes.getAvailableClasses());

            // if compilation was ok, load future CodeFile in byte[]...
            final String className = source.getClassName();
            if (compRes.isSuccessful()) {
                // everything fine!
                return getTheClass(className, compRes);
            } else {
                // if there where compilation errors, do some error finding...
                final List<Diagnostic<? extends JavaFileObject>> err = compRes.getDiagnostics().getDiagnostics();
                final List<String> det = TypeSafeCollections.newArrayList();
                final List<String> pos = TypeSafeCollections.newArrayList();
                final Iterator<Diagnostic<? extends JavaFileObject>> it = err.iterator();
                while (it.hasNext()) {
                    final Diagnostic<? extends JavaFileObject> d = it.next();
                    final FileObject fo = d.getSource();
                    try {
                        final String[] errs = fo.getCharContent(true).toString().split("\n");
                        if (d.getKind() == Diagnostic.Kind.ERROR) {
                            final int no = ((int)d.getLineNumber()) - 1;
                            final StringBuilder detail = new StringBuilder();
                            final String line = errs[no].trim();
                            detail.append(d);
                            detail.append("\n    >>>     ");
                            detail.append(line);
                            detail.append("\n\n");
                            det.add(detail.toString());
                            final int x = line.indexOf(ImportMetaInfo.ERROR_FINDER);
                            if (x != -1) {
                                String errorsource = line.substring(x + ImportMetaInfo.ERROR_FINDER.length());
                                if (errorsource.trim().length() < 1) {
                                    errorsource = "a source field is empty or contains only whitespaces!";
                                }
                                pos.add(errorsource);
                            }
                        }
                    } catch (IOException ex) {
                        log.warn("IOException", ex);
                    } catch (Exception ex) {
                        if (log.isDebugEnabled()) {
                            // ignore rest
                            log.debug("Unexpected but unimportant Exception", ex);
                        }
                    }
                }
                final String codeView = createSourceCodeView(source);
                final DynamicCompilingException dce = new DynamicCompilingException(errorOutStream.toString(),
                        pos,
                        det,
                        codeView);
                log.error("DynamicCompilationException: " + codeView, dce);
                // and throw the result via exception...
                throw dce;
            }
        } catch (DynamicCompilingException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DynamicCompilingException("Internal Compilation Bug: " + ex.toString(),
                createSourceCodeView(source));
        } finally {
            System.setErr(System.out);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   jcd  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String createSourceCodeView(final JavaClassDefinition jcd) {
        final StringBuilder sb = new StringBuilder(jcd.getSourceCode().length() + 50);
        sb.append("\n\nSource Code for " + jcd.getClassName()).append("\n");
        int i = sb.length();
        while (i > -1) {
            sb.append("--");
            --i;
        }
        sb.append("\n");
        sb.append(jcd.getSourceCode());
        return sb.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  this classloaders additional classpath as String-representation
     */
    @Override
    public String getCompileClasspath() {
        return compileClasspath;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the baseDir which acts as the default package root directory
     */
    @Override
    public String getBaseDir() {
        return baseDir;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String getFilter() {
        return filter.pattern();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean isParentFirstDelegation() {
        return parentFirstDelegation;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    static class JavaFileFilter implements FileFilter {

        //~ Methods ------------------------------------------------------------

        @Override
        public boolean accept(final File pathname) {
            return pathname.getAbsolutePath().toLowerCase().endsWith("." + JPressoFileManager.END_JAVA);
        }
    }
}
