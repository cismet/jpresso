/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.nodes;

import de.cismet.jpresso.project.JPressoProject;
import de.cismet.jpresso.project.nodes.actions.NewCodeAction;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.List;
import javax.swing.Action;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;

/**
 *
 * @author srichter
 */
public final class CodeManagementNode extends AbstractJPNode {

    /** Creates a new instance of UserManagement */
    public CodeManagementNode(JPressoProject project) {
        //super(new CodeManagementChildren(project), project);
        super(project.getProjectDirectory().getFileObject(JPressoFileManager.DIR_CDE), JPressoFileManager.END_JAVA, project);
//        super(project.getProjectDirectory().getFileObject(JPressoFileManager.DIR_CDE), JPressoFileManager.END_CODE, project);
    }

    @Override
    public Image getIcon(int i) {
        return Utilities.loadImage(
                "de/cismet/jpresso/project/res/java.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    public String getDisplayName() {
        return "Code";
    }

    @Override
    public Action[] getActions(boolean context) {
        Action[] result = new Action[]{ //RefreshPropsAction ist selbstdefiniert im Projekt
            //Tools- und PropertyAction sind Standardaktions die
            //jeder Node haben sollte
            SystemAction.get(NewCodeAction.class), //            null,
        //            //SystemAction.get(OpenLocalExplorerAction.class),
        //            //null,
        //            SystemAction.get(NewAction.class),
        //            null,
        //            SystemAction.get(RefreshPropsAction.class),
        //            null,
        //            //SystemAction.get(ToolsAction.class),
        //            SystemAction.get(PropertiesAction.class)
        };
        return result;
    }

    FileObject getChildrenDir() {
        return getProject().getProjectDirectory().getFileObject(JPressoFileManager.DIR_CDE);
    }

    @Override
    public String getSupportedFileExt() {
        return JPressoFileManager.END_JAVA;
    }
    //TODO better for copy, why doesn't it work like move with refactoring??
    @Override
    protected void createPasteTypes(Transferable transferable, List<PasteType> list) {
//        NodeTransfer.Paste p = NodeTransfer.findPaste(transferable);
        int mode = NodeTransfer.COPY;
        final Node node = NodeTransfer.node(transferable, mode);
        if (node != null) {
            final MultiDataObject data = node.getLookup().lookup(MultiDataObject.class);
            if (data != null && data.getPrimaryFile().getExt().equals(getSupportedFileExt())) {
                list.add(new PasteType() {

                    @Override
                    public Transferable paste() throws IOException {
                        String name = FileUtil.findFreeFileName(getChildrenDir(), data.getPrimaryFile().getName(), data.getPrimaryFile().getExt());
                        data.getPrimaryFile().copy(getChildrenDir(), name, data.getPrimaryFile().getExt());
                        return null;
                    }
                });
            }
        }
//        mode = NodeTransfer.MOVE;
//        node = NodeTransfer.node(transferable, mode);
//        if (node != null) {
//            final MultiDataObject data = node.getLookup().lookup(MultiDataObject.class);
//            if (data != null && data.getPrimaryFile().getExt().equals(getSupportedFileExt())) {
//                list.add(new PasteType() {
//
//                    @Override
//                    public Transferable paste() throws IOException {
//                        String name = FileUtil.findFreeFileName(getChildrenDir(), data.getPrimaryFile().getName(), data.getPrimaryFile().getExt());
//                        data.getPrimaryFile().copy(getChildrenDir(), name, data.getPrimaryFile().getExt());
//                        data.getPrimaryFile().delete();
//                        return null;
//                    }
//                });
//            }
//        }
    }
}
