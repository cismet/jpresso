/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.kernel;

import de.cismet.jpresso.core.serviceprovider.DynamicDriverManager;
import java.sql.Connection;

/**
 * An Interface implemented by the dynamically compiled assigners to use them 
 * without calls to java reflection.
 * 
 * @author srichter
 */
public interface Assigner {

    /**
     * 
     * @param tc
     * @param args
     * @param universalC
     * @return
     */
    public String[][] assign(Connection tc, String[] args, UniversalContainer universalC);

    /**
     * 
     * @return the target db connection
     */
    public void setDriverManager(DynamicDriverManager manager);

    public DynamicDriverManager getDriverManager();

    public Connection getTargetConnection();

    public void setTargetConnection(Connection targetConnection);

    public Connection getSourceConnection();

    public void setSourceConnection(Connection targetConnection);

    /**
     * provides an universal container to store an object and pass it from every 
     * call to "assign(Connection tc, String[] args, UniversalContainer universalC)"
     * to the next call to save information from one call to the next.
     * 
     * @return universal container for Object
     */
    public UniversalContainer getUniversalContainer();

    public void setUniversalContainer(UniversalContainer universalContainer);

    public boolean isStopped();
}
