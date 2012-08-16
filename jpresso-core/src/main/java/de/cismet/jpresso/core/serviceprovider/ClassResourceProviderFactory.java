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
package de.cismet.jpresso.core.serviceprovider;

import java.io.File;

import de.cismet.jpresso.core.kernel.ClassResourceProviderIml;

/**
 * Creates a ClassRessourceProvider for a given project directory.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public abstract class ClassResourceProviderFactory {

    //~ Methods ----------------------------------------------------------------

    /**
     * Creates a ClassRessourceProvider for a given project directory.
     *
     * @param   projDir  DOCUMENT ME!
     *
     * @return  the classpathprovider
     */
    public static ClassResourceProvider createClassRessourceProvider(final File projDir) {
        return new ClassResourceProviderIml(projDir);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   projDir  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static ClassResourceProvider createClassRessourceProvider(final String projDir) {
        return new ClassResourceProviderIml(new File(projDir));
    }
}
