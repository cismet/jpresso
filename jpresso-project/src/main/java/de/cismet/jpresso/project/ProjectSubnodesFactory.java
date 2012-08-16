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
package de.cismet.jpresso.project;

import org.openide.nodes.Node;

import de.cismet.jpresso.project.nodes.*;

/**
 * Builds the JPressoProjectNode's children. They represent the categories nodes like "Runs" or "Queries".
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class ProjectSubnodesFactory {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   project  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Node[] createProjectNodes(final JPressoProject project) {
        // HINT: Nodes order in the TreeView is determined by the order in this array!
        final Node[] ret = new Node[] {
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
