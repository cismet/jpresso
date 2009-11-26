/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.filetypes.refactoring;

import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.project.filetypes.connection.ConnectionDataNode;
import de.cismet.jpresso.project.filetypes.query.QueryDataNode;
import de.cismet.jpresso.project.filetypes.query.QueryDataObject;
import de.cismet.jpresso.project.filetypes.run.RunDataObject;
import de.cismet.jpresso.project.filetypes.sql.SQLDataObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author srichter
 */
public class RefactoringEngine {

//    private static List<DataObject> rollback = new ArrayList<DataObject>();
//
//    /**
//     * Refactor project files using the renamed connection. 
//     * @param cNode
//     * @param oldfilename
//     * @param newfilename
//     */
//    public static void refactorConnection(final ConnectionDataNode cNode, final String oldfilename, final String newfilename) {
//        try {
//            boolean asked = false;
//            final FileObject projDir = cNode.getParentNode().getLookup().lookup(ProjectCookie.class).getProject().getProjectDirectory();
//            System.out.println("old: " + oldfilename + ", new: " + newfilename);
//            if (cNode != null) {
//                FileObject folder = projDir.getFileObject("/" + JPressoFileManager.DIR_QRY);
//                //List<QueryDataObject> todo = new ArrayList();
//                for (final FileObject f : folder.getChildren()) {
//                    System.out.println("looking:" + f.getNameExt());
//                    final DataObject data = DataObject.find(f);
//                    final QueryDataObject qdo = data.getLookup().lookup(QueryDataObject.class);
//                    final String conFile = qdo.getData().getConnectionFile();
//                    System.out.println("vgl.:" + conFile + " mit " + oldfilename);
//                    //if connectionfile entry equals to file to rename
//                    if (conFile != null && conFile.equals(oldfilename)) {
//                        if (!asked) {
//                            boolean refactor = askForRefactoring(oldfilename);
//                            if (!refactor) {
//                                return;
//                            } else {
//                                asked = true;
//                            }
//                        }
//                        System.out.println("found:" + qdo.getPrimaryFile());
//                        boolean modified = qdo.isModified();
//                        qdo.getData().setConnectionFile(newfilename);
//                        if (modified) {
//                            qdo.save();
//                        }
//                        qdo.setModified(modified);
//                        rollback.add(qdo);
//                    }
//
//                }
//                folder = projDir.getFileObject("/" + JPressoFileManager.DIR_RUN);
//                //List<QueryDataObject> todo = new ArrayList();
//                for (final FileObject f : folder.getChildren()) {
//                    System.out.println("looking:" + f.getNameExt());
//
//                    final DataObject data = DataObject.find(f);
//                    final RunDataObject rdo = data.getLookup().lookup(RunDataObject.class);
//                    String conFile = null;
//                    if (rdo != null && rdo.getData() != null && rdo.getData().getTargetConnection() != null) {
//                        conFile = rdo.getData().getTargetConnection();
//                    }
//                    //if connectionfile entry equals to file to rename
//                    if (conFile != null && conFile.equals(oldfilename)) {
//                        if (!asked) {
//                            boolean refactor = askForRefactoring(oldfilename);
//                            if (!refactor) {
//                                return;
//                            } else {
//                                asked = true;
//                            }
//                        }
//                        System.out.println("found:" + rdo.getPrimaryFile());
//                        boolean modified = rdo.isModified();
//                        rdo.getData().setTargetConnection(newfilename);
//                        if (modified) {
//                            rdo.save();
//                        }
//                        rdo.setModified(modified);
//                        rollback.add(rdo);
//                    }
//                }
//                folder = projDir.getFileObject("/" + JPressoFileManager.DIR_SQL);
//                //List<QueryDataObject> todo = new ArrayList();
//                for (final FileObject f : folder.getChildren()) {
//                    final DataObject data = DataObject.find(f);
//                    final SQLDataObject sdo = data.getLookup().lookup(SQLDataObject.class);
//                    String conFile = null;
//                    if (sdo != null && sdo.getData() != null && sdo.getData().getConnectionFile() != null) {
//                        conFile = sdo.getData().getConnectionFile();
//                    }
//                    //if connectionfile entry equals to file to rename
//                    if (conFile != null && conFile.equals(oldfilename)) {
//                        boolean modified = sdo.isModified();
//                        sdo.getData().setConnectionFile(newfilename);
//                        if (modified) {
//                            sdo.save();
//                        }
//                        sdo.setModified(modified);
//                        rollback.add(sdo);
//                    }
//                }
//            }
//            rollback.clear();
//        } catch (DataObjectNotFoundException ex) {
//            rollback(cNode, oldfilename);
//            Exceptions.printStackTrace(ex);
//        } catch (IOException ex) {
//            rollback(cNode, oldfilename);
//            Exceptions.printStackTrace(ex);
//
//        }
//    }
//
//    /**
//     * Refactor project files using the renamed query.
//     * @param qNode
//     * @param oldfilename
//     * @param newfilename
//     */
//    public static void refactorQuery(final QueryDataNode qNode, final String oldfilename, final String newfilename) {
//        try {
//            boolean asked = false;
//            final FileObject projDir = qNode.getParentNode().getLookup().lookup(ProjectCookie.class).getProject().getProjectDirectory();
//            System.out.println("old: " + oldfilename + ", new: " + newfilename);
//            if (qNode != null) {
//                final FileObject folder = projDir.getFileObject("/" + JPressoFileManager.DIR_RUN);
//                for (final FileObject f : folder.getChildren()) {
//                    System.out.println("looking:" + f.getNameExt());
//                    final DataObject data = DataObject.find(f);
//                    final RunDataObject rdo = data.getLookup().lookup(RunDataObject.class);
//                    final String qryFile = rdo.getData().getSourceQuery();
//                    System.out.println("vgl.:" + qryFile + " mit " + oldfilename);
//                    //if connectionfile entry equals to file to rename
//                    if (qryFile != null && qryFile.equals(oldfilename)) {
//                        if (!asked) {
//                            boolean refactor = askForRefactoring(oldfilename);
//                            if (!refactor) {
//                                return;
//                            } else {
//                                asked = true;
//                            }
//                        }
//                        System.out.println("found:" + rdo.getPrimaryFile());
//                        boolean modified = rdo.isModified();
//                        rdo.getData().setTargetConnection(newfilename);
//                        if (modified) {
//                            rdo.save();
//                        }
//                        rdo.setModified(modified);
//                        rollback.add(rdo);
//                    }
//                }
//            }
//            rollback.clear();
//        } catch (DataObjectNotFoundException ex) {
//            rollback(qNode, oldfilename);
//            Exceptions.printStackTrace(ex);
//        } catch (IOException ex) {
//            rollback(qNode, oldfilename);
//            Exceptions.printStackTrace(ex);
//        }
//    }
//
//    /**
//     * Try to rollback changes if something went wrong.
//     * @param node
//     * @param oldfilename
//     */
//    private static void rollback(final Node node, final String oldfilename) {
//        //TODO file auch wieder zurueckbenennen xD
//        if (node instanceof ConnectionDataNode) {
//            for (final DataObject d : rollback) {
//                boolean modified = d.isModified();
//                if (d instanceof QueryDataObject) {
//                    try {
//                        final QueryDataObject qd = (QueryDataObject) d;
//                        qd.getData().setConnectionFile(oldfilename);
//                        if (modified) {
//                            qd.save();
//                        }
//                        qd.setModified(modified);
//                    } catch (IOException ex) {
//                        Exceptions.printStackTrace(ex);
//                    }
//                } else if (d instanceof RunDataObject) {
//                    try {
//                        final RunDataObject rd = (RunDataObject) d;
//                        rd.getData().setTargetConnection(oldfilename);
//                        if (modified) {
//                            rd.save();
//                        }
//                        rd.setModified(modified);
//                    } catch (IOException ex) {
//                        Exceptions.printStackTrace(ex);
//                    }
//
//                } else if (d instanceof SQLDataObject) {
//                    try {
//                        final SQLDataObject rd = (SQLDataObject) d;
//                        rd.getData().setConnectionFile(oldfilename);
//                        if (modified) {
//                            rd.save();
//                        }
//                        rd.setModified(modified);
//                    } catch (IOException ex) {
//                        Exceptions.printStackTrace(ex);
//                    }
//                }
//            }
//        } else if (node instanceof QueryDataNode) {
//            for (final DataObject d : rollback) {
//                boolean modified = d.isModified();
//                if (d instanceof RunDataObject) {
//                    try {
//                        final RunDataObject rd = (RunDataObject) d;
//                        rd.getData().setSourceQuery(oldfilename);
//                        if (modified) {
//                            rd.save();
//                        }
//                        rd.setModified(modified);
//                    } catch (IOException ex) {
//                        Exceptions.printStackTrace(ex);
//                    }
//                }
//            }
//        }
//        rollback.clear();
//    }
//
//    private static boolean askForRefactoring(final String oldFileName) {
//        NotifyDescriptor.Confirmation dlg = new NotifyDescriptor.Confirmation("The File " + oldFileName + " is referenced by other Files. Do you wish to update these Files?\n(If you are not sure, choose yes)", "Refactor Project", NotifyDescriptor.YES_NO_OPTION);
//        return NotifyDescriptor.YES_OPTION.equals(DialogDisplayer.getDefault().notify(dlg));
//    }
}
