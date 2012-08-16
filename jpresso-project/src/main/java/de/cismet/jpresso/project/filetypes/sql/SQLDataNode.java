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
package de.cismet.jpresso.project.filetypes.sql;

import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;

import javax.swing.Action;

import de.cismet.jpresso.project.filetypes.sql.action.RunSQLAction;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class SQLDataNode extends DataNode {

    //~ Static fields/initializers ---------------------------------------------

    private static final String IMAGE_ICON_BASE = "de/cismet/jpresso/project/res/toggle_log.png";

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SQLDataNode object.
     *
     * @param  obj  DOCUMENT ME!
     */
    public SQLDataNode(final DataObject obj) {
        super(obj, Children.LEAF);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }

    /**
     * Creates a new SQLDataNode object.
     *
     * @param  obj     DOCUMENT ME!
     * @param  lookup  DOCUMENT ME!
     */
    SQLDataNode(final DataObject obj, final Lookup lookup) {
        super(obj, Children.LEAF, lookup);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getName() {
        return getDataObject().getPrimaryFile().getName();
    }

    @Override
    public String getDisplayName() {
        return getDataObject().getPrimaryFile().getName();
    } //

    @Override
    public Action[] getActions(final boolean context) {
        final Action[] superResult = super.getActions(context);
        final Action[] result = new Action[superResult.length + 2];
        System.arraycopy(superResult, 0, result, 0, 1);
        result[1] = SystemAction.get(RunSQLAction.class);
        System.arraycopy(superResult, 1, result, 2, superResult.length - 1);
        return result;
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
