/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.dnd;

import de.cismet.jpresso.core.data.Mapping;
import java.util.List;

/**
 * Wraps multiple Mapable-Instances into a single Mapable.
 * 
 * @author srichter
 */
public class MapableCollector implements Mapable {

    public MapableCollector(List<Mapping> mappings) {
        if (mappings == null) {
            throw new IllegalArgumentException("Mapping list in MapableCollector may not be null!");
        }
        this.mappings = mappings;
    }
    private List<Mapping> mappings;

    public List<Mapping> getMappings() {
        return this.mappings;
    }
}
