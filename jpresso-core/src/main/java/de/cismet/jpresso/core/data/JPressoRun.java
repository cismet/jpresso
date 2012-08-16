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

import java.util.List;

import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
@XStreamAlias("JPressoRun")
public class JPressoRun implements JPLoadable {

    //~ Constructors -----------------------------------------------------------

    /**
     * <editor-fold defaultstate="collapsed" desc="Constructors">.
     *
     * @param  sourceQuery        DOCUMENT ME!
     * @param  targetConnection   DOCUMENT ME!
     * @param  mappings           DOCUMENT ME!
     * @param  references         DOCUMENT ME!
     * @param  runtimeProperties  DOCUMENT ME!
     * @param  options            DOCUMENT ME!
     */
    public JPressoRun(final String sourceQuery,
            final String targetConnection,
            final List<Mapping> mappings,
            final List<Reference> references,
            final RuntimeProperties runtimeProperties,
            final Options options) {
        this.sourceQuery = sourceQuery;
        this.targetConnection = targetConnection;
        this.mappings = mappings;
        this.references = references;
        this.runtimeProperties = runtimeProperties;
        this.normalization = options;
    }

    /**
     * Creates a new JPressoRun object.
     */
    public JPressoRun() {
        mappings = TypeSafeCollections.newArrayList();
        references = TypeSafeCollections.newArrayList();
        runtimeProperties = new RuntimeProperties();
    }
    // </editor-fold>

    /**
     * <editor-fold defaultstate="collapsed" desc="Setters & Getters">.
     *
     * @return  DOCUMENT ME!
     */
    public String getSourceQuery() {
        return sourceQuery;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sourceQuery  DOCUMENT ME!
     */
    public void setSourceQuery(final String sourceQuery) {
        if (sourceQuery != null) {
            this.sourceQuery = sourceQuery;
        } else {
            this.sourceQuery = "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getTargetConnection() {
        return targetConnection;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  targetConnection  DOCUMENT ME!
     */
    public void setTargetConnection(final String targetConnection) {
        if (targetConnection != null) {
            this.targetConnection = targetConnection;
        } else {
            this.targetConnection = "";
        }
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
        if (mappings != null) {
            this.mappings = mappings;
        } else {
            this.mappings = TypeSafeCollections.newArrayList();
        }
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
        if (references != null) {
            this.references = references;
        } else {
            this.references = TypeSafeCollections.newArrayList();
        }
    }

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
        if (runtimeProperties != null) {
            this.runtimeProperties = runtimeProperties;
        } else {
            this.runtimeProperties = new RuntimeProperties();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Options getOptions() {
        return normalization;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  options  DOCUMENT ME!
     */
    public void setOptions(final Options options) {
        if (options != null) {
            this.normalization = options;
        } else {
            this.normalization = new Options();
        }
    }
// </editor-fold>

    //~ Instance fields --------------------------------------------------------

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
}
