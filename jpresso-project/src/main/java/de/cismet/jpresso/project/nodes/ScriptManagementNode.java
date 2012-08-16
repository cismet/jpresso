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
package de.cismet.jpresso.project.nodes;

import org.openide.filesystems.FileObject;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;

import java.awt.Image;

import javax.swing.Action;

import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;

import de.cismet.jpresso.project.JPressoProject;
import de.cismet.jpresso.project.nodes.actions.NewSQLAction;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class ScriptManagementNode extends AbstractJPNode {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of UserManagement.
     *
     * @param  project  DOCUMENT ME!
     */
    public ScriptManagementNode(final JPressoProject project) {
        // super(new ScriptManagementChildren(project), project);
        super(project.getProjectDirectory().getFileObject(JPressoFileManager.DIR_SQL),
            JPressoFileManager.END_SQL,
            project);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Image getIcon(final int i) {
        return Utilities.loadImage(
                "de/cismet/jpresso/project/res/toggle_log.png");
    }

    @Override
    public Image getOpenedIcon(final int i) {
        return getIcon(i);
    }

    @Override
    public String getDisplayName() {
        return "SQL";
    }

    /**
     * DOCUMENT ME!
     */
    public void refreshChildren() {
    }

    @Override
    public Action[] getActions(final boolean context) {
        final Action[] result = new Action[] {
                // RefreshPropsAction ist selbstdefiniert im Projekt
                // Tools- und PropertyAction sind Standardaktions die
                // jeder Node haben sollte
                SystemAction.get(NewSQLAction.class),
//            null,
//            //SystemAction.get(OpenLocalExplorerAction.class),
//            //null,
//            SystemAction.get(NewAction.class),
//            null,
//            SystemAction.get(RefreshPropsAction.class),
//            null,
//            //SystemAction.get(ToolsAction.class),
//            //TODO soll was anderes anzeige...mit Rollback=true etc...
//            SystemAction.get(PropertiesAction.class)
            };
        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String getChildrenExtension() {
        return JPressoFileManager.END_SQL;
    }

    @Override
    FileObject getChildrenDir() {
        return getProject().getProjectDirectory().getFileObject(JPressoFileManager.DIR_SQL);
    }

    @Override
    public String getSupportedFileExt() {
        return JPressoFileManager.END_SQL;
    }
}
