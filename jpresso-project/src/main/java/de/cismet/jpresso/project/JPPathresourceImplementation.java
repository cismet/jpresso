/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project;

import de.cismet.jpresso.core.utils.URLTools;
import java.net.URL;
import org.netbeans.spi.java.classpath.ClassPathImplementation;
import org.netbeans.spi.java.classpath.support.PathResourceBase;

/**
 * Represents a single classpath-entry like e.g. a jar-file.
 * 
 * @author srichter
 */
public final class JPPathresourceImplementation extends PathResourceBase {

    //Although  it is an array it will contain only one URL.
    private final URL url;

    /**
     * Create a ClassPathEntry for the given URL.
     * 
     * @param root
     */
    public JPPathresourceImplementation(final URL root) {
        if (root == null) {
            throw new IllegalArgumentException();
        }
        this.url = root;
    }

    /**
     * 
     * @return
     */
    public URL[] getRoots() {
        return new URL[]{url};
    }

//    public void setRoots(URL root) {
//        this.url = new URL[]{root};
//        firePropertyChange(PROP_ROOTS, null, null);
//    }
    @Deprecated
    public ClassPathImplementation getContent() {
        return null;
    }

    @Override
    public String toString() {
        return "JPPathresourceImplementation{" + url + "}";   //NOI18N
    }

    @Override
    public int hashCode() {
        return URLTools.urlHashCode(url);
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof JPPathresourceImplementation) {
            final JPPathresourceImplementation opr = (JPPathresourceImplementation) other;
            return URLTools.testEquals(this.url, opr.url);
        } else {
            return false;
        }
    }
}
