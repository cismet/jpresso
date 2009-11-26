/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.cismet.jpresso.core.serviceprovider.AntExecutableInterface;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.util.List;

/**
 *
 * @author srichter
 */
@XStreamAlias("SQLRun")
public class SQLRun implements AntExecutableInterface, JPLoadable {

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    public SQLRun(String connectionFile, List<String> script) {
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

    public SQLRun() {
        script = TypeSafeCollections.newArrayList();
    }
    // </editor-fold>
    @XStreamAlias("Connection")
    private String connectionFile = "";
    @XStreamAlias("Statements")
    private List<String> script;
    private transient DatabaseConnection connection;

    // <editor-fold defaultstate="collapsed" desc="Setters & Getters">
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

    public List<String> getScript() {
        return script;
    }

    public void setScript(List<String> script) {
        if (script != null) {
            this.script = script;
        } else {
            this.script = TypeSafeCollections.newArrayList();
        }
    }

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
// </editor-fold>
}
