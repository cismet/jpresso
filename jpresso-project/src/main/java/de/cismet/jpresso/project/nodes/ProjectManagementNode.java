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

import org.netbeans.api.project.Project;

import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;

import java.awt.Image;
import java.awt.datatransfer.Transferable;

import java.io.File;
import java.io.IOException;

import java.util.List;

import javax.swing.Action;
import javax.swing.JOptionPane;

import de.cismet.jpresso.core.data.ProjectOptions;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

import de.cismet.jpresso.project.JPressoProject;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class ProjectManagementNode extends AbstractNode {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(
            ProjectManagementNode.class);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProjectManagementNode object.
     *
     * @param  project  DOCUMENT ME!
     */
    public ProjectManagementNode(final JPressoProject project) {
        super(new ProjectManagementChildren(project));
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Action[] getActions(final boolean context) {
        return new SystemAction[] {};
    }

    @Override
    public Image getIcon(final int i) {
        return Utilities.loadImage(
                "de/cismet/jpresso/project/res/lin_agt_wrench.png");
    }

    @Override
    public Image getOpenedIcon(final int i) {
        return getIcon(i);
    }

    @Override
    public String getDisplayName() {
        return "Control";
    }

    @Override
    public boolean canCopy() {
        return false;
    }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    protected void createPasteTypes(final Transferable t, final List<PasteType> s) {
//        s = TypeSafeCollections.newArrayList();
    }
}

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
final class ProjectManagementChildren extends Children.Array {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(
            ProjectManagementChildren.class);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProjectManagementChildren object.
     *
     * @param  project  DOCUMENT ME!
     */
    public ProjectManagementChildren(final JPressoProject project) {
        super(createNodes(project));
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   project  DOCUMENT ME!
     * @param   name     DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static DataObject getDataObject(final Project project, final String name) {
        try {
            final FileObject projDir = project.getProjectDirectory();
            FileObject fo = projDir.getFileObject("/" + name);
            if (fo == null) {
                try {
                    fo = projDir.createData(name);
                    if (name.equalsIgnoreCase(JPressoFileManager.PROJECT_OPTIONS)) {
                        JPressoFileManager.getDefault().persist(FileUtil.toFile(fo), new ProjectOptions());
                    }
                    fo = projDir.getFileObject("/" + name);
                } catch (IOException ex) {
                    log.error("IO Error on " + name + "!", ex);
                }
            }
            final DataObject dObj = DataObject.find(fo);
            return dObj;
        } catch (DataObjectNotFoundException ex) {
            log.error("No " + name + " found!", ex);
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   project  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static List<Node> createNodes(final Project project) {
        final List<Node> nodeList = TypeSafeCollections.newArrayList();
        DataObject dob = getDataObject(project, JPressoFileManager.PROJECT_PLAN);
        if ((dob != null) && dob.getPrimaryFile().isValid()) {
            // a filternode around a clone of the java code node to disable some unwished functions
            // IMPORTANT: must use a clone and not the original node!
            final Node jn = new FilterNode(dob.getNodeDelegate().cloneNode()) {

                    @Override
                    public Image getIcon(final int type) {
//                    return Utilities.loadImage(
//                            "de/cismet/jpresso/project/res/enumList.png");
                        return super.getIcon(type);
                    }

                    @Override
                    public String getName() {
                        return "Project Plan";
                    }

                    @Override
                    public String getDisplayName() {
                        return getName();
                    }

                    @Override
                    public boolean canRename() {
                        return false;
                    }

                    @Override
                    public boolean canCopy() {
                        return false;
                    }

                    @Override
                    public boolean canCut() {
                        return false;
                    }

                    @Override
                    public boolean canDestroy() {
                        return false;
                    }
                };
            if (jn != null) {
                nodeList.add(jn);
            }
        }
        dob = getDataObject(project, JPressoFileManager.PROJECT_OPTIONS);
        if ((dob != null) && dob.getPrimaryFile().isValid()) {
            // IMPORTANT: must use a clone and not the original node!
            final Node on = dob.getNodeDelegate().cloneNode();
            if (on != null) {
                nodeList.add(on);
            }
        } else {
            try {
                log.info("Create ProjectOptions for " + project.getProjectDirectory().getName());
                final File f = new File(FileUtil.toFile(project.getProjectDirectory()).getAbsolutePath()
                                + File.separator + JPressoFileManager.PROJECT_OPTIONS);
                if (f.exists()) {
                    f.delete();
                    f.createNewFile();
                }
                FileUtil.refreshFor(f);
                final FileSystem fs = Repository.getDefault().getDefaultFileSystem();
                final FileObject result = fs.findResource(JPressoFileManager.PROJECT_OPTIONS);
                ProjectOptions po = null;
                if (result == null) {
                    po = new ProjectOptions();
                } else {
                    try {
                        log.info("Using global default ProjectOptions.");
                        po = JPressoFileManager.getDefault().load(FileUtil.toFile(result), ProjectOptions.class);
                    } catch (Exception e) {
                        log.warn("Global default ProjectOptions not found. Creating with new ProjectOptions object!");
                        po = new ProjectOptions();
                    }
                }
                JPressoFileManager.getDefault().persist(f, po);
            } catch (IOException ex) {
                log.error("Error creating new " + JPressoFileManager.PROJECT_OPTIONS + "!", ex);
            }
            dob = getDataObject(project, JPressoFileManager.PROJECT_OPTIONS);
            if (dob != null) {
                // IMPORTANT: must use a clone and not the original node!
                final Node on = dob.getNodeDelegate().cloneNode();
                if (on != null) {
                    nodeList.add(on);
                }
            } else {
                JOptionPane.showMessageDialog(
                    null,
                    "Could not create new file "
                            + JPressoFileManager.PROJECT_OPTIONS
                            + "! Options node for this project will not be available!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        // ---test
        // nodeList.add(DataFolder.findFolder(project.getProjectDirectory()).getNodeDelegate().cloneNode());
        // ---
        return nodeList;
    }
}
