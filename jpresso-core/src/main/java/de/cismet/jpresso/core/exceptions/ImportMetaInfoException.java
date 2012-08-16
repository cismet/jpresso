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
package de.cismet.jpresso.core.exceptions;

import de.cismet.jpresso.core.kernel.*;
import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;

/**
 * Wird geworfen, wenn beim Anlegen der Metainformation ein Fehler auftritt.
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class ImportMetaInfoException extends JPressoException {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of ImportMetaInfoException.
     */
    public ImportMetaInfoException() {
    }

    /**
     * Creates a new instance of ImportMetaInfoException.
     *
     * @param  s  Beschreibung der Exception
     */
    public ImportMetaInfoException(final String s) {
        super(s);
    }

    /**
     * Creates a new instance of ImportMetaInfoException.
     *
     * @param  s      Beschreibung der Exception
     * @param  cause  DOCUMENT ME!
     */
    public ImportMetaInfoException(final String s, final Throwable cause) {
        super(s, cause);
    }
}
