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
package de.cismet.jpresso.project.gui.drivermanager;

import java.beans.PropertyChangeListener;

import java.io.File;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.core.data.DriverJar;
import de.cismet.jpresso.core.serviceprovider.DynamicDriverManager;
import de.cismet.jpresso.core.serviceprovider.JarDriverScanner;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

import de.cismet.jpresso.project.serviceprovider.ExecutorProvider;

/**
 * Provides a TreeModel for all the jars in a DriverDescription, with they available jdbc driver classes.
 *
 * <p>Has some utility methods and classes for adding/removing jars, scanning them for jdbc driver, etc.</p>
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public class DriverTreeModel extends DefaultTreeModel {

    //~ Instance fields --------------------------------------------------------

// private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final DefaultMutableTreeNode rootNode;
    private final DefaultComboBoxModel driverList;
    private final Collection<DriverJar> driverJars;
    private final Set<PropertyChangeListener> listener = TypeSafeCollections.newHashSet(1);
    private ScanWorker scanWorker = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DriverTreeModel object.
     *
     * @param   driverJars  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public DriverTreeModel(final Collection<DriverJar> driverJars) {
        super(new DefaultMutableTreeNode());
        this.rootNode = (DefaultMutableTreeNode)getRoot();
        this.driverList = new DefaultComboBoxModel();
        this.driverJars = driverJars;
        // install a listener that keeps our provided comboboxmodel up-to-date
        final Set<String> filterDoubles = TypeSafeCollections.newHashSet();
        for (final DriverJar dJar : driverJars) {
            if (dJar == null) {
                throw new IllegalArgumentException("DriverJar == null!");
            }

            final JarNode jN = new JarNode(dJar.getJarFile());
            if (dJar.isValid()) {
                for (final String classname : dJar.getDriverClassNames()) {
                    jN.add(new DriverNode(classname));
                    filterDoubles.add(classname);
                }
            }
            rootNode.add(jN);
        }
        for (final String classname : filterDoubles) {
            driverList.addElement(classname);
        }
        reload();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  pl  DOCUMENT ME!
     */
    public void addWorkerPropertyChangeListener(final PropertyChangeListener pl) {
        this.getListener().add(pl);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  pl  DOCUMENT ME!
     */
    public void removeListener(final PropertyChangeListener pl) {
        this.getListener().remove(pl);
    }

    /**
     * DOCUMENT ME!
     */
    public void removeAllListener() {
        this.getListener().clear();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  jar  DOCUMENT ME!
     * @param  up   DOCUMENT ME!
     */
    public void moveNode(final DefaultMutableTreeNode jar, final boolean up) {
        final DefaultMutableTreeNode parent = (DefaultMutableTreeNode)jar.getParent();
        final int index = parent.getIndex(jar);
        parent.getChildAt(index);
        if (up && (index > 0)) {
            insertNodeInto(jar, parent, index - 1);
            reload(parent);
            reload(jar);
        } else if (!up && (index < (parent.getChildCount() - 1))) {
            parent.remove(index);
            insertNodeInto(jar, parent, index + 1);
            reload(parent);
            reload(jar);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void updateDriverComboboxModel() {
        final Object currentSelection = driverList.getSelectedItem();
        driverList.removeAllElements();
        final Set<String> drv = getFoundDrivers();
        boolean inside = false;
        for (final String s : drv) {
            driverList.addElement(s);
            if (s.equals(currentSelection)) {
                inside = true;
            }
        }
        if (inside) {
            driverList.setSelectedItem(currentSelection);
        }
    }

//    /**
//     * not needed, removed via removeNode
//     * @param jar
//     */
//    public void removeJar(final File jar) {
//        throw new UnsupportedOperationException("not yet implemented!");
//    }
    /**
     * Scan jar files and add them into the model as JarNodes with all found drivers as their children.
     *
     * @param   jars  DOCUMENT ME!
     *
     * @throws  NullPointerException  DOCUMENT ME!
     */
    public void addJars(final File... jars) {
        final List<JarNode> newInserts = TypeSafeCollections.newArrayList();
        if (jars == null) {
            throw new NullPointerException("File to jar can not be null!");
        }
        Enumeration<JarNode> children;
        for (final File jar : jars) {
            boolean alreadyIn = false;
            // cast
            children = rootNode.children();
            while (children.hasMoreElements()) {
                if (children.nextElement().getJarPath().equals(jar)) {
                    alreadyIn = true;
                    continue;
                }
            }
            if (!alreadyIn) {
                final JarNode toInsert = new JarNode(jar);
                newInserts.add(toInsert);
                insertNodeInto(toInsert, rootNode, rootNode.getChildCount());
            }
        }
        scanDriver(newInserts.toArray(new JarNode[] {}));
    }

    /**
     * DOCUMENT ME!
     *
     * @return  a set of String cotaining all available jdbc driver classnames
     */
    public Set<String> getFoundDrivers() {
        final Set<String> set = TypeSafeCollections.newHashSet();
        DefaultMutableTreeNode leaf = rootNode.getFirstLeaf();
        JarNode parent;
        while (leaf != null) {
            if (leaf instanceof DriverNode) {
                parent = (JarNode)leaf.getParent();
                if ((parent.getJarPath() != null) && parent.getJarPath().isFile()) {
                    set.add(leaf.toString());
                }
            }
            leaf = leaf.getNextLeaf();
        }
        return set;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  a list of all available DriverJars
     */
    public List<DriverJar> getDriverJars() {
        final List<DriverJar> list = TypeSafeCollections.newArrayList();
        // cast
        final Enumeration<JarNode> children = rootNode.children();
        JarNode cNode;
        File jarFile;
        Set<String> driverClassNames;
        Enumeration<DriverNode> drivernodes;
        while (children.hasMoreElements()) {
            cNode = children.nextElement();
            jarFile = cNode.getJarPath();
            driverClassNames = TypeSafeCollections.newHashSet();
            // cast
            drivernodes = cNode.children();
            while (drivernodes.hasMoreElements()) {
                final DriverNode cur = drivernodes.nextElement();
                driverClassNames.add(cur.toString());
            }
            final DriverJar current = new DriverJar(jarFile, driverClassNames);
            list.add(current);
        }
        return list;
    }

    @Override
    public boolean isLeaf(final Object node) {
        if (node == getRoot()) {
            return false;
        }
        return super.isLeaf(node);
    }

    /**
     * Helps to remove multiple nodes with a single update on the combobox model.
     *
     * @param  nodes  DOCUMENT ME!
     */
    public void removeNodesFromParentNode(final MutableTreeNode... nodes) {
        for (final MutableTreeNode cur : nodes) {
            super.removeNodeFromParent(cur);
        }
    }

    /**
     * Helps to add multiple nodes in a row with a single update on the combobox model.
     *
     * @param  parent       DOCUMENT ME!
     * @param  newChildren  DOCUMENT ME!
     */
    public void insertNodesInto(final MutableTreeNode parent, final MutableTreeNode... newChildren) {
        for (final MutableTreeNode cur : newChildren) {
            super.insertNodeInto(cur, parent, parent.getChildCount());
        }
    }

    /**
     * Scans JarNodes and adds them with all scanned driver's nodes added.
     *
     * @param  jarNodes  DOCUMENT ME!
     */
    private void scanDriver(final JarNode... jarNodes) {
        if (this.getScanWorker() == null) {
            scanWorker = new ScanWorker(this, jarNodes);
            for (final PropertyChangeListener pl : listener) {
                scanWorker.addPropertyChangeListener(pl);
            }
            ExecutorProvider.execute(scanWorker);
        }
    }

    /**
     * cancels a running ScanWorker.
     */
    public void cancelScan() {
        if (scanWorker != null) {
            scanWorker.cancel(true);
        }
    }

    /**
     * (Re)scans the complete Tree .
     */
    public void scanDriver() {
        final List<JarNode> list = TypeSafeCollections.newArrayList();
        final Enumeration e = rootNode.children();
        JarNode current;
        Object o;
        while (e.hasMoreElements()) {
            o = e.nextElement();
            if (o instanceof JarNode) {
                current = (JarNode)o;
                current.removeAllChildren();
                list.add(current);
            }
        }
        reload();
        scanDriver(list.toArray(new JarNode[] {}));
    }

    /**
     * DOCUMENT ME!
     *
     * @param   manager  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Map<DriverDescription, DriverTreeModel> getDriverTreeModelsForDriverManager(
            final DynamicDriverManager manager) {
        return getDriverTreeModelsForDriverDescriptions(manager.getDriverDescriptionList());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   driverDescriptions  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Map<DriverDescription, DriverTreeModel> getDriverTreeModelsForDriverDescriptions(
            final List<DriverDescription> driverDescriptions) {
        final Map<DriverDescription, DriverTreeModel> ret = TypeSafeCollections.newHashMap();
        final List<DriverDescription> dds = (driverDescriptions != null) ? driverDescriptions
                                                                         : new ArrayList<DriverDescription>();
        // DANGER NOT THREADSAFE!
        final Iterator<DriverDescription> it = dds.iterator();
        DriverDescription cur;
        while (it.hasNext()) {
            cur = it.next();
            if (ret.get(cur) == null) {
                ret.put(cur, new DriverTreeModel(cur.getJarFiles()));
            }
        }
        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public DefaultComboBoxModel getDriverList() {
        return driverList;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Set<PropertyChangeListener> getListener() {
        return listener;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    ScanWorker getScanWorker() {
        return scanWorker;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  scanWorker  DOCUMENT ME!
     */
    void setScanWorker(final ScanWorker scanWorker) {
        this.scanWorker = scanWorker;
    }
}

/**
 * A SwingWorker for scanning Drivers in jars and adding them to the treemodel.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
final class ScanWorker extends SwingWorker<Void, NodeAssigner> {

    //~ Instance fields --------------------------------------------------------

    private final JarNode[] jarNodes;
    private final DriverTreeModel model;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ScanWorker object.
     *
     * @param   model      DOCUMENT ME!
     * @param   toProcess  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public ScanWorker(final DriverTreeModel model, final JarNode... toProcess) {
        if ((toProcess == null) || (model == null)) {
            throw new IllegalArgumentException("Null value not allowed in ScanWorker constructor!");
        }
        this.jarNodes = toProcess;
        this.model = model;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected Void doInBackground() throws Exception {
        final Map<JarNode, String[]> entries = TypeSafeCollections.newHashMap();
        int prog = 0;
        for (final JarNode jarNode : jarNodes) {
            entries.put(jarNode, jarNode.getJarFileEntries());
        }
        int resCount = 0;
        for (final String[] res : entries.values()) {
            resCount += res.length;
        }
        // --------------
        final Set<File> fileList = TypeSafeCollections.newHashSet();
        for (final JarNode jarNode : jarNodes) {
            fileList.add(jarNode.getJarPath());
        }
        final JarDriverScanner scanner = new JarDriverScanner(fileList.toArray(new File[] {}));
        for (final JarNode jarNode : jarNodes) {
            final List<DriverNode> children = TypeSafeCollections.newArrayList();
            final String[] resourceNames = entries.get(jarNode);

            for (String s : resourceNames) {
                final int progress = ++prog * 100 / resCount;
                setProgress(progress);
                if (isCancelled()) {
                    return null;
                }
                s = scanner.scanResourceForDriverClass(s);
                if (s != null) {
                    children.add(new DriverNode(s));
                }
            }
            publish(new NodeAssigner(jarNode, children));
        }
        setProgress(100);
        return null;
    }

    @Override
    protected void done() {
        try {
            get();
        } catch (InterruptedException ex) {
            log.warn("InterruptedException", ex);
        } catch (ExecutionException ex) {
            log.warn("ExecutionException", ex);
        } catch (CancellationException e) {
            // ignore!
// e.printStackTrace();
        }
//        model.reload();
        model.updateDriverComboboxModel();
        if (isCancelled()) {
            setProgress(0);
        }
        model.setScanWorker(null);
        // clear memory and finalize classloaders to release filelocks
        System.gc();
        System.gc();
    }

    @Override
    protected void process(final List<NodeAssigner> chunks) {
        for (final NodeAssigner na : chunks) {
            na.assign(model);
        }
    }
}

/**
 * A helper class that adds drivers to the right JarNode when dealing with threads.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
final class NodeAssigner {

    //~ Instance fields --------------------------------------------------------

    private final JarNode parent;
    private final List<DriverNode> children;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new NodeAssigner object.
     *
     * @param   parent    DOCUMENT ME!
     * @param   children  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public NodeAssigner(final JarNode parent, final List<DriverNode> children) {
        if ((parent == null) || (children == null)) {
            throw new IllegalArgumentException("Null values not allowed in NodeAssigner constructor!");
        }
        this.parent = parent;
        this.children = children;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  model  DOCUMENT ME!
     */
    public void assign(final DriverTreeModel model) {
        model.insertNodesInto(parent, children.toArray(new DriverNode[] {}));
    }
}
