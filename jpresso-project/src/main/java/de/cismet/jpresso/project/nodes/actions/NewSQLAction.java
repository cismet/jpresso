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
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;

import java.io.File;
import java.io.IOException;

import de.cismet.jpresso.core.data.SQLRun;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;

import de.cismet.jpresso.project.ProjectCookie;

/**
 * TODO should be Node action!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class NewSQLAction extends CallableSystemAction {

    //~ Methods ----------------------------------------------------------------

    @Override
    public void performAction() {
        try {
            final NotifyDescriptor.InputLine desc = new NotifyDescriptor.InputLine("Enter name", "Create new query");
            DialogDisplayer.getDefault().notify(desc);
            if (desc.getValue().equals(InputLine.OK_OPTION)) {
                String filename = desc.getInputText();
                if ("".equals(filename)) {
                    return;
                }

                final FilterNode activeNode = Utilities.actionsGlobalContext().lookup(FilterNode.class);
                final ProjectCookie projNode = activeNode.getCookie(ProjectCookie.class);
                // TODO Nullpointer checken
                final FileObject dir = projNode.getProject()
                            .getProjectDirectory()
                            .getFileObject(JPressoFileManager.DIR_SQL);
                // FileObject dir = ((ConnectionManagementNode)
                // activeNode).getProject().getProjectDirectory().getFileObject(JPressoProject.CONNECTION_DIR);
                if (dir != null) {
                    final String dirString = FileUtil.toFile(dir).getAbsolutePath();
                    filename = FileUtil.findFreeFileName(dir, filename, JPressoFileManager.END_SQL);
                    final SQLRun qr = new SQLRun();
                    final File dest = new File(dirString + File.separator + filename + "."
                                    + JPressoFileManager.END_SQL);
                    JPressoFileManager.getDefault().persist(dest, qr);
                    FileUtil.toFileObject(dest).getParent().refresh();
                    final DataObject dob = DataObject.find(FileUtil.toFileObject(dest));
                    final OpenCookie oc = dob.getLookup().lookup(OpenCookie.class);
                    if (oc != null) {
                        oc.open();
                    }
                }
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(NewSQLAction.class, "CTL_NewSQLAction");
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
