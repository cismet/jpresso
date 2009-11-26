/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.filetypes.action;

import de.cismet.jpresso.project.filetypes.connection.ConnectionDataNode;
import de.cismet.jpresso.project.filetypes.query.QueryDataNode;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.actions.RenameAction;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

public final class JPNodeRenameAction extends RenameAction {

    @Override
    //TODO nullpointer prüfen!
    protected void performAction(Node[] activatedNodes) {
        ProgressHandle ph = null;
        try {
            final Node node = activatedNodes[0]; // we suppose that one node is activated
            NotifyDescriptor.InputLine dlg = new NotifyDescriptor.InputLine(
                    NbBundle.getMessage(RenameAction.class, "CTL_RenameLabel"),
                    NbBundle.getMessage(RenameAction.class, "CTL_RenameTitle"));
            dlg.setInputText(node.getName());

            if (NotifyDescriptor.OK_OPTION.equals(DialogDisplayer.getDefault().notify(dlg))) {
                String newname = null;

                try {
                    newname = dlg.getInputText();
                    ph = ProgressHandleFactory.createHandle("Renaming...");
                    ph.start();
//                    final ConnectionDataNode cNode = n.getLookup().lookup(ConnectionDataNode.class);
//                    final QueryDataNode qNode = n.getLookup().lookup(QueryDataNode.class);

                    if (newname.length() > 0) {
//                        String oldfilename = null;
//                        if (cNode != null && cNode.getDataObject() != null && cNode.getDataObject().getPrimaryFile() != null) {
//                            oldfilename = cNode.getDataObject().getPrimaryFile().getNameExt();
//                            n.setName(newname); // NOI18N
//                            RefactoringEngine.refactorConnection(cNode, oldfilename, cNode.getDataObject().getPrimaryFile().getNameExt());
//                        }
//                        else if (qNode != null && qNode.getDataObject() != null && qNode.getDataObject().getPrimaryFile() != null) {
////                            oldfilename = qNode.getDataObject().getPrimaryFile().getNameExt();
//                            n.setName(newname); // NOI18N
////                            RefactoringEngine.refactorQuery(qNode, oldfilename, oldfilename);
//                        }
//                        else {
                            node.setName(newname); // NOI18N
//                        }
                    }

                } catch (IllegalArgumentException e) {
                    // determine if "printStackTrace"  and  "new annotation" of this exception is needed
                    boolean needToAnnotate = Exceptions.findLocalizedMessage(e) == null;

                    // annotate new localized message only if there is no localized message yet
                    if (needToAnnotate) {
                        Exceptions.attachLocalizedMessage(e,
                                NbBundle.getMessage(RenameAction.class,
                                "MSG_BadFormat",
                                node.getName(),
                                newname));
                    }

                    Exceptions.printStackTrace(e);
                }
            }
        } finally {
            if (ph != null) {
                ph.finish();
            }
        }
    }
    }
    

