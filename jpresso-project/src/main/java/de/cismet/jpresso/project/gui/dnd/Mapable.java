/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.dnd;

import de.cismet.jpresso.core.data.Mapping;
import java.util.List;

/**
 * An Interface for Objects that can deliver a List of Mappings
 * @author srichter
 */
public interface Mapable {
    
    /**
     * 
     * @return a list of mappings
     */
    public List<Mapping> getMappings();
}
