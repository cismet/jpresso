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
package de.cismet.jpresso.core.finalizer.autovalue;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface AutoValueStrategy {

    //~ Methods ----------------------------------------------------------------

    /**
     * Generates an id for the selected field.
     *
     * @return  generated id for the selected field.
     *
     * @throws  SQLException  IllegalStateException if no field was selected
     */
    String nextValue() throws SQLException;

    /**
     * Selects the field for which nextValue() will generate an ID.
     *
     * @param   tableName   DOCUMENT ME!
     * @param   fieldNames  DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    void setCurrentFields(String tableName, List<String> fieldNames) throws SQLException;

    /**
     * Configure the Strategy.
     *
     * @param  con     DOCUMENT ME!
     * @param  config  DOCUMENT ME!
     */
    void configure(Connection con, String config);

    /**
     * Call when finalization is done, to cleanup - when needed.
     *
     * @param  rollback  indicates whether or not a rollback was executed
     */
    void finish(boolean rollback);

    /**
     * Ask the strategy which driver classes it can deal with.
     *
     * @return  the driver supported by this startegy
     */
    String getSupportedDriver();
}
