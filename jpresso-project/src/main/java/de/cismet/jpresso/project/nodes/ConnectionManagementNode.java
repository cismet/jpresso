/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.nodes;

import de.cismet.jpresso.project.JPressoProject;

import de.cismet.jpresso.project.nodes.actions.NewConnectionAction;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.project.filetypes.cookies.ConnectionListModelProvider;
import java.awt.Image;
import javax.swing.Action;
import org.openide.explorer.view.NodeListModel;
import org.openide.filesystems.FileObject;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;

/**
 *
 * @author srichter
 */
public final class ConnectionManagementNode extends AbstractJPNode implements ConnectionListModelProvider {

    /** Creates a new instance of UserManagement */
    public ConnectionManagementNode(JPressoProject project) {
        //super(new ConnectionManagementChildren(project), project);
        super(project.getProjectDirectory().getFileObject(JPressoFileManager.DIR_CON), JPressoFileManager.END_CONNECTION, project);
    }

    @Override
    public Image getIcon(int i) {
        return Utilities.loadImage(
                "de/cismet/jpresso/project/res/database.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    public String getDisplayName() {
        return "Connections";
    }

    @Override
    public Action[] getActions(boolean context) {
        Action[] result = new Action[]{
            //RefreshPropsAction ist selbstdefiniert im Projekt
            //Tools- und PropertyAction sind Standardaktions die
            //jeder Node haben sollte
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

    FileObject getChildrenDir() {
        return getProject().getProjectDirectory().getFileObject(JPressoFileManager.DIR_CON);
    }

    public NodeListModel getConnectionListModel() {
        return createNodeListModel();
    }

    @Override
    public String getSupportedFileExt() {
        return JPressoFileManager.END_CONNECTION;
    }
}




