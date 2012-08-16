/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.jpresso.project.filetypes;

import java.util.HashMap;

import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public abstract class DefaultURLProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> drivers = TypeSafeCollections.newHashMap(75);

    static {
        drivers.put("COM.ibm.db2.jdbc.net.DB2Driver", "jdbc:db2://<HOST>:<PORT>/<DB>");

        drivers.put("Sirius.metajdbc.driver.CidsDriver", "jdbc:cidsDB:system://");

        drivers.put("COM.ibm.db2.jdbc.app.DB2Driver", "jdbc:db2:<DB>");

        drivers.put("sun.jdbc.odbc.JdbcOdbcDriver", "jdbc:odbc:<DB>");

        drivers.put("weblogic.jdbc.mssqlserver4.Driver", "jdbc:weblogic:mssqlserver4:<DB>@<HOST>:<PORT>");

        drivers.put("oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@<HOST>:<PORT>:<SID>");

        drivers.put("com.pointbase.jdbc.jdbcUniversalDriver", "jdbc:pointbase://<HOST>[:<PORT>]/<DB>");

        drivers.put("com.pointbase.jdbc.jdbcUniversalDriver", "jdbc:pointbase://embedded[:<PORT>]/<DB>");

        drivers.put("com.pointbase.jdbc.jdbcUniversalDriver", "jdbc:pointbase:<DB>");

        drivers.put("COM.cloudscape.core.JDBCDriver", "jdbc:cloudscape:<DB>");

        drivers.put("RmiJdbc.RJDriver", "jdbc:rmi://<HOST>:<PORT>/jdbc:cloudscape:<DB>");

        drivers.put("org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:<DB>");

        drivers.put(
            "org.apache.derby.jdbc.ClientDriver",
            "jdbc:derby://<HOST>[:<PORT>]/databaseName[;attr1=value1[;...]]");

        drivers.put("com.ibm.db2.jcc.DB2Driver", "jdbc:db2://<hostname>:<port>/<database>");

        drivers.put("org.firebirdsql.jdbc.FBDriver", "jdbc:firebirdsql:[//<HOST>[:<PORT>]/]<DB>");

        drivers.put("COM.FirstSQL.Dbcp.DbcpDriver", "jdbc:dbcp://<HOST>:<PORT>");

        drivers.put("COM.FirstSQL.Dbcp.DbcpDriver", "jdbc:dbcp://local");

        drivers.put("com.ddtek.jdbc.db2.DB2Driver", "jdbc:datadirect:db2://<HOST>:<PORT>[;databaseName=<DB>]");

        drivers.put("ids.sql.IDSDriver", "jdbc:ids://<HOST>:<PORT>/conn?dsn='<ODBC_DSN_NAME>'");

        drivers.put(
            "com.informix.jdbc.IfxDriver",
            "jdbc:informix-sqli://<HOST>:<PORT>/<DB>:INFORMIXSERVER=<SERVER_NAME>");

        drivers.put(
            "com.ddtek.jdbc.informix.InformixDriver",
            "jdbc:datadirect:informix://<HOST>:<PORT>;informixServer=<SERVER_NAME>;databaseName=<DB>");

        drivers.put("jdbc.idbDriver", "jdbc:idb:<DB>");

        drivers.put("org.enhydra.instantdb.jdbc.idbDriver", "jdbc:idb:<DB>");

        drivers.put("interbase.interclient.Driver", "jdbc:interbase://<HOST>/<DB>");

        drivers.put("org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://<HOST>[:<PORT>]");

        drivers.put("org.hsqldb.jdbcDriver", "jdbc:hsqldb:<DB>");

        drivers.put("org.hsqldb.jdbcDriver", "jdbc:hsqldb:http://<HOST>[:<PORT>]");

        drivers.put("org.hsqldb.jdbcDriver", "jdbc:hsqldb:.");

        drivers.put("hSql.hDriver", "jdbc:HypersonicSQL:<DB>");

        drivers.put("org.hsql.jdbcDriver", "jdbc:HypersonicSQL:<DB>");

        drivers.put("net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:sqlserver://<server>[:<PORT>][/<DATABASE>]");

        drivers.put("net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:sybase://<server>[:<PORT>][/<DATABASE>]");

        drivers.put("com.mckoi.JDBCDriver", "jdbc:mckoi://<HOST>[:<PORT>]");

        drivers.put("com.mckoi.JDBCDriver", "jdbc:mckoi:local://<DB>");

        drivers.put(
            "com.ddtek.jdbc.sqlserver.SQLServerDriver",
            "jdbc:datadirect:sqlserver://<HOST>:<PORT>[;databaseName=<DB>]");

        drivers.put("com.ashna.jturbo.driver.Driver", "jdbc:JTurbo://<HOST>:<PORT>/<DB>");

        drivers.put("com.inet.tds.TdsDriver", "jdbc:inetdae:<HOST>:<PORT>?database=<DB>");

        drivers.put(
            "com.microsoft.jdbc.sqlserver.SQLServerDriver",
            "jdbc:microsoft:sqlserver://<HOST>:<PORT>[;DatabaseName=<DB>]");

        drivers.put("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://<HOST>:<PORT>;databaseName=<DB>");

        drivers.put("com.mysql.jdbc.Driver", "jdbc:mysql://<HOST>:<PORT>/<DB>");

        drivers.put("org.gjt.mm.mysql.Driver", "jdbc:mysql://<HOST>:<PORT>/<DB>");

        drivers.put("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:oci8:@<SID>");

        drivers.put("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:oci:@<SID>");

        drivers.put("com.ddtek.jdbc.oracle.OracleDriver", "jdbc:datadirect:oracle://<HOST>:<PORT>;SID=<SID>");

        drivers.put("postgresql.Driver", "jdbc:postgresql://<HOST>:<PORT>/<DB>");

        drivers.put("org.postgresql.Driver", "jdbc:postgresql://<HOST>:<PORT>/<DB>");

        drivers.put("com.quadcap.jdbc.JdbcDriver", "jdbc:qed:<DB>");

        drivers.put("com.sybase.jdbc.SybDriver", "jdbc:sybase:Tds:<HOST>:<PORT>");

        drivers.put("com.sybase.jdbc2.jdbc.SybDriver", "jdbc:sybase:Tds:<HOST>:<PORT>");

        drivers.put("com.ddtek.jdbc.sybase.SybaseDriver", "jdbc:datadirect:sybase://<HOST>:<PORT>[;databaseName=<DB>]");

        drivers.put("com.sun.sql.jdbc.sqlserver.SQLServerDriver", "jdbc:sun:sqlserver://server_name[:portNumber]");

        drivers.put(
            "com.sun.sql.jdbc.db2.DB2Driver",
            "jdbc:sun:db2://server_name:portNumber;databaseName=DATABASENAME");

        drivers.put(
            "com.sun.sql.jdbc.oracle.OracleDriver",
            "jdbc:sun:oracle://server_name[:portNumber][;SID=DATABASENAME]");

        drivers.put("com.sun.sql.jdbc.sybase.SybaseDriver", "jdbc:sun:sybase://server_name[:portNumber]");
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Lookup the standart URL to a database with the given driver classname.
     *
     * @param   classname  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static final String getDefaultURL(final String classname) {
        final String ret = drivers.get(classname);
        if (classname != null) {
            return ret;
        } else {
            return "";
        }
    }
}
