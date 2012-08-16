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
 * Wird geworfen wenn wahrscheinlich ein falscher Name gewaehlt wurde. Meistens dann wenn kein Key in einer HasMap
 * gefunden wurde, er aber da sein muesste.<br>
 * muesste aber ;-)
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class WrongNameException extends JPressoException {

    //~ Instance fields --------------------------------------------------------

    private final String errorSource;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of WrongNameException.
     */
    public WrongNameException() {
        this.errorSource = "";
    }

    /**
     * Creates a new instance of WrongNameException.
     *
     * @param  s  Beschreibung der Exception
     */
    public WrongNameException(final String s) {
        super(s);
        this.errorSource = "";
    }

    /**
     * Creates a new instance of WrongNameException.
     *
     * @param  s       Beschreibung der Exception
     * @param  errSrc  DOCUMENT ME!
     */
    public WrongNameException(final String s, final String errSrc) {
        super(s);
        this.errorSource = errSrc;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getErrorSource() {
        return errorSource;
    }
}
