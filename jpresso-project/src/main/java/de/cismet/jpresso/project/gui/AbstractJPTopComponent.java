/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui;

import de.cismet.jpresso.core.data.JPLoadable;
import de.cismet.jpresso.project.filetypes.AbstractJPDataObject;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.TreeNode;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.explorer.view.Visualizer;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.windows.TopComponent;

/**
 * This class should be superclass for all TopComponents used in JPresso.
 * 
 * It defines some standart functions and behavior that all or most TopComponents 
 * in JPresso need.
 * 
 * @author srichter
 */
public abstract class AbstractJPTopComponent<T extends AbstractJPDataObject<? extends JPLoadable>> extends TopComponent implements DocumentListener, PropertyChangeListener {

    public  static final String MARK_MODIFIED = "*";
    public  static final String EMPTY = "";
    public static final String PROJECT_NAME_PREFIX = " - [";
    public static final String PROJECT_NAME_POSTFIX = "] ";

    public AbstractJPTopComponent(final T data) {
        this.data = data;
//        this.data.addPropertyChangeListener(this);
        this.listenerActive = true;
        this.progress = null;
        this.project = null;
    }
    protected final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    //The appropriate AbstractJPDataObject-extending class.
    private T data;
    //A possibility to show progess
    private ProgressHandle progress;
    //A flag that deactivates all listener. Used when a bunch of changes must be made
    //ore changes that should not be noticed.
    private boolean listenerActive;
    //The project it belongs to
    private Project project;
    private static final String STRING_EMPTY = "";

    /**
     * @see org.openide.windows.TopComponent
     */
    @Override
    public void requestActive() {
        if (data.getPrimaryFile().isValid()) {
            super.requestActive();
            //enables the cookies for the related node, if topcomponent is focused
            setActivatedNodes(new Node[]{data.getNodeDelegate()});
        }
    }

    @Override
    /**
     * @see org.openide.windows.TopComponent
     */
    public void open() {
        super.open();
        setListenerActive(true);
    }

    /**
     * Listen to properties of the apropriate DataObject.
     * 
     * @param evt
     */
    public void propertyChange(final PropertyChangeEvent evt) {
        //change displayname on certain DataObject-property-changes
        final String propName = evt.getPropertyName();
        if (propName != null) {
            final boolean primaryFileChange = propName.equals(DataObject.PROP_PRIMARY_FILE);
            final boolean validChange = propName.equals(DataObject.PROP_VALID);
            if (propName.equals(DataObject.PROP_NAME) || propName.equals(DataObject.PROP_MODIFIED) || primaryFileChange) {
                if (EventQueue.isDispatchThread()) {
                    setDisplayName(getDisplayName());
                    if (primaryFileChange && data != null) {
                        project = null;
                        owningProjectChanged();
                    }
                } else {
                    EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            setDisplayName(getDisplayName());
                            if (primaryFileChange && data != null) {
                                project = null;
                                owningProjectChanged();
                            }
                        }
                    });
                }
            } else if (validChange) {
                if (EventQueue.isDispatchThread()) {
                    close();
                } else {
                    EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            close();
                        }
                    });
                }
            }
        }
    }

    /**
     * @see org.openide.windows.TopComponent
     */
    @Override
    public final int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    /**
     * @see org.openide.windows.TopComponent
     */
    @Override
    public String getDisplayName() {
        final String saveMark = getData().isModified() ? MARK_MODIFIED : EMPTY;
        if (getData() == null || getData().getPrimaryFile() == null || getData().getPrimaryFile().getParent() == null || getData().getPrimaryFile().getParent().getParent() == null) {
            return getName() + saveMark;
        } else {
            return getName() + PROJECT_NAME_PREFIX + getData().getPrimaryFile().getParent().getParent().getName() + PROJECT_NAME_POSTFIX + saveMark;
        }
    }

    /**
     * @see org.openide.windows.TopComponent
     */
    @Override
    public String getName() {
        if (getData() == null || getData().getPrimaryFile() == null) {
            return "";
        } else {
            return getData().getPrimaryFile().getNameExt();
        }

    }

    /**
     * Asks for saving changes if this shall be closed.
     * More:
     * @see org.openide.windows.TopComponent
     */
    @Override
    public boolean canClose() {
        boolean can = super.canClose();
        if (can) {
            if (getData().isModified()) {
                final Confirmation msg = new NotifyDescriptor.Confirmation("File " + getData().getPrimaryFile().getNameExt() + " has been modified. Save it?");
                final Object result = DialogDisplayer.getDefault().notify(msg);
                if (result.equals(NotifyDescriptor.YES_OPTION)) {
                    try {
                        getData().save();
                        can = true;
                    } catch (IOException ex) {
                        log.error("Error saving " + getData().getPrimaryFile().getPath(), ex);
                        can = false;
                    }
                } else if (result.equals(NotifyDescriptor.NO_OPTION)) {
                    try {
                        getData().reset();
                        can = true;
                    } catch (FileNotFoundException ex) {
                        final String message = ("Can not find file.");
                        log.error(message, ex);
                        final NotifyDescriptor err = new NotifyDescriptor.Exception(ex, message);
                        DialogDisplayer.getDefault().notify(err);
                    } catch (IOException ex) {
                        final String message = ("Possibly corrupted file.");
                        log.error(message, ex);
                        final NotifyDescriptor err = new NotifyDescriptor.Exception(ex, message);
                        DialogDisplayer.getDefault().notify(err);
                    } catch (Exception ex) {
                        final String message = ("Possible file problem.");
                        log.error(message, ex);
                        final NotifyDescriptor err = new NotifyDescriptor.Exception(ex, message);
                        DialogDisplayer.getDefault().notify(err);
                    }
                } else {
                    can = false;
                }
            }
        }
        return can && !getData().isModified();
    }

    /**
     * Returns the appropriate DataObject
     * 
     * @return 
     */
    public T getData() {
        return data;
    }

    /**
     * Exchanges the appropriate DataObject that is presented by this.
     * 
     * E.g. used when a node is moved between projects, when the FileObject 
     * needs to be copied and the DataObject is another accordingly. 
     * 
     * @param data
     */
    public void owningProjectChanged() {
        //hook
    }

    public void insertUpdate(final DocumentEvent e) {
        if (listenerActive) {
            getData().setModified(true);
        }
    }

    public void removeUpdate(final DocumentEvent e) {
        if (listenerActive) {
            getData().setModified(true);
        }
    }

    public void changedUpdate(final DocumentEvent e) {
//        if (listenerActive) {
//            System.out.println("changed update");
//            getData().setModified(true);
//
//        }
    }

    /**
     * Indicate that a longer task is running and the user needs to wait.
     * 
     * @param wait
     */
    public final void startWait(String waitText) {
        if (getProgress() != null) {
            getProgress().finish();
        }
        setProgress(ProgressHandleFactory.createHandle(waitText));
        getProgress().start();

    }

    /**
     * Indicate that a longer task is running and the user needs to wait.
     * 
     * @param wait
     */
    public final void stopWait() {
        if (getProgress() != null) {
            getProgress().finish();
            setProgress(null);
        }
    }

    /**
     * 
     * @return the ProgressHandle
     */
    public ProgressHandle getProgress() {
        return progress;
    }

    /**
     * 
     * @param progress
     */
    public void setProgress(final ProgressHandle progress) {
        this.progress = progress;
    }

    /**
     * Indicates if the listeners are active.
     * @return
     */
    protected boolean isListenerActive() {
        return listenerActive;
    }

    /**
     * Deactivate all listener.
     * 
     * @param listenerActive
     */
    protected void setListenerActive(final boolean listenerActive) {
        this.listenerActive = listenerActive;
    }

    /**
     * Returns the selected item in the parameter-combobox, if it matches the
     * parameter-class, null otherwise.
     * 
     * @param cb
     * @param clazz
     * @return
     */
    protected <T extends AbstractJPDataObject> T getDataFromComboBox(final JComboBox cb, final Class<T> clazz) {
        Node n = null;
        if (cb != null && cb.getModel() != null && cb.getModel().getSelectedItem() != null) {
            n = Visualizer.findNode(cb.getModel().getSelectedItem());
        }
        return (n != null) ? n.getLookup().lookup(clazz) : null;
    }

    /**
     * @see org.openide.windows.TopComponent
     */
    @Override
    protected void componentClosed() {
        super.componentClosed();
        setListenerActive(false);
        removeAllListenerOnClosed();
    }

    /**
     * Takes care of removing all listener if the is closed.
     */
    protected abstract void removeAllListenerOnClosed();

    /**
     * Show the output panel if the extending TopComponent has the ability to do
     * so. 
     * (a hook for JPTopComponents with output panel - they should override it)
     * 
     * REMEMBER: call only from EDT!
     * 
     * @param out
     */
    public void addOutput(final JPanel out) {
        //a hook for JPTopComponents with output panel.
    }

    /**
     * update the dataobject with the gui information
     */
    public abstract boolean updateDataObject();

    /**
     * Returns the appropriate Project
     * 
     * @return the appropriate Project
     */
    public Project getProject() {
        if (project == null && data != null && data.getPrimaryFile() != null) {
            project = FileOwnerQuery.getOwner(data.getPrimaryFile());
            if (project == null) {
                close();
            }
        }
        return project;
    }

    /**
     * Selects an JComboBox item, if the item is a TreeNode (like the ones NodeListModel delivers)
     * and the apropriate Node->DataObject->PrimaryFile->NameExt matches the given filename.
     * 
     * (This is used to select the right Items in Run, Query etc ComboBoxes)
     * 
     * @param cBox - a JComboBox with TreeNodes (NodeListModel!) in its item list.
     * @param fileName - the filename (+ extension) for the which the item to select has as primary file. 
     * @return was a matching item found in the combobox?
     */
    public boolean setComboBoxItemForFileName(final JComboBox cBox, final String fileName) {
        if (cBox == null) {
            throw new IllegalArgumentException("Null-values not allowed for Method setComboBoxItemForFileName(JComboBox cBox, String fileName)");
        }
        if (fileName != null && !fileName.equals(STRING_EMPTY)) {
            for (int i = 0; i < cBox.getItemCount(); ++i) {
                final Node n = Visualizer.findNode(cBox.getItemAt(i));
                if (n != null) {
                    final DataObject dob = n.getLookup().lookup(DataObject.class);
                    if (dob != null) {
                        if (dob.getPrimaryFile().getNameExt().equalsIgnoreCase(fileName)) {
                            cBox.setSelectedIndex(i);
                            return true;
                        }
                    }
                }
            }
        } else {
            //don't complain on new/empty filenames
            return true;
        }
        return false;
    }

    /**
     * Selects an JComboBox item, if the item is a TreeNode (like the ones NodeListModel delivers)
     * and the DataNode it represents equals the given node.
     * 
     * (E.g. used for ComboBox adjustment after (DataNode) drop events)
     * 
     * @param cBox - a JComboBox with TreeNodes (NodeListModel!) in its item list.
     * @param node - node to select
     * @return was node found in ComboBox and selected?
     */
    public boolean setComboBoxItemForNode(final JComboBox cBox, final DataNode node) {
        if (cBox == null || node == null) {
            throw new IllegalArgumentException("Null-values not allowed for Method setComboBoxItemForNode(JComboBox cBox, DataNode node)");
        }
        final DataObject dobIn = node.getLookup().lookup(DataObject.class);
        if (dobIn != null) {
            for (int i = 0; i < cBox.getItemCount(); ++i) {
                final Object cur = cBox.getItemAt(i);
                if (cur instanceof TreeNode) {
                    final Node tmp = Visualizer.findNode(cur);
                    if (tmp != null) {
                        final DataObject dobBox = tmp.getLookup().lookup(DataObject.class);
                        if (dobBox != null && dobBox.equals(dobIn)) {
                            cBox.setSelectedIndex(i);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
