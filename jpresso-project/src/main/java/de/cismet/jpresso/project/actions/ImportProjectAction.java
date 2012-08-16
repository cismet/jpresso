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
package de.cismet.jpresso.project.actions;

import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ui.OpenProjects;

import org.openide.ErrorManager;
import org.openide.filesystems.FileUtil;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.WindowManager;

import java.awt.BorderLayout;
import java.awt.Frame;

import java.io.File;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;

import de.cismet.jpresso.core.serviceprovider.ImporterExporter;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;

import de.cismet.jpresso.project.serviceprovider.ExecutorProvider;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class ImportProjectAction extends CallableSystemAction {

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(getClass());
    private final JFileChooser chooserSrc;
    private final JFileChooser chooserDest;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ImportProjectAction object.
     */
    public ImportProjectAction() {
        chooserSrc = new JFileChooser();
        chooserSrc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooserSrc.setMultiSelectionEnabled(true);
        chooserSrc.setFileFilter(new FileFilter() {

                @Override
                public boolean accept(final File f) {
                    return (f.isDirectory() || f.getName().endsWith("." + JPressoFileManager.END_ZIP));
                }

                @Override
                public String getDescription() {
                    return "." + JPressoFileManager.END_ZIP;
                }
            });
        chooserSrc.setAcceptAllFileFilterUsed(false);
        chooserDest = new JFileChooser();
        chooserDest.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void performAction() {
        final Frame mainWindow = WindowManager.getDefault().getMainWindow();
        //
        if (chooserSrc.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
            final File toExtract = chooserSrc.getSelectedFile();

            if (chooserDest.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
                final File dest = chooserDest.getSelectedFile();
                dest.mkdirs();
                final SwingWorker iw = new ImportWorker(toExtract, dest);
                ExecutorProvider.execute(iw);
            }
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(ImportProjectAction.class, "CTL_ImportProjectAction");
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

    //~ Inner Classes ----------------------------------------------------------

    /**
     * The "importing please wait"-panel.
     *
     * @version  $Revision$, $Date$
     */
    private static final class ImportingPanel extends JPanel {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ImportingPanel object.
         */
        public ImportingPanel() {
            final JProgressBar prog = new JProgressBar();
            setLayout(new BorderLayout());
            add(new JLabel("Importing project...please wait..."), BorderLayout.CENTER);
            add(prog, BorderLayout.SOUTH);
            prog.setIndeterminate(true);
        }
    }

    /**
     * A SwingWorker that imports a JPProject from zip.
     *
     * @version  $Revision$, $Date$
     */
    static final class ImportWorker extends SwingWorker<Project, Void> {

        //~ Instance fields ----------------------------------------------------

        private final File zip;
        private final File dest;
        private JDialog dlg;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ImportWorker object.
         *
         * @param  zip   DOCUMENT ME!
         * @param  dest  DOCUMENT ME!
         */
        public ImportWorker(final File zip, final File dest) {
            this.zip = zip;
            this.dest = dest;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        protected Project doInBackground() throws Exception {
            publish((Void)null);
            ImporterExporter.importProjectFromZip(zip, dest);
            Project p = null;
            int failed = 0;
            while ((p == null) && (failed < 15)) {
                Thread.sleep(500);
                p = ProjectManager.getDefault().findProject(FileUtil.toFileObject(dest));
                ++failed;
            }
            if (p == null) {
                throw new IllegalStateException("Can not open created project!");
            }
//            final FilterNode activeNode = Utilities.actionsGlobalContext().lookup(FilterNode.class);
//            final ProjectCookie projNode = activeNode.getCookie(ProjectCookie.class);
//            final FileObject buildXML = projNode.getProject().getProjectDirectory().getFileObject(JPressoFileManager.BUILD_XML);
//            final FileObject buildXML = p.getProjectDirectory().getFileObject(JPressoFileManager.BUILD_XML);
//            if (buildXML != null) {
//                try {
//                    AntHandler.writeDefaultProperties();
//                } catch (IOException ex) {
//                    log.error("", ex);
//                }
//            }
            return p;
        }

        @Override
        protected void done() {
            try {
                OpenProjects.getDefault().open(new Project[] { get() }, false);
            } catch (InterruptedException ex) {
                ErrorManager.getDefault().notify(ErrorManager.ERROR, ex);
            } catch (ExecutionException ex) {
                ErrorManager.getDefault().notify(ErrorManager.ERROR, ex.getCause());
            } catch (Exception ex) {
                ErrorManager.getDefault().notify(ErrorManager.ERROR, ex);
            } finally {
                if (dlg != null) {
                    dlg.setVisible(false);
                    dlg.dispose();
                }
            }
        }

        @Override
        protected void process(final List<Void> chunks) {
            final Frame mainWindow = WindowManager.getDefault().getMainWindow();
            dlg = new JDialog(mainWindow, "Importing JPresso Project", true);
            dlg.getContentPane().add(new ImportingPanel());
            dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            dlg.pack();
            dlg.setResizable(false);
            dlg.setVisible(true);
        }
    }
}
