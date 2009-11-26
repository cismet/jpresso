/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.dnd;

import de.cismet.jpresso.project.filetypes.connection.ConnectionDataObject;
import de.cismet.jpresso.project.filetypes.query.QueryDataObject;
import de.cismet.jpresso.project.filetypes.run.RunDataObject;
import de.cismet.jpresso.project.filetypes.sql.SQLDataObject;
import java.awt.datatransfer.DataFlavor;

/**
 * The DataFlavors for Copy/Paste. Drag/Drop in JPresso
 * 
 * @author srichter
 */
public class JPDataFlavors {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JPDataFlavors.class);
    //-----LogicalView D&D
    public static final DataFlavor CONNECTION_FLAVOR = new DataFlavor(ConnectionDataObject.class, "Connection");
    public static final DataFlavor QUERY_FLAVOR = new DataFlavor(QueryDataObject.class, "Query");
    public static final DataFlavor RUN_FLAVOR = new DataFlavor(RunDataObject.class, "Run");
    public static final DataFlavor SQL_FLAVOR = new DataFlavor(SQLDataObject.class, "SQL");
    //-----Editor internal D&D
    public static final DataFlavor MAP_FLAVOR = new DataFlavor(Mapable.class, "Mapable");
    public static final DataFlavor REF_FLAVOR = new DataFlavor(Referenceable.class, "Referenceable");
    public static final DataFlavor NETBEANS_NODE_FLAVOR = initNetbeansNodeFlavor();

    private static final DataFlavor initNetbeansNodeFlavor() {
        try {
            return new DataFlavor("application/x-java-openide-nodednd; class=org.openide.nodes.Node");
        } catch (ClassNotFoundException ex) {
            log.warn("Can not find DataFlavor for Netbeans DataNodes -> Drag&Drop for DataNodes may not work everywhere!", ex);
            return new DataFlavor();
        }
    }
}
