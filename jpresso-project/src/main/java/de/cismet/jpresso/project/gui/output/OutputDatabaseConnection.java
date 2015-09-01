/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * OutputDatabaseConnection.java
 *
 * Created on 30. Januar 2008, 16:57
 */
package de.cismet.jpresso.project.gui.output;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.io.File;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import de.cismet.jpresso.core.data.JPressoRun;
import de.cismet.jpresso.core.data.Mapping;
import de.cismet.jpresso.core.data.Query;
import de.cismet.jpresso.core.data.Reference;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

import de.cismet.jpresso.project.gui.AbstractJPTopComponent;
import de.cismet.jpresso.project.gui.connection.ConnectionTopComponent;
import de.cismet.jpresso.project.gui.dnd.JPDataFlavors;
import de.cismet.jpresso.project.gui.dnd.Mapable;
import de.cismet.jpresso.project.gui.dnd.MapableCollector;
import de.cismet.jpresso.project.gui.dnd.Referenceable;
import de.cismet.jpresso.project.gui.dnd.ReferenceableCollector;
import de.cismet.jpresso.project.gui.editors.TopComponentFinderPanel;
import de.cismet.jpresso.project.gui.run.RunTopComponent;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class OutputDatabaseConnection extends TopComponentFinderPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final String CIDS_ROOT_STMT =
        "select a.id, c.name,c.table_name,c.primary_key_field,a.field_name,t.name,t.complex_type, a.foreign_key, c_zwo.table_name, c_zwo.primary_key_field from cs_class c ,cs_attr a,cs_type t, cs_class c_zwo where c.id=a.class_id and t.id=a.type_id and c.id=c_zwo.id and foreign_key_references_to is null "
                + "union select a.id,c.name,c.table_name,c.primary_key_field,a.field_name,t.name,t.complex_type, a.foreign_key, c_zwo.table_name, c_zwo.primary_key_field from cs_class c ,cs_attr a,cs_type t, cs_class c_zwo where c.id=a.class_id and t.id=a.type_id and foreign_key_references_to=c_zwo.id order by 1";
    private static final String SYSTEM = "system";
    private static final String T_STRING = "t";

    //~ Instance fields --------------------------------------------------------

    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private Connection connection;
    private boolean cids;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JMenuItem mnuAddToMappings;
    private javax.swing.JMenuItem mnuAddToRelations;
    private javax.swing.JPanel panCids;
    private javax.swing.JPopupMenu popCreate;
    private javax.swing.JPopupMenu popMain;
    private javax.swing.JScrollPane scpCids;
    private javax.swing.JScrollPane scpSystem;
    private javax.swing.JTabbedPane tabMain;
    private javax.swing.JTree trvCids;
    private javax.swing.JTree trvSystem;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form OutputDatabaseConnection.
     *
     * @param  connection  DOCUMENT ME!
     */
    public OutputDatabaseConnection(final Connection connection) {
        cids = true;
        this.connection = connection;
        initComponents();
        this.setName("Target");
        if (!cids) {
            tabMain.remove(panCids);
        }
        this.connection = null;
        // Install the Tansfer Handler for D&D on the "system" tree
        trvSystem.setTransferHandler(new TreeNodeTransferHandler(trvSystem));
        // Install the Tansfer Handler for D&D on the "cids" tree
        trvCids.setTransferHandler(new TreeNodeTransferHandler(trvCids));

        trvSystem.setDragEnabled(true);
        trvCids.setDragEnabled(true);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        popMain = new javax.swing.JPopupMenu();
        mnuAddToMappings = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        mnuAddToRelations = new javax.swing.JMenuItem();
        popCreate = new javax.swing.JPopupMenu();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        jMenuItem2 = new javax.swing.JMenuItem();
        tabMain = new javax.swing.JTabbedPane();
        scpSystem = new javax.swing.JScrollPane();
        trvSystem = new JTree(new DefaultTreeModel(getSystemRoot(connection)));
        panCids = new javax.swing.JPanel();
        scpCids = new javax.swing.JScrollPane();
        trvCids = new JTree(new DefaultTreeModel(getCidsRoot(connection)));

        mnuAddToMappings.setText(org.openide.util.NbBundle.getMessage(
                OutputDatabaseConnection.class,
                "OutputDatabaseConnection.mnuAddToMappings.text_1")); // NOI18N
        mnuAddToMappings.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    mnuAddToMappingsActionPerformed(evt);
                }
            });
        popMain.add(mnuAddToMappings);
        mnuAddToMappings.getAccessibleContext()
                .setAccessibleName(org.openide.util.NbBundle.getMessage(
                        OutputDatabaseConnection.class,
                        "OutputDatabaseConnection.mnuAddToMappings.AccessibleContext.accessibleName")); // NOI18N

        popMain.add(jSeparator1);

        mnuAddToRelations.setText(org.openide.util.NbBundle.getMessage(
                OutputDatabaseConnection.class,
                "OutputDatabaseConnection.mnuAddToRelations.text_1")); // NOI18N
        mnuAddToRelations.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    mnuAddToRelationsActionPerformed(evt);
                }
            });
        popMain.add(mnuAddToRelations);

        popCreate.add(jSeparator2);

        jMenuItem1.setText(org.openide.util.NbBundle.getMessage(
                OutputDatabaseConnection.class,
                "OutputDatabaseConnection.jMenuItem1.text")); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jMenuItem1ActionPerformed(evt);
                }
            });
        popCreate.add(jMenuItem1);
        popCreate.add(jSeparator3);

        jMenuItem2.setText(org.openide.util.NbBundle.getMessage(
                OutputDatabaseConnection.class,
                "OutputDatabaseConnection.jMenuItem2.text")); // NOI18N
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jMenuItem2ActionPerformed(evt);
                }
            });
        popCreate.add(jMenuItem2);

        setLayout(new java.awt.BorderLayout());

        tabMain.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mousePressed(final java.awt.event.MouseEvent evt) {
                    tabMainMousePressed(evt);
                }
                @Override
                public void mouseReleased(final java.awt.event.MouseEvent evt) {
                    tabMainMouseReleased(evt);
                }
            });

        scpSystem.setName("system"); // NOI18N

        trvSystem.setFont(new java.awt.Font("Dialog", 0, 10));
        trvSystem.setRootVisible(false);
        final DefaultTreeCellRenderer systemRenderer = new DefaultTreeCellRenderer();
        systemRenderer.setOpenIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/jpresso/project/res/sysdb.gif")));
        systemRenderer.setClosedIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/jpresso/project/res/sysdb_c.gif")));
        systemRenderer.setLeafIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/jpresso/project/res/rem_co.gif")));
        trvSystem.setCellRenderer(systemRenderer);
        trvSystem.setName("system");
        trvSystem.setDragEnabled(true);
        trvSystem.setRootVisible(false);
        trvSystem.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mousePressed(final java.awt.event.MouseEvent evt) {
                    trvSystemMousePressed(evt);
                }
                @Override
                public void mouseReleased(final java.awt.event.MouseEvent evt) {
                    trvSystemMouseReleased(evt);
                }
            });
        scpSystem.setViewportView(trvSystem);

        tabMain.addTab(org.openide.util.NbBundle.getMessage(
                OutputDatabaseConnection.class,
                "OutputDatabaseConnection.scpSystem.TabConstraints.tabTitle_2"),
            scpSystem); // NOI18N

        panCids.setName("cids"); // NOI18N
        panCids.setLayout(new java.awt.BorderLayout());

        trvCids.setRootVisible(false);
        trvCids.setFont(new java.awt.Font("Dialog", 0, 10));
        trvCids.setRootVisible(false);
        final DefaultTreeCellRenderer cidsRenderer = new DefaultTreeCellRenderer();
        cidsRenderer.setOpenIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/jpresso/project/res/class_obj.gif")));
        cidsRenderer.setClosedIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/jpresso/project/res/innerclass_protected_obj.gif")));
        cidsRenderer.setLeafIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/jpresso/project/res/protected_co.gif")));
        trvCids.setCellRenderer(cidsRenderer);
        trvCids.setName("cids");
        trvCids.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mousePressed(final java.awt.event.MouseEvent evt) {
                    trvCidsMousePressed(evt);
                }
                @Override
                public void mouseReleased(final java.awt.event.MouseEvent evt) {
                    trvCidsMouseReleased(evt);
                }
            });
        scpCids.setViewportView(trvCids);

        panCids.add(scpCids, java.awt.BorderLayout.CENTER);

        tabMain.addTab(org.openide.util.NbBundle.getMessage(
                OutputDatabaseConnection.class,
                "OutputDatabaseConnection.panCids.TabConstraints.tabTitle_1"),
            panCids); // NOI18N

        add(tabMain, java.awt.BorderLayout.CENTER);
    } // </editor-fold>//GEN-END:initComponents
    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void trvSystemMousePressed(final java.awt.event.MouseEvent evt) { //GEN-FIRST:event_trvSystemMousePressed
        if (evt.isPopupTrigger()) {
            final TreePath[] tp = trvSystem.getSelectionPaths();
            handlePopupEventForTree(tp, evt);
        }
    }                                                                         //GEN-LAST:event_trvSystemMousePressed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void trvSystemMouseReleased(final java.awt.event.MouseEvent evt) { //GEN-FIRST:event_trvSystemMouseReleased
        if (evt.isPopupTrigger()) {
            final TreePath[] tp = trvSystem.getSelectionPaths();
            handlePopupEventForTree(tp, evt);
        }
    }                                                                          //GEN-LAST:event_trvSystemMouseReleased

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void trvCidsMousePressed(final java.awt.event.MouseEvent evt) { //GEN-FIRST:event_trvCidsMousePressed
        if (evt.isPopupTrigger()) {
            final TreePath[] tp = trvCids.getSelectionPaths();
            handlePopupEventForTree(tp, evt);
        }
    }                                                                       //GEN-LAST:event_trvCidsMousePressed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void trvCidsMouseReleased(final java.awt.event.MouseEvent evt) { //GEN-FIRST:event_trvCidsMouseReleased
        if (evt.isPopupTrigger()) {
            final TreePath[] tp = trvCids.getSelectionPaths();
            handlePopupEventForTree(tp, evt);
        }
    }                                                                        //GEN-LAST:event_trvCidsMouseReleased

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void tabMainMousePressed(final java.awt.event.MouseEvent evt) { //GEN-FIRST:event_tabMainMousePressed
    }                                                                       //GEN-LAST:event_tabMainMousePressed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void tabMainMouseReleased(final java.awt.event.MouseEvent evt) { //GEN-FIRST:event_tabMainMouseReleased
    }                                                                        //GEN-LAST:event_tabMainMouseReleased

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void mnuAddToMappingsActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_mnuAddToMappingsActionPerformed

        TreePath[] tps = null;
        if (tabMain.getSelectedComponent().getName().equals(SYSTEM)) {
            tps = trvSystem.getSelectionPaths();
        } else {
            tps = trvCids.getSelectionPaths();
        }
        for (final TreePath tp : tps) {
            final Object selection = tp.getLastPathComponent();
            if (selection instanceof Mapable) {
                final Mapable m = (Mapable)selection;
                final List<Mapping> maps = m.getMappings();
                findSpecificTopComponent(RunTopComponent.class).addMappings(maps);
            }
        }
    } //GEN-LAST:event_mnuAddToMappingsActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void mnuAddToRelationsActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_mnuAddToRelationsActionPerformed

        final TreePath[] tps = trvCids.getSelectionPaths();
        for (final TreePath tp : tps) {
            final Object selection = tp.getLastPathComponent();
            if (selection instanceof Referenceable) {
                final Referenceable r = (Referenceable)selection;
                final List<Reference> rel = r.getReferences();
                findSpecificTopComponent(RunTopComponent.class).addRelations(rel);
            }
        }
    } //GEN-LAST:event_mnuAddToRelationsActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jMenuItem1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jMenuItem1ActionPerformed
        final String filename = promptFileName();
        if ((filename != null) && ("").equals(filename)) {
            return;
        }
        final ConnectionTopComponent ctc = findSpecificTopComponent(ConnectionTopComponent.class);
        final String path = FileUtil.toFile(ctc.getProject().getProjectDirectory()).getAbsolutePath();
        TreePath[] tps = null;
        if (tabMain.getSelectedComponent().getName().equals(SYSTEM)) {
            tps = trvSystem.getSelectionPaths();
        } else {
            tps = trvCids.getSelectionPaths();
        }
        final JPressoRun run = new JPressoRun();
        final List<Mapping> mappings = TypeSafeCollections.newArrayList();
        final Query qry = new Query();
        qry.setQueryStatement("SELECT * FROM $TABLE_TO_IMPORT");
        for (final TreePath tp : tps) {
            if ((tp != null) && (tp.getLastPathComponent() != null)) {
                final Object selection = tp.getLastPathComponent();
                if (selection instanceof TargetTable) {
                    final TargetTable t = (TargetTable)selection;
                    mappings.addAll(t.getMappings());
                }
            }
        }
        run.setSourceQuery(filename + "." + JPressoFileManager.END_QUERY);         //GEN-LAST:event_jMenuItem1ActionPerformed
        run.setMappings(mappings);
        run.setTargetConnection(ctc.getData().getPrimaryFile().getNameExt());
        createFiles(qry, run, filename, path);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String promptFileName() {
        final NotifyDescriptor.InputLine desc = new NotifyDescriptor.InputLine("Enter name", "Create new Import");
        DialogDisplayer.getDefault().notify(desc);
        String filename = desc.getInputText();
        filename = (filename != null) ? filename : "";
        return filename;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jMenuItem2ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jMenuItem2ActionPerformed
        final String filename = promptFileName();
        if ((filename != null) && ("").equals(filename)) {
            return;
        }
        TreePath[] tps = null;
        if (tabMain.getSelectedComponent().getName().equals(SYSTEM)) {
            tps = trvSystem.getSelectionPaths();
        } else {
            tps = trvCids.getSelectionPaths();
        }
        final Query qry = new Query();
        qry.setQueryStatement("SELECT * FROM $TABLE_TO_IMPORT");
        final ConnectionTopComponent ctc = findSpecificTopComponent(ConnectionTopComponent.class);
        final JPressoRun run = new JPressoRun();
        run.getRuntimeProperties().setFinalizerClass("StandardFinalizer");
        run.getRuntimeProperties().getFinalizerProperties().setProperty("Rollback", "true");
        final List<Mapping> mappings = TypeSafeCollections.newArrayList();
        final String path = FileUtil.toFile(ctc.getProject().getProjectDirectory()).getAbsolutePath();
        for (final TreePath tp : tps) {
            if ((tp != null) && (tp.getLastPathComponent() != null)) {
                final Object selection = tp.getLastPathComponent();
                if (selection instanceof TargetTable) {
                    final TargetTable t = (TargetTable)selection;
                    for (final Mapping m : t.getMappings()) {
                        m.setContent(m.getTargetField());
                        mappings.add(m);
                    }
                }
            }
        }
        run.setSourceQuery(filename + "." + JPressoFileManager.END_QUERY);         //GEN-LAST:event_jMenuItem2ActionPerformed
        run.setMappings(mappings);
        run.setTargetConnection(ctc.getData().getPrimaryFile().getNameExt());
        createFiles(qry, run, filename, path);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  qry   DOCUMENT ME!
     * @param  run   DOCUMENT ME!
     * @param  name  DOCUMENT ME!
     * @param  path  DOCUMENT ME!
     */
    private void createFiles(final Query qry, final JPressoRun run, final String name, final String path) {
        try {
            final File q = new File(path + File.separator + JPressoFileManager.DIR_QRY + File.separator + name + "."
                            + JPressoFileManager.END_QUERY);
            final File r = new File(path + File.separator + JPressoFileManager.DIR_RUN + File.separator + name + "."
                            + JPressoFileManager.END_RUN);
            if (q.isFile()) {
                if (
                    JOptionPane.showConfirmDialog(
                                this,
                                "Query file already exists. Overwrite?",
                                "Query file already exists",
                                JOptionPane.YES_NO_OPTION)
                            == JOptionPane.YES_OPTION) {
                    final FileObject p = FileUtil.toFileObject(q).getParent();
                    q.delete();
                    p.refresh();
                }
            }
            if (r.isFile()) {
                if (
                    JOptionPane.showConfirmDialog(
                                this,
                                "Run file already exists. Overwrite?",
                                "Run file already exists",
                                JOptionPane.YES_NO_OPTION)
                            == JOptionPane.YES_OPTION) {
                    final FileObject p = FileUtil.toFileObject(r).getParent();
                    r.delete();
                    p.refresh();
                }
            }
            JPressoFileManager.getDefault().persist(q, qry);
            JPressoFileManager.getDefault().persist(r, run);
            final FileObject fobRun = FileUtil.toFileObject(r);
            fobRun.refresh();
            fobRun.getParent().refresh();
            final FileObject fobQuery = FileUtil.toFileObject(q);
            fobQuery.refresh();
            fobQuery.getParent().refresh();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connection  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private DefaultMutableTreeNode getSystemRoot(final Connection connection) {
        final DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

        try {
            final DatabaseMetaData md = connection.getMetaData();
            // ResultSet rs=md.getTables(null, null, null, new String[] {"TABLE"});
            final ResultSet rs = md.getColumns(null, null, null, null);
            String tablename = "";
            TargetTable table = null;
            while (rs.next()) {
                final String fetchedName = rs.getString(3);
                if (!tablename.equals(fetchedName)) {
                    tablename = fetchedName;
                    table = new TargetTable(tablename);
                    root.add(table);
                }
                final TargetField tf = new TargetField();
                tf.setName(rs.getString(4));
                tf.setTableName(tablename);
                tf.setType(rs.getString(6));
                table.add(tf); // new DefaultMutableTreeNode(rs.getString(4)+"
                               // ["+rs.getString(6)+"("+rs.getString(7)+"), "+rs.getString(18)+"]"));
            }
        } catch (Exception e) {
        }
        return root;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  tps  DOCUMENT ME!
     * @param  evt  DOCUMENT ME!
     */
    private void handlePopupEventForTree(final TreePath[] tps, final java.awt.event.MouseEvent evt) {
        final AbstractJPTopComponent tc = findTopComponent();
        if ((tc != null) && (tps != null)) {
            if (tc instanceof RunTopComponent) {
                boolean canMap = false;
                boolean hasRel = false;
                for (final TreePath tp : tps) {
                    final Object selection = tp.getLastPathComponent();
                    if (selection instanceof TargetTable) {
                        final TargetTable t = (TargetTable)selection;
                        canMap = true;
                        if (t.isMasterTable()) {
                            hasRel = true;
                        }
                    } else if (evt.getComponent().getName().equals(SYSTEM)) {
                        canMap = true;
                    }
                    if (canMap && hasRel) {
                        break;
                    }
                }
                mnuAddToMappings.setEnabled(canMap);
                mnuAddToRelations.setEnabled(hasRel);
                this.popMain.show(evt.getComponent(), evt.getX(), evt.getY());
            } else if (tc instanceof ConnectionTopComponent) {
                boolean allTargetTables = true;
                for (final TreePath tp : tps) {
                    final Object selection = tp.getLastPathComponent();
                    if (selection instanceof TargetTable) {
                        jMenuItem1.setEnabled(true);
                        jMenuItem2.setEnabled(true);
                        break;
                    } else {
                        allTargetTables = false;
                    }
                }
                if (allTargetTables) {
                    this.popCreate.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        }
    }

    /**
     * Root node of a cids system tree.
     *
     * @param   connection  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private DefaultMutableTreeNode getCidsRoot(final Connection connection) {
        final DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        TargetTable targetTable = null;
        TargetField targetField = null;
        try {
            final Statement stmnt = connection.createStatement();
            String tablename = "";
            String primKey = "";
            final ResultSet rs = stmnt.executeQuery(CIDS_ROOT_STMT);
            // -> "select c.name,c.primary_key_field,a.field_name,r.name,r.complex_type from cs_class c ,cs_attr
            // a,cs_type r where c.id=a.class_id and r.id=a.type_id");
            while (rs.next()) {
                if (!tablename.equals(rs.getString(2))) {
                    tablename = rs.getString(2);
                    primKey = rs.getString(4);
                    targetTable = new TargetTable(tablename);
                    root.add(targetTable);
                }
                targetField = new TargetField();
                targetField.setName(rs.getString(5));
                targetField.setTableName(tablename);
                if (primKey.equals(rs.getString(5))) {
                    targetField.setPrimaryKey(true);
                } else {
                    targetField.setPrimaryKey(false);
                }
                targetField.setType(rs.getString(6));
                if (rs.getString(7).equalsIgnoreCase(T_STRING)) {
                    targetTable.setMasterTable(true);
                    targetField.setForeignKey(true);
                    targetField.setDetailTable(rs.getString(9));
                    targetField.setDetailField(rs.getString(10));
                    // field=field+"   -> "+rs.getString(4);
                } else {
                    targetField.setForeignKey(false);
                }

                targetTable.add(targetField);
            }
            cids = true;
        } catch (Exception e) {
            // log.error("kein cids-System weil Exception im Analyseprozess:", e);
            cids = false;
            root.add(new DefaultMutableTreeNode("no cids-System"));
        } catch (Throwable t) {
            log.error("something unexpected happened!", t);
            cids = false;
            root.add(new DefaultMutableTreeNode("no cids-System"));
        }
        return root;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Node representation of a traget field.
     *
     * @version  $Revision$, $Date$
     */
    static class TargetField extends DefaultMutableTreeNode implements Mapable, Referenceable {

        //~ Instance fields ----------------------------------------------------

        /** Holds value of property name. */
        private String name = "";
        /** Holds value of property tableName. */
        private String tableName = "";
//        /** Holds value of property stringVal. */
//        private boolean stringVal = false;
        /** Holds value of property foreignKey. */
        private boolean foreignKey = false;
        /** Holds value of property type. */
        private String type = "";
        /** Holds value of property detailTable. */
        private String detailTable = "";
        /** Holds value of property detailField. */
        private String detailField = "";
        /** Holds value of property primaryKey. */
        private boolean primaryKey = false;

        //~ Methods ------------------------------------------------------------

        /**
         * Getter for property name.
         *
         * @return  Value of property name.
         */
        public String getName() {
            return this.name;
        }

        /**
         * Setter for property name.
         *
         * @param  name  New value of property name.
         */
        public void setName(final String name) {
            this.name = name;
        }

        /**
         * Getter for property tableName.
         *
         * @return  Value of property tableName.
         */
        public String getTableName() {
            return this.tableName;
        }

        /**
         * Setter for property tableName.
         *
         * @param  tableName  New value of property tableName.
         */
        public void setTableName(final String tableName) {
            this.tableName = tableName;
        }

        /**
         * Getter for property stringVal.
         *
         * @return  Value of property stringVal.
         */
        public boolean isStringDateVal() {
            if ((getType().toLowerCase().indexOf("char") != -1)
                        || (getType().toLowerCase().indexOf("text") != -1)
                        || (getType().toLowerCase().indexOf("date") != -1)
                        || (getType().toLowerCase().indexOf("time") != -1)
                        ||// (getType().toLowerCase().indexOf("geometry") != -1) ||
                        (getType().toLowerCase().indexOf("timestamp") != -1)) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * Getter for property foreignKey.
         *
         * @return  Value of property foreignKey.
         */
        public boolean isForeignKey() {
            return this.foreignKey;
        }

        /**
         * Setter for property foreignKey.
         *
         * @param  foreignKey  New value of property foreignKey.
         */
        public void setForeignKey(final boolean foreignKey) {
            this.foreignKey = foreignKey;
        }

        /**
         * Getter for property type.
         *
         * @return  Value of property type.
         */
        public String getType() {
            return this.type;
        }

        /**
         * Setter for property type.
         *
         * @param  type  New value of property type.
         */
        public void setType(final String type) {
            this.type = type;
        }

        /**
         * Getter for property detailTable.
         *
         * @return  Value of property detailTable.
         */
        public String getDetailTable() {
            return this.detailTable;
        }

        /**
         * Setter for property detailTable.
         *
         * @param  detailTable  New value of property detailTable.
         */
        public void setDetailTable(final String detailTable) {
            this.detailTable = detailTable;
        }

        /**
         * Getter for property detailField.
         *
         * @return  Value of property detailField.
         */
        public String getDetailField() {
            return this.detailField;
        }

        /**
         * Setter for property detailField.
         *
         * @param  detailField  New value of property detailField.
         */
        public void setDetailField(final String detailField) {
            this.detailField = detailField;
        }

        /**
         * Getter for property primaryKey.
         *
         * @return  Value of property primaryKey.
         */
        public boolean isPrimaryKey() {
            return this.primaryKey;
        }

        /**
         * Setter for property primaryKey.
         *
         * @param  primaryKey  New value of property primaryKey.
         */
        public void setPrimaryKey(final boolean primaryKey) {
            this.primaryKey = primaryKey;
        }

        @Override
        public String toString() {
            String ret = "";
            if (isPrimaryKey()) {
                ret += "*"
                            + name;
            } else {
                ret += " "
                            + name;
            }
            if (isForeignKey()) {
                ret += "      ["
                            + this.getType()
                            + "]   >> "
                            + getDetailTable()
                            + "."
                            + getDetailField();
            } else {
                ret += "      ["
                            + this.getType()
                            + "]";
            }
            return ret;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        @Override
        public List<Mapping> getMappings() {
            final List<Mapping> maps = TypeSafeCollections.newArrayList();
            final Mapping newMapping = new Mapping();
            newMapping.setTargetField(getName());
            newMapping.setTargetTable(getTableName());
            if (isStringDateVal()) {
                newMapping.setEnclosingChar("'");
            }
            maps.add(newMapping);
            return maps;
        }

        @Override
        public List<Reference> getReferences() {
            final List<Reference> ret = TypeSafeCollections.newArrayList();
            final Reference newMapping = new Reference();
            newMapping.setReferencingField(getName());
            newMapping.setReferencingTable(getTableName());
            if (isStringDateVal()) {
                newMapping.setEnclosingChar("'");
            }
            ret.add(newMapping);
            return ret;
        }
    }

    /**
     * Node representation of a target table.
     *
     * @version  $Revision$, $Date$
     */
    static class TargetTable extends DefaultMutableTreeNode implements Mapable, Referenceable {

        //~ Instance fields ----------------------------------------------------

        /** Holds value of property masterTable. */
        private boolean masterTable = false;
        /** Holds value of property name. */
        private String name = "";

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new TargetTable object.
         *
         * @param  name  DOCUMENT ME!
         */
        public TargetTable(final String name) {
            super();
            this.name = name;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * Getter for property masterTable.
         *
         * @return  Value of property masterTable.
         */
        public boolean isMasterTable() {
            return this.masterTable;
        }

        /**
         * Setter for property masterTable.
         *
         * @param  masterTable  New value of property masterTable.
         */
        public void setMasterTable(final boolean masterTable) {
            this.masterTable = masterTable;
        }

        /**
         * Getter for property name.
         *
         * @return  Value of property name.
         */
        public String getName() {
            return this.name;
        }

        /**
         * Setter for property name.
         *
         * @param  name  New value of property name.
         */
        public void setName(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        @Override
        public List<Mapping> getMappings() {
            final List<Mapping> maps = TypeSafeCollections.newArrayList();
            for (int i = 0; i < getChildCount(); ++i) {
                final Mapable mapable = (Mapable)getChildAt(i);
//                Mapping newMapping = new Mapping();
//                newMapping.setTargetField(mapable.getName());
//                newMapping.setTargetTable(mapable.getTableName());
//                if (mapable.isStringDateVal()) {
//                    newMapping.setEnclosingChar("'");
//                }
//                maps.add(newMapping);
                maps.addAll(mapable.getMappings());
            }
            return maps;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        @Override
        public List<Reference> getReferences() {
            final List<Reference> rel = TypeSafeCollections.newArrayList();
            // Relations rel=new Relations();
            Reference r;
            for (int i = 0; i < getChildCount(); ++i) {
                final TargetField f = (TargetField)getChildAt(i);
                if (f.isForeignKey()) {
                    r = new Reference();
                    r.setReferencingTable(f.getTableName());
                    r.setReferencingField(f.getName());
                    r.setReferencedTable(f.getDetailTable());
                    r.setReferencedField(f.getDetailField());
                    rel.add(r);
                }
            }
            return rel;
        }
    }

    /**
     * Transfer handler for the connection output tree.
     *
     * @version  $Revision$, $Date$
     */
    static class TreeNodeTransferHandler extends TransferHandler {

        //~ Instance fields ----------------------------------------------------

        private JTree tree;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new TreeNodeTransferHandler object.
         *
         * @param   tree  DOCUMENT ME!
         *
         * @throws  IllegalArgumentException  DOCUMENT ME!
         */
        public TreeNodeTransferHandler(final JTree tree) {
            if (tree == null) {
                throw new IllegalArgumentException("JTree for D&D handler can not be null!");
            }
            this.tree = tree;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public int getSourceActions(final JComponent c) {
            return COPY_OR_MOVE;
        }

        @Override
        protected Transferable createTransferable(final JComponent c) {
            final TreePath[] paths = tree.getSelectionPaths();
            final List<Mapping> mappings = TypeSafeCollections.newArrayList();
            if (paths != null) {
                for (final TreePath tp : paths) {
                    final Object pc = tp.getLastPathComponent();
                    if ((pc != null) && (pc instanceof Mapable)) {
                        final Mapable cur = (Mapable)pc;
                        mappings.addAll(cur.getMappings());
                    }
                }
            }
            final List<Reference> rels = TypeSafeCollections.newArrayList();
            if (paths != null) {
                for (final TreePath tp : paths) {
                    final Object pc = tp.getLastPathComponent();
                    if ((pc != null) && (pc instanceof Referenceable)) {
                        final Referenceable cur = (Referenceable)pc;
                        rels.addAll(cur.getReferences());
                    }
                }
            }
            return new DualDataTableTransferable(new MapableCollector(mappings), new ReferenceableCollector(rels));
        }
    }

    /**
     * A transferable for both, Mapable and Referenceable.
     *
     * @version  $Revision$, $Date$
     */
    private static class DualDataTableTransferable implements Transferable {

        //~ Instance fields ----------------------------------------------------

        private final Mapable mapable;
        private final Referenceable relationable;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new DualDataTableTransferable object.
         *
         * @param  mapable       DOCUMENT ME!
         * @param  relationable  DOCUMENT ME!
         */
        public DualDataTableTransferable(final Mapable mapable, final Referenceable relationable) {
            if ((mapable != null) && (mapable.getMappings() != null)) {
                this.mapable = mapable;
            } else {
                this.mapable = new Mapable() {

                        @Override
                        public List<Mapping> getMappings() {
                            return TypeSafeCollections.newArrayList();
                        }
                    };
            }
            if ((relationable != null) && (relationable.getReferences() != null)) {
                this.relationable = relationable;
            } else {
                this.relationable = new Referenceable() {

                        @Override
                        public List<Reference> getReferences() {
                            return TypeSafeCollections.newArrayList();
                        }
                    };
            }
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            final List<DataFlavor> list = TypeSafeCollections.newArrayList(2);
            if ((mapable.getMappings() != null) && (mapable.getMappings().size() > 0)) {
                list.add(JPDataFlavors.MAP_FLAVOR);
            }
            if ((relationable.getReferences() != null) && (relationable.getReferences().size() > 0)) {
                list.add(JPDataFlavors.REF_FLAVOR);
            }
            return list.toArray(new DataFlavor[] {});
        }

        @Override
        public boolean isDataFlavorSupported(final DataFlavor flavor) {
            return (flavor.equals(JPDataFlavors.MAP_FLAVOR) && (mapable.getMappings() != null)
                            && (mapable.getMappings().size() > 0))
                        || (flavor.equals(JPDataFlavors.REF_FLAVOR) && (relationable.getReferences() != null)
                            && (relationable.getReferences().size() > 0));
        }

        @Override
        public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (flavor.equals(JPDataFlavors.MAP_FLAVOR)) {
                return this.mapable;
            } else if (flavor.equals(JPDataFlavors.REF_FLAVOR)) {
                return this.relationable;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }
    }
}
