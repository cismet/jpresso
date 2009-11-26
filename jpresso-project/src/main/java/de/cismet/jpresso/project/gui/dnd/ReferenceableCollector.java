/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.dnd;

import de.cismet.jpresso.core.data.Reference;
import java.util.List;

/**
 * Wraps multiple Referenceable-Instances into a single Referenceable.
 * 
 * @author srichter
 */
public class ReferenceableCollector implements Referenceable {

    public ReferenceableCollector(final List<Reference> references) {
        if (references == null) {
            throw new IllegalArgumentException("Reference list in ReferenceableCollector may not be null!");
        }
        this.references = references;
    }
    private List<Reference> references;

    public List<Reference> getReferences() {
        return this.references;
    }
}
