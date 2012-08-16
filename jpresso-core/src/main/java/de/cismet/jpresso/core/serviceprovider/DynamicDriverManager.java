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
package de.cismet.jpresso.core.serviceprovider;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.List;
import java.util.Properties;

import de.cismet.jpresso.core.data.DriverDescription;
import de.cismet.jpresso.core.serviceprovider.exceptions.DuplicateEntryException;

/**
 * Interface for a dynamic jdbc driver manager.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface DynamicDriverManager {

    //~ Methods ----------------------------------------------------------------

    /**
     * Creates a connection as described in the parameters.
     *
     * @param   driverDescriptionAlias  DOCUMENT ME!
     * @param   url                     DOCUMENT ME!
     * @param   props                   DOCUMENT ME!
     *
     * @return  the established jdbc connection
     *
     * @throws  SQLException            DOCUMENT ME!
     * @throws  ClassNotFoundException  DOCUMENT ME!
     */
    Connection getConnection(final String driverDescriptionAlias, final String url, final Properties props)
            throws SQLException, ClassNotFoundException;

    /**
     * DOCUMENT ME!
     *
     * @return  list of all driver descriptions known by the manager
     */
    List<DriverDescription> getDriverDescriptionList();

    /**
     * DOCUMENT ME!
     *
     * @return  list of all valid driver descriptions known by the manager
     */
    List<DriverDescription> getValidDrivers();

    /**
     * Sets the known diverdescriptions of the manager.
     *
     * @param   driverDescriptionList  DOCUMENT ME!
     *
     * @throws  DuplicateEntryException  de.cismet.drivermanager.manager.DuplicateEntryException
     */
    void setDriverDescriptionList(final List<DriverDescription> driverDescriptionList) throws DuplicateEntryException;
}
