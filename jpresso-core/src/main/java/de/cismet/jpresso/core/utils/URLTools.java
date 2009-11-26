/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.utils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author srichter
 */
public final class URLTools {

    public static final File convertURLToFile(final URL toConvert) {
        try {
            final String urlString = toConvert.getFile();
            if (urlString.endsWith(".jar!/")) {
                return new File(new URI(urlString.substring(0, urlString.length() - 2)));
            } else {
                return new File(urlString);
            }
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static final boolean testEquals(URL u1, URL u2) {
        if (u1 == u2) {
            return true;
        } else if (u1 == null || u2 == null) {
            return false;
        } else {
            return u1.toString().equals(u2.toString());
        }
    }
    
    public static final int urlHashCode(URL u) {
        if(u == null) {
            return 0;
        } else {
            return u.toString().hashCode();
        }
    }
}
