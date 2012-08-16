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
package de.cismet.jpresso.core.kernel;

import java.util.List;

import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 * @TODO     Not yet finished implementation of a better and more performant (meta)data storage. Better
 *           RowMetaInfo/Container? TableNameWithPath -> IntermedTable (w/o path!). Better name? StorageMI?
 */
public class TableMetaInfo {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TableMetaInfo.class);
    public static final String NO_NORMALIZATION = "-";
    public static final String MEMORY_ONLY_NORMALIZATION = "#";
    public static final String FULL_NORMALIZATION = "!";

    //~ Instance fields --------------------------------------------------------

    private final List<StoreOffset> references;
    private final String tableNameWithPath;
    private final String tableName;
    private final int[] detailKeyFields;
    private final int[] autoIncrementFields;
    private final String[] enclosingCharacters;
    private final String[] compareFields;
    private final String normalizationType;
    private final boolean masterTable;
    private final boolean deleteOrphaned;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new TableMetaInfo object.
     *
     * @param   builder  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    private TableMetaInfo(final Builder builder) {
        if (builder == null) {
            throw new IllegalArgumentException();
        }
        this.references = builder.references;
        this.tableNameWithPath = builder.tableNameWithPath;
        this.tableName = builder.tableName;
        this.detailKeyFields = builder.detailKeyFields;
        this.autoIncrementFields = builder.autoIncrementFields;
        this.enclosingCharacters = builder.enclosingCharacters;
        this.compareFields = builder.compareFields;
        this.normalizationType = builder.normalizationType;
        this.masterTable = builder.masterTable;
        if (references.size() > 0) {
            this.deleteOrphaned = builder.deleteOrphaned;
        } else {
            this.deleteOrphaned = false;
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static final Builder getBuilder() {
        return new Builder();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public final boolean isDeleteOrphaned() {
        return deleteOrphaned;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public final boolean isDetailTable() {
        return !references.isEmpty();
    }

    @Override
    public final String toString() {
        final StringBuffer sb = new StringBuffer("TableInfo for ").append(getTableNameWithPath())
                    .append(" : ")
                    .append(getReferences());
        return sb.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the references
     */
    final List<StoreOffset> getReferences() {
        return references;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the tableNameWithPath
     */
    public final String getTableNameWithPath() {
        return tableNameWithPath;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the tableName
     */
    public final String getTableName() {
        return tableName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the detailKeyFields
     */
    public final int[] getDetailKeyFields() {
        return detailKeyFields;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the autoIncrementFields
     */
    public final int[] getAutoIncrementFields() {
        return autoIncrementFields;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the enclosingCharacters
     */
    public final String[] getEnclosingCharacters() {
        return enclosingCharacters;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the compareFields
     */
    public final String[] getCompareFields() {
        return compareFields;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the normalizationType
     */
    public final String getNormalizationType() {
        return normalizationType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the isMasterTable
     */
    public final boolean isMasterTable() {
        return masterTable;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static final class Builder {

        //~ Instance fields ----------------------------------------------------

        private final List<StoreOffset> references;
        private String tableNameWithPath;
        private String tableName;
        private int[] detailKeyFields;
        private int[] autoIncrementFields;
        private String[] enclosingCharacters;
        private String[] compareFields;
        private String normalizationType;
        private boolean masterTable;
        private boolean deleteOrphaned;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Builder object.
         */
        private Builder() {
            references = TypeSafeCollections.newArrayList();
            tableNameWithPath = "";
            tableName = "";
            detailKeyFields = new int[0];
            autoIncrementFields = new int[0];
            enclosingCharacters = new String[0];
            compareFields = new String[0];
            normalizationType = NO_NORMALIZATION;
            masterTable = false;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param   deleteOrphaned  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Builder setDeleteOrphaned(final boolean deleteOrphaned) {
            this.deleteOrphaned = deleteOrphaned;
            return this;
        }

        /**
         * DOCUMENT ME!
         *
         * @param   tableName  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Builder setTableName(final String tableName) {
            if (tableName != null) {
                this.tableName = tableName;
            }
            return this;
        }

        /**
         * DOCUMENT ME!
         *
         * @param   tableNameWithPath  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Builder setTableNameWithPath(final String tableNameWithPath) {
            if (tableNameWithPath != null) {
                this.tableNameWithPath = tableNameWithPath;
            }
            return this;
        }

        /**
         * DOCUMENT ME!
         *
         * @param   autoIncFields  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Builder setAutoIncrementFields(final int[] autoIncFields) {
            if (autoIncFields != null) {
                this.autoIncrementFields = autoIncFields.clone();
            }
            return this;
        }

        /**
         * DOCUMENT ME!
         *
         * @param   enclosingChars  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Builder setEnclosingCharacters(final String[] enclosingChars) {
            if (enclosingChars != null) {
                this.enclosingCharacters = enclosingChars.clone();
            }
            return this;
        }

        /**
         * DOCUMENT ME!
         *
         * @param   compareFields  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Builder setCompareFields(final String[] compareFields) {
            if (compareFields != null) {
                this.compareFields = compareFields.clone();
            }
            return this;
        }

        /**
         * DOCUMENT ME!
         *
         * @param   masterTable  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Builder setMasterTable(final boolean masterTable) {
            this.masterTable = masterTable;
            return this;
        }

        /**
         * DOCUMENT ME!
         *
         * @param   normalizationType  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Builder setNormalizationType(final String normalizationType) {
            if (normalizationType != null) {
                this.normalizationType = normalizationType;
            }
            return this;
        }

        /**
         * DOCUMENT ME!
         *
         * @param   masterTableNo        DOCUMENT ME!
         * @param   masterFieldPosition  DOCUMENT ME!
         * @param   detailFieldPosition  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Builder addStoreOffsets(final int masterTableNo,
                final int masterFieldPosition,
                final int detailFieldPosition) {
            references.add(new StoreOffset(masterTableNo, masterFieldPosition, detailFieldPosition));
            return this;
        }

        /**
         * DOCUMENT ME!
         *
         * @param   detailKeyFields  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Builder setDetailKeyFields(final int[] detailKeyFields) {
            if (detailKeyFields != null) {
                this.detailKeyFields = detailKeyFields.clone();
            }
            return this;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public TableMetaInfo build() {
            return new TableMetaInfo(this);
        }
    }
}

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
final class StoreOffset {

    //~ Instance fields --------------------------------------------------------

    // Name of MasterTable
    public final int masterTabIndex;
    // Position in MasterTable to copy into
    public final int masterFieldPosition;
    // Position in CurrentTable that we want to copy in
    public final int detailFieldPosition;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new StoreOffset object.
     *
     * @param   masterTabIndex       DOCUMENT ME!
     * @param   masterFieldPosition  DOCUMENT ME!
     * @param   detailFieldPosition  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public StoreOffset(final int masterTabIndex, final int masterFieldPosition, final int detailFieldPosition) {
        if ((masterTabIndex < 0) || (masterFieldPosition < 0) || (detailFieldPosition < 0)) {
            throw new IllegalArgumentException();
        }
        this.masterTabIndex = masterTabIndex;
        this.masterFieldPosition = masterFieldPosition;
        this.detailFieldPosition = detailFieldPosition;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DetailField [");
        sb.append(detailFieldPosition)
                .append("] -> Mastertable [")
                .append(masterTabIndex)
                .append("], MasterField [")
                .append(masterFieldPosition)
                .append("]");
        return sb.toString();
    }
}
