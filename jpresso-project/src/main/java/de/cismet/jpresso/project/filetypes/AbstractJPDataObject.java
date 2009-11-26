/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.filetypes;

import de.cismet.jpresso.project.gui.AbstractJPTopComponent;
import de.cismet.jpresso.core.data.JPLoadable;
import de.cismet.jpresso.project.filetypes.cookies.RunCookie;
import java.awt.EventQueue;
import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.cookies.OpenCookie;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 * This class should be used as superclass for all JPresso DataObjects.
 * 
 * It defines some standard functions and behavior that all or most DataObjects 
 * in JPresso need.
 * 
 * @author srichter
 */
public abstract class AbstractJPDataObject<T extends JPLoadable> extends MultiDataObject implements OpenCookie {

    protected final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private AbstractJPTopComponent<? extends AbstractJPDataObject<T>> topComponent;
    private SaveCookieImpl saveCookie;
    private T data;
    private final JPFileLoader<T> loader;

    public AbstractJPDataObject(final FileObject pf, final JPFileLoader<T> loader, final T data) throws DataObjectExistsException {
        super(pf, loader);
        this.data = data;
        this.loader = loader;
        saveCookie = new SaveCookieImpl();
    }

    /**
     * 
     * @return
     */
    public T getData() {
        return data;
    }

    /**
     * 
     * @param data
     */
    public void setData(final T data) {
        this.data = data;
    }

    /**
     * Reset the changes on this Object to the last saved status from file.
     * 
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public final void reset() throws FileNotFoundException, IOException {
        setData(loader.loadData(getPrimaryFile()));
        topComponent = null;
        setModified(false);
    }

    /**
     * Delegates to SaveCookie and therefore Saves the data to file.
     * 
     * @throws java.io.IOException
     */
    public void save() throws IOException {
        saveCookie.save();
    }

    /**
     * Indicate that the DataObject has been modified and should be saved.
     * 
     * @param modif
     */
    @Override
    public void setModified(final boolean modif) {
        if (modif != isModified()) {
            super.setModified(modif);
            if (modif) {
                getCookieSet().add(saveCookie);
            } else {
                getCookieSet().remove(saveCookie);
            }
        }
    }

    /**
     * 
     * @param valid
     * @throws java.beans.PropertyVetoException
     */
    @Override
    public void setValid(boolean valid) throws PropertyVetoException {
        if (!valid) {
            setModified(false);
        }
        super.setValid(valid);
    }

    /**
     * Returns true if the DataObject was modified.
     * 
     * @return 
     */
    @Override
    public final boolean isModified() {
        if (!getPrimaryFile().isValid()) {
            return false;
        }
        return super.isModified();
    }

    /**
     * @see org.openide.util.Lookup
     */
    @Override
    public final Lookup getLookup() {
        return getCookieSet().getLookup();
    }

    /**
     * @see org.openide.nodes.Node
     */
    @Override
    protected abstract Node createNodeDelegate();

    /**
     * Creates the appropriate TC.
     * 
     * @return
     */
    protected abstract AbstractJPTopComponent<? extends AbstractJPDataObject<T>> createTopComponent();

    /**
     * 
     * @return
     */
    public AbstractJPTopComponent<? extends AbstractJPDataObject<T>> getTopComponent() {
        if (topComponent == null) {
            if (!EventQueue.isDispatchThread()) {
                log.warn("Try to create TopComponent from non-AWT-Thread!");
            }
            final ProgressHandle ph = ProgressHandleFactory.createHandle("Opening...");
            ph.start();
            try {
                final AbstractJPTopComponent<? extends AbstractJPDataObject<T>> tc = createTopComponent();
                if (tc != null) {
                    topComponent = tc;
                    addPropertyChangeListener(topComponent);
                }
            } finally {
                ph.finish();
            }
        }
        log.debug("getTopComponent() returned " + topComponent);
        return topComponent;
    }

    /**
     * persist the encapsulated primary data (like JPressoRun, DatabaseConnection, etc. ) to the primary file.
     * 
     * Implementation will keep track of automatically update the changes from GUI, so you just have to do the actuall saving.
     * 
     */
    protected void persistDataToFile() throws IOException {
        loader.persistDataToFile(this);
        notifyChangeListeners();
    }

    protected void notifyChangeListeners() {
        firePropertyChange(getClass().getCanonicalName(), null, null);
    }

    /**
     * Open the appropriate TopComponent.
     */
    public final void open() {

        if (!getTopComponent().isOpened()) {
            getTopComponent().open();
        }
        getTopComponent().requestActive();
    }

    /**
     * Class that takes care of saving the changes to file.
     */
    private class SaveCookieImpl implements SaveCookie {

        /**
         * The actual saving procedure.
         * 
         * @throws java.io.IOException
         */
        public final void save() throws IOException {
            //DANGER ...nicht getTopComponent() != null, weil sie sonst auf jeden Fall instanziiert wird!
            if (topComponent != null) {
                AbstractJPTopComponent top = getTopComponent();
                if (top.updateDataObject()) {
                    persistDataToFile();
                    setModified(false);
                }
            } else {
                setModified(false);
            }
        }
    }

    @Override
    protected final Object clone() throws CloneNotSupportedException {
        return new UnsupportedOperationException("This node is unique for every project! There should be no need for cloning this node!");
    }

    /**
     * Indicates if the file represented by this data object can be executed
     * from the AntUniversal Executor. So far this is true for *.run and *.rqs
     * representators.
     *  
     * @return true if the represented file is AntUniversalExecutor executable.
     */
    public final boolean isExecutable() {
        return (this instanceof RunCookie);
    }
}
