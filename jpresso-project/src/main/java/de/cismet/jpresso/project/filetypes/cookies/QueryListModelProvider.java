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

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface QueryListModelProvider {

    //~ Methods ----------------------------------------------------------------

    /**
     * Used for the Query-ComboBoxes.
     *
     * @return  a NodeListModel with all Query Nodes in the project
     */
    NodeListModel getQueryListModel();
}
