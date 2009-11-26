/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.drivermanager.loading;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * A helper class to deregister driver loaded by DynamicDriverClassLoaders.
 * 
 * @author stefan
 */
public final class Cleaner {

    public Cleaner() {
        deregisterAll();
    }

    /**
     * Deregisters all drivers loaded by the same classloader as class to avoid 
     * classloader memory leaks due to java.sql.DriverManagers references on the 
     * driver. This is needed, because the class that deregisters a driver must 
     * be loaded by the same classloader as the driver itself.
     */
    private static final void deregisterAll() {
        final ClassLoader loader = Cleaner.class.getClassLoader();
        final Enumeration<Driver> drv = DriverManager.getDrivers();
        Driver current = null;
        while (drv.hasMoreElements()) {
            current = drv.nextElement();
            if (current.getClass().getClassLoader() != null && current.getClass().getClassLoader().equals(loader)) {
                try {
                    DriverManager.deregisterDriver(current);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
