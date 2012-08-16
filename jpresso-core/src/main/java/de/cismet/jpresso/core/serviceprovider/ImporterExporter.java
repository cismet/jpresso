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
package de.cismet.jpresso.core.serviceprovider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.core.data.DriverJar;
import de.cismet.jpresso.core.data.ProjectOptions;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * Utility class.
 *
 * <p>Copies all referenced drivers and external libs into the project directory and creates a modified options.jpo and
 * build.xml with hardcoded values for usage with external ant.</p>
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public abstract class ImporterExporter {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ImporterExporter.class);

    //~ Methods ----------------------------------------------------------------

    /**
     * Exports the project to a single zip file. As Java zip API sucks, we first have to collect all necessary files
     * into our project directory. Therefore all libs and driver jars in the options.jpo are looked up and copied into
     * the project directory. They will be deleted afterwards!.
     *
     * @param   projDir  DOCUMENT ME!
     * @param   dest     DOCUMENT ME!
     *
     * @throws  IOException            DOCUMENT ME!
     * @throws  FileNotFoundException  DOCUMENT ME!
     */
    public static final void exportProjectToZip(final File projDir, final File dest) throws IOException {
        String msg = "\nStarting export of " + projDir.getAbsolutePath() + " to " + dest.getAbsolutePath();
        System.out.println(msg);
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
        final JPressoFileManager manager = JPressoFileManager.getDefault();
        msg = "- Preparing temporary directory...";
        System.out.println(msg);
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
        final File tmpDir = JPressoFileManager.createTempDir();
        manager.copyFile(projDir, tmpDir);
        final File driverDir = new File(tmpDir, JPressoFileManager.DIR_DRV);
        final File libDir = new File(tmpDir, JPressoFileManager.DIR_LIB);
        final File options = new File(tmpDir, JPressoFileManager.PROJECT_OPTIONS);

        if (!options.isFile()) {
            throw new FileNotFoundException("Can not find " + JPressoFileManager.PROJECT_OPTIONS + "!");
        }
        final ProjectOptions po = manager.load(options, ProjectOptions.class);
        // ----collect driver file paths
        msg = "- Collecting driver and libraries...";
        System.out.println(msg);
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
        final Set<File> driver = TypeSafeCollections.newHashSet();
        final List<DriverDescription> dds = po.getDriver();
        for (final DriverDescription dd : dds) {
            for (int i = 0; i < dd.getJarFiles().size(); ++i) {
                final DriverJar dj = dd.getJarFiles().get(i);
                final File cur = dj.getJarFile();
                driver.add(cur);
            }
        }
        // ----collect libjar file paths
        final Set<File> libs = po.getAddClassPath();
        // ----copy collected files to destination and create source -> dest maps
        final Map<File, File> driverMap = copyFilesToDirWithAutomaticRename(driver, driverDir);
        final Map<File, File> libMap = copyFilesToDirWithAutomaticRename(libs, libDir);
        // ----fill the project options with new values
        final Set<File> newCPSet = TypeSafeCollections.newHashSet(libs.size() + 10);
        for (final File f : libs) {
            newCPSet.add(libMap.get(f));
        }
        po.setAddClassPath(newCPSet);
        for (final DriverDescription dd : dds) {
            for (int i = 0; i < dd.getJarFiles().size(); ++i) {
                final DriverJar dj = dd.getJarFiles().get(i);
                final File file = driverMap.get(dj.getJarFile());
                if (file != null) {
                    dd.getJarFiles().set(i, new DriverJar(file, dj.getDriverClassNames()));
                }
            }
        }
        // ----save this project options as import project options
        msg = "- Writing import meta information...";
        System.out.println(msg);
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
        manager.persist(new File(tmpDir, JPressoFileManager.IMPORT_OPTIONS), po);
        // ----compress the whole project as zip
        msg = "- Writing project to zip...";
        System.out.println(msg);
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
        manager.compressToZip(tmpDir, dest);
        // ----delete the collected files from the project directory
        msg = "- Deleting temorary directory... ";
        System.out.println(msg);
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
        tmpDir.delete();
        msg = "Done.\n";
        System.out.println(msg);
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
    }

    /**
     * Imports the project from a previously created zip. After unzipping, the options.jpo is adapted to the new project
     * directory, regarding all absolute paths.
     *
     * @param   importFile  DOCUMENT ME!
     * @param   dest        DOCUMENT ME!
     *
     * @throws  IOException            DOCUMENT ME!
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    public static final void importProjectFromZip(final File importFile, final File dest) throws IOException {
        if ((importFile == null) || (dest == null)) {
            throw new IllegalStateException("One import file parameter is null!");
        }
        if (!importFile.isFile()) {
            throw new IllegalStateException("The given import file " + importFile.getAbsolutePath()
                        + " is not a file or does not exist!");
        }
        if (!dest.isDirectory()) {
            throw new IllegalStateException("The given destination file " + dest.getAbsolutePath()
                        + " is not a directory or does not exist!");
        }

        final JPressoFileManager manager = JPressoFileManager.getDefault();
        String msg = "\nStarting import of " + importFile.getAbsolutePath() + " to " + dest.getAbsolutePath();
        System.out.println(msg);
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
        msg = "- Extracting zip...";
        System.out.println(msg);
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
        manager.extractFromZip(importFile, dest);
        restoreProjectEnvironment(dest);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   destDir  DOCUMENT ME!
     *
     * @throws  IOException            DOCUMENT ME!
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    public static final void restoreProjectEnvironment(final File destDir) throws IOException {
        String msg;
        final JPressoFileManager manager = JPressoFileManager.getDefault();
        final File impOpts = new File(destDir, JPressoFileManager.IMPORT_OPTIONS);
        if (!impOpts.isFile()) {
            throw new IllegalStateException("The file you try to import is not a valid JPresso Project! Can not find "
                        + JPressoFileManager.IMPORT_OPTIONS);
        }
        final ProjectOptions po = manager.load(impOpts, ProjectOptions.class);
        final Set<File> oldcpf = po.getAddClassPath();
        final Set<File> newcpf = TypeSafeCollections.newHashSet();
        for (final File cur : oldcpf) {
            // XXX: cross-platform fix
            final File toProcess = new File(cur.getAbsolutePath().replace("\\", File.separator).replace(
                        "/",
                        File.separator));
            newcpf.add(new File(
                    destDir.getAbsolutePath()
                            + File.separator
                            + JPressoFileManager.DIR_LIB
                            + File.separator
                            + toProcess.getName()));
            po.setAddClassPath(newcpf);
        }
        msg = "Project contains import options. Restoring options.jpo ...";
        System.out.println(msg);
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
        msg = "- Looking up driver paths...";
        System.out.println(msg);
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
        for (final DriverDescription dd : po.getDriver()) {
            for (int i = 0; i < dd.getJarFiles().size(); ++i) {
                final DriverJar dj = dd.getJarFiles().get(i);
                File oldF = dj.getJarFile();
                // XXX: cross-platform fix
                oldF = new File(oldF.getAbsolutePath().replace("\\", File.separator).replace("/", File.separator));
                final File newF = new File(destDir.getAbsolutePath() + File.separator + JPressoFileManager.DIR_DRV
                                + File.separator + oldF.getName());
                dd.getJarFiles().set(i, new DriverJar(newF, dj.getDriverClassNames()));
            }
        }
        msg = "- Writing options.jpo...";
        System.out.println(msg);
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
        final File poFile = new File(destDir, JPressoFileManager.PROJECT_OPTIONS);
        if (poFile.exists()) {
            poFile.delete();
        }
        manager.persist(poFile, po);
        msg = "- Deleting import meta information...";
        System.out.println(msg);
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
        impOpts.delete();
        msg = "Done.";
        System.out.println(msg);
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
    }

    /**
     * Copies a Set of Files to a destination directory. If the destination or directory file does not exist, it is
     * created. If two files would have the same name, a similar name is chosen for th second file.
     *
     * @param   files    DOCUMENT ME!
     * @param   destDir  DOCUMENT ME!
     *
     * @return  a hash mapping old file to actual new one.
     *
     * @throws  IOException               DOCUMENT ME!
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public static final Map<File, File> copyFilesToDirWithAutomaticRename(final Set<File> files, final File destDir)
            throws IOException {
        final JPressoFileManager manager = JPressoFileManager.getDefault();
        if ((files == null) || (destDir == null)) {
            throw new IllegalArgumentException("no nullpointer allowed here");
        }
        final Map<File, File> oldToNewMap = TypeSafeCollections.newHashMap();
        for (final File f : files) {
            // need for copy?
            if (f.isFile() && !f.getParentFile().equals(destDir)) {
                final File dest = manager.findFreeFile(new File(destDir, f.getName()));
                final String msg = "\t* Copy " + f + " to " + dest;
                System.out.println(msg);
                if (log.isDebugEnabled()) {
                    log.debug(msg);
                }
                manager.copyFile(f, dest);
                oldToNewMap.put(f, dest);
            } else {
                oldToNewMap.put(f, f);
            }
        }
        return oldToNewMap;
    }
}
