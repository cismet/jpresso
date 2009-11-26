/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.classloading;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.regex.Pattern;

/**
 *
 * @author stefan
 */
public class FeatureClassLoader extends URLClassLoader {

    private final Pattern filter;
    private final boolean parentFirstDelegation;

    public FeatureClassLoader(final URL[] urls, final ClassLoader parent, final boolean parentFirstDelegation, final String delegationFilterRegex) {
        super(urls, parent);
        //further checks...
        if (urls == null) {
            throw new IllegalArgumentException("No nullpointer allowed for URL[] urls in DynamicCompileClassLoader!");
        }
        //finally set attributes
        if (delegationFilterRegex != null) {
            this.filter = Pattern.compile(delegationFilterRegex);
        } else {
            filter = null;
        }
        this.parentFirstDelegation = parentFirstDelegation;
    }

    /**
     * 
     * @param name
     * @return
     * @throws java.lang.ClassNotFoundException
     */
    @Override
    public final synchronized Class<?> loadClass(final String name) throws ClassNotFoundException {
        Class loadedClass;
        final boolean filtered = (filter != null && filter.matcher(name).matches());
        if (parentFirstDelegation == filtered) {
            try {
                // First check whether it's already been loaded, if so use it
                loadedClass = findLoadedClass(name);
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
            } catch (Throwable e) {
                // If not found locally, use normal parent delegation in URLClassloader
                loadedClass = super.getParent().loadClass(name);
            }
        } else {
            loadedClass = super.loadClass(name);
        }
        return loadedClass;
    }

    /**
     * 
     * @param name
     * @param clazz
     * @param findInterfacesAllowed
     * @return
     * @throws java.lang.ClassNotFoundException
     */
    public Class<?> loadAssignableClass(final String name, final Class<?> clazz, boolean findInterfacesAllowed) throws ClassNotFoundException {
        final Class loadedClass = loadClass(name);
        if (clazz != null) {
            if (loadedClass != null && clazz.isAssignableFrom(loadedClass) && (findInterfacesAllowed || !clazz.isInterface())) {
                return loadedClass;
            }
        } else {
            return loadedClass;
        }
        throw new ClassNotFoundException();
    }
}
