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
package de.cismet.jpresso.project.filetypes.connection;

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.text.DataEditorSupport;

import java.io.IOException;

import de.cismet.jpresso.core.data.DatabaseConnection;

import de.cismet.jpresso.project.filetypes.AbstractJPDataObject;
import de.cismet.jpresso.project.gui.AbstractJPTopComponent;
import de.cismet.jpresso.project.gui.connection.ConnectionTopComponent;

/**
 * wichtig: wenn sichergestellt sein muss, dass connection mit dem editorinhalt uebereinstimmt:
 * topComponent.updateDataObject() aufrufen!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class ConnectionDataObject extends AbstractJPDataObject<DatabaseConnection> {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ConnectionDataObject object.
     *
     * @param   pf      DOCUMENT ME!
     * @param   loader  DOCUMENT ME!
     * @param   cd      DOCUMENT ME!
     *
     * @throws  DataObjectExistsException  DOCUMENT ME!
     * @throws  IOException                DOCUMENT ME!
     */
    public ConnectionDataObject(final FileObject pf, final ConnectionDataLoader loader, final DatabaseConnection cd)
            throws DataObjectExistsException, IOException {
        super(pf, loader, cd);
        // TODO bei exception abfangen und durchgestrichenes symbol oder sowas anzeigen...
        final CookieSet cookies = getCookieSet();
        cookies.add((Node.Cookie)DataEditorSupport.create(this, getPrimaryEntry(), cookies));
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected Node createNodeDelegate() {
        return new ConnectionDataNode(this, getLookup());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public FileObject getConnectionFile() {
        return getPrimaryFile();
    }

    @Override
    protected AbstractJPTopComponent<ConnectionDataObject> createTopComponent() {
        return new ConnectionTopComponent(this);
    }
}
