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

import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.UniFileLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.lang.reflect.ParameterizedType;

import de.cismet.jpresso.core.data.JPLoadable;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;

/**
 * Generic Loader for JPresso Files.
 *
 * <p>IMPORTANT: this class MUST be parameterized!</p>
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public abstract class JPFileLoader<T extends JPLoadable> extends UniFileLoader {

    //~ Instance fields --------------------------------------------------------

    protected final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JPFileLoader.class);
    private final JPressoFileManager fm = JPressoFileManager.getDefault();
    private final Class<T> typeClass;

    //~ Constructors -----------------------------------------------------------

    /**
     * Generic Loader for JPresso Files.
     *
     * <p>IMPORTANT: this class MUST be parameterized!</p>
     *
     * @param  representationClassName  DOCUMENT ME!
     */
    @SuppressWarnings("unchecked") // throws exception if not parameterized
    public JPFileLoader(final String representationClassName) {
        super(representationClassName);
        // determines the runtime class of the generic type T, as T.class is impossible in Java because of
        // compatibility :(
        Class<T> determineClass = null;
        try {
            // cast
            determineClass = (Class<T>)
                ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (Exception ex) {
            log.warn("JPFileLoaders MUST be parameterized!");
            // cast
            determineClass = (Class<T>)JPLoadable.class;
        }
        typeClass = determineClass;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   dob  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public void persistDataToFile(final AbstractJPDataObject dob) throws IOException {
        fm.persist(FileUtil.toFile(dob.getPrimaryFile()), dob.getData());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   pf  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException            DOCUMENT ME!
     * @throws  FileNotFoundException  DOCUMENT ME!
     */
    public T loadData(final FileObject pf) throws IOException {
        final File toLoad = FileUtil.toFile(pf);
        if ((toLoad == null) || !toLoad.isFile()) {
            throw new FileNotFoundException("File " + pf + " not found!");
        }
        return fm.load(toLoad, typeClass);
    }
}
