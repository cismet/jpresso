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
package de.cismet.jpresso.project.filetypes.options;

import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class OptionsDataNode extends DataNode {

    //~ Static fields/initializers ---------------------------------------------

    private static final String IMAGE_ICON_BASE = "de/cismet/jpresso/project/res/kservices.png";

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new OptionsDataNode object.
     *
     * @param  obj  DOCUMENT ME!
     */
    public OptionsDataNode(final OptionsDataObject obj) {
        super(obj, Children.LEAF);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }

    /**
     * Creates a new OptionsDataNode object.
     *
     * @param  obj     DOCUMENT ME!
     * @param  lookup  DOCUMENT ME!
     */
    OptionsDataNode(final OptionsDataObject obj, final Lookup lookup) {
        super(obj, Children.LEAF, lookup);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getName() {
        final org.openide.filesystems.FileObject fo = getDataObject().getPrimaryFile();
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
    } // /** Creates a property sheet. */
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
