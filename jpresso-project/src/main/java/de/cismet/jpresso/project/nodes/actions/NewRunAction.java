/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.nodes.actions;

import de.cismet.jpresso.core.data.JPressoRun;
import de.cismet.jpresso.core.data.ProjectOptions;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.project.ProjectCookie;
import java.io.File;
import java.io.IOException;
import org.netbeans.api.project.Project;
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
public final class NewRunAction extends CallableSystemAction {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    public void performAction() {
        final NotifyDescriptor.InputLine desc = new NotifyDescriptor.InputLine("Enter name", "Create new run");
        DialogDisplayer.getDefault().notify(desc);
        if (desc.getValue().equals(InputLine.OK_OPTION)) {
            String filename = desc.getInputText();

            if (("").equals(filename)) {
                return;
            }

            try {
                final FilterNode activeNode = Utilities.actionsGlobalContext().lookup(FilterNode.class);
                final ProjectCookie projNode = activeNode.getCookie(ProjectCookie.class);
                if (projNode != null) {
                    final Project pro = projNode.getProject();
                    ProjectOptions po;
                    try {
                        final FileObject fo = pro.getProjectDirectory().getFileObject("/" + JPressoFileManager.PROJECT_OPTIONS);
                        po = JPressoFileManager.getDefault().load(FileUtil.toFile(fo), ProjectOptions.class);
                    } catch (Throwable t) {
                        po = new ProjectOptions();
                    }
                    //TODO Nullpointer checken!!!
                    final FileObject dir = pro.getProjectDirectory().getFileObject(JPressoFileManager.DIR_RUN);
                    //FileObject dir = ((ConnectionManagementNode) activeNode).getProject().getProjectDirectory().getFileObject(JPressoProject.CONNECTION_DIR);
                    if (dir != null) {
                        final String dirString = FileUtil.toFile(dir).getAbsolutePath();
                        filename = FileUtil.findFreeFileName(dir, filename, JPressoFileManager.END_RUN);
                        final JPressoRun run = new JPressoRun();
                        run.getRuntimeProperties().setFinalizerClass(po.getDefaultFinalizerClass());
                        run.getRuntimeProperties().setFinalizerProperties(po.getDefaultFinalizerProperties());
                        final File dest = new File(dirString + File.separator + filename + "." + JPressoFileManager.END_RUN);
                        JPressoFileManager.getDefault().persist(dest, run);
                        FileUtil.toFileObject(dest).getParent().refresh();
                        final DataObject dob = DataObject.find(FileUtil.toFileObject(dest));
                        final OpenCookie oc = dob.getLookup().lookup(OpenCookie.class);
                        if (oc != null) {
                            oc.open();
                        }
                        log.debug("Datei anlegen: " + dirString + File.separator + filename + "." + JPressoFileManager.END_RUN);
                    }
                }
            } catch (IOException ex) {
                final String message = ("Can not create file: " + filename + "." + JPressoFileManager.END_RUN);
                log.error(message, ex);
                NotifyDescriptor err = new NotifyDescriptor.Exception(ex, message);
                DialogDisplayer.getDefault().notify(err);
            }
        }
    }

    public String getName() {
        return NbBundle.getMessage(NewRunAction.class, "CTL_NewRunAction");
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
