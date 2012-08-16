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
package de.cismet.jpresso.project.nodes.actions;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.cismet.jpresso.core.data.DatabaseConnection;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;

import de.cismet.jpresso.project.ProjectCookie;

/**
 * TODO should be Node action!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class NewCodeAction extends CallableSystemAction {

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    //~ Methods ----------------------------------------------------------------

    @Override
    public void performAction() {
        final NotifyDescriptor.InputLine desc = new NotifyDescriptor.InputLine("Enter name", "New code file");
        DialogDisplayer.getDefault().notify(desc);
        if (desc.getValue().equals(InputLine.OK_OPTION)) {
            String filename = desc.getInputText();
            if (("").equals(filename)) {
                return;
            }

            try {
                final FilterNode activeNode = Utilities.actionsGlobalContext().lookup(FilterNode.class);
                final ProjectCookie projNode = activeNode.getCookie(ProjectCookie.class);
                final FileObject dir = projNode.getProject()
                            .getProjectDirectory()
                            .getFileObject(JPressoFileManager.DIR_CDE);
                if (dir != null) {
                    final String dirString = FileUtil.toFile(dir).getAbsolutePath();
                    filename = FileUtil.findFreeFileName(dir, filename, JPressoFileManager.END_JAVA);
//                    DatabaseConnection dc = new DatabaseConnection();
//                File dest = new File(dirString + File.separator + filename + "." + JPressoFileManager.END_JAVA);
//                dest.createNewFile();
                    final String file = dirString + File.separator + filename + "." + JPressoFileManager.END_JAVA;
                    try {
                        final BufferedWriter out = new BufferedWriter(new FileWriter(file));
                        out.write("package code;\n\npublic class " + filename + " extends AssignerBase {\n}");
                        out.close();
                    } catch (IOException e) {
                        log.warn(e.toString(), e);
                    }
                    final DataObject dob = DataObject.find(FileUtil.toFileObject(new File(file)));
                    if (dob != null) {
                        final OpenCookie oc = dob.getCookie(OpenCookie.class);
                        if (oc != null) {
                            oc.open();
                        }
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("Datei anlegen: " + dirString + File.separator + filename + "."
                                    + JPressoFileManager.END_JAVA);
                    }
                }
            } catch (IOException ex) {
                final String message = ("Can not create file: " + filename + "." + JPressoFileManager.END_JAVA);
                log.error(message, ex);
                final NotifyDescriptor err = new NotifyDescriptor.Exception(ex, message);
                DialogDisplayer.getDefault().notify(err);
            }
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(NewCodeAction.class, "CTL_NewCodeAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
