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
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class InvalidFormatFileException extends Exception {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new InvalidFormatFileException object.
     */
    public InvalidFormatFileException() {
    }

    /**
     * Creates a new InvalidFormatFileException object.
     *
     * @param  message  DOCUMENT ME!
     */
    public InvalidFormatFileException(final String message) {
        super(message);
    }
    /**
     * Creates a new InvalidFormatFileException object.
     *
     * @param  e  DOCUMENT ME!
     */
    public InvalidFormatFileException(final Exception e) {
        super(e);
    }
}
