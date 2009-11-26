/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.options;

import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import java.awt.Component;
import java.io.File;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

/**
 *
 * @author stefan
 */
public class ClasspathListCellRenderer extends DefaultListCellRenderer {

    private static final ImageIcon ICON_JAR = new ImageIcon(ClasspathListCellRenderer.class.getResource("/de/cismet/jpresso/project/res/jar_16.png"));
    private static final ImageIcon ICON_DIR = new ImageIcon(ClasspathListCellRenderer.class.getResource("/de/cismet/jpresso/project/res/folder_16.png"));
    private static final ImageIcon ICON_ERR = new ImageIcon(ClasspathListCellRenderer.class.getResource("/de/cismet/jpresso/project/res/invalid.png"));

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof File) {
            File f = (File) value;
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

