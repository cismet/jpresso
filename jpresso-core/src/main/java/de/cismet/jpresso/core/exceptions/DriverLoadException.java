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
package de.cismet.jpresso.core.exceptions;

/**
 * DOCUMENT ME!
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public class DriverLoadException extends Exception {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DriverLoadException object.
     */
    public DriverLoadException() {
        super();
    }

    /**
     * Creates a new DriverLoadException object.
     *
     * @param  msg  DOCUMENT ME!
     */
    public DriverLoadException(final String msg) {
        super(msg);
    }

    /**
     * Creates a new DriverLoadException object.
     *
     * @param  ex  DOCUMENT ME!
     */
    public DriverLoadException(final Exception ex) {
        super(ex);
    }

    /**
     * Creates a new DriverLoadException object.
     *
     * @param  msg  DOCUMENT ME!
     * @param  ex   DOCUMENT ME!
     */
    public DriverLoadException(final String msg, final Exception ex) {
        super(msg, ex);
    }
}
