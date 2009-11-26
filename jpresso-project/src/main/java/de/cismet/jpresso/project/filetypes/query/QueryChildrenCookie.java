/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.jpresso.project.filetypes.query;

import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author srichter
 */
public interface QueryChildrenCookie extends Node.Cookie{
    public Children getChildren();
    public QueryDataNode getSelf();
}
