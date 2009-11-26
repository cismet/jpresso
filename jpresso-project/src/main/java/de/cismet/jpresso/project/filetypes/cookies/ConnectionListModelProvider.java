/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.filetypes.cookies;

import org.openide.explorer.view.NodeListModel;
import org.openide.nodes.Node;

/**
 *
 * @author srichter
 */
public interface ConnectionListModelProvider extends Node.Cookie {

    /**
     * Used for the Connection-ComboBoxes
     * 
     * @return a NodeListModel with all Connection Nodes in the Project
     */
    public NodeListModel getConnectionListModel();
}
