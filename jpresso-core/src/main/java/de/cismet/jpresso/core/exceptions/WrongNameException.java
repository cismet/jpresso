/*
 * WrongNameException.java
 *
 * Created on 22. September 2003, 11:02
 */
package de.cismet.jpresso.core.exceptions;

import de.cismet.jpresso.core.serviceprovider.exceptions.JPressoException;
import de.cismet.jpresso.core.kernel.*;

/** Wird geworfen wenn wahrscheinlich ein falscher Name gewaehlt wurde. Meistens dann
 * wenn kein Key in einer HasMap gefunden wurde, er aber da sein muesste.
 * <br>
 * muesste aber ;-)
 * @author hell
 */
public class WrongNameException extends JPressoException {

    private final String errorSource;

    /** Creates a new instance of WrongNameException */
    public WrongNameException() {
        this.errorSource = "";
    }

    /** Creates a new instance of WrongNameException
     * @param s Beschreibung der Exception
     */
    public WrongNameException(String s) {
        super(s);
        this.errorSource = "";
    }

    /** Creates a new instance of WrongNameException
     * @param s Beschreibung der Exception
     */
    public WrongNameException(String s, String errSrc) {
        super(s);
        this.errorSource = errSrc;
    }

    public

    String getErrorSource() {
        return errorSource;
    }   
}
