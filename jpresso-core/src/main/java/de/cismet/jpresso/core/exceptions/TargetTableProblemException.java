/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * TargetTableProblemException.java
 *
 * Created on 26. September 2003, 11:18
 */
package de.cismet.jpresso.core.exceptions;

import de.cismet.jpresso.core.kernel.*;
import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;

/**
 * Wird geworfen wenn im Zielsystem eine Tabelle nicht uebereinstimmt.
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class TargetTableProblemException extends JPressoException {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of TargetTableProblemException.
     *
     * @param  s  Beschreibung der Exception
     */
    public TargetTableProblemException(final String s) {
        super(s);
    }
}
