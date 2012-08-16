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
package de.cismet.jpresso.core.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Collections;
import java.util.List;

import de.cismet.jpresso.core.serviceprovider.AntExecutableInterface;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * Encapsulates all the import descrition datas //TODO Zur Fasscade machen, so dass z.b.
 * getConnectionInfo.getSourceJdbcConnection.getDriverClass() zu getSourceJDBCConnectionDriverClass() wird. -> weniger
 * Abhängigkeiten von den Einzelklassen dieses Pakets.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
@XStreamAlias("ImportRules")
public final class ImportRules implements AntExecutableInterface, JPLoadable {

    //~ Constructors -----------------------------------------------------------

    /**
     * <editor-fold defaultstate="collapsed" desc="Constructors">.
     *
     * @param  target             DOCUMENT ME!
     * @param  source             DOCUMENT ME!
     * @param  mappings           DOCUMENT ME!
     * @param  references         DOCUMENT ME!
     * @param  runtimeProperties  DOCUMENT ME!
     * @param  options            DOCUMENT ME!
     */
    public ImportRules(final DatabaseConnection target,
            final Query source,
            final List<Mapping> mappings,
            final List<Reference> references,
            final RuntimeProperties runtimeProperties,
            final Options options) {
        if (mappings != null) {
            this.mappings = mappings;
        } else {
            this.mappings = TypeSafeCollections.newArrayList();
        }
        if (references != null) {
            this.references = references;
        } else {
            this.references = TypeSafeCollections.newArrayList();
        }
        if (runtimeProperties != null) {
            this.runtimeProperties = runtimeProperties;
        } else {
            this.runtimeProperties = new RuntimeProperties();
        }
        if (options != null) {
            this.options = options;
        } else {
            this.options = new Options();
        }
        if (target != null) {
            this.targetConnection = target;
        } else {
            this.targetConnection = new DatabaseConnection();
        }
        if (source != null) {
            this.sourceQuery = source;
        } else {
            this.sourceQuery = new Query();
        }
    }

    /**
     * Creates a new ImportRules object.
     */
    public ImportRules() {
        mappings = TypeSafeCollections.newArrayList();
        references = TypeSafeCollections.newArrayList();
        sourceQuery = new Query();
        targetConnection = new DatabaseConnection();
    }
// </editor-fold>

    //~ Instance fields --------------------------------------------------------

    // private String name;
// @XStreamAlias("ConnectionInfo")
// private ConnectionInfo connectionInfo;
    @XStreamAlias("TargetConnection")
    private DatabaseConnection targetConnection;
    @XStreamAlias("SourceQuery")
    private Query sourceQuery;
    @XStreamAlias("Mappings")
    private List<Mapping> mappings;
    @XStreamAlias("References")
    private List<Reference> references;
    @XStreamAlias("RuntimeProperties")
    private RuntimeProperties runtimeProperties;
    @XStreamAlias("Options")
    private Options options;
    private transient String fileName = "";
    // key = X-JDBCConnection.getName()
    // <editor-fold defaultstate="collapsed" desc="Setters & Getters">

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public RuntimeProperties getRuntimeProperties() {
        return runtimeProperties;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  runtimeProperties  DOCUMENT ME!
     */
    public void setRuntimeProperties(final RuntimeProperties runtimeProperties) {
        this.runtimeProperties = runtimeProperties;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<Reference> getReferences() {
        return references;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  references  DOCUMENT ME!
     */
    public void setReferences(final List<Reference> references) {
        this.references = references;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<Mapping> getMappings() {
        return mappings;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  mappings  DOCUMENT ME!
     */
    public void setMappings(final List<Mapping> mappings) {
        this.mappings = mappings;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Options getOptions() {
        return options;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  options  DOCUMENT ME!
     */
    public void setOptions(final Options options) {
        this.options = options;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Query getSourceQuery() {
        return this.sourceQuery;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sourceConnection  DOCUMENT ME!
     */
    public void setSourceQuery(final Query sourceConnection) {
        if (sourceConnection != null) {
            this.sourceQuery = sourceConnection;
        } else {
            this.sourceQuery = new Query();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public DatabaseConnection getTargetConnection() {
        return this.targetConnection;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  targetConnection  DOCUMENT ME!
     */
    public void setTargetConnection(final DatabaseConnection targetConnection) {
        if (targetConnection != null) {
            this.targetConnection = targetConnection;
        } else {
            this.targetConnection = new DatabaseConnection();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fileName  DOCUMENT ME!
     */
    public void setFileName(String fileName) {
        if (fileName != null) {
            this.fileName = fileName;
        } else {
            fileName = "";
        }
    }
// </editor-fold>
}
