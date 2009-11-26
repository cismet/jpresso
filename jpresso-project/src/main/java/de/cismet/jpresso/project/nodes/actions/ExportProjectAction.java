/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.nodes.actions;

import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.project.filetypes.AntHandler;
import java.awt.Frame;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.netbeans.api.project.Project;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.openide.windows.WindowManager;

/**
 * Exporting a project to zip.
 * 
 * @author srichter
 */
public final class ExportProjectAction extends CookieAction {

    private static final Class[] COOKIE_CLASSES = new Class[]{Project.class};

    public ExportProjectAction() {
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return (f.isDirectory() || f.getName().endsWith("." + JPressoFileManager.END_ZIP));
            }

            @Override
            public String getDescription() {
                return "." + JPressoFileManager.END_ZIP;
            }
        });
        chooser.setAcceptAllFileFilterUsed(false);
    }
    private final JFileChooser chooser;

    protected void performAction(Node[] activatedNodes) {
        Project project = activatedNodes[0].getLookup().lookup(Project.class);
        final Frame mainWindow = WindowManager.getDefault().getMainWindow();

        if (chooser.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
            File sel = chooser.getSelectedFile();
            if (!sel.getName().endsWith("." + JPressoFileManager.END_ZIP)) {
                sel = new File(sel.getAbsolutePath().replace(".", "_") + "." + JPressoFileManager.END_ZIP);
            }
            if (sel.isFile()) {
                if (JOptionPane.showConfirmDialog(mainWindow, "File already exists", "File " + sel.getAbsolutePath() + " already exists. Override?", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                    return;
                }
            }

            AntHandler.startExport(project.getProjectDirectory().getFileObject("/" + JPressoFileManager.BUILD_XML), sel.getAbsolutePath());
        }
    // TODO use project
    // TODO use project

//        Project project = activatedNodes[0].getLookup().lookup(Project.class);
//        AntHandler.startProject(project.getProjectDirectory().getFileObject("/" + JPressoFileManager.BUILD_XML));
    }

    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    public String getName() {
        return NbBundle.getMessage(ExportProjectAction.class, "CTL_ExportProjectAction");
    }

    protected Class[] cookieClasses() {
        return COOKIE_CLASSES;
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

