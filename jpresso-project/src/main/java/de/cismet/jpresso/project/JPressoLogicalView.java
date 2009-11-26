/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project;

import de.cismet.jpresso.project.filetypes.cookies.ConnectionListModelProvider;
import de.cismet.jpresso.project.filetypes.cookies.QueryListModelProvider;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.explorer.view.NodeListModel;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFilter;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 * Represents the LogivalView of a JPresso Project. The LogicalView is what you
 * can see e.g. as a tree-structure in Netbeans navigator window. It is mainly
 * represented by the project's top-level node, which has all other nodes in 
 * parent-child relations below it.
 * 
 * @author srichter
 */
public final class JPressoLogicalView implements LogicalViewProvider, ConnectionListModelProvider, QueryListModelProvider {
    
    //the project
    private final JPressoProject project;
    //the top-level project node
    private JPressoProjectNode projectNode;
    //a nodelistmodel for all connections
    private ConnectionListModelProvider conProv;
    //a nodelistmodel for all connections
    private QueryListModelProvider qryProv;
    
    /**
     * 
     * @param project
     */
    public JPressoLogicalView(JPressoProject project) {
        this.project = project;
        conProv = null;
        qryProv = null;
    }
    
    /**
     * Create the LogicalView and return its top-level node.
     * 
     * @return the top-level (project) node with a structure of children.
     */
    public Node createLogicalView() {
        //Get the scenes directory, creating if deleted
        final FileObject main = project.getProjectDirectory();

        //Get the DataObject that represents it
        final DataFolder projDirDataObject = DataFolder.findFolder(main);

        final Children children = projDirDataObject.createNodeChildren(new DataFilter() {

            public boolean acceptDataObject(final DataObject dataObject) {
                if (dataObject.getPrimaryFile().getExt().equalsIgnoreCase("properties")) {
                    return true;
                } else {
                    return false;
                }
            }
            });

        Node n = new AbstractNode(children);

//        try {
        final JPressoProjectNode pn = new JPressoProjectNode(n, project);
        setProjectNode(pn);

        return pn;

//        }   catch (DataObjectNotFoundException donfe) {
//            ErrorManager.getDefault().notify(donfe);
//            //Fallback - the directory couldn't be created -
//            //read-only filesystem or something evil happened
//            return new AbstractNode(Children.LEAF);
//        }
    }

    /**
     * Dummy implementation. Not important.
     * 
     * @param root
     * @param target
     * @return
     */
    public Node findPath(final Node root, final Object target) {
        return null;
    }
    
    /**
     * 
     * @return
     */
    JPressoProjectNode getProjectNode() {
        return projectNode;
    }
    
    /**
     * 
     * @param projectNode
     */
    void setProjectNode(final JPressoProjectNode projectNode) {
        this.projectNode = projectNode;
    }
    
    /**
     * 
     * @return a NodeListModel containing all nodes under the 
     * ConnectionManagementNode (e.g. for use in the selection combobox 
     * in the runs).
     */
    public NodeListModel getConnectionListModel() {
        if (conProv != null) {
            return conProv.getConnectionListModel();
        } else {
            findListProvider();
            return conProv.getConnectionListModel();
        }
    }
    
    /**
     * 
     * @return a NodeListModel containing all nodes under the 
     * QueryManagementNode (e.g. for use in the selection combobox 
     * in the runs).
     */
    public NodeListModel getQueryListModel() {
        if (qryProv != null) {
            return qryProv.getQueryListModel();
        } else {
            findListProvider();
            return qryProv.getQueryListModel();
        }
    }
    
    /**
     * 
     */
    private void findListProvider() {
        final Node projNode = getProjectNode();
        if (projNode != null) {
            final Node[] subNodes = projNode.getChildren().getNodes();
            if (this.conProv == null) {
                for (final Node n : subNodes) {
                    this.conProv = n.getLookup().lookup(ConnectionListModelProvider.class);
                    if (this.conProv != null) {
                        break;
                    }
                }
            }
            if (this.qryProv == null) {
                for (final Node n : subNodes) {
                    this.qryProv = n.getLookup().lookup(QueryListModelProvider.class);
                    if (this.qryProv != null) {
                        break;
                    }
                }
            }
        }
    }
}
