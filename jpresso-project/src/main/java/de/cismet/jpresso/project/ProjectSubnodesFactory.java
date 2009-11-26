/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project;

import de.cismet.jpresso.project.nodes.*;
import org.openide.nodes.Node;

/**
 * Builds the JPressoProjectNode's children. They represent the categories nodes
 * like "Runs" or "Queries".
 * 
 * @author srichter
 */
public final class ProjectSubnodesFactory {

    public static final Node[] createProjectNodes(final JPressoProject project) {
        //HINT: Nodes order in the TreeView is determined by the order in this array!
        final Node[] ret = new Node[]{
            new ConnectionManagementNode(project),
            new QueryManagementNode(project),
            new CodeManagementNode(project),
            new RunManagementNode(project),
            new ScriptManagementNode(project),
            new ProjectManagementNode(project)
        };
        return ret;
    }
}
