/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.io;

import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import com.thoughtworks.xstream.XStream;
import de.cismet.jpresso.core.data.DatabaseConnection;
import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.core.data.DriverJar;
import de.cismet.jpresso.core.data.ImportRules;
import de.cismet.jpresso.core.data.JPLoadable;
import de.cismet.jpresso.core.data.JPressoRun;
import de.cismet.jpresso.core.data.Mapping;
import de.cismet.jpresso.core.data.Options;
import de.cismet.jpresso.core.data.ProjectOptions;
import de.cismet.jpresso.core.data.Query;
import de.cismet.jpresso.core.data.Reference;
import de.cismet.jpresso.core.data.RuntimeProperties;
import de.cismet.jpresso.core.data.SQLRun;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/**
 *  TODO classcasts prüfen bzw Exceptions fangen und ClasscastException als IOException weiterreichen.
 * 
 * @see JPressoFileManager
 * @author srichter
 */
public final class XStreamJPressoFileManager extends JPressoFileManager {

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    private XStreamJPressoFileManager() {
        log.debug("Instantiating DefaultNetbeansLoader");
        init();
    }
    // </editor-fold>
    private static final JPressoFileManager instance = new XStreamJPressoFileManager();
    // Maps Class -> apropriate LoadingProcedure
    private final Map<Class<? extends JPLoadable>, LoadingProcedure<? extends JPLoadable>> loadingProcedures = TypeSafeCollections.newHashMap();
    //The XStream
    private final XStream xs = new XStream();

    public static final JPressoFileManager getInstance() {
        return instance;
    }
    
    @Override
    public void persist(final File xmlFilename, JPLoadable toSave) throws IOException {
        if (toSave != null) {
            log.debug("Persisting " + toSave.getClass().getName() + " to file: " + xmlFilename.getAbsoluteFile());
            BufferedWriter out = null;
            try {
                xmlFilename.getParentFile().mkdirs();
                out = new BufferedWriter(new FileWriter(xmlFilename));
                xs.toXML(toSave, out);
                out.close();
            } finally {
                try {
                    out.close();
                } catch (IOException ex) {
                    log.error("Can not close file.", ex);
                }
            }
        } else {
            //TODO Exception?
            log.error("Object to save is null. Do nothing.");
        }
    }

    /**
     * Actual loading routine using XStream to parse Objects from XML.
     * 
     * @param <T>
     * @param file
     * @param clazz
     * @return
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    private final <T extends JPLoadable> T loadSimpleObject(final File file, final Class<T> clazz) throws FileNotFoundException, IOException {
        if (file == null || clazz == null) {
            throw new IllegalArgumentException("no nullpointer allowed here");
        }
        
        final BufferedReader in = new BufferedReader(new FileReader(file));
        T ret = null;
        try {
            final Object tmp = xs.fromXML(in);
            ret = clazz.cast(tmp);
        } catch (ClassCastException ce) {
            String msg = "Can not parse Object from " + file + ". File describes object from another class!";
            log.error(msg, ce);
            throw new IOException(msg);
        } catch (Exception e) {
            String msg = "Can not parse Object from " + file + ". File seems to be corrupted!";
            log.error(msg, e);
            throw new IOException(msg);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                log.error("Can not close file.", ex);
            }
        }
        return ret;
    }

    /**
     * init the FileManager and XStream.
     */
    private void init() {
        //Annotations.configureAliases(xs,
        xs.processAnnotations(new Class[] {
                ImportRules.class,
                Mapping.class,
                Options.class,
                Reference.class,
                RuntimeProperties.class,
                Query.class,
                SQLRun.class,
                DatabaseConnection.class,
                JPressoRun.class,
                ProjectOptions.class,
                DriverDescription.class,
                DriverJar.class});
        addLoadingProcedure(DatabaseConnection.class, new ConnectionLoadingProcedure());
        addLoadingProcedure(Query.class, new QueryLoadingProcedure());
        addLoadingProcedure(JPressoRun.class, new RunLoadingProcedure());
        addLoadingProcedure(SQLRun.class, new SQLLoadingProcedure());
        addLoadingProcedure(ProjectOptions.class, new OptionsLoadingProcedure());
    }

    /**
     * Adds a new LoadingProcedure to the Filemanager.
     * 
     * IMPORTANT: Class and LoadingProcedure must be of the same parameterized type!!!
     * 
     * @param procedure
     * @param key
     */
    @Override
    public void addLoadingProcedure(final Class<? extends JPLoadable> key, final LoadingProcedure<? extends JPLoadable> procedure) {
        final Class procedureClass = procedure.getClass();
        final Type[] genericInterfaces = procedureClass.getGenericInterfaces();
        for (final Type t : genericInterfaces) {
            if (t instanceof ParameterizedType) {
                final ParameterizedType pt = (ParameterizedType) t;
                if (pt.getRawType().equals(LoadingProcedure.class)) {
                    final Type[] genericTypes = pt.getActualTypeArguments();
                    //danger: genericTypes[0] is bound to the parameters of LoadingProcedure in its current definition!
                    if (genericTypes.length > 0 && genericTypes[0].equals(key)) {
                        loadingProcedures.put(key, procedure);
                        return;
                    }
                }
                break;
            }
        }
        throw new IllegalArgumentException("Key and LodingProcedure are not of the same parameterized runtime-type!");
    }

    /**
     * 
     * @return
     */
    public Set<Class<? extends JPLoadable>> getLoadableClasses() {
        return loadingProcedures.keySet();
    }

    /**
     * Loads a JPLoadable Object from File.
     * 
     * @param <T>
     * @param file
     * @param clazz
     * @return
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    @Override
    public final <T extends JPLoadable> T load(final File file, final Class<T> clazz) throws FileNotFoundException, IOException {
        final LoadingProcedure<? extends JPLoadable> lp = loadingProcedures.get(clazz);
        return clazz.cast(lp.load(file));
    }

    /**
     * LoadingProcedure implementation for DatabaseConnection.class
     */
    final class ConnectionLoadingProcedure implements LoadingProcedure<DatabaseConnection> {

        @Override
        public DatabaseConnection load(final File file) throws FileNotFoundException, IOException {
            final DatabaseConnection r = loadSimpleObject(file, DatabaseConnection.class);
            return r;
        }
    }

    /**
     * LoadingProcedure implementation for Query.class
     */
    final class QueryLoadingProcedure implements LoadingProcedure<Query> {

        @Override
        public Query load(final File file) throws FileNotFoundException, IOException {
            log.debug("Loading query file: " + file);
            final Query q = loadSimpleObject(file, Query.class);
            if (q != null) {
                if (q.getConnectionFile() != null && q.getConnectionFile().length() > 0) {
                    final File projDir = file.getParentFile().getParentFile();
                    if (projDir != null && projDir.exists() && projDir.isDirectory()) {
                        final File con = new File(projDir.getAbsolutePath() + File.separator + DIR_CON + File.separator + q.getConnectionFile());
                        if (con != null && con.exists() && con.isFile()) {
                            log.debug("Preloading Query connection " + con.getAbsolutePath() + " for " + file.getName());
                            final DatabaseConnection dc = instance.load(con, DatabaseConnection.class);
                            if (dc != null) {
                                q.setConnection(dc);
                            } else {
                                q.setConnection(new DatabaseConnection());
                            }
                        }
                    }
                }

            }
            return q;
        }
    }

    /**
     * LoadingProcedure implementation for JPressoRun.class
     */
    final class RunLoadingProcedure implements LoadingProcedure<JPressoRun> {

        @Override
        public JPressoRun load(final File file) throws FileNotFoundException, IOException {
            log.debug("Loading run file: " + file);
            final JPressoRun r = loadSimpleObject(file, JPressoRun.class);
            return r;
        }
    }

    /**
     * LoadingProcedure implementation for SQLRun.class
     */
    final class SQLLoadingProcedure implements LoadingProcedure<SQLRun> {

        @Override
        public SQLRun load(final File file) throws FileNotFoundException, IOException {
            //TODO Cast checken!
            log.debug("Loading sql file: " + file);
            final SQLRun run = loadSimpleObject(file, SQLRun.class);
            DatabaseConnection dc = null;
            if (run != null) {
                if (run.getConnectionFile() != null && run.getConnectionFile().length() > 0) {
                    final File projDir = file.getParentFile().getParentFile();
                    if (projDir != null && projDir.exists() && projDir.isDirectory()) {
                        final File con = new File(projDir.getAbsolutePath() + File.separator + DIR_CON + File.separator + run.getConnectionFile());
                        if (con != null && con.exists() && con.isFile()) {
                            log.debug("Preloading SQL Run connection" + con.getAbsolutePath() + " for " + file.getName());
                            dc = instance.load(con, DatabaseConnection.class);
                        }
                    }
                }
                if (dc == null) {
                    dc = new DatabaseConnection();
                }
                run.setConnection(dc);
            }
            return run;
        }
    }

    /**
     * LoadingProcedure implementation for ProjectOptions.class
     */
    final class OptionsLoadingProcedure implements LoadingProcedure<ProjectOptions> {

        @Override
        public ProjectOptions load(final File file) throws FileNotFoundException, IOException {
            log.debug("Loading project options file: " + file);
            final ProjectOptions po = loadSimpleObject(file, ProjectOptions.class);
            return po;
        }
    }
}
