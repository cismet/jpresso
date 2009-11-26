/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.util.List;

/**
 *
 * @author srichter
 */
@XStreamAlias("JPressoRun")
public class JPressoRun implements JPLoadable {

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    public JPressoRun(String sourceQuery, String targetConnection, List<Mapping> mappings, List<Reference> references, RuntimeProperties runtimeProperties, Options options) {
        this.sourceQuery = sourceQuery;
        this.targetConnection = targetConnection;
        this.mappings = mappings;
        this.references = references;
        this.runtimeProperties = runtimeProperties;
        this.normalization = options;
    }

    public JPressoRun() {
        mappings = TypeSafeCollections.newArrayList();
        references = TypeSafeCollections.newArrayList();
        runtimeProperties = new RuntimeProperties();
    }
    // </editor-fold>
    @XStreamAlias("SourceQuery")
    private String sourceQuery;
    @XStreamAlias("TargetConnection")
    private String targetConnection;
    @XStreamAlias("Mappings")
    private List<Mapping> mappings;
    @XStreamAlias("References")
    private List<Reference> references;
    @XStreamAlias("RuntimeProperties")
    private RuntimeProperties runtimeProperties;
    @XStreamAlias("Options")
    private Options normalization;

    // <editor-fold defaultstate="collapsed" desc="Setters & Getters">
    public String getSourceQuery() {
        return sourceQuery;
    }

    public void setSourceQuery(final String sourceQuery) {
        if (sourceQuery != null) {
            this.sourceQuery = sourceQuery;
        } else {
            this.sourceQuery = "";
        }
    }

    public String getTargetConnection() {
        return targetConnection;
    }

    public void setTargetConnection(final String targetConnection) {
        if (targetConnection != null) {
            this.targetConnection = targetConnection;
        } else {
            this.targetConnection = "";
        }
    }

    public List<Mapping> getMappings() {
        return mappings;
    }

    public void setMappings(final List<Mapping> mappings) {
        if (mappings != null) {
            this.mappings = mappings;
        } else {
            this.mappings = TypeSafeCollections.newArrayList();
        }
    }

    public List<Reference> getReferences() {
        return references;
    }

    public void setReferences(final List<Reference> references) {
        if (references != null) {
            this.references = references;
        } else {
            this.references = TypeSafeCollections.newArrayList();
        }
    }

    public RuntimeProperties getRuntimeProperties() {
        return runtimeProperties;
    }

    public void setRuntimeProperties(final RuntimeProperties runtimeProperties) {
        if (runtimeProperties != null) {
            this.runtimeProperties = runtimeProperties;
        } else {
            this.runtimeProperties = new RuntimeProperties();
        }
    }

    public Options getOptions() {
        return normalization;
    }

    public void setOptions(final Options options) {
        if (options != null) {
            this.normalization = options;
        } else {
            this.normalization = new Options();
        }
    }
// </editor-fold>
}
