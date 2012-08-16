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

import de.cismet.jpresso.core.data.Reference;

/**
 * An Interface for Objects that can deliver a List of References.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface Referenceable {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  a list of references
     */
    List<Reference> getReferences();
}
