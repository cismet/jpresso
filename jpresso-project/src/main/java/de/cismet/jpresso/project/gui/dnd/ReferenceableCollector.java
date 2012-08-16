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
 * Wraps multiple Referenceable-Instances into a single Referenceable.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class ReferenceableCollector implements Referenceable {

    //~ Instance fields --------------------------------------------------------

    private List<Reference> references;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ReferenceableCollector object.
     *
     * @param   references  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public ReferenceableCollector(final List<Reference> references) {
        if (references == null) {
            throw new IllegalArgumentException("Reference list in ReferenceableCollector may not be null!");
        }
        this.references = references;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public List<Reference> getReferences() {
        return this.references;
    }
}
