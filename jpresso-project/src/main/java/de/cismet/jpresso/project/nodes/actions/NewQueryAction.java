/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.nodes.actions;

import de.cismet.jpresso.core.data.Query;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.project.ProjectCookie;
import java.io.File;
import java.io.IOException;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.InputLine;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.FilterNode;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;

/**
 * TODO should be Node action!
 * @author srichter
 */
public final class NewQueryAction extends CallableSystemAction {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    public void performAction() {
        final NotifyDescriptor.InputLine desc = new NotifyDescriptor.InputLine("Enter name", "Create new query");
        DialogDisplayer.getDefault().notify(desc);
        if (desc.getValue().equals(InputLine.OK_OPTION)) {
            String filename = desc.getInputText();
            if (("").equals(filename)) {
                return;
            }
            try {
                FilterNode activeNode = Utilities.actionsGlobalContext().lookup(FilterNode.class);
                ProjectCookie projNode = activeNode.getCookie(ProjectCookie.class);
                //TODO Nullpointer checken!!!
                FileObject dir = projNode.getProject().getProjectDirectory().getFileObject(JPressoFileManager.DIR_QRY);
                //FileObject dir = ((ConnectionManagementNode) activeNode).getProject().getProjectDirectory().getFileObject(JPressoProject.CONNECTION_DIR);
                if (dir != null) {
                    String dirString = FileUtil.toFile(dir).getAbsolutePath();
                    filename = FileUtil.findFreeFileName(dir, filename, JPressoFileManager.END_QUERY);
                    Query qr = new Query();
                    File dest = new File(dirString + File.separator + filename + "." + JPressoFileManager.END_QUERY);
                    JPressoFileManager.getDefault().persist(dest, qr);
                    FileUtil.toFileObject(dest).getParent().refresh();
                    DataObject dob = DataObject.find(FileUtil.toFileObject(dest));
                    OpenCookie oc = dob.getLookup().lookup(OpenCookie.class);
                    if (oc != null) {
                        oc.open();
                    }
                    log.debug("Datei anlegen: " + dirString + File.separator + filename + "." + JPressoFileManager.END_QUERY);
                }
            } catch (IOException ex) {
                String message = ("Can not create file: " + filename + "." + JPressoFileManager.END_CONNECTION);
                log.error(message, ex);
                NotifyDescriptor err = new NotifyDescriptor.Exception(ex, message);
                DialogDisplayer.getDefault().notify(err);
            }
        }
    }

    public String getName() {
        return NbBundle.getMessage(NewQueryAction.class, "CTL_NewQueryAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
