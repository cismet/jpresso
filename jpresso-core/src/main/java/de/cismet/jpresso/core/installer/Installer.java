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
package de.cismet.jpresso.core.installer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Properties;

import de.cismet.jpresso.core.serviceprovider.ImporterExporter;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.core.utils.URLTools;

/**
 * Installs JPresso: creates ant properties for headless use in the user's home directoy
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class Installer {

    //~ Static fields/initializers ---------------------------------------------

    // Script Variable Names
    private static final String JPCORE_JAR = "jpcore.jar";
    private static final String LIB_DIR = "lib.dir";
    // Memory settings
    private static final String MAX_MEMORY = "memory.max";
    private static final String MAX_MEMORY_VALUE = "512M";

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Installer object.
     */
    private Installer() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public static void main(final String[] args) throws IOException {
        if (args.length == 2) {
            final File src = new File(args[0]);
            final File dst = new File(args[1]);
            if (src.isFile() && src.getName().endsWith("zip")) {
                if (dst.mkdirs()) {
                    importZipToFolder(src, dst);
                } else {
                    System.err.println("Can not create destination directory, or directory already exists: " + dst);
                }
            } else {
                System.err.println("Can not find source file or source file is not a zip-file: " + src);
            }
        } else {
            writeDefaultProperties();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  RuntimeException  DOCUMENT ME!
     */
    private static Properties createProjectAntProperties() {
        final Properties props = new Properties();
        final Class c = JPressoFileManager.class;
        final File pathCore = URLTools.convertURLToFile(JPressoFileManager.locateJarForClass(c));
        if (!pathCore.isFile()) {
            throw new RuntimeException("Error: Can not locate JPressoCore.jar! Exit.");
        }
        final File pathLib = new File(pathCore.getParentFile(), File.separator + "ext");
        if (!pathLib.isDirectory() || (pathLib.list().length < 1)) {
            throw new RuntimeException("Error: Can not locate find JPresso's external library directory under "
                        + pathLib + " or directory is emtpy! Exit.");
        }
        props.put(JPCORE_JAR, pathCore.getAbsolutePath());
        System.out.println("Found Ant property: JPressoCore.jar -> " + JPCORE_JAR + " = " + pathCore.getAbsolutePath());
        props.put(LIB_DIR, pathLib.getAbsolutePath());
        System.out.println("Found Ant property: JPressoCore.jar -> " + LIB_DIR + " = " + pathCore.getAbsolutePath());
        return props;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    private static void writeDefaultProperties() throws IOException {
        final File defProp = new File(System.getProperty("user.home"), File.separator + JPressoFileManager.ANT_PROPS);
        final Properties p = createProjectAntProperties();
        p.put(MAX_MEMORY, MAX_MEMORY_VALUE);
        System.out.println("Writing ant properties to file " + defProp + ". Done.");
        p.store(new BufferedOutputStream(new FileOutputStream(defProp)), "");
    }

    /**
     * DOCUMENT ME!
     *
     * @param   src  DOCUMENT ME!
     * @param   dst  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    private static void importZipToFolder(final File src, final File dst) throws IOException {
        ImporterExporter.importProjectFromZip(src, dst);
    }
}
