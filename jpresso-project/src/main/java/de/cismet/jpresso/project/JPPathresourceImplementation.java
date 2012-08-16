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
import org.netbeans.spi.java.classpath.support.PathResourceBase;

import java.net.URL;

import de.cismet.jpresso.core.utils.URLTools;

/**
 * Represents a single classpath-entry like e.g. a jar-file.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class JPPathresourceImplementation extends PathResourceBase {

    //~ Instance fields --------------------------------------------------------

    // Although  it is an array it will contain only one URL.
    private final URL url;

    //~ Constructors -----------------------------------------------------------

    /**
     * Create a ClassPathEntry for the given URL.
     *
     * @param   root  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public JPPathresourceImplementation(final URL root) {
        if (root == null) {
            throw new IllegalArgumentException();
        }
        this.url = root;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public URL[] getRoots() {
        return new URL[] { url };
    }

//    public void setRoots(URL root) {
//        this.url = new URL[]{root};
//        firePropertyChange(PROP_ROOTS, null, null);
//    }
    @Override
    @Deprecated
    public ClassPathImplementation getContent() {
        return null;
    }

    @Override
    public String toString() {
        return "JPPathresourceImplementation{" + url + "}"; // NOI18N
    }

    @Override
    public int hashCode() {
        return URLTools.urlHashCode(url);
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof JPPathresourceImplementation) {
            final JPPathresourceImplementation opr = (JPPathresourceImplementation)other;
            return URLTools.testEquals(this.url, opr.url);
        } else {
            return false;
        }
    }
}
