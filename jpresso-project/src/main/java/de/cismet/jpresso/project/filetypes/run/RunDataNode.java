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
package de.cismet.jpresso.project.filetypes.run;

import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;

import javax.swing.Action;

import de.cismet.jpresso.project.filetypes.run.action.RunAction;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class RunDataNode extends DataNode {

    //~ Static fields/initializers ---------------------------------------------

    private static final String IMAGE_ICON_BASE = "de/cismet/jpresso/project/res/run_it.png";

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new RunDataNode object.
     *
     * @param  obj  DOCUMENT ME!
     */
    RunDataNode(final DataObject obj) {
        super(obj, Children.LEAF);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
//        getCookieSet().add(this);
    }

    /**
     * Creates a new RunDataNode object.
     *
     * @param  obj     DOCUMENT ME!
     * @param  lookup  DOCUMENT ME!
     */
    RunDataNode(final DataObject obj, final Lookup lookup) {
        super(obj, Children.LEAF, lookup);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Action[] getActions(final boolean context) {
        final Action[] superResult = super.getActions(context);
        final Action[] result = new Action[superResult.length + 2];
        System.arraycopy(superResult, 0, result, 0, 1);
        result[1] = SystemAction.get(RunAction.class);
        System.arraycopy(superResult, 1, result, 2, superResult.length - 1);
        return result;
    }

    @Override
    public String getName() {
        return getDataObject().getPrimaryFile().getName();
    }

    @Override
    public String getDisplayName() {
        return getDataObject().getPrimaryFile().getName();
    } //
    /** Creates a property sheet. */
// /**
// * Start Run with ANT!!!
// */
// public void startRun() {
// System.out.println("in node!");
// Project p = getParentNode().getLookup().lookup(ProjectCookie.class).getProject();
// //Project p = ((ProjectCookie) getParentNode()).getProject();
// if (p != null) {
// System.out.println(p.getProjectDirectory().getFileObject("/" + JPressoFileManager.BUILD_XML).getName());
// AntHandler.startSingleRun(p.getProjectDirectory().getFileObject("/" + JPressoFileManager.BUILD_XML), this.getDataObject());
//        } else {
//        //TODO
//        }
//    }
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
