/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.serviceprovider;

import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.core.serviceprovider.exceptions.DuplicateEntryException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * Interface for a dynamic jdbc driver manager
 * @author srichter
 */
public interface DynamicDriverManager {

    /**
     * Creates a connection as described in the parameters
     * 
     * @param driverDescriptionAlias
     * @param url
     * @param props
     * @return the established jdbc connection
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public Connection getConnection(final String driverDescriptionAlias, final String url, final Properties props) throws SQLException, ClassNotFoundException;

    /**
     * 
     * @return list of all driver descriptions known by the manager
     */
    public List<DriverDescription> getDriverDescriptionList();

    /**
     * 
     * @return list of all valid driver descriptions known by the manager
     */
    public List<DriverDescription> getValidDrivers();

    /**
     * Sets the known diverdescriptions of the manager
     * 
     * @param driverDescriptionList
     * @throws de.cismet.drivermanager.manager.DuplicateEntryException
     */
    public void setDriverDescriptionList(final List<DriverDescription> driverDescriptionList) throws DuplicateEntryException;
}
