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
package de.cismet.jpresso.project.filetypes.cookies;

import org.openide.explorer.view.NodeListModel;
import org.openide.nodes.Node;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface ConnectionListModelProvider extends Node.Cookie {

    //~ Methods ----------------------------------------------------------------

    /**
     * Used for the Connection-ComboBoxes.
     *
     * @return  a NodeListModel with all Connection Nodes in the Project
     */
    NodeListModel getConnectionListModel();
}
