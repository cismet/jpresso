/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.kernel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Formatter;
import java.util.Iterator;

/**
 * DataSource implementation for JDBC
 *
 * @author srichter
 */
public final class DatabaseDataSource implements DataSource {

    private static final String COUNT_STMNT = "select count(*) as rcount from (%1$2s) as t";

    public DatabaseDataSource(final Connection con, final String query) throws SQLException {
        if (con == null || query == null) {
            throw new NullPointerException();
        }
        this.query = query + "\n"; //append newline against "--" comments
        this.con = con;
        this.cachedResultSet = con.createStatement().executeQuery(query);
        metaData = cachedResultSet.getMetaData();
    }
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final Connection con;
    private ResultSet cachedResultSet;
    private ResultSetMetaData metaData;
    private Exception internalEx;
    private int cachedRowCount = -1;
    private final String query;

    @Override
    public final int getColumnCount() {
        try {
            return metaData.getColumnCount();
        } catch (SQLException ex) {
            log.error(ex, ex);
        }
        return -1;
    }

    /**
     *
     * @param index, between 0 and getColumnCount()
     * @return
     */
    @Override
    public final String getColumnLabel(int index) {
        try {
            //on jdbc, they start with 1...
            return metaData.getColumnLabel(index + 1);
        } catch (SQLException ex) {
            log.error(ex, ex);
        }
        return null;
    }

    @Override
    public final Iterator<String[]> iterator() {
        if (cachedResultSet == null) {
            refreshResultSet();
        }
        final Iterator<String[]> res = new ResultSetIterator(cachedResultSet, getRowCount());
        cachedResultSet = null;
        return res;
    }

    @Override
    public final boolean close() {
        try {
            con.close();
            return true;
        } catch (SQLException ex) {
            log.error(ex, ex);
        }
        return false;
    }

    @Override
    public final int getRowCount() {
        if (cachedRowCount < 0) {
            final Formatter formatter = new Formatter(new StringBuilder());
            final String countQuery = formatter.format(COUNT_STMNT, query).toString();
            if (log.isDebugEnabled()) {
                log.debug("Count Query: " + countQuery);
            }
            try {
                final ResultSet countRS = createStatement().executeQuery(countQuery);
                if (countRS.next()) {
                    cachedRowCount = countRS.getInt(1);
                }
            } catch (SQLException ex) {
                log.warn("Smart rowcount detection failed. Query was: " + countQuery, ex);
            }
            cachedRowCount = getRowCountFailSafte();
        }
        return cachedRowCount;
    }

    private final Statement createStatement() {
        Statement stmnt = null;
        try {
            stmnt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException sqlE) {
            log.warn("Error creating statement with options", sqlE);
            try {
                stmnt = con.createStatement();
            } catch (SQLException ex) {
                log.error(ex, ex);
            }
        }
        return stmnt;
    }

    private final int getRowCountFailSafte() {
        if (cachedResultSet == null) {
            refreshResultSet();
        }
        int counter = 0;
        try {
            while (cachedResultSet.next()) {
                ++counter;
            }
            return counter;
        } catch (SQLException ex) {
            log.error(ex, ex);
        } finally {
            cachedResultSet = null;
        }
        return 0;
    }

    private final void refreshResultSet() {
        try {
            cachedResultSet = createStatement().executeQuery(query);
            metaData = cachedResultSet.getMetaData();
        } catch (SQLException ex) {
            cachedResultSet = null;
            internalEx = ex;
            log.error(ex, ex);
        }
    }

    @Override
    public boolean isClosed() {
        try {
            return con.isClosed();
        } catch (SQLException ex) {
            log.warn(ex, ex);
        }
        return true;
    }

    final class ResultSetIterator implements Iterator<String[]> {

        public ResultSetIterator(final ResultSet rs, int rowCount) {
            if (rs == null) {
                throw new IllegalStateException(internalEx);
            }
            this.rs = rs;
            this.rowCount = rowCount;
            this.columnCount = getColumnCount();
            this.currentPosition = 0;
        }
        private final ResultSet rs;
        private final int rowCount;
        private final int columnCount;
        private int currentPosition;

        @Override
        public final boolean hasNext() {
            return currentPosition < rowCount;
        }

        @Override
        public final String[] next() {
            try {
                if (rs.next()) {
                    ++currentPosition;
                    return retrieveCurrent();
                }
            } catch (Exception ex) {
                log.error(ex, ex);
            }
            throw new IllegalStateException("Could not fetch next row (" + currentPosition + " of " + rowCount + ")!");
        }

        @Override
        public void remove() {
            //not supported
        }

        private final String[] retrieveCurrent() {
            final String[] ret = new String[columnCount];
            for (int i = 0; i < columnCount; ++i) {
                try {
                    ret[i] = rs.getString(i + 1);
                } catch (Exception ex) {
                    log.error(ex, ex);
                    ret[i] = null;
                }
            }
            return ret;
        }
    }
}

