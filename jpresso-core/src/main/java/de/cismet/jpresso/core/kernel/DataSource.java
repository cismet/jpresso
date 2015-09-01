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

/**
 * The interface that all data sources to be used in JPresso must implement.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface DataSource extends Iterable<String[]> {

    //~ Instance fields --------------------------------------------------------

    int NO_FETCH_SIZE = -712345;
    String FETCH_SIZE_PROPERTY = "fetchSize";

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the column count of the datasource
     */
    int getColumnCount();

    /**
     * DOCUMENT ME!
     *
     * @return  the column count of the datasource
     */
    int getRowCount();

    /**
     * DOCUMENT ME!
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  the column labels (names) of the datasource
     */
    String getColumnLabel(int index);

    /**
     * DOCUMENT ME!
     *
     * @return  datasource closed sucessfully?
     */
    boolean close();

    /**
     * DOCUMENT ME!
     *
     * @return  is datasource closed?
     */
    boolean isClosed();
}
