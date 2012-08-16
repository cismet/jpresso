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
package de.cismet.jpresso.project.nodes;

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.nodes.NodeTransfer;
import org.openide.util.datatransfer.PasteType;

import java.awt.datatransfer.Transferable;

import java.io.IOException;

/**
 * A PastyType to deal with copy/paste of all JPresso datas.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class JPressoPasteType extends PasteType {

    //~ Instance fields --------------------------------------------------------

    private final DataObject data;
    private final int mode;
    private final FileObject childrenDir;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new JPressoPasteType object.
     *
     * @param  data         DOCUMENT ME!
     * @param  mode         DOCUMENT ME!
     * @param  childrenDir  DOCUMENT ME!
     */
    public JPressoPasteType(final DataObject data, final int mode, final FileObject childrenDir) {
        this.data = data;
        this.mode = mode;
        this.childrenDir = childrenDir;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Transferable paste() throws IOException {
        if (mode == NodeTransfer.COPY) {
            data.copy(DataFolder.findFolder(childrenDir));
        } else if (mode == NodeTransfer.MOVE) {
            data.move(DataFolder.findFolder(childrenDir));
        }
        return null;
    }
}
