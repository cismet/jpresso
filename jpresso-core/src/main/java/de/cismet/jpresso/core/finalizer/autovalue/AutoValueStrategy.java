/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.finalizer.autovalue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author srichter
 */
public interface AutoValueStrategy {

    /**
     * Generates an id for the selected field.
     *
     * @return generated id for the selected field.
     * @throws IllegalStateException if no field was selected
     */
    public String nextValue() throws SQLException;

    /**
     * Selects the field for which nextValue() will generate an ID.
     *
     * @param tableName
     * @param fieldName
     */
    public void setCurrentFields(String tableName, List<String> fieldNames) throws SQLException;

    /**
     * Configure the Strategy
     *
     * @param config
     */
    public void configure(Connection con, String config);

    /**
     * Call when finalization is done, to cleanup - when needed
     *
     * @param rollback indicates whether or not a rollback was executed
     */
    public void finish(boolean rollback);

    /**
     * Ask the strategy which driver classes it can deal with
     * 
     * @return the driver supported by this startegy
     */
    public String getSupportedDriver();
}
