/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.kernel;

import de.cismet.jpresso.core.kernel.*;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.util.List;

/**
 * @TODO
 * Not yet finished implementation of a better and more performant (meta)data
 * storage. Better RowMetaInfo/Container?
 * TableNameWithPath -> IntermedTable (w/o path!).
 * Better name? StorageMI?
 * @author srichter
 */
public class TableMetaInfo {

    public static final Builder getBuilder() {
        return new Builder();
    }

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
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TableMetaInfo.class);
    public static final String NO_NORMALIZATION = "-";
    public static final String MEMORY_ONLY_NORMALIZATION = "#";
    public static final String FULL_NORMALIZATION = "!";
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

    public final boolean isDeleteOrphaned() {
        return deleteOrphaned;
    }

    public final boolean isDetailTable() {
        return !references.isEmpty();
    }

    @Override
    public final String toString() {
        final StringBuffer sb = new StringBuffer("TableInfo for ").append(getTableNameWithPath()).append(" : ").append(getReferences());
        return sb.toString();
    }

    /**
     * @return the references
     */
    final List<StoreOffset> getReferences() {
        return references;
    }

    /**
     * @return the tableNameWithPath
     */
    public final String getTableNameWithPath() {
        return tableNameWithPath;
    }

    /**
     * @return the tableName
     */
    public final String getTableName() {
        return tableName;
    }

    /**
     * @return the detailKeyFields
     */
    public final int[] getDetailKeyFields() {
        return detailKeyFields;
    }

    /**
     * @return the autoIncrementFields
     */
    public final int[] getAutoIncrementFields() {
        return autoIncrementFields;
    }

    /**
     * @return the enclosingCharacters
     */
    public final String[] getEnclosingCharacters() {
        return enclosingCharacters;
    }

    /**
     * @return the compareFields
     */
    public final String[] getCompareFields() {
        return compareFields;
    }

    /**
     * @return the normalizationType
     */
    public final String getNormalizationType() {
        return normalizationType;
    }

    /**
     * @return the isMasterTable
     */
    public final boolean isMasterTable() {
        return masterTable;
    }

    /**
     *
     */
    public static final class Builder {

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

        public final Builder setDeleteOrphaned(boolean deleteOrphaned) {
            this.deleteOrphaned = deleteOrphaned;
            return this;
        }

        public final Builder setTableName(String tableName) {
            if (tableName != null) {
                this.tableName = tableName;
            }
            return this;
        }

        public final Builder setTableNameWithPath(String tableNameWithPath) {
            if (tableNameWithPath != null) {
                this.tableNameWithPath = tableNameWithPath;
            }
            return this;
        }

        public final Builder setAutoIncrementFields(int[] autoIncFields) {
            if (autoIncFields != null) {
                this.autoIncrementFields = autoIncFields.clone();
            }
            return this;
        }

        public final Builder setEnclosingCharacters(String[] enclosingChars) {
            if (enclosingChars != null) {
                this.enclosingCharacters = enclosingChars.clone();
            }
            return this;
        }

        public final Builder setCompareFields(String[] compareFields) {
            if (compareFields != null) {
                this.compareFields = compareFields.clone();
            }
            return this;
        }

        public final Builder setMasterTable(boolean masterTable) {
            this.masterTable = masterTable;
            return this;
        }

        public final Builder setNormalizationType(String normalizationType) {
            if (normalizationType != null) {
                this.normalizationType = normalizationType;
            }
            return this;
        }

        public final Builder addStoreOffsets(int masterTableNo, int masterFieldPosition, int detailFieldPosition) {
            references.add(new StoreOffset(masterTableNo, masterFieldPosition, detailFieldPosition));
            return this;
        }

        public final Builder setDetailKeyFields(int[] detailKeyFields) {
            if (detailKeyFields != null) {
                this.detailKeyFields = detailKeyFields.clone();
            }
            return this;
        }

        public final TableMetaInfo build() {
            return new TableMetaInfo(this);
        }
    }
}

/**
 *
 * @author srichter
 */
final class StoreOffset {

    public StoreOffset(int masterTabIndex, int masterFieldPosition, int detailFieldPosition) {
        if (masterTabIndex < 0 || masterFieldPosition < 0 || detailFieldPosition < 0) {
            throw new IllegalArgumentException();
        }
        this.masterTabIndex = masterTabIndex;
        this.masterFieldPosition = masterFieldPosition;
        this.detailFieldPosition = detailFieldPosition;
    }
    //Name of MasterTable
    public final int masterTabIndex;
    //Position in MasterTable to copy into
    public final int masterFieldPosition;
    //Position in CurrentTable that we want to copy in
    public final int detailFieldPosition;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DetailField [");
        sb.append(detailFieldPosition).append("] -> Mastertable [").append(masterTabIndex).append("], MasterField [").append(masterFieldPosition).append("]");
        return sb.toString();
    }
}
