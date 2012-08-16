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

import org.openide.explorer.view.NodeListModel;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.util.datatransfer.PasteType;

import java.awt.datatransfer.Transferable;

import java.util.List;

import de.cismet.jpresso.project.ProjectCookie;
import de.cismet.jpresso.project.nodes.children.JPressoChildren;

/**
 * Abstract superclass for all Project structure defining Node. (The nodes right below the project node) Can not be
 * renamed, cut, copied and creates a PasteType for the apropriate children type.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public abstract class AbstractJPNode extends AbstractNode implements ProjectCookie {

    //~ Instance fields --------------------------------------------------------

// private NodeListModel nodeListModel;
    private Project project;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AbstractJPNode object.
     *
     * @param  childDir  DOCUMENT ME!
     * @param  childExt  DOCUMENT ME!
     * @param  p         DOCUMENT ME!
     */
    public AbstractJPNode(final FileObject childDir, final String childExt, final Project p) {
        super(new JPressoChildren(childDir, childExt));
        this.project = p;
        // HINT: loades (precaches) all children of the extending node.
// this.nodeListModel = new NodeListModel(this);
        // getCookieSet().add(this);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  this nodes owning project
     */
    @Override
    public Project getProject() {
        return project;
    }

    /**
     * Creates a new NodeListModel that contains all children. Usage in query, run and sqlrun gui to get models for the
     * comboboxes.
     *
     * @return  children nodelistmodel
     */
    @Override
    public NodeListModel createNodeListModel() {
        return new NodeListModel(this);
    }
//    /**
//     * Creates a new NodeListModel that contains all children.
//     * Usage in query, run and sqlrun gui to get models for the comboboxes.
//     *
//     * @return children nodelistmodel
//     */
//    public synchronized NodeListModel createNodeListModel() {
//        NodeListModel tmp = this.nodeListModel;
//        this.nodeListModel = new NodeListModel(this);
//        return tmp;
//    }
    /**
     * Creates a pastetype accoring to the extending node's apropriate children type.
     *
     * @param  transferable  DOCUMENT ME!
     * @param  list          DOCUMENT ME!
     */
    @Override
    protected void createPasteTypes(final Transferable transferable, final List<PasteType> list) {
//        super.createPasteTypes(transferable, list);
        int mode = NodeTransfer.COPY;
        Node node = NodeTransfer.node(transferable, mode);
        if (node != null) {
            final DataObject data = node.getLookup().lookup(DataObject.class);
            if ((data != null) && data.getPrimaryFile().getExt().equals(getSupportedFileExt())) {
                list.add(new JPressoPasteType(data, mode, getChildrenDir()));
            }
        }
        mode = NodeTransfer.MOVE;
        node = NodeTransfer.node(transferable, mode);
        if (node != null) {
            final DataObject data = node.getLookup().lookup(DataObject.class);
            if ((data != null) && data.getPrimaryFile().getExt().equals(getSupportedFileExt())) {
                list.add(new JPressoPasteType(data, mode, getChildrenDir()));
            }
        }
    }

    @Override
    public final boolean canCopy() {
        return false;
    }

    @Override
    public final boolean canCut() {
        return false;
    }

    @Override
    public final boolean canRename() {
        return false;
    }

    /**
     * Returns the file extentions for a dataobjects primary file, who's Node delegate can be a subnode of the
     * implementing node.
     *
     * @return  the file extentions
     */
    public abstract String getSupportedFileExt();

    /**
     * Returns the directoy of the files from which the subnodes are node delegates.
     *
     * @return  the directoy of the files from which the subnodes are node delegates.
     */
    abstract FileObject getChildrenDir();
}
