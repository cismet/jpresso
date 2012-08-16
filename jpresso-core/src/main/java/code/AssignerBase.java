/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import java.sql.Connection;

import de.cismet.jpresso.core.kernel.*;
import de.cismet.jpresso.core.serviceprovider.DynamicDriverManager;

/**
 * The base class for all classes in the dynamic code folder. It contains the resources to share with all custom-code
 * classes.
 *
 * <p>(Another reason: there must always be at least one class in package code, otherwise "import code.*" e.g. in
 * Assigner would fail!)</p>
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public abstract class AssignerBase {

    //~ Static fields/initializers ---------------------------------------------

    public static Connection FLD$TargetConnection;
    public static Connection FLD$SourceConnection;
    public static UniversalContainer FLD$UniversalContainer;
    public static DynamicDriverManager FLD$DriverManager;
    static boolean FLD$Stopped = false;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isStopped() {
        return FLD$Stopped;
    }

    /**
     * DOCUMENT ME!
     */
    public static void stop() {
        FLD$Stopped = true;
    }
}
