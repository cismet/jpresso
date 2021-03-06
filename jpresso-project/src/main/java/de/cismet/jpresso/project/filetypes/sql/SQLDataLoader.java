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

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.util.NbBundle;

import java.io.IOException;

import de.cismet.jpresso.core.data.SQLRun;

import de.cismet.jpresso.project.filetypes.JPFileLoader;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class SQLDataLoader extends JPFileLoader<SQLRun> {

    //~ Static fields/initializers ---------------------------------------------

    public static final String REQUIRED_MIME = "text/x-jpresso-rqs";
    private static final long serialVersionUID = 1L;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SQLDataLoader object.
     */
    public SQLDataLoader() {
        super("de.cismet.jpresso.project.filetypes.sql.SQLDataObject");
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected String defaultDisplayName() {
        return NbBundle.getMessage(SQLDataLoader.class, "LBL_SQL_loader_name");
    }

    @Override
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }

    @Override
    protected MultiDataObject createMultiObject(final FileObject primaryFile) throws DataObjectExistsException,
        IOException {
        return new SQLDataObject(primaryFile, this, loadData(primaryFile));
    }

    @Override
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
}
