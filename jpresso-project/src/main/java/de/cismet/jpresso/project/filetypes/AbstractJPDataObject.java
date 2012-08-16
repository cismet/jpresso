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
package de.cismet.jpresso.project.filetypes;

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;

import org.openide.cookies.OpenCookie;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

import java.awt.EventQueue;

import java.beans.PropertyVetoException;

import java.io.FileNotFoundException;
import java.io.IOException;

import de.cismet.jpresso.core.data.JPLoadable;

import de.cismet.jpresso.project.filetypes.cookies.RunCookie;
import de.cismet.jpresso.project.gui.AbstractJPTopComponent;

/**
 * This class should be used as superclass for all JPresso DataObjects.
 *
 * <p>It defines some standard functions and behavior that all or most DataObjects in JPresso need.</p>
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public abstract class AbstractJPDataObject<T extends JPLoadable> extends MultiDataObject implements OpenCookie {

    //~ Instance fields --------------------------------------------------------

    protected final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private AbstractJPTopComponent<? extends AbstractJPDataObject<T>> topComponent;
    private SaveCookieImpl saveCookie;
    private T data;
    private final JPFileLoader<T> loader;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AbstractJPDataObject object.
     *
     * @param   pf      DOCUMENT ME!
     * @param   loader  DOCUMENT ME!
     * @param   data    DOCUMENT ME!
     *
     * @throws  DataObjectExistsException  DOCUMENT ME!
     */
    public AbstractJPDataObject(final FileObject pf, final JPFileLoader<T> loader, final T data)
            throws DataObjectExistsException {
        super(pf, loader);
        this.data = data;
        this.loader = loader;
        saveCookie = new SaveCookieImpl();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public T getData() {
        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  data  DOCUMENT ME!
     */
    public void setData(final T data) {
        this.data = data;
    }

    /**
     * Reset the changes on this Object to the last saved status from file.
     *
     * @throws  FileNotFoundException  DOCUMENT ME!
     * @throws  IOException            DOCUMENT ME!
     */
    public final void reset() throws FileNotFoundException, IOException {
        setData(loader.loadData(getPrimaryFile()));
        topComponent = null;
        setModified(false);
    }

    /**
     * Delegates to SaveCookie and therefore Saves the data to file.
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public void save() throws IOException {
        saveCookie.save();
    }

    /**
     * Indicate that the DataObject has been modified and should be saved.
     *
     * @param  modif  DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param   valid  DOCUMENT ME!
     *
     * @throws  PropertyVetoException  DOCUMENT ME!
     */
    @Override
    public void setValid(final boolean valid) throws PropertyVetoException {
        if (!valid) {
            setModified(false);
        }
        super.setValid(valid);
    }

    /**
     * Returns true if the DataObject was modified.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public final boolean isModified() {
        if (!getPrimaryFile().isValid()) {
            return false;
        }
        return super.isModified();
    }

    /**
     * @see  org.openide.util.Lookup
     */
    @Override
    public final Lookup getLookup() {
        return getCookieSet().getLookup();
    }

    /**
     * @see  org.openide.nodes.Node
     */
    @Override
    protected abstract Node createNodeDelegate();

    /**
     * Creates the appropriate TC.
     *
     * @return  DOCUMENT ME!
     */
    protected abstract AbstractJPTopComponent<? extends AbstractJPDataObject<T>> createTopComponent();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
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
        if (log.isDebugEnabled()) {
            log.debug("getTopComponent() returned " + topComponent);
        }
        return topComponent;
    }

    /**
     * persist the encapsulated primary data (like JPressoRun, DatabaseConnection, etc. ) to the primary file.
     *
     * <p>Implementation will keep track of automatically update the changes from GUI, so you just have to do the
     * actuall saving.</p>
     *
     * @throws  IOException  DOCUMENT ME!
     */
    protected void persistDataToFile() throws IOException {
        loader.persistDataToFile(this);
        notifyChangeListeners();
    }

    /**
     * DOCUMENT ME!
     */
    protected void notifyChangeListeners() {
        firePropertyChange(getClass().getCanonicalName(), null, null);
    }

    /**
     * Open the appropriate TopComponent.
     */
    @Override
    public final void open() {
        if (!getTopComponent().isOpened()) {
            getTopComponent().open();
        }
        getTopComponent().requestActive();
    }

    @Override
    protected final Object clone() throws CloneNotSupportedException {
        return new UnsupportedOperationException(
                "This node is unique for every project! There should be no need for cloning this node!");
    }

    /**
     * Indicates if the file represented by this data object can be executed from the AntUniversal Executor. So far this
     * is true for *.run and *.rqs representators.
     *
     * @return  true if the represented file is AntUniversalExecutor executable.
     */
    public final boolean isExecutable() {
        return (this instanceof RunCookie);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Class that takes care of saving the changes to file.
     *
     * @version  $Revision$, $Date$
     */
    private class SaveCookieImpl implements SaveCookie {

        //~ Methods ------------------------------------------------------------

        /**
         * The actual saving procedure.
         *
         * @throws  IOException  DOCUMENT ME!
         */
        @Override
        public final void save() throws IOException {
            // DANGER ...nicht getTopComponent() != null, weil sie sonst auf jeden Fall instanziiert wird!
            if (topComponent != null) {
                final AbstractJPTopComponent top = getTopComponent();
                if (top.updateDataObject()) {
                    persistDataToFile();
                    setModified(false);
                }
            } else {
                setModified(false);
            }
        }
    }
}
