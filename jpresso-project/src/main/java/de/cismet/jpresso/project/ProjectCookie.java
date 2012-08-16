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

import org.netbeans.api.project.Project;

import org.openide.explorer.view.NodeListModel;
import org.openide.nodes.Node;

import de.cismet.jpresso.project.filetypes.cookies.*;

/**
 * Project providing interface.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface ProjectCookie extends Node.Cookie, NodeListModelProviderCookie {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Project getProject();
    @Override
    NodeListModel createNodeListModel();
}
