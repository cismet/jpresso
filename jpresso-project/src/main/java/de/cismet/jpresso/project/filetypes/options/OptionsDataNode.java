/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.filetypes.options;

import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

public final class OptionsDataNode extends DataNode {

    private static final String IMAGE_ICON_BASE = "de/cismet/jpresso/project/res/kservices.png";

    public OptionsDataNode(OptionsDataObject obj) {
        super(obj, Children.LEAF);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }

    OptionsDataNode(OptionsDataObject obj, Lookup lookup) {
        super(obj, Children.LEAF, lookup);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }

    @Override
    public String getName() {
        org.openide.filesystems.FileObject fo = getDataObject().getPrimaryFile();
        if ((fo.getName() + "." + fo.getExt()).equals(JPressoFileManager.PROJECT_OPTIONS)) {
            return "Options";
        } else {
            return fo.getName();
        }
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public boolean canCopy() {
        return false;
    }//    /** Creates a property sheet. */
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
