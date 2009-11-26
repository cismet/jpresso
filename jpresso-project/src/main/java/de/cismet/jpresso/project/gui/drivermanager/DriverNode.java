/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.drivermanager;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A node that represents a single Driver in the DriverTreeModel. 
 * 
 * @author srichter
 */
class DriverNode extends DefaultMutableTreeNode {

    private String driverClassName;

    public DriverNode(String driverClassName) {
        super(driverClassName);
        this.driverClassName = driverClassName;
        setAllowsChildren(false);
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
    }
