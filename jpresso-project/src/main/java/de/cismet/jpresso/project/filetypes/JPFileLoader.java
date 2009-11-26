/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.filetypes;

import de.cismet.jpresso.core.data.JPLoadable;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.UniFileLoader;

/**
 * Generic Loader for JPresso Files.
 * 
 * IMPORTANT: this class MUST be parameterized!
 * 
 * @author srichter
 */
public abstract class JPFileLoader<T extends JPLoadable> extends UniFileLoader {

    protected final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JPFileLoader.class);
    private final JPressoFileManager fm = JPressoFileManager.getDefault();
    private final Class<T> typeClass;

    /**
     * Generic Loader for JPresso Files.
     * 
     * IMPORTANT: this class MUST be parameterized!
     * 
     * @param representationClassName
     */
    @SuppressWarnings("unchecked") //throws exception if not parameterized
    public JPFileLoader(final String representationClassName) {
        super(representationClassName);
        //determines the runtime class of the generic type T, as T.class is impossible in Java because of compatibility :(
        Class<T> determineClass = null;
        try {
            //cast
            determineClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (Exception ex) {
            log.warn("JPFileLoaders MUST be parameterized!");
            //cast
            determineClass = (Class<T>) JPLoadable.class;
        }
        typeClass = determineClass;
    }

    /**
     * 
     * @param dob
     * @throws java.io.IOException
     */
    public void persistDataToFile(final AbstractJPDataObject dob) throws IOException {
        fm.persist(FileUtil.toFile(dob.getPrimaryFile()), dob.getData());
    }

    /**
     * 
     * @param pf
     * @return
     * @throws java.io.IOException
     */
    public T loadData(final FileObject pf) throws IOException {
        final File toLoad = FileUtil.toFile(pf);
        if (toLoad == null || !toLoad.isFile()) {
            throw new FileNotFoundException("File " + pf + " not found!");
        }
        return fm.load(toLoad, typeClass);
    }
}
