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
package de.cismet.jpresso.core.io;

import com.thoughtworks.xstream.XStream;

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
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * TODO classcasts prüfen bzw Exceptions fangen und ClasscastException als IOException weiterreichen.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 * @see      JPressoFileManager
 */
public final class XStreamJPressoFileManager extends JPressoFileManager {

    //~ Static fields/initializers ---------------------------------------------

    private static final JPressoFileManager instance = new XStreamJPressoFileManager();

    //~ Instance fields --------------------------------------------------------

    // Maps Class -> apropriate LoadingProcedure
    private final Map<Class<? extends JPLoadable>, LoadingProcedure<? extends JPLoadable>> loadingProcedures =
        TypeSafeCollections.newHashMap();
    // The XStream
    private final XStream xs = new XStream();

    //~ Constructors -----------------------------------------------------------

    /**
     * <editor-fold defaultstate="collapsed" desc="Constructors">.
     */
    private XStreamJPressoFileManager() {
        if (log.isDebugEnabled()) {
            log.debug("Instantiating DefaultNetbeansLoader");
        }
        init();
    }
    // </editor-fold>

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static JPressoFileManager getInstance() {
        return instance;
    }

    @Override
    public void persist(final File xmlFilename, final JPLoadable toSave) throws IOException {
        if (toSave != null) {
            if (log.isDebugEnabled()) {
                log.debug("Persisting " + toSave.getClass().getName() + " to file: " + xmlFilename.getAbsoluteFile());
            }
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
            // TODO Exception?
            log.error("Object to save is null. Do nothing.");
        }
    }

    /**
     * Actual loading routine using XStream to parse Objects from XML.
     *
     * @param   <T>    DOCUMENT ME!
     * @param   file   DOCUMENT ME!
     * @param   clazz  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  FileNotFoundException     DOCUMENT ME!
     * @throws  IOException               DOCUMENT ME!
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    private <T extends JPLoadable> T loadSimpleObject(final File file, final Class<T> clazz)
            throws FileNotFoundException, IOException {
        if ((file == null) || (clazz == null)) {
            throw new IllegalArgumentException("no nullpointer allowed here");
        }

        final BufferedReader in = new BufferedReader(new FileReader(file));
        T ret = null;
        try {
            final Object tmp = xs.fromXML(in);
            ret = clazz.cast(tmp);
        } catch (ClassCastException ce) {
            final String msg = "Can not parse Object from " + file + ". File describes object from another class!";
            log.error(msg, ce);
            throw new IOException(msg);
        } catch (Exception e) {
            final String msg = "Can not parse Object from " + file + ". File seems to be corrupted!";
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
        // Annotations.configureAliases(xs,
        xs.processAnnotations(
            new Class[] {
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
                DriverJar.class
            });
        addLoadingProcedure(DatabaseConnection.class, new ConnectionLoadingProcedure());
        addLoadingProcedure(Query.class, new QueryLoadingProcedure());
        addLoadingProcedure(JPressoRun.class, new RunLoadingProcedure());
        addLoadingProcedure(SQLRun.class, new SQLLoadingProcedure());
        addLoadingProcedure(ProjectOptions.class, new OptionsLoadingProcedure());
    }

    /**
     * Adds a new LoadingProcedure to the Filemanager.
     *
     * <p>IMPORTANT: Class and LoadingProcedure must be of the same parameterized type!!!</p>
     *
     * @param   key        DOCUMENT ME!
     * @param   procedure  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    @Override
    public void addLoadingProcedure(final Class<? extends JPLoadable> key,
            final LoadingProcedure<? extends JPLoadable> procedure) {
        final Class procedureClass = procedure.getClass();
        final Type[] genericInterfaces = procedureClass.getGenericInterfaces();
        for (final Type t : genericInterfaces) {
            if (t instanceof ParameterizedType) {
                final ParameterizedType pt = (ParameterizedType)t;
                if (pt.getRawType().equals(LoadingProcedure.class)) {
                    final Type[] genericTypes = pt.getActualTypeArguments();
                    // danger: genericTypes[0] is bound to the parameters of LoadingProcedure in its current definition!
                    if ((genericTypes.length > 0) && genericTypes[0].equals(key)) {
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
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Set<Class<? extends JPLoadable>> getLoadableClasses() {
        return loadingProcedures.keySet();
    }

    /**
     * Loads a JPLoadable Object from File.
     *
     * @param   <T>    DOCUMENT ME!
     * @param   file   DOCUMENT ME!
     * @param   clazz  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  FileNotFoundException  DOCUMENT ME!
     * @throws  IOException            DOCUMENT ME!
     */
    @Override
    public <T extends JPLoadable> T load(final File file, final Class<T> clazz) throws FileNotFoundException,
        IOException {
        final LoadingProcedure<? extends JPLoadable> lp = loadingProcedures.get(clazz);
        return clazz.cast(lp.load(file));
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * LoadingProcedure implementation for DatabaseConnection.class.
     *
     * @version  $Revision$, $Date$
     */
    final class ConnectionLoadingProcedure implements LoadingProcedure<DatabaseConnection> {

        //~ Methods ------------------------------------------------------------

        @Override
        public DatabaseConnection load(final File file) throws FileNotFoundException, IOException {
            final DatabaseConnection r = loadSimpleObject(file, DatabaseConnection.class);
            return r;
        }
    }

    /**
     * LoadingProcedure implementation for Query.class.
     *
     * @version  $Revision$, $Date$
     */
    final class QueryLoadingProcedure implements LoadingProcedure<Query> {

        //~ Methods ------------------------------------------------------------

        @Override
        public Query load(final File file) throws FileNotFoundException, IOException {
            if (log.isDebugEnabled()) {
                log.debug("Loading query file: " + file);
            }
            final Query q = loadSimpleObject(file, Query.class);
            if (q != null) {
                if ((q.getConnectionFile() != null) && (q.getConnectionFile().length() > 0)) {
                    final File projDir = file.getParentFile().getParentFile();
                    if ((projDir != null) && projDir.exists() && projDir.isDirectory()) {
                        final File con = new File(projDir.getAbsolutePath() + File.separator + DIR_CON + File.separator
                                        + q.getConnectionFile());
                        if ((con != null) && con.exists() && con.isFile()) {
                            if (log.isDebugEnabled()) {
                                log.debug("Preloading Query connection " + con.getAbsolutePath() + " for "
                                            + file.getName());
                            }
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
     * LoadingProcedure implementation for JPressoRun.class.
     *
     * @version  $Revision$, $Date$
     */
    final class RunLoadingProcedure implements LoadingProcedure<JPressoRun> {

        //~ Methods ------------------------------------------------------------

        @Override
        public JPressoRun load(final File file) throws FileNotFoundException, IOException {
            if (log.isDebugEnabled()) {
                log.debug("Loading run file: " + file);
            }
            final JPressoRun r = loadSimpleObject(file, JPressoRun.class);
            return r;
        }
    }

    /**
     * LoadingProcedure implementation for SQLRun.class.
     *
     * @version  $Revision$, $Date$
     */
    final class SQLLoadingProcedure implements LoadingProcedure<SQLRun> {

        //~ Methods ------------------------------------------------------------

        @Override
        public SQLRun load(final File file) throws FileNotFoundException, IOException {
            if (log.isDebugEnabled()) {
                // TODO Cast checken!
                log.debug("Loading sql file: " + file);
            }
            final SQLRun run = loadSimpleObject(file, SQLRun.class);
            DatabaseConnection dc = null;
            if (run != null) {
                if ((run.getConnectionFile() != null) && (run.getConnectionFile().length() > 0)) {
                    final File projDir = file.getParentFile().getParentFile();
                    if ((projDir != null) && projDir.exists() && projDir.isDirectory()) {
                        final File con = new File(projDir.getAbsolutePath() + File.separator + DIR_CON + File.separator
                                        + run.getConnectionFile());
                        if ((con != null) && con.exists() && con.isFile()) {
                            if (log.isDebugEnabled()) {
                                log.debug("Preloading SQL Run connection" + con.getAbsolutePath() + " for "
                                            + file.getName());
                            }
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
     * LoadingProcedure implementation for ProjectOptions.class.
     *
     * @version  $Revision$, $Date$
     */
    final class OptionsLoadingProcedure implements LoadingProcedure<ProjectOptions> {

        //~ Methods ------------------------------------------------------------

        @Override
        public ProjectOptions load(final File file) throws FileNotFoundException, IOException {
            if (log.isDebugEnabled()) {
                log.debug("Loading project options file: " + file);
            }
            final ProjectOptions po = loadSimpleObject(file, ProjectOptions.class);
            return po;
        }
    }
}
