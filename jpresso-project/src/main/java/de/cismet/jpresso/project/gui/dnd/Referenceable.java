/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.dnd;

import de.cismet.jpresso.core.data.Reference;
import java.util.List;

/**
 * An Interface for Objects that can deliver a List of References
 * 
 * @author srichter
 */
public interface Referenceable {

    /**
     * 
     * @return a list of references
     */
    public List<Reference> getReferences();
}
