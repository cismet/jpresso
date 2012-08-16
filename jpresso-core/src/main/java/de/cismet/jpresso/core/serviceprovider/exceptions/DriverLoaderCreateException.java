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
package de.cismet.jpresso.core.serviceprovider.exceptions;

/**
 * DOCUMENT ME!
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public class DriverLoaderCreateException extends Exception {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DriverLoaderCreateException object.
     */
    public DriverLoaderCreateException() {
        super();
    }

    /**
     * Creates a new DriverLoaderCreateException object.
     *
     * @param  msg  DOCUMENT ME!
     */
    public DriverLoaderCreateException(final String msg) {
        super(msg);
    }

    /**
     * Creates a new DriverLoaderCreateException object.
     *
     * @param  ex  DOCUMENT ME!
     */
    public DriverLoaderCreateException(final Exception ex) {
        super(ex);
    }

    /**
     * Creates a new DriverLoaderCreateException object.
     *
     * @param  msg  DOCUMENT ME!
     * @param  ex   DOCUMENT ME!
     */
    public DriverLoaderCreateException(final String msg, final Exception ex) {
        super(msg, ex);
    }
}
