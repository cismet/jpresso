/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import de.cismet.jpresso.core.kernel.*;
import java.sql.Connection;

/**
 * The base class for all classes in the dynamic code folder. It contains the 
 * resources to share with all custom-code classes.
 * 
 * (Another reason: there must always be at least one class in package code, 
 * otherwise "import code.*" e.g. in Assigner would fail!)
 * 
 * @author srichter
 */
public abstract class AssignerBase {

    public static Connection FLD$TargetConnection;
    public static Connection FLD$SourceConnection;
    public static UniversalContainer FLD$UniversalContainer;
    static boolean FLD$Stopped = false;

    /**
     * 
     * @return
     */
    public boolean isStopped() {
        return FLD$Stopped;
    }

    /**
     * 
     */
    public static void stop() {
        FLD$Stopped = true;
    }
}
