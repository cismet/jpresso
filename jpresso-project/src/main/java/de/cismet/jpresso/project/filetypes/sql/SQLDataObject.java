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
package de.cismet.jpresso.project.filetypes.sql;

import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.text.DataEditorSupport;

import java.io.IOException;

import de.cismet.jpresso.core.data.SQLRun;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;

import de.cismet.jpresso.project.filetypes.AbstractJPDataObject;
import de.cismet.jpresso.project.filetypes.AntHandler;
import de.cismet.jpresso.project.filetypes.cookies.RunCookie;
import de.cismet.jpresso.project.gui.AbstractJPTopComponent;
import de.cismet.jpresso.project.gui.sql.SQLTopComponent;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class SQLDataObject extends AbstractJPDataObject<SQLRun> implements RunCookie {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SQLDataObject object.
     *
     * @param   pf      DOCUMENT ME!
     * @param   loader  DOCUMENT ME!
     * @param   sql     DOCUMENT ME!
     *
     * @throws  DataObjectExistsException  DOCUMENT ME!
     * @throws  IOException                DOCUMENT ME!
     */
    public SQLDataObject(final FileObject pf, final SQLDataLoader loader, final SQLRun sql)
            throws DataObjectExistsException, IOException {
        super(pf, loader, sql);
        final CookieSet cookies = getCookieSet();
        cookies.add((Node.Cookie)DataEditorSupport.create(this, getPrimaryEntry(), cookies));
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected Node createNodeDelegate() {
        return new SQLDataNode(this, getLookup());
    }

    /**
     * DOCUMENT ME!
     */
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
    protected AbstractJPTopComponent<SQLDataObject> createTopComponent() {
        return new SQLTopComponent(this);
    }
}
