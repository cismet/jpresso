/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.serviceprovider;

import de.cismet.jpresso.core.kernel.ClassResourceProviderIml;
import java.io.File;

/**
 * Creates a ClassRessourceProvider for a given project directory.
 * 
 * @author srichter
 */
public abstract class ClassResourceProviderFactory {

    /**
     * Creates a ClassRessourceProvider for a given project directory.
     * 
     * @param projDir
     * @return the classpathprovider
     */
    public static ClassResourceProvider createClassRessourceProvider(File projDir) {
        return new ClassResourceProviderIml(projDir);
    }

    public static ClassResourceProvider createClassRessourceProvider(String projDir) {
        return new ClassResourceProviderIml(new File(projDir));
    }
}
