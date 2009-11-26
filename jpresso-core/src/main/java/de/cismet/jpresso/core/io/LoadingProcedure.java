/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.io;

import de.cismet.jpresso.core.data.JPLoadable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author srichter
 */
/**
 * Interface for filetype-specific loading procedures
 * 
 * @param <T>
 */
public interface LoadingProcedure<T extends JPLoadable> {

    /**
     * 
     * @param file
     * @return
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public T load(File file) throws FileNotFoundException, IOException;
}
