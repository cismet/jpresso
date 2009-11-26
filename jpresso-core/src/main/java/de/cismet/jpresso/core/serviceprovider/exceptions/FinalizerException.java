/*
 * WrongNameException.java
 *
 * Created on 22. September 2003, 11:02
 */
package de.cismet.jpresso.core.serviceprovider.exceptions;

import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;
import de.cismet.jpresso.core.kernel.*;

/** Wird geworfen wenn beim "finalizen" was schief geht, viele reflection fehler
 *
 * @author hell
 */
public class FinalizerException extends JPressoException {

    /** Creates a new instance of FinalizerException */
    public FinalizerException() {
    }

    /** Creates a new instance of FinalizerException
     * @param s Beschreibung der Exception
     */
    public FinalizerException(String s) {
        super(s);
    }

    public FinalizerException(String s, Throwable e) {
        super(s, e);
    }
}
