/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.drivermanager;

import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A class thet represents a single jar in the DriverTreeModel. 
 * 
 * @author srichter
 */
class JarNode extends DefaultMutableTreeNode {

    private File jarPath;

    public JarNode(File jarPath) {
        super(jarPath);
        this.jarPath = jarPath;
    }

    public File getJarPath() {
        return jarPath;
    }

    public void setJarPath(File jarPath) {
        this.jarPath = jarPath;
    }

    /**
     * Lists all files in the jar represented by this node.
     * 
     * @return filenames in jar
     */
    public String[] getJarFileEntries() {
        final ArrayList<String> jarEntries = TypeSafeCollections.newArrayList();
        if (getJarPath() != null) {
            try {
                final ZipFile zf = new ZipFile(getJarPath());
                final Enumeration e = zf.entries();
                while (e.hasMoreElements()) {
                    final ZipEntry ze = (ZipEntry) e.nextElement();
                    if (!ze.isDirectory()) {
                        jarEntries.add(ze.getName());
                    }
                }
                zf.close();
            } catch (IOException ex) {
            }
        }
        return jarEntries.toArray(new String[jarEntries.size()]);
    }
}
