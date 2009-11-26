/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project;

import de.cismet.jpresso.project.filetypes.cookies.*;
import org.netbeans.api.project.Project;
import org.openide.explorer.view.NodeListModel;
import org.openide.nodes.Node;

/**
 * Project providing interface.
 * 
 * @author srichter
 */
public interface ProjectCookie extends Node.Cookie, NodeListModelProviderCookie {

    public Project getProject();

    public NodeListModel createNodeListModel();
}
