/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.nodes.actions;

import de.cismet.jpresso.core.data.DatabaseConnection;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.project.ProjectCookie;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
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
public final class NewConnectionAction extends CallableSystemAction {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    public void performAction() {
        NotifyDescriptor.InputLine desc = new NotifyDescriptor.InputLine("Enter name", "New Connection");
        DialogDisplayer.getDefault().notify(desc);
        if (desc.getValue().equals(InputLine.OK_OPTION)) {
            String filename = desc.getInputText();
            if (("").equals(filename)) {
                return;
            }

            try {
                FilterNode activeNode = Utilities.actionsGlobalContext().lookup(FilterNode.class);
                ProjectCookie projNode = activeNode.getCookie(ProjectCookie.class);
                FileObject dir = projNode.getProject().getProjectDirectory().getFileObject(JPressoFileManager.DIR_CON);
                if (dir != null) {
                    String dirString = FileUtil.toFile(dir).getAbsolutePath();
                    filename = FileUtil.findFreeFileName(dir, filename, JPressoFileManager.END_CONNECTION);
                    DatabaseConnection dc = new DatabaseConnection();
                    Properties p = new Properties();
                    p.setProperty("user", "your_username_here");
                    p.setProperty("password", "your_password_here");
                    dc.setProps(p);
                    File dest = new File(dirString + File.separator + filename + "." + JPressoFileManager.END_CONNECTION);
                    JPressoFileManager.getDefault().persist(dest, dc);
                    FileUtil.toFileObject(dest).getParent().refresh();
                    DataObject dob = DataObject.find(FileUtil.toFileObject(dest));
                    OpenCookie oc = dob.getLookup().lookup(OpenCookie.class);
                    if (oc != null) {
                        oc.open();
                    }
                    log.debug("Datei anlegen: " + dirString + File.separator + filename + "." + JPressoFileManager.END_CONNECTION);
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
        return NbBundle.getMessage(NewConnectionAction.class, "CTL_NewConnectionAction");
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
