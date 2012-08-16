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

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

import de.cismet.jpresso.core.data.DriverDescription;

/**
 * DOCUMENT ME!
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public class DriverListCellRenderer extends DefaultListCellRenderer {

    //~ Static fields/initializers ---------------------------------------------

    private static final ImageIcon ICON_VALID = new ImageIcon(DriverListCellRenderer.class.getResource(
                "/de/cismet/jpresso/project/res/ok.png"));
    private static final ImageIcon ICON_INVALID = new ImageIcon(DriverListCellRenderer.class.getResource(
                "/de/cismet/jpresso/project/res/invalid.png"));

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DriverListCellRenderer object.
     */
    public DriverListCellRenderer() {
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Component getListCellRendererComponent(final JList list,
            final Object value,
            final int index,
            final boolean isSelected,
            final boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        ImageIcon imageIcon = null;

        if (value instanceof DriverDescription) {
            if (((DriverDescription)value).isValid()) {
                imageIcon = ICON_VALID;
            } else {
                imageIcon = ICON_INVALID;
            }
        }

//        Image image = imageIcon.getImage();
//        final Dimension dimension = this.getPreferredSize();
//        final double height = dimension.getHeight();
//        final double width = (height / imageIcon.getIconHeight()) * imageIcon.getIconWidth();
//        image = image.getScaledInstance((int) width, (int) height, Image.SCALE_SMOOTH);
//        final ImageIcon finalIcon = new ImageIcon(image);
//        setIcon(finalIcon);
        setIcon(imageIcon);

        return this;
    }
}
