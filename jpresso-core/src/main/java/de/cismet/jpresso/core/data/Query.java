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

import java.util.Properties;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
@XStreamAlias("SourceQuery")
public class Query implements JPLoadable {

    //~ Constructors -----------------------------------------------------------

    /**
     * <editor-fold defaultstate="collapsed" desc="Constructors">.
     *
     * @param  driverClass     DOCUMENT ME!
     * @param  url             DOCUMENT ME!
     * @param  statement       DOCUMENT ME!
     * @param  labelCase       DOCUMENT ME!
     * @param  props           DOCUMENT ME!
     * @param  previewMaxRows  DOCUMENT ME!
     */
    public Query(final String driverClass,
            final String url,
            final String statement,
            final String labelCase,
            final Properties props,
            final int previewMaxRows) {
        this.connection = new DatabaseConnection(driverClass, url, props);
        this.queryStatement = (statement != null) ? statement : "";
        this.labelCase = (labelCase != null) ? labelCase : "";
        if (previewMaxRows > -1) {
            this.previewMaxRows = previewMaxRows;
        } else {
            this.previewMaxRows = 0;
        }
    }

    /**
     * Creates a new Query object.
     *
     * @param  connection      DOCUMENT ME!
     * @param  queryStatement  DOCUMENT ME!
     */
    public Query(final DatabaseConnection connection, final String queryStatement) {
        this.connection = connection;
        this.queryStatement = queryStatement;
        this.previewMaxRows = 0;
    }

    /**
     * Creates a new Query object.
     *
     * @param  connection      DOCUMENT ME!
     * @param  queryStatement  DOCUMENT ME!
     * @param  previewMaxRows  DOCUMENT ME!
     */
    public Query(final DatabaseConnection connection, final String queryStatement, final int previewMaxRows) {
        this.connection = connection;
        this.queryStatement = queryStatement;
        if (previewMaxRows > -1) {
            this.previewMaxRows = previewMaxRows;
        } else {
            this.previewMaxRows = 0;
        }
    }

    /**
     * Creates a new Query object.
     */
    public Query() {
        this.connection = new DatabaseConnection("", "", new Properties());
        this.previewMaxRows = 0;
    }
    // </editor-fold>
    public static final String UPPER_CASE = "upper";
    public static final String LOWER_CASE = "lower";

    /**
     * <editor-fold defaultstate="collapsed" desc="Setters & Getters">.
     *
     * @return  DOCUMENT ME!
     */
    public DatabaseConnection getConnection() {
        return connection;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  connection  DOCUMENT ME!
     */
    public void setConnection(final DatabaseConnection connection) {
        if (connection != null) {
            this.connection = connection;
        } else {
            this.connection = new DatabaseConnection();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getQueryStatement() {
        return queryStatement;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  queryStatement  DOCUMENT ME!
     */
    public void setQueryStatement(final String queryStatement) {
        this.queryStatement = queryStatement;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getDriverClass() {
        if (connection != null) {
            return connection.getDriverClass();
        } else {
            return "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  driverClass  DOCUMENT ME!
     */
    public void setDriverClass(final String driverClass) {
        if (driverClass != null) {
            connection.setDriverClass(driverClass);
        } else {
            connection.setDriverClass("");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getUrl() {
        if (connection != null) {
            return connection.getUrl();
        } else {
            return "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  url  DOCUMENT ME!
     */
    public void setUrl(final String url) {
        if (url != null) {
            connection.setUrl(url);
        } else {
            connection.setUrl("");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Properties getProps() {
        if (connection != null) {
            return connection.getProps();
        } else {
            return new Properties();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  props  DOCUMENT ME!
     */
    public void setProps(final Properties props) {
        if (props != null) {
            connection.setProps(props);
        } else {
            connection.setProps(new Properties());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getConnectionFile() {
        return connectionFile;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  connectionFile  DOCUMENT ME!
     */
    public void setConnectionFile(final String connectionFile) {
        if (connectionFile != null) {
            this.connectionFile = connectionFile;
        } else {
            this.connectionFile = "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getPreviewMaxRows() {
        return previewMaxRows;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  previewMaxRows  DOCUMENT ME!
     */
    public void setPreviewMaxRows(final int previewMaxRows) {
        if (previewMaxRows > -1) {
            this.previewMaxRows = previewMaxRows;
        } else {
            this.previewMaxRows = 0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the labelCase
     */
    public String getLabelCase() {
        return (labelCase != null) ? labelCase : "";
    }

    /**
     * DOCUMENT ME!
     *
     * @param  labelCase  the labelCase to set
     */
    public void setLabelCase(final String labelCase) {
        if (labelCase != null) {
            this.labelCase = labelCase;
        } else {
            this.labelCase = "";
        }
    }
// </editor-fold>

    //~ Instance fields --------------------------------------------------------

    @XStreamAlias("Connection")
    private String connectionFile = "";
    private transient DatabaseConnection connection;
    @XStreamAlias("Statement")
    private String queryStatement = "";
    @XStreamAlias("PreviewMaxRows")
    private int previewMaxRows = 0;
    @XStreamAlias("LabelCase")
    private String labelCase = "";

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   other  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean weakEquals(final Query other) {
        if (other == null) {
            return false;
        } else if (other == this) {
            return true;
        } else {
            return getConnectionFile().equals(other.getConnectionFile())
                        && getQueryStatement().equals(other.getQueryStatement());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean labelsToLowerCase() {
        return (labelCase != null) ? labelCase.equalsIgnoreCase(LOWER_CASE) : false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean labelsToUpperCase() {
        return (labelCase != null) ? labelCase.equalsIgnoreCase(UPPER_CASE) : false;
    }
}
