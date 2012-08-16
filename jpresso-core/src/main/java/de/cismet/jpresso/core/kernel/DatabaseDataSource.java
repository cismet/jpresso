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
package de.cismet.jpresso.core.kernel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Formatter;
import java.util.Iterator;

/**
 * DataSource implementation for JDBC.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class DatabaseDataSource implements DataSource {

    //~ Static fields/initializers ---------------------------------------------

    private static final String COUNT_STMNT = "select count(*) as rcount from (%1$2s) as t";
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DatabaseDataSource.class);

    //~ Instance fields --------------------------------------------------------

    private final Connection con;
    private ResultSet cachedResultSet;
    private ResultSetMetaData metaData;
    private Exception internalEx;
    private int cachedRowCount = -1;
    private int fetchSize = NO_FETCH_SIZE;
    private final String query;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DatabaseDataSource object.
     *
     * @param   con        DOCUMENT ME!
     * @param   query      DOCUMENT ME!
     * @param   fetchSize  DOCUMENT ME!
     *
     * @throws  SQLException          DOCUMENT ME!
     * @throws  NullPointerException  DOCUMENT ME!
     */
    public DatabaseDataSource(final Connection con, final String query, final int fetchSize) throws SQLException {
        if ((con == null) || (query == null)) {
            throw new NullPointerException();
        }
        this.query = query + "\n"; // append newline against "--" comments
        this.con = con;
        this.fetchSize = fetchSize;
        refreshResultSet();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public int getColumnCount() {
        try {
            return metaData.getColumnCount();
        } catch (SQLException ex) {
            log.error(ex, ex);
        }
        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   index  between 0 and getColumnCount()
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String getColumnLabel(final int index) {
        try {
            // on jdbc, they start with 1...
            return metaData.getColumnLabel(index + 1);
        } catch (SQLException ex) {
            log.error(ex, ex);
        }
        return null;
    }

    @Override
    public Iterator<String[]> iterator() {
        // this order is important, as getRowCount affects cachedResultSet!
        final int rowCount = getRowCount();
        if (cachedResultSet == null) {
            refreshResultSet();
        }
        final Iterator<String[]> res = new ResultSetIterator(cachedResultSet, rowCount);
        cachedResultSet = null;
        return res;
    }

    @Override
    public boolean close() {
        try {
            con.close();
            return true;
        } catch (SQLException ex) {
            log.error(ex, ex);
        }
        return false;
    }

    @Override
    public int getRowCount() {
        if (cachedRowCount < 0) {
            final Formatter formatter = new Formatter(new StringBuilder());
            final String countQuery = formatter.format(COUNT_STMNT, query).toString();
            if (log.isDebugEnabled()) {
                log.debug("Count Query: " + countQuery);
            }
            ResultSet countRS = null;
            try {
                countRS = createStatement().executeQuery(countQuery);
                if (countRS.next()) {
                    cachedRowCount = countRS.getInt(1);
                }
            } catch (SQLException ex) {
                log.warn("Smart rowcount detection failed. Query was: " + countQuery, ex);
            }
            if (cachedRowCount < 0) {
                try {
                    if (!con.getAutoCommit()) {
                        // known bug: org.postgresql.util.PSQLException: FEHLER: Portal C_3 existiert nicht
                        con.rollback();
                    }
                } catch (Exception ex) {
                    if (log.isDebugEnabled()) {
                        log.debug(ex);
                    }
                }

                cachedRowCount = getRowCountFailSafte();
            }
            closeResultSetAndStatementSilently(countRS);
        }
        return cachedRowCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Statement createStatement() {
        Statement stmnt = null;
        try {
            stmnt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
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

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int getRowCountFailSafte() {
        if (cachedResultSet == null) {
            refreshResultSet();
        }
        int counter = 0;
        try {
            while (cachedResultSet.next()) {
                ++counter;
            }
            closeResultSetAndStatementSilently(cachedResultSet);
            cachedResultSet = null;
            return counter;
        } catch (Exception ex) {
            log.error(ex, ex);
        }
        closeResultSetAndStatementSilently(cachedResultSet);
        cachedResultSet = null;
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  rs  DOCUMENT ME!
     */
    private static void closeResultSetAndStatementSilently(final ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                rs.getStatement().close();
            } catch (Exception ex) {
                if (log.isDebugEnabled()) {
                    log.debug("Error on closing resultset and statement!", ex);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void refreshResultSet() {
        try {
            final Statement stmnt = createStatement();
            if (fetchSize != NO_FETCH_SIZE) {
                try {
                    // postgres fix
                    con.setAutoCommit(false);
                    stmnt.setFetchSize(fetchSize);
                    if (log.isDebugEnabled()) {
                        log.debug("Setting connection fetch size = " + fetchSize);
                    }
                } catch (Exception ex) {
                    if (log.isDebugEnabled()) {
                        log.debug("Error on setting fetch size!", ex);
                    }
                }
            }
            closeResultSetAndStatementSilently(cachedResultSet);
            cachedResultSet = stmnt.executeQuery(query);
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

    // some jalopy issues in this section

    //J-
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
                } else {
                    closeResultSetAndStatementSilently(rs);
                }
            } catch (Exception ex) {
                log.error(ex, ex);
                throw new IllegalStateException("Could not fetch next row (" + currentPosition + " of " + rowCount + ")!", ex);
            }
            throw new IllegalStateException("Could not fetch next row (" + currentPosition + " of " + rowCount + ")!");
        }

        @Override
        public void remove() {
            //not supported
        }

        private String[] retrieveCurrent() throws SQLException {
            final String[] ret = new String[columnCount];
            for (int i = 0; i < columnCount; ++i) {
                ret[i] = rs.getString(i + 1);
            }
            return ret;
        }
    }
    //J+
}
