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
package de.cismet.jpresso.project;

import org.netbeans.spi.java.classpath.ClassPathImplementation;
import org.netbeans.spi.java.classpath.PathResourceImplementation;

import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.filesystems.FileUtil;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.net.URL;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * A ClassPathImplementation that allows changes and reports them to listeners. Used to provide the additional classpath
 * from jars etc. , which may change at runtime. These changes need to be recognized e.g. by the JavaEditor.
 *
 * <p>Needed to have access to all jars on the current classpath in the code completion of the JavaEditor.</p>
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 * @see      ClassPathImplementation
 */
public class JPClassPathImplementation implements ClassPathImplementation {

    //~ Instance fields --------------------------------------------------------

    private final List<PathResourceImplementation> entries;
    private final Vector<PropertyChangeListener> listener;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a classpath that consists of the given FileObjects.
     *
     * @param  roots  DOCUMENT ME!
     */
    public JPClassPathImplementation(final FileObject... roots) {
        assert roots != null;
        listener = new Vector<PropertyChangeListener>();
        entries = TypeSafeCollections.newArrayList();
        setRoots(roots);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Produces PathResourceImplementations, which abstractly represents a single classpath- entry like a dir or a jar.
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  NullPointerException      DOCUMENT ME!
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public static PathResourceImplementation createResource(final URL url) {
        if (url == null) {
            throw new NullPointerException("Cannot pass null URL to ClassPathSupport.createResource"); // NOI18N
        }
        // FU.iAF is a bit slow, so don't call it except when assertions are on:
        boolean assertions = false;
        assert assertions = true;
        if (assertions && FileUtil.isArchiveFile(url)) {
            throw new IllegalArgumentException("File URL pointing to " // NOI18N
                        + "JAR is not valid classpath entry. Use jar: URL. Was: " + url); // NOI18N
        }
        if (!url.toExternalForm().endsWith("/")) { // NOI18N

            throw new IllegalArgumentException("URL must be a folder URL (append '/' if necessary): " + url); // NOI18N
        }
        return new JPPathresourceImplementation(url);
    }

    /**
     * Called to change the classpath. The given list represents the new classpath, which each item being one entry.
     *
     * @param  roots  DOCUMENT ME!
     */
    public void setRoots(final FileObject... roots) {
        assert roots != null;
        entries.clear();
        for (final FileObject root : roots) {
            if (root == null) {
                continue;
            }

            try {
                final URL u = root.getURL();
                entries.add(createResource(u));
            } catch (FileStateInvalidException e) {
                ErrorManager.getDefault().notify(e);
            }
        }
        fireClassPathChanged();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  a read-only list of all classpath entries.
     */
    @Override
    public List<? extends PathResourceImplementation> getResources() {
        return Collections.unmodifiableList(entries);
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param  listener  DOCUMENT ME!
     */
    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.listener.add(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param  listener  DOCUMENT ME!
     */
    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        this.listener.remove(listener);
    }

    /**
     * Notify listeners that classpath has changed.
     */
    private void fireClassPathChanged() {
        for (final PropertyChangeListener l : listener) {
            l.propertyChange(new PropertyChangeEvent(this, PROP_RESOURCES, null, null));
        }
    }
}
