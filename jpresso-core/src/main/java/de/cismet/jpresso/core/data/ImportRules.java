/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.cismet.jpresso.core.serviceprovider.AntExecutableInterface;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.util.Collections;
import java.util.List;

/**
 *  Encapsulates all the import descrition datas
 * //TODO Zur Fasscade machen, so dass z.b. 
 * getConnectionInfo.getSourceJdbcConnection.getDriverClass()
 * zu getSourceJDBCConnectionDriverClass() wird. 
 * -> weniger Abhängigkeiten von den Einzelklassen dieses Pakets.
 * 
 * 
 * @author srichter
 */
@XStreamAlias("ImportRules")
public final class ImportRules implements AntExecutableInterface, JPLoadable {

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    public ImportRules(final DatabaseConnection target, final Query source, final List<Mapping> mappings, final List<Reference> references, final RuntimeProperties runtimeProperties, final Options options) {
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

    public ImportRules() {
        mappings = TypeSafeCollections.newArrayList();
        references = TypeSafeCollections.newArrayList();
        sourceQuery = new Query();
        targetConnection = new DatabaseConnection();
    }
// </editor-fold>
    //private String name;
//    @XStreamAlias("ConnectionInfo")
//    private ConnectionInfo connectionInfo;
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
    //key = X-JDBCConnection.getName()
    // <editor-fold defaultstate="collapsed" desc="Setters & Getters">

    public RuntimeProperties getRuntimeProperties() {
        return runtimeProperties;
    }

    public void setRuntimeProperties(RuntimeProperties runtimeProperties) {
        this.runtimeProperties = runtimeProperties;
    }

    public List<Reference> getReferences() {
        return references;
    }

    public void setReferences(List<Reference> references) {
        this.references = references;
    }

    public List<Mapping> getMappings() {
        return mappings;
    }

    public void setMappings(List<Mapping> mappings) {
        this.mappings = mappings;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public Query getSourceQuery() {
        return this.sourceQuery;
    }

    public void setSourceQuery(Query sourceConnection) {
        if (sourceConnection != null) {
            this.sourceQuery = sourceConnection;
        } else {
            this.sourceQuery = new Query();
        }
    }

    public DatabaseConnection getTargetConnection() {
        return this.targetConnection;

    }

    public void setTargetConnection(DatabaseConnection targetConnection) {
        if (targetConnection != null) {
            this.targetConnection = targetConnection;
        } else {
            this.targetConnection = new DatabaseConnection();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        if (fileName != null) {
            this.fileName = fileName;
        } else {
            fileName = "";
        }
    }
// </editor-fold>
}
