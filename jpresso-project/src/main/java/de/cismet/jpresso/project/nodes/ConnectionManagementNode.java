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

import org.openide.explorer.view.NodeListModel;
import org.openide.filesystems.FileObject;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;

import java.awt.Image;

import javax.swing.Action;

import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;

import de.cismet.jpresso.project.JPressoProject;
import de.cismet.jpresso.project.filetypes.cookies.ConnectionListModelProvider;
import de.cismet.jpresso.project.nodes.actions.NewConnectionAction;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class ConnectionManagementNode extends AbstractJPNode implements ConnectionListModelProvider {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of UserManagement.
     *
     * @param  project  DOCUMENT ME!
     */
    public ConnectionManagementNode(final JPressoProject project) {
        // super(new ConnectionManagementChildren(project), project);
        super(project.getProjectDirectory().getFileObject(JPressoFileManager.DIR_CON),
            JPressoFileManager.END_CONNECTION,
            project);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Image getIcon(final int i) {
        return Utilities.loadImage(
                "de/cismet/jpresso/project/res/database.png");
    }

    @Override
    public Image getOpenedIcon(final int i) {
        return getIcon(i);
    }

    @Override
    public String getDisplayName() {
        return "Connections";
    }

    @Override
    public Action[] getActions(final boolean context) {
        final Action[] result = new Action[] {
                // RefreshPropsAction ist selbstdefiniert im Projekt
                // Tools- und PropertyAction sind Standardaktions die
                // jeder Node haben sollte
                SystemAction.get(NewConnectionAction.class),
//            SystemAction.get(ConnectionWizardAction.class),
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

    @Override
    FileObject getChildrenDir() {
        return getProject().getProjectDirectory().getFileObject(JPressoFileManager.DIR_CON);
    }

    @Override
    public NodeListModel getConnectionListModel() {
        return createNodeListModel();
    }

    @Override
    public String getSupportedFileExt() {
        return JPressoFileManager.END_CONNECTION;
    }
}
