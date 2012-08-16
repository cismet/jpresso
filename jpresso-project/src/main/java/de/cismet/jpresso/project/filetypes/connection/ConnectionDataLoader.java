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
import org.openide.loaders.MultiDataObject;
import org.openide.util.NbBundle;

import java.io.IOException;

import de.cismet.jpresso.core.data.DatabaseConnection;

import de.cismet.jpresso.project.filetypes.JPFileLoader;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ConnectionDataLoader extends JPFileLoader<DatabaseConnection> {

    //~ Static fields/initializers ---------------------------------------------

    public static final String REQUIRED_MIME = "text/x-jpresso-connection";
    private static final long serialVersionUID = 1L;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ConnectionDataLoader object.
     */
    public ConnectionDataLoader() {
        super("de.cismet.jpresso.project.filetypes.connection.ConnectionDataObject");
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected String defaultDisplayName() {
        return NbBundle.getMessage(ConnectionDataLoader.class, "LBL_Connection_loader_name");
    }

    @Override
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }

    @Override
    protected MultiDataObject createMultiObject(final FileObject primaryFile) throws DataObjectExistsException,
        IOException {
        return new ConnectionDataObject(primaryFile, this, loadData(primaryFile));
    }

    @Override
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
}
