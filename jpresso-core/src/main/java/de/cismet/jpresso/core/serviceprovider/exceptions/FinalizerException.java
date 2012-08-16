/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * WrongNameException.java
 *
 * Created on 22. September 2003, 11:02
 */
package de.cismet.jpresso.core.serviceprovider.exceptions;

import de.cismet.jpresso.core.kernel.*;

/**
 * Wird geworfen wenn beim "finalizen" was schief geht, viele reflection fehler.
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class FinalizerException extends JPressoException {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of FinalizerException.
     */
    public FinalizerException() {
    }

    /**
     * Creates a new instance of FinalizerException.
     *
     * @param  s  Beschreibung der Exception
     */
    public FinalizerException(final String s) {
        super(s);
    }

    /**
     * Creates a new FinalizerException object.
     *
     * @param  s  DOCUMENT ME!
     * @param  e  DOCUMENT ME!
     */
    public FinalizerException(final String s, final Throwable e) {
        super(s, e);
    }
}
