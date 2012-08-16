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
package de.cismet.jpresso.project.gui.options;

import java.awt.Component;

import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;

/**
 * DOCUMENT ME!
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public class ClasspathListCellRenderer extends DefaultListCellRenderer {

    //~ Static fields/initializers ---------------------------------------------

    private static final ImageIcon ICON_JAR = new ImageIcon(ClasspathListCellRenderer.class.getResource(
                "/de/cismet/jpresso/project/res/jar_16.png"));
    private static final ImageIcon ICON_DIR = new ImageIcon(ClasspathListCellRenderer.class.getResource(
                "/de/cismet/jpresso/project/res/folder_16.png"));
    private static final ImageIcon ICON_ERR = new ImageIcon(ClasspathListCellRenderer.class.getResource(
                "/de/cismet/jpresso/project/res/invalid.png"));

    //~ Methods ----------------------------------------------------------------

    @Override
    public Component getListCellRendererComponent(final JList list,
            final Object value,
            final int index,
            final boolean isSelected,
            final boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof File) {
            final File f = (File)value;
            if (f.isFile() && f.getAbsolutePath().endsWith("." + JPressoFileManager.END_JAR)) {
                setIcon(ICON_JAR);
            } else if (f.isDirectory()) {
                setIcon(ICON_DIR);
            } else {
                setIcon(ICON_ERR);
            }
        } else {
            setIcon(ICON_ERR);
        }
        return this;
    }
}
