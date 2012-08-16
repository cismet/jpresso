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
 * Wraps multiple Mapable-Instances into a single Mapable.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class MapableCollector implements Mapable {

    //~ Instance fields --------------------------------------------------------

    private List<Mapping> mappings;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MapableCollector object.
     *
     * @param   mappings  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public MapableCollector(final List<Mapping> mappings) {
        if (mappings == null) {
            throw new IllegalArgumentException("Mapping list in MapableCollector may not be null!");
        }
        this.mappings = mappings;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public List<Mapping> getMappings() {
        return this.mappings;
    }
}
