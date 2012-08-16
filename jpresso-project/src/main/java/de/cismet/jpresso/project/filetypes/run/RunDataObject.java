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
package de.cismet.jpresso.project.filetypes.run;

import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;

import java.io.IOException;

import de.cismet.jpresso.core.data.JPressoRun;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;

import de.cismet.jpresso.project.filetypes.AbstractJPDataObject;
import de.cismet.jpresso.project.filetypes.AntHandler;
import de.cismet.jpresso.project.filetypes.cookies.RunCookie;
import de.cismet.jpresso.project.gui.AbstractJPTopComponent;
import de.cismet.jpresso.project.gui.run.RunTopComponent;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class RunDataObject extends AbstractJPDataObject<JPressoRun> implements RunCookie {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new RunDataObject object.
     *
     * @param   pf      DOCUMENT ME!
     * @param   loader  DOCUMENT ME!
     * @param   run     DOCUMENT ME!
     *
     * @throws  DataObjectExistsException  DOCUMENT ME!
     * @throws  IOException                DOCUMENT ME!
     */
    public RunDataObject(final FileObject pf, final RunDataLoader loader, final JPressoRun run)
            throws DataObjectExistsException, IOException {
        super(pf, loader, run);
        final CookieSet cookies = getCookieSet();
        // cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));
        cookies.add(this);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected Node createNodeDelegate() {
        return new RunDataNode(this, getLookup());
    }

    @Override
    public void startRun() {
        final Project p = FileOwnerQuery.getOwner(getPrimaryFile());
        if (p != null) {
            AntHandler.startSingleRun(p.getProjectDirectory().getFileObject("/" + JPressoFileManager.BUILD_XML), this);
        } else {
            // TODO
        }
    }

    @Override
    protected AbstractJPTopComponent<RunDataObject> createTopComponent() {
        return new RunTopComponent(this);
    }
}
