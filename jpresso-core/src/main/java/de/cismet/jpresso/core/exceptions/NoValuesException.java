/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * NoValuesException.java
 *
 * Created on 22. September 2003, 10:59
 */
package de.cismet.jpresso.core.exceptions;

import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;

/**
 * Wir geworfen wenn kein Wert gefunden wurde.
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class NoValuesException extends JPressoException {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of NoValuesException.
     */
    public NoValuesException() {
    }

    /**
     * Creates a new instance of NoValuesException.
     *
     * @param  s  Beschreibung der Exception
     */
    public NoValuesException(final String s) {
        super(s);
    }
}
