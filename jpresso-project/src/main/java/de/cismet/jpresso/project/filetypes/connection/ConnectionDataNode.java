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
package de.cismet.jpresso.project.filetypes.connection;

import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;

import java.io.IOException;

import javax.swing.Action;

import de.cismet.jpresso.project.filetypes.action.JPNodeRenameAction;
import de.cismet.jpresso.project.filetypes.refactoring.RefactoringEngine;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class ConnectionDataNode extends DataNode {

    //~ Static fields/initializers ---------------------------------------------

    private static final String IMAGE_ICON_BASE = "de/cismet/jpresso/project/res/database.png";

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ConnectionDataNode object.
     *
     * @param  obj  DOCUMENT ME!
     */
    public ConnectionDataNode(final DataObject obj) {
        super(obj, Children.LEAF);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }

    /**
     * Creates a new ConnectionDataNode object.
     *
     * @param  obj     DOCUMENT ME!
     * @param  lookup  DOCUMENT ME!
     */
    ConnectionDataNode(final DataObject obj, final Lookup lookup) {
        super(obj, Children.LEAF, lookup);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Action[] getActions(final boolean context) {
        final Action[] superResult = super.getActions(context);
        superResult[7] = SystemAction.get(JPNodeRenameAction.class);
        return superResult;
    }

    @Override
    public String getName() {
        return getDataObject().getPrimaryFile().getName();
    }

    @Override
    public String getDisplayName() {
        return getDataObject().getPrimaryFile().getName();
    }

    @Override
    public void destroy() throws IOException {
//        RefactoringEngine.refactorConnection(this, getDataObject().getPrimaryFile().getNameExt(), "");
        super.destroy();
    }

//    /** Creates a property sheet. */
//    @Override
//    protected Sheet createSheet() {
//        Sheet s = super.createSheet();
//        Sheet.Set ss = s.get(Sheet.PROPERTIES);
//        if (ss == null) {
//            ss = Sheet.createPropertiesSet();
//            s.put(ss);
//        }
//        // TODO add some relevant properties: ss.put(...)
//        return s;
//    }

}
