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

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * DOCUMENT ME!
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public class DriverTreeCellRenderer extends DefaultTreeCellRenderer {

    //~ Instance fields --------------------------------------------------------

    private final ImageIcon ICON_DRIVER;
    private final ImageIcon ICON_FOLDER;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DriverTreeCellRenderer object.
     */
    public DriverTreeCellRenderer() {
        ICON_DRIVER = new ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/driver.png"));
        ICON_FOLDER = new ImageIcon(getClass().getResource("/de/cismet/jpresso/project/res/folder.png"));
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Component getTreeCellRendererComponent(final JTree tree,
            final Object value,
            final boolean selected,
            final boolean expanded,
            final boolean leaf,
            final int row,
            final boolean hasFocus) {
        final Component ret = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        if (value instanceof DriverNode) {
            setIcon(ICON_DRIVER);
            if ((ret != null) && (ret instanceof JLabel)
                        && !((JarNode)((DriverNode)value).getParent()).getJarPath().isFile()) {
                ret.setForeground(Color.red);
            }
        } else if (value instanceof JarNode) {
            setIcon(ICON_FOLDER);
            if ((ret instanceof JLabel) && !((JarNode)value).getJarPath().isFile()) {
                ret.setForeground(Color.red);
            }
        }
        return ret;
    }
}
