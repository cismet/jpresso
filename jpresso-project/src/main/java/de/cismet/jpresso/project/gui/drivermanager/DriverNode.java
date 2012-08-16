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
package de.cismet.jpresso.project.gui.drivermanager;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A node that represents a single Driver in the DriverTreeModel.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
class DriverNode extends DefaultMutableTreeNode {

    //~ Instance fields --------------------------------------------------------

    private String driverClassName;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DriverNode object.
     *
     * @param  driverClassName  DOCUMENT ME!
     */
    public DriverNode(final String driverClassName) {
        super(driverClassName);
        this.driverClassName = driverClassName;
        setAllowsChildren(false);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getDriverClassName() {
        return driverClassName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  driverClassName  DOCUMENT ME!
     */
    public void setDriverClassName(final String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
