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
package de.cismet.jpresso.core.utils;

import java.io.File;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class URLTools {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   toConvert  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  RuntimeException  DOCUMENT ME!
     */
    public static File convertURLToFile(final URL toConvert) {
        try {
            // warning maybe still buggy :(
            final String urlString = toConvert.getFile();
            if (urlString.endsWith(".jar!/")) {
                return new File(new URI(urlString.substring(0, urlString.length() - 2)));
            } else {
                return new File(toConvert.toURI());
            }
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   u1  DOCUMENT ME!
     * @param   u2  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static boolean testEquals(final URL u1, final URL u2) {
        if (u1 == u2) {
            return true;
        } else if ((u1 == null) || (u2 == null)) {
            return false;
        } else {
            return u1.toString().equals(u2.toString());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   u  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static int urlHashCode(final URL u) {
        if (u == null) {
            return 0;
        } else {
            return u.toString().hashCode();
        }
    }
}
