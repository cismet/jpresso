/*
 * NoValuesException.java
 *
 * Created on 22. September 2003, 10:59
 */
package de.cismet.jpresso.core.exceptions;

import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;


/** Wir geworfen wenn kein Wert gefunden wurde
 * @author hell
 */
public class NoValuesException extends JPressoException {

    /** Creates a new instance of NoValuesException */
    public NoValuesException() {
    }

    /** Creates a new instance of NoValuesException
     * @param s Beschreibung der Exception
     */
    public NoValuesException(String s) {
        super(s);
    }
}
