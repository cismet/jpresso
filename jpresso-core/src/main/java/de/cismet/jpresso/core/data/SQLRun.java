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

import de.cismet.jpresso.core.serviceprovider.AntExecutableInterface;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
@XStreamAlias("SQLRun")
public class SQLRun implements AntExecutableInterface, JPLoadable {

    //~ Constructors -----------------------------------------------------------

    /**
     * <editor-fold defaultstate="collapsed" desc="Constructors">.
     *
     * @param  connectionFile  DOCUMENT ME!
     * @param  script          DOCUMENT ME!
     */
    public SQLRun(final String connectionFile, final List<String> script) {
        if (connectionFile != null) {
            this.connectionFile = connectionFile;
        } else {
            this.connectionFile = "";
        }
        if (script != null) {
            this.script = script;
        } else {
            this.script = TypeSafeCollections.newArrayList();
        }
    }

    /**
     * Creates a new SQLRun object.
     */
    public SQLRun() {
        script = TypeSafeCollections.newArrayList();
    }
    // </editor-fold>

    /**
     * <editor-fold defaultstate="collapsed" desc="Setters & Getters">.
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
    public List<String> getScript() {
        return script;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  script  DOCUMENT ME!
     */
    public void setScript(final List<String> script) {
        if (script != null) {
            this.script = script;
        } else {
            this.script = TypeSafeCollections.newArrayList();
        }
    }

    /**
     * DOCUMENT ME!
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
// </editor-fold>

    //~ Instance fields --------------------------------------------------------

    @XStreamAlias("Connection")
    private String connectionFile = "";
    @XStreamAlias("Statements")
    private List<String> script;
    private transient DatabaseConnection connection;
}
