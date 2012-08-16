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

import de.cismet.jpresso.core.serviceprovider.DynamicDriverManager;

/**
 * An Interface implemented by the dynamically compiled assigners to use them without calls to java reflection.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface Assigner {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   tc          DOCUMENT ME!
     * @param   args        DOCUMENT ME!
     * @param   universalC  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String[][] assign(Connection tc, String[] args, UniversalContainer universalC);

    /**
     * DOCUMENT ME!
     *
     * @param  manager  DOCUMENT ME!
     */
    void setDriverManager(DynamicDriverManager manager);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    DynamicDriverManager getDriverManager();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Connection getTargetConnection();

    /**
     * DOCUMENT ME!
     *
     * @param  targetConnection  DOCUMENT ME!
     */
    void setTargetConnection(Connection targetConnection);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Connection getSourceConnection();

    /**
     * DOCUMENT ME!
     *
     * @param  targetConnection  DOCUMENT ME!
     */
    void setSourceConnection(Connection targetConnection);

    /**
     * provides an universal container to store an object and pass it from every call to "assign(Connection tc, String[]
     * args, UniversalContainer universalC)" to the next call to save information from one call to the next.
     *
     * @return  universal container for Object
     */
    UniversalContainer getUniversalContainer();

    /**
     * DOCUMENT ME!
     *
     * @param  universalContainer  DOCUMENT ME!
     */
    void setUniversalContainer(UniversalContainer universalContainer);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isStopped();
}
