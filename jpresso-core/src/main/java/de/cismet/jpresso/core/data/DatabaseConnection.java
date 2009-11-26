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
@XStreamAlias("Connection")
public final class DatabaseConnection implements JPLoadable {

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    public DatabaseConnection(String driverClass, String url, Properties props) {
        this.driverClass = driverClass;
        this.url = url;
        this.props = props;
    }

    public DatabaseConnection() {
        props = new Properties();
    }
    // </editor-fold>
    //private transient DatabaseConnection connection; + getConnection baut verbindung auf?
    @XStreamAlias("DriverClass")
    private String driverClass = "";
    @XStreamAlias("URL")
    private String url = "";
    @XStreamAlias("ConnectionProperties")
    private Properties props;


    // <editor-fold defaultstate="collapsed" desc="Setters & Getters">
    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        if (driverClass != null) {
            this.driverClass = driverClass;
        } else {
            this.driverClass = "";
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (url != null) {
            this.url = url;
        } else {
            this.url = "";
        }
    }

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        if (props != null) {
            this.props = props;
        } else {
            this.props = new Properties();
        }
    }
// </editor-fold>

    public boolean weakEquals(final DatabaseConnection other) {
        if (other == null) {
            return false;
        } else if (other == this) {
            return true;
        } else {
            return getUrl().equals(other.getUrl());
        }
    }

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
