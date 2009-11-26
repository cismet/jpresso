/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.classloading;

import de.cismet.jpresso.core.serviceprovider.DynamicCompileClassLoader;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * Helps creating DynamicClassLoaders for multiple possible parent/jar configurations.
 * This class sets the default-package root directory to the passed project directory. 
 * 
 * @author stefan
 */
public final class DynamicCompileClassLoaderFactory {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DynamicCompileClassLoaderFactory.class);

    /**
     * 
     * @return Classloader with no jars in classpath and current thread classloader as parent.
     */
    public static DynamicCompileClassLoader createDynamicClassLoader(final String projectDir) {
        return createDynamicClassLoader(new HashSet<File>(), projectDir);
    }

    /**
     * 
     * @param parent
     * @return Classloader with no jars in classpath and param parent as parent.
     */
    public static DynamicCompileClassLoader createDynamicClassLoader(final ClassLoader parent, final String projectDir) {
        return createDynamicClassLoader(new HashSet<File>(), parent, projectDir);
    }

    /**
     * 
     * @param classPathEntries
     * @return Classloader with jars from param in classpath and current thread classloader as parent.
     */
    public static DynamicCompileClassLoader createDynamicClassLoader(final Set<File> classPathEntires, final String projectDir) {
        return createDynamicClassLoader(classPathEntires, Thread.currentThread().getContextClassLoader(), projectDir);
    }

    /**
     * 
     * @param classPathEntries
     * @param parent
     * @return Classloader with jars from param in classpath and param classloader as parent.
     */
    public static DynamicCompileClassLoader createDynamicClassLoader(final Set<File> classPathEntires, final ClassLoader parent, final String projectDir) {
        return new DynamicCompileClassLoaderImpl(processFiles(classPathEntires), parent, projectDir, true, null);
    }

    /**
     * 
     * @param classPathEntries
     * @return Classloader with jars from param in classpath that has NO parent classloader.
     */
    public static DynamicCompileClassLoader createDynamicClassLoaderWithoutParent(final Set<File> classPathEntires, final String projectDir) {
        return new DynamicCompileClassLoaderImpl(processFiles(classPathEntires), null, projectDir, true, null);
    }
    //-------------------------------------- Creator methods with flexible delegation/filtering -----------------------------------------

    /**
     * 
     * @param projectDir
     * @param parentFirstDelegation
     * @param delegationFilterRegex
     * @return
     */
    public static DynamicCompileClassLoader createDynamicClassLoader(final String projectDir, final boolean parentFirstDelegation, final String delegationFilterRegex) {
        return createDynamicClassLoader(new HashSet<File>(), projectDir, parentFirstDelegation, delegationFilterRegex);
    }

    /**
     * 
     * @param parent
     * @param projectDir
     * @param parentFirstDelegation
     * @param delegationFilterRegex
     * @return
     */
    public static DynamicCompileClassLoader createDynamicClassLoader(final ClassLoader parent, final String projectDir, final boolean parentFirstDelegation, final String delegationFilterRegex) {
        return createDynamicClassLoader(new HashSet<File>(), parent, projectDir, parentFirstDelegation, delegationFilterRegex);
    }

    /**
     * 
     * @param classPathEntires
     * @param projectDir
     * @param parentFirstDelegation
     * @param delegationFilterRegex
     * @return
     */
    public static DynamicCompileClassLoader createDynamicClassLoader(final Set<File> classPathEntires, final String projectDir, final boolean parentFirstDelegation, final String delegationFilterRegex) {
        return createDynamicClassLoader(classPathEntires, Thread.currentThread().getContextClassLoader(), projectDir, parentFirstDelegation, delegationFilterRegex);
    }

    /**
     * 
     * @param classPathEntires
     * @param projectDir
     * @param parentFirstDelegation
     * @param delegationFilterRegex
     * @return
     */
    public static DynamicCompileClassLoader createDynamicClassLoaderWithoutParent(final Set<File> classPathEntires, final String projectDir, final boolean parentFirstDelegation, final String delegationFilterRegex) {
        return new DynamicCompileClassLoaderImpl(processFiles(classPathEntires), null, projectDir, parentFirstDelegation, delegationFilterRegex);
    }

    /**
     * 
     * @param classPathEntires
     * @param parent
     * @param projectDir
     * @param parentFirstDelegation
     * @param delegationFilterRegex
     * @return
     */
    public static DynamicCompileClassLoader createDynamicClassLoader(final Set<File> classPathEntires, final ClassLoader parent, final String projectDir, final boolean parentFirstDelegation, final String delegationFilterRegex) {
        return new DynamicCompileClassLoaderImpl(processFiles(classPathEntires), parent, projectDir, parentFirstDelegation, delegationFilterRegex);
    }

    /**
     * Convert a Set<Files> to an URL[]
     * 
     * @param classPathEntries
     * @return
     */
    @SuppressWarnings("deprecation")
    private static URL[] processFiles(final Set<File> classPathEntries) {
        final List<URL> fileFilter = TypeSafeCollections.newArrayList();
        //add own jar (kernel classfiles!) to classpath
        if (classPathEntries != null) {
            for (final File f : classPathEntries) {
                if (f != null && (f.isFile() && (f.getAbsolutePath().toLowerCase().endsWith("." + JPressoFileManager.END_JAR)) || f.isDirectory())) {
                    try {
                        //use deprecated API to handle Files with space in name to replace %20
                        fileFilter.add(f.toURL());
                    } catch (MalformedURLException ex) {
                        log.warn("Malformed URL in Classpath File: " + f.getAbsolutePath() + " !", ex);
                    }
                }
            }
        } else {
            log.warn("classPathEntries == null!");
        }
        final URL[] urls = new URL[fileFilter.size() + 1];
        for (int i = 0; i < fileFilter.size(); i++) {
            urls[i] = fileFilter.get(i);
        }

        urls[fileFilter.size()] = JPressoFileManager.locateJarForClass(DynamicCompileClassLoaderFactory.class);
        return urls;
    }
}
