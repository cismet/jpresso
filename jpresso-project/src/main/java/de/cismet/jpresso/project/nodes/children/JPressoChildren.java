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
package de.cismet.jpresso.project.nodes.children;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.loaders.DataObject;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Utilities;

import java.awt.Image;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.Action;

import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class JPressoChildren extends Children.Keys<FileObject> implements FileChangeListener {

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private FileObject childrenDir;
    private String childrenFileExt;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new JPressoChildren object.
     *
     * @param  childDir      DOCUMENT ME!
     * @param  childFileExt  DOCUMENT ME!
     */
    public JPressoChildren(final FileObject childDir, final String childFileExt) {
        super();
        this.childrenDir = childDir;
        this.childrenFileExt = childFileExt;

        if ((childDir != null) && childrenDir.isValid()) {
//                FileUtil.weakFileChangeListener(this, childrenDir);
//                FileUtil.weakFileChangeListener(this, childrenDir.getParent());
            childDir.addFileChangeListener(this);
            childrenDir.getParent().addFileChangeListener(this);
        } else {
            childrenDir = null;
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected void addNotify() {
        // impliziert setKeys()!!
        refreshList();
    }

    @SuppressWarnings("unchecked") // can not compromise
    @Override
    protected void removeNotify() {
        setKeys(Collections.EMPTY_SET);
    }

    @Override
    protected Node[] createNodes(final FileObject key) {
        try {
            // Für alle Children wird ausgeführt:
            DataObject data = null;
            if (key != null) {
                data = DataObject.find(key);
                if (data != null) {
                    final FileObject pf = data.getPrimaryFile();
                    if ((pf != null) && pf.isValid()) {
                        Node cnode = data.getNodeDelegate().cloneNode();
//                Node cnode = JPressoChildrenFactory.createChildNode(data);
                        if (pf.getExt().equals(JPressoFileManager.END_JAVA)) {
                            cnode = new FilterNode(cnode) {

                                    @Override
                                    public Action[] getActions(final boolean context) {
                                        final Action[] act = super.getActions(context);
                                        return act;
                                    }

                                    @Override
                                    public Image getIcon(final int type) {
                                        return Utilities.loadImage("de/cismet/jpresso/project/res/java.png");
                                    }

                                    @Override
                                    public String getName() {
                                        return super.getName();
                                    }

                                    @Override
                                    public String getDisplayName() {
                                        return getName();
                                    }
                                };
                        }
                        if (cnode != null) {
                            return new Node[] { cnode };
                        }
                    }
                }
            }
            return new Node[] {};
        } catch (Exception ex) {
            final String message = "Error creating children for directory " + childrenDir.getPath()
                        + ".\nPossibly corrupted or not existing File.\n";
            log.error(message, ex);
            DialogDisplayer.getDefault()
                    .notify(new NotifyDescriptor.Message(message + ex.toString(), NotifyDescriptor.ERROR_MESSAGE));
        }
        return new Node[] {};
    }

    /**
     * DOCUMENT ME!
     */
    public void refreshList() {
        FileObject[] fos = null;
        final List<FileObject> sortedFileCol = TypeSafeCollections.newArrayList();
        if ((childrenDir != null) && childrenDir.isValid()) {
            childrenDir.refresh();
            fos = childrenDir.getChildren();
            // gesuchte dateien filtern...alle andern ignorieren
            for (final FileObject fo : fos) {
                if (fo.getExt().equalsIgnoreCase(childrenFileExt)) {
                    sortedFileCol.add(fo);
                }
            }
            // -----------------optional sorting------------------------
            Collections.sort(sortedFileCol, new Comparator<FileObject>() {

                    @Override
                    public int compare(final FileObject o1, final FileObject o2) {
                        if ((o1 != null) && (o2 != null)) {
                            return o1.getName().compareToIgnoreCase(o2.getName());
                        }
                        return 0;
                    }
                });
            // -----------------------------------------------------------
        }
        // Festlegen für welche "Elemente"/keys createNode aufgerufen wird
        // quasi: Welche Knoten sind zu erzeugen?
        setKeys(sortedFileCol);
    }

    @Override
    public void fileFolderCreated(final FileEvent fe) {
        // if the folder was deleted or renamed, and a new connection folder appears,
        // it becomes the new childDir und is listened to.
        if (!childrenDir.isValid() && fe.getFile().getPath().equals(childrenDir.getPath())) {
            childrenDir.removeFileChangeListener(this);
            childrenDir = fe.getFile();
            childrenDir.addFileChangeListener(this);
            refreshList();
        }
    }

    @Override
    public void fileDataCreated(final FileEvent fe) {
        if (fe.getFile().getExt().equals(childrenFileExt) || fe.getFile().equals(childrenDir)) {
            refreshList();
        }
    }

    @Override
    public void fileChanged(final FileEvent fe) {
        if (fe.getFile().getExt().equals(childrenFileExt) || fe.getFile().equals(childrenDir)) {
            refreshList();
        }
    }

    @Override
    public void fileDeleted(final FileEvent fe) {
        // TODO ueberall FileUtil.createFolder() benutzen??
        if (fe.getFile().getExt().equals(childrenFileExt) || fe.getFile().equals(childrenDir)) {
            refreshList();
        }
    }

    @Override
    public void fileRenamed(final FileRenameEvent fe) {
        if (fe.getFile().getExt().equals(childrenFileExt) || fe.getFile().equals(childrenDir)) {
            refreshList();
        }
    }

    @Override
    public void fileAttributeChanged(final FileAttributeEvent fe) {
//        if (fe.getFile().getExt().equals(childrenFileExt) || fe.getFile().equals(childrenDir)) {
//            refreshList();
//        }
    }
}
