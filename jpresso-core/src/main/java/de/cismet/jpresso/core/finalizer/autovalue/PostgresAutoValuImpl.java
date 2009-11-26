/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.finalizer.autovalue;

import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author srichter
 */
public class PostgresAutoValuImpl implements AutoValueStrategy {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    public final String SEQUENCE_FIND_QUERY = "SELECT max(adsrc) FROM pg_attrdef WHERE adrelid = (SELECT max(oid) FROM pg_class WHERE relname ilike ?)";
    public final String SEQUENCE_FIND_QUERY_TODO = "SELECT n.nspname, c.relname, a.attname, def.adsrc FROM pg_catalog.pg_namespace n JOIN pg_catalog.pg_class c ON ( c.relnamespace = n.oid ) JOIN pg_catalog.pg_attribute a ON ( a.attrelid= c.oid ) JOIN pg_catalog.pg_type t ON ( a.atttypid = t.oid ) LEFT JOIN pg_catalog.pg_attrdef def ON ( a.attrelid = def.adrelid AND a.attnum = def.adnum ) LEFT JOIN pg_catalog.pg_description dsc ON ( c.oid = dsc.objoid AND a.attnum = dsc.objsubid ) LEFT JOIN pg_catalog.pg_class dc ON ( dc.oid = dsc.classoid AND dc.relname= 'pg_class' ) LEFT JOIN pg_catalog.pg_namespace dn ON ( dc.relnamespace= dn.oid  AND dn.nspname = 'pg_catalog' ) WHERE a.attnum > 0 AND NOT a.attisdropped AND n.nspname LIKE 'public' AND c.relname LIKE ? AND a.attname LIKE '%' AND def.adsrc IS NOT NULL";
    public final String DRIVER_CLASS = "PostgreSQL";
    public final Map<String, PreparedStatement> autoValStatements;
    public PreparedStatement findNextValStatementForTable = null;
    public Connection connection = null;
    public PreparedStatement currentNextVal = null;
    private boolean configured = false;

    public PostgresAutoValuImpl() {
        autoValStatements = TypeSafeCollections.newHashMap();
    }

    public String nextValue() throws SQLException {
        if (configured && currentNextVal != null) {
            final ResultSet rs = currentNextVal.executeQuery();
            if (rs.next()) {
                final String ret = rs.getString(1);
                rs.close();
                return ret;
            } else {
                throw new IllegalStateException();
            }
        } else {
            throw new IllegalStateException(getClass() + " is not configured properly! Check if configure(...) was called before usage!");
        }
    }

    public void setCurrentFields(String tableName, List<String> fieldNames) throws SQLException {
        if (configured) {
            currentNextVal = autoValStatements.get(tableName);
            if (currentNextVal == null) {
                findNextValStatementForTable.setString(1, tableName);
                final ResultSet rs = findNextValStatementForTable.executeQuery();
                if (rs.next()) {
                    currentNextVal = connection.prepareStatement("SELECT " + rs.getString(1));
                    autoValStatements.put(tableName, currentNextVal);
                }
                rs.close();
            }
        }
    }

    public void finish(boolean rollback) {
        if (configured) {
            for (final PreparedStatement ps : autoValStatements.values()) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    log.warn(ex, ex);
                }
                try {
                    findNextValStatementForTable.close();
                } catch (SQLException ex) {
                    log.warn(ex, ex);
                }
            }
            autoValStatements.clear();
        //TODO: maybe reset sequences on rollback?
        }
    }

    public String getSupportedDriver() {
        return DRIVER_CLASS;
    }

    public void configure(Connection con, String config) {
        if (con == null) {
            throw new NullPointerException("Connection is null!");
        }
        try {
            if (!con.getMetaData().getDatabaseProductName().equalsIgnoreCase(DRIVER_CLASS)) {
                throw new IllegalStateException(getClass() + " does not support driver " + con.getMetaData().getDriverName() + "!\nSupported driver is " + DRIVER_CLASS + ".");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
        this.connection = con;
        try {
            this.findNextValStatementForTable = con.prepareStatement(SEQUENCE_FIND_QUERY);
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
        configured = true;
    }
}
