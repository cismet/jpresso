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
package de.cismet.jpresso.project.filetypes.options;

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.text.DataEditorSupport;

import java.io.IOException;

import de.cismet.jpresso.core.data.ProjectOptions;

import de.cismet.jpresso.project.filetypes.AbstractJPDataObject;
import de.cismet.jpresso.project.gui.AbstractJPTopComponent;
import de.cismet.jpresso.project.gui.options.OptionsTopComponent;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class OptionsDataObject extends AbstractJPDataObject<ProjectOptions> {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new OptionsDataObject object.
     *
     * @param   pf      DOCUMENT ME!
     * @param   loader  DOCUMENT ME!
     * @param   po      DOCUMENT ME!
     *
     * @throws  DataObjectExistsException  DOCUMENT ME!
     * @throws  IOException                DOCUMENT ME!
     */
    public OptionsDataObject(final FileObject pf, final OptionsDataLoader loader, final ProjectOptions po)
            throws DataObjectExistsException, IOException {
        super(pf, loader, po);
        final CookieSet cookies = getCookieSet();
        cookies.add((Node.Cookie)DataEditorSupport.create(this, getPrimaryEntry(), cookies));
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected Node createNodeDelegate() {
        return new OptionsDataNode(this, getLookup());
    }

    @Override
    protected AbstractJPTopComponent<OptionsDataObject> createTopComponent() {
        return new OptionsTopComponent(this);
    }
}
