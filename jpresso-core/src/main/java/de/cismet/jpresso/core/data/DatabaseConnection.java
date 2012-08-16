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
@XStreamAlias("Connection")
public final class DatabaseConnection implements JPLoadable {

    //~ Constructors -----------------------------------------------------------

    /**
     * <editor-fold defaultstate="collapsed" desc="Constructors">.
     *
     * @param  driverClass  DOCUMENT ME!
     * @param  url          DOCUMENT ME!
     * @param  props        DOCUMENT ME!
     */
    public DatabaseConnection(final String driverClass, final String url, final Properties props) {
        this.driverClass = driverClass;
        this.url = url;
        this.props = props;
    }

    /**
     * Creates a new DatabaseConnection object.
     */
    public DatabaseConnection() {
        props = new Properties();
    }
    // </editor-fold>

    /**
     * <editor-fold defaultstate="collapsed" desc="Setters & Getters">.
     *
     * @return  DOCUMENT ME!
     */
    public String getDriverClass() {
        return driverClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  driverClass  DOCUMENT ME!
     */
    public void setDriverClass(final String driverClass) {
        if (driverClass != null) {
            this.driverClass = driverClass;
        } else {
            this.driverClass = "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getUrl() {
        return url;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  url  DOCUMENT ME!
     */
    public void setUrl(final String url) {
        if (url != null) {
            this.url = url;
        } else {
            this.url = "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Properties getProps() {
        return props;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  props  DOCUMENT ME!
     */
    public void setProps(final Properties props) {
        if (props != null) {
            this.props = props;
        } else {
            this.props = new Properties();
        }
    }
// </editor-fold>

    //~ Instance fields --------------------------------------------------------

    // private transient DatabaseConnection connection; + getConnection baut verbindung auf?
    @XStreamAlias("DriverClass")
    private String driverClass = "";
    @XStreamAlias("URL")
    private String url = "";
    @XStreamAlias("ConnectionProperties")
    private Properties props;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   other  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean weakEquals(final DatabaseConnection other) {
        if (other == null) {
            return false;
        } else if (other == this) {
            return true;
        } else {
            return getUrl().equals(other.getUrl());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   other  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean deepEquals(final DatabaseConnection other) {
        if (other == null) {
            return false;
        } else if (other == this) {
            return true;
        } else {
            boolean equalProps = true;
            if (getProps() != null) {
                for (final Object key : getProps().keySet()) {
                    equalProps &= getProps().get(key).equals(other.getProps().get(key));
                }
                equalProps &= getProps().keySet().containsAll(other.getProps().keySet());
            } else if (other.getProps() == null) {
                equalProps = true;
            }
            return equalProps && getUrl().equals(other.getUrl()) && getDriverClass().equals(other.getDriverClass());
        }
    }
}
