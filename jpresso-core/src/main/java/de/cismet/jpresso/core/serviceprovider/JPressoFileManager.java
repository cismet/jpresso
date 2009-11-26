/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.serviceprovider;

import de.cismet.jpresso.core.data.DatabaseConnection;
import de.cismet.jpresso.core.data.ImportRules;
import de.cismet.jpresso.core.data.JPLoadable;
import de.cismet.jpresso.core.data.JPressoRun;
import de.cismet.jpresso.core.data.Query;
import de.cismet.jpresso.core.io.LoadingProcedure;
import de.cismet.jpresso.core.io.XStreamJPressoFileManager;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Responsable for all File I/O in JPresso
 * 
 * @author srichter
 */
public abstract class JPressoFileManager {

    public static JPressoFileManager getDefault() {
        return XStreamJPressoFileManager.getInstance();
    }
    protected static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JPressoFileManager.class);
    //IO-Buffer
    public static final int BUFFER = 4096;
    //---------------------directory names-----------------------
    public static final String DIR_CON = "connection";
    public static final String DIR_QRY = "query";
    public static final String DIR_SQL = "sql";
    public static final String DIR_RUN = "run";
    public static final String DIR_CDE = "code";
    public static final String DIR_JPP = "jpproject";
    public static final String DIR_DRV = "driver";
    public static final String DIR_LIB = "lib";
    //---------------------important files-----------------------
    public static final String BUILD_XML = "build.xml";
    public static final String PROJECT_OPTIONS = "options.jpo";
    public static final String IMPORT_OPTIONS = "imp_options.jpo";
    public static final String PROJECT_PLAN = "ProjectPlan.java";
    public static final String PROJECT_PROPS = "project.properties";
    public static final String ANT_PROPS = ".jp_config";
    public static final String DEBUG_INDICATOR = ".jp_debug";
    public static final String LOG_CONFIG = ".jp_logconf";
    //------------------------file endings-----------------------
    public static final String END_CONNECTION = "con";
    public static final String END_QUERY = "qry";
    public static final String END_RUN = "run";
    public static final String END_SQL = "rqs";
    public static final String END_CODE = "cde";
    public static final String END_OPTIONS = "jpo";
    public static final String END_JAR = "jar";
    public static final String END_ZIP = "zip";
    public static final String END_JAVA = "java";
    public static final String END_CLASS = "class";
    //---------------Map: filetype -> directory-------------------
    public static final Map<String, String> DIRECTORY_LOOKUP = TypeSafeCollections.newHashMap();


    static {
        DIRECTORY_LOOKUP.put(END_CONNECTION, DIR_CON);
        DIRECTORY_LOOKUP.put(END_QUERY, DIR_QRY);
        DIRECTORY_LOOKUP.put(END_RUN, DIR_RUN);
        DIRECTORY_LOOKUP.put(END_SQL, DIR_SQL);
        DIRECTORY_LOOKUP.put(END_CODE, DIR_CDE);
        DIRECTORY_LOOKUP.put(END_CONNECTION.toUpperCase(), DIR_CON);
        DIRECTORY_LOOKUP.put(END_QUERY.toUpperCase(), DIR_QRY);
        DIRECTORY_LOOKUP.put(END_RUN.toUpperCase(), DIR_RUN);
        DIRECTORY_LOOKUP.put(END_SQL.toUpperCase(), DIR_SQL);
        DIRECTORY_LOOKUP.put(END_CODE.toUpperCase(), DIR_CDE);
    }

    /**
     * Parses and loads a loadable object of class T from file (like Connection, Query, Run,...).
     * 
     * @param <T>
     * @param file
     * @param clazz
     * @return
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public abstract <T extends JPLoadable> T load(File file, Class<T> clazz) throws FileNotFoundException, IOException;

    /**
     * Saves an object to the given filename.
     * 
     * @param toSave
     * @param file
     * @throws java.io.IOException
     */
    public abstract void persist(File file, JPLoadable toSave) throws IOException;

    /**
     * Adds a new LoadingProcedure to the Filemanager.
     * 
     * IMPORTANT: As part of the contract, Class and LoadingProcedure must be of the same parameterized type!!!
     * 
     * @param procedure
     * @param key
     */
    public abstract void addLoadingProcedure(Class<? extends JPLoadable> key, LoadingProcedure<? extends JPLoadable> procedure);

    /**
     * Assembles an ImportRules Object from all Files and Information in the JPressoRun
     * 
     * @param run
     * @return importrules
     */
    public final ImportRules importRulesFromRunData(final JPressoRun runRules, final String projDir) throws FileNotFoundException, IOException {
        if (runRules == null || projDir == null) {
            throw new IllegalArgumentException("no nullpointer allowed here");
        }
        log.info("Start creating ImportRules from JPressoRun...");
        log.debug("Loading Query");
        final Query q = load(new File(projDir + File.separator + JPressoFileManager.DIR_QRY + File.separator + runRules.getSourceQuery()), Query.class);
        log.debug("Loading target connection file" + q.getConnectionFile());
        final DatabaseConnection c = load(new File(projDir + File.separator + JPressoFileManager.DIR_CON + File.separator + runRules.getTargetConnection()), DatabaseConnection.class);
        log.debug("Creating ImportRules");
        final ImportRules rules = new ImportRules();
        rules.setMappings(runRules.getMappings());
        rules.setOptions(runRules.getOptions());
        rules.setReferences(runRules.getReferences());
        rules.setRuntimeProperties(runRules.getRuntimeProperties());
        rules.setSourceQuery(q);
        rules.setTargetConnection(c);
        log.info("Finished creating ImportRules");
        return rules;
    }

    /**
     * Copies the file source to destination dest
     * 
     * @param src
     * @param dest
     * @param alwaysFreeFiles
     * @throws java.io.IOException
     */
    public void copyFile(File src, File dest) throws IOException {
        if (src == null || dest == null) {
            throw new IllegalArgumentException("no nullpointer allowed here");
        }
        if (src.isDirectory()) {
            dest.mkdirs();
            final String list[] = src.list();
            for (int i = 0; i < list.length; i++) {
                final File dest1 = new File(dest.getAbsolutePath() + File.separator + list[i]);
                final File src1 = new File(src.getAbsolutePath() + File.separator + list[i]);
                copyFile(src1, dest1);
            }
        } else {
            dest.getParentFile().mkdirs();
            final BufferedInputStream fin = new BufferedInputStream((new FileInputStream(src)));
            final BufferedOutputStream fout = new BufferedOutputStream((new FileOutputStream(dest)));
            int c;
            while ((c = fin.read()) >= 0) {
                fout.write(c);
            }
            fin.close();
            fout.close();
        }
    }

    /**
     * Extract the given zip-archive to the destination.
     * 
     * @param archive
     * @param destDir
     * @throws java.util.zip.ZipException
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public void extractFromZip(final File archive, final File destDir) throws ZipException, FileNotFoundException, IOException {
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        final ZipFile zipFile = new ZipFile(archive);
        final Enumeration entries = zipFile.entries();
        byte[] buffer = new byte[BUFFER];
        int len;
        while (entries.hasMoreElements()) {
            final ZipEntry entry = (ZipEntry) entries.nextElement();
            final String entryFileName = entry.getName();
            final File dir = buildDirectoryHierarchyFor(entryFileName, destDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!entry.isDirectory()) {
                final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(destDir, entryFileName)));
                final BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                while ((len = bis.read(buffer)) > 0) {
                    bos.write(buffer, 0, len);
                }
                bos.flush();
                bos.close();
                bis.close();
            }
        }
    }

    /**
     * Compress the input file into a zip-file.
     *
     * @param input
     * @param zipfile
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public void compressToZip(final File input, final File zipfile) throws FileNotFoundException, IOException {
        ZipOutputStream zipOutputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipfile));
            for (final File file : input.listFiles()) {
                interateAndCompress(file, file.getParentFile(), zipOutputStream);
            }
        } finally {
            if (zipOutputStream != null) {
                zipOutputStream.close();
            }
        }
    }

    /**
     * Finds the next (similar name) free file for a file in its parent directory. 
     * 
     * @param toTest
     * @return
     */
    public File findFreeFile(final File toTest) throws FileNotFoundException {
        if (toTest == null) {
            throw new IllegalArgumentException("File is null!");
        }
        File ret = toTest;
        for (int i = 1; ret.exists() && i < Integer.MAX_VALUE; ++i) {
            final String absP = toTest.getAbsolutePath();
            final int dotIndex = absP.lastIndexOf(".");
            if (!(dotIndex > 0)) {
                throw new FileNotFoundException("Can not find File " + toTest);
            }
            ret = new File(absP.substring(0, dotIndex) + "_" + i + absP.substring(dotIndex));
        }
        if (!ret.exists()) {
            return ret;
        } else {
            throw new FileNotFoundException("Can not find File " + toTest);
        }
    }

    /**
     * Read the file-content into a String.
     * 
     * @param toRead
     * @return
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public String readFileToString(final File toRead) throws FileNotFoundException, IOException {
        final StringBuilder fileData = new StringBuilder(BUFFER);
        final BufferedReader reader = new BufferedReader(new FileReader(toRead));
        final char[] buf = new char[BUFFER];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            fileData.append(buf, 0, numRead);
        }
        reader.close();
        return fileData.toString();
    }

    /**
     * Finds the URL of the jar-File containing the code of the given class.
     *
     * @param clazz
     * @return
     */
    public static URL locateJarForClass(final Class<?> clazz) {
        URL ret = null;
        //first method
        try {
            ret = clazz.getProtectionDomain().getCodeSource().getLocation();
        } catch (Exception e) {
            log.warn(e, e);
        }
        if (ret != null) {
            return ret;
        } else {
            //second method
            final URL clsUrl = clazz.getResource(clazz.getSimpleName() + ".class");
            if (clsUrl != null) {
                try {
                    final URLConnection conn = clsUrl.openConnection();
                    if (conn instanceof JarURLConnection) {
                        final JarURLConnection connection = (JarURLConnection) conn;
                        return connection.getJarFileURL();
                    }
                } catch (Exception e) {
                    log.warn("Can not locate Jar for class " + clazz.getCanonicalName(), e);
                }
            }
        }
        throw new RuntimeException("Can not locate Jar for class " + clazz.getCanonicalName());
    }

    /**
     * Creates a temporary directory.
     *
     * @return
     */
    public static File createTempDir() {
        final String sysTemp = getSystemTempDir();
        File newTemp = null;
        while (newTemp == null || newTemp.exists() || !newTemp.mkdirs()) {
            newTemp = new File(sysTemp, "jp" + System.currentTimeMillis());
        }
        newTemp.deleteOnExit();
        return newTemp;
    }

    // <editor-fold defaultstate="collapsed" desc="Private helper methods">
    /**
     * Used by compressToZip
     *
     * @param dir
     * @param file
     * @return
     */
    private String getRelativePath(final File dir, final File file) {
        final Stack<String> stack = new Stack<String>();
        File tempFile = file;
        while (tempFile != null && !tempFile.equals(dir)) {
            stack.push(tempFile.getName());
            tempFile = tempFile.getParentFile();
        }
        assert tempFile != null : file.getAbsolutePath() + "not found in " + dir.getAbsolutePath();
        final StringBuilder retval = new StringBuilder();
        while (!stack.isEmpty()) {
            retval.append(stack.pop());
            if (!stack.isEmpty()) {
                retval.append('/');
            }
        }
        return retval.toString();
    }

    /**
     * Used by compressToZip
     *
     * @param file
     * @param relateTo
     * @param zipOutputStream
     * @throws java.lang.Exception
     */
    private void interateAndCompress(final File file, final File relateTo, final ZipOutputStream zipOutputStream) throws FileNotFoundException, IOException {
        final byte[] buffer = new byte[BUFFER];
        int len = 0;
        if (!file.isDirectory()) {
            final String relPath = getRelativePath(relateTo, file);
            final ZipEntry entry = new ZipEntry(relPath);
            zipOutputStream.putNextEntry(entry);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                while ((len = fis.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
        } else {
            final File[] children = file.listFiles();
            for (int i = 0; i < children.length; i++) {
                final File child = children[i];
                interateAndCompress(child, relateTo, zipOutputStream);
            }
        }
    }

    /**
     * Used by extractFromZip
     *
     * @param entryName
     * @param destDir
     * @return
     */
    private File buildDirectoryHierarchyFor(final String entryName, final File destDir) {
        int lastIndex = entryName.lastIndexOf('/');
        final String internalPathToEntry = entryName.substring(0, lastIndex + 1);
        return new File(destDir, internalPathToEntry);
    }

    /**
     * Used by createTempDir(). Finds the system's temp directory.
     *
     * @return
     */
    private static final String getSystemTempDir() {
        return System.getProperty("java.io.tmpdir");
    }
// </editor-fold>
}
