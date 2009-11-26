/*
 * WrongNameException.java
 *
 * Created on 22. September 2003, 11:02
 */
package de.cismet.jpresso.core.serviceprovider.exceptions;

import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.awt.Point;
import java.util.List;

/** 
 * Wird geworfen wenn beim Initialisieren was schief geht
 *
 * @author hell
 */
public class InitializingException extends JPressoException {

    private String initializeLog = "";
    private List<Point> mappingErrors = TypeSafeCollections.newArrayList();
    private List<Point> referenceErrors = TypeSafeCollections.newArrayList();

    /** Creates a new instance of InitializingException */
    public InitializingException() {
    }

    /** Creates a new instance of InitializingException
     * @param s Beschreibung der Exception
     */
    public InitializingException(String s, String log) {
        super(s);
        initializeLog = log;
    }

    public InitializingException(String s, String log, final List<Point> mapErr, final List<Point> refErr) {
        super(s);
        initializeLog = log;
        if (mapErr != null) {
            mappingErrors = mapErr;
        }
        if (refErr != null) {
            referenceErrors = refErr;
        }
    }

    public InitializingException(String s, String log, Throwable e) {
        super(s, e);
        initializeLog = log;
    }

    /** Getter for property initializeLog.
     * @return Value of property initializeLog.
     *
     */
    public java.lang.String getInitializeLog() {
        return initializeLog;
    }

    public List<Point> getMappingErrorFields() {
        return mappingErrors;
    }

    public List<Point> getReferenceErrorFields() {
        return referenceErrors;
    }
}
