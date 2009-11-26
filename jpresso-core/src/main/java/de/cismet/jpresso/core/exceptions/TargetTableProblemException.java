/*
 * TargetTableProblemException.java
 *
 * Created on 26. September 2003, 11:18
 */
package de.cismet.jpresso.core.exceptions;

import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;
import de.cismet.jpresso.core.kernel.*;

/** Wird geworfen wenn im Zielsystem eine Tabelle nicht uebereinstimmt
 * @author hell
 */
public class TargetTableProblemException extends JPressoException {

    /** Creates a new instance of TargetTableProblemException
     * @param s Beschreibung der Exception
     */
    public TargetTableProblemException(String s) {
        super(s);
    }
}
