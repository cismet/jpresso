/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.Properties;

/**
 *
 * @author srichter
 */
@XStreamAlias("SourceQuery")
public class Query implements JPLoadable {

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    public Query(String driverClass, String url, String statement, String labelCase, Properties props, int previewMaxRows) {
        this.connection = new DatabaseConnection(driverClass, url, props);
        this.queryStatement = statement != null ? statement : "";
        this.labelCase = labelCase != null ? labelCase : "";
        if (previewMaxRows > -1) {
            this.previewMaxRows = previewMaxRows;
        } else {
            this.previewMaxRows = 0;
        }
    }

    public Query(DatabaseConnection connection, String queryStatement) {
        this.connection = connection;
        this.queryStatement = queryStatement;
        this.previewMaxRows = 0;
    }

    public Query(DatabaseConnection connection, String queryStatement, int previewMaxRows) {
        this.connection = connection;
        this.queryStatement = queryStatement;
        if (previewMaxRows > -1) {
            this.previewMaxRows = previewMaxRows;
        } else {
            this.previewMaxRows = 0;
        }
    }

    public Query() {
        this.connection = new DatabaseConnection("", "", new Properties());
        this.previewMaxRows = 0;
    }
    // </editor-fold>
    public static final String UPPER_CASE = "upper";
    public static final String LOWER_CASE = "lower";
    @XStreamAlias("Connection")
    private String connectionFile = "";
    private transient DatabaseConnection connection;
    @XStreamAlias("Statement")
    private String queryStatement = "";
    @XStreamAlias("PreviewMaxRows")
    private int previewMaxRows = 0;
    @XStreamAlias("LabelCase")
    private String labelCase = "";

    // <editor-fold defaultstate="collapsed" desc="Setters & Getters">
    public DatabaseConnection getConnection() {
        return connection;
    }

    public void setConnection(DatabaseConnection connection) {
        if (connection != null) {
            this.connection = connection;
        } else {
            this.connection = new DatabaseConnection();
        }
    }

    public String getQueryStatement() {
        return queryStatement;
    }

    public void setQueryStatement(String queryStatement) {
        this.queryStatement = queryStatement;
    }

    public String getDriverClass() {
        if (connection != null) {
            return connection.getDriverClass();
        } else {
            return "";
        }
    }

    public void setDriverClass(String driverClass) {
        if (driverClass != null) {
            connection.setDriverClass(driverClass);
        } else {
            connection.setDriverClass("");
        }
    }

    public String getUrl() {
        if (connection != null) {
            return connection.getUrl();
        } else {
            return "";
        }
    }

    public void setUrl(String url) {
        if (url != null) {
            connection.setUrl(url);
        } else {
            connection.setUrl("");
        }
    }

    public Properties getProps() {
        if (connection != null) {
            return connection.getProps();
        } else {
            return new Properties();
        }
    }

    public void setProps(Properties props) {
        if (props != null) {
            connection.setProps(props);
        } else {
            connection.setProps(new Properties());
        }
    }

    public String getConnectionFile() {
        return connectionFile;
    }

    public void setConnectionFile(String connectionFile) {
        if (connectionFile != null) {
            this.connectionFile = connectionFile;
        } else {
            this.connectionFile = "";
        }
    }

    public int getPreviewMaxRows() {
        return previewMaxRows;
    }

    public void setPreviewMaxRows(int previewMaxRows) {
        if (previewMaxRows > -1) {
            this.previewMaxRows = previewMaxRows;
        } else {
            this.previewMaxRows = 0;
        }
    }

    /**
     * @return the labelCase
     */
    public String getLabelCase() {
        return labelCase != null ? labelCase : "";
    }

    /**
     * @param labelCase the labelCase to set
     */
    public void setLabelCase(String labelCase) {
        if (labelCase != null) {
            this.labelCase = labelCase;
        } else {
            this.labelCase = "";
        }
    }
// </editor-fold>

    public boolean weakEquals(final Query other) {
        if (other == null) {
            return false;
        } else if (other == this) {
            return true;
        } else {
            return getConnectionFile().equals(other.getConnectionFile()) &&
                    getQueryStatement().equals(other.getQueryStatement());
        }
    }

    public boolean labelsToLowerCase() {
        return labelCase != null ? labelCase.equalsIgnoreCase(LOWER_CASE) : false;
    }

    public boolean labelsToUpperCase() {
        return labelCase != null ? labelCase.equalsIgnoreCase(UPPER_CASE) : false;
    }
}
