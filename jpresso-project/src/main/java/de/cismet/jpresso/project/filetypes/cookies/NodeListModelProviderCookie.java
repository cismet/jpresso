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
public interface NodeListModelProviderCookie extends Node.Cookie {

    /**
     * 
     * @return usually a NodeListModel with the children of the implementing Node
     */
    public NodeListModel createNodeListModel();
}
