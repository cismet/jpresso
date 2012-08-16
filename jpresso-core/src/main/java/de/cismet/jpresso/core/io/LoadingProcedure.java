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
package de.cismet.jpresso.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.cismet.jpresso.core.data.JPLoadable;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
/**
 * Interface for filetype-specific loading procedures.
 *
 * @param    <T>
 *
 * @version  $Revision$, $Date$
 */
public interface LoadingProcedure<T extends JPLoadable> {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   file  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  FileNotFoundException  DOCUMENT ME!
     * @throws  IOException            DOCUMENT ME!
     */
    T load(File file) throws FileNotFoundException, IOException;
}
