/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.filetypes.sql;


import de.cismet.jpresso.project.filetypes.sql.action.RunSQLAction;
import javax.swing.Action;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;

public final class SQLDataNode extends DataNode {

    private static final String IMAGE_ICON_BASE = "de/cismet/jpresso/project/res/toggle_log.png";

    public SQLDataNode(DataObject obj) {
        super(obj, Children.LEAF);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }

    SQLDataNode(DataObject obj, Lookup lookup) {
        super(obj, Children.LEAF, lookup);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }

    @Override
    public String getName() {
        return getDataObject().getPrimaryFile().getName();
    }

    @Override
    public String getDisplayName() {
        return getDataObject().getPrimaryFile().getName();
    }//  

    @Override
    public Action[] getActions(boolean context) {
        Action[] superResult = super.getActions(context);
        Action[] result = new Action[superResult.length + 2];
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
