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

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.tree.DefaultMutableTreeNode;

import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * A class thet represents a single jar in the DriverTreeModel.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
class JarNode extends DefaultMutableTreeNode {

    //~ Instance fields --------------------------------------------------------

    private File jarPath;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new JarNode object.
     *
     * @param  jarPath  DOCUMENT ME!
     */
    public JarNode(final File jarPath) {
        super(jarPath);
        this.jarPath = jarPath;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public File getJarPath() {
        return jarPath;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  jarPath  DOCUMENT ME!
     */
    public void setJarPath(final File jarPath) {
        this.jarPath = jarPath;
    }

    /**
     * Lists all files in the jar represented by this node.
     *
     * @return  filenames in jar
     */
    public String[] getJarFileEntries() {
        final ArrayList<String> jarEntries = TypeSafeCollections.newArrayList();
        if (getJarPath() != null) {
            try {
                final ZipFile zf = new ZipFile(getJarPath());
                final Enumeration e = zf.entries();
                while (e.hasMoreElements()) {
                    final ZipEntry ze = (ZipEntry)e.nextElement();
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
