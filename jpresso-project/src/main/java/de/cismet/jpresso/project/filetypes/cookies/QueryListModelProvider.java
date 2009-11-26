/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.filetypes.cookies;

import org.openide.explorer.view.NodeListModel;

/**
 *  
 * @author srichter
 */
public interface QueryListModelProvider {
    
    /**
     * Used for the Query-ComboBoxes
     * 
     * @return a NodeListModel with all Query Nodes in the project
     */
    public NodeListModel getQueryListModel();
}
