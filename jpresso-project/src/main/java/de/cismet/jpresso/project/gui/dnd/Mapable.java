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
package de.cismet.jpresso.project.gui.dnd;

import java.util.List;

import de.cismet.jpresso.core.data.Mapping;

/**
 * An Interface for Objects that can deliver a List of Mappings.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface Mapable {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  a list of mappings
     */
    List<Mapping> getMappings();
}
