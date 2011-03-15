/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.kernel;

/**
 * The interface that all data sources to be used in JPresso must
 * implement.
 *
 * @author srichter
 */
public interface DataSource extends Iterable<String[]> {

    public static final int NO_FETCH_SIZE = -712345;
    public static final String FETCH_SIZE_PROPERTY = "fetchSize";

    /**
     *
     * @return the column count of the datasource
     */
    public int getColumnCount();

    /**
     *
     * @return the column count of the datasource
     */
    public int getRowCount();

    /**
     *
     * @return the column labels (names) of the datasource
     */
    public String getColumnLabel(int index);

    /**
     *
     * @return datasource closed sucessfully?
     */
    public boolean close();

    /**
     *
     * @return is datasource closed?
     */
    public boolean isClosed();
}
