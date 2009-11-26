/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.filetypes.query;

import de.cismet.jpresso.core.data.Query;
import de.cismet.jpresso.project.filetypes.JPFileLoader;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.util.NbBundle;

public class QueryDataLoader extends JPFileLoader<Query> {

    public static final String REQUIRED_MIME = "text/x-jpresso-query";
    private static final long serialVersionUID = 1L;

    public QueryDataLoader() {
        super("de.cismet.jpresso.project.filetypes.query.QueryDataObject");
    }

    @Override
    protected String defaultDisplayName() {
        return NbBundle.getMessage(QueryDataLoader.class, "LBL_Query_loader_name");
    }

    @Override
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }

    protected MultiDataObject createMultiObject(FileObject pf) throws DataObjectExistsException, IOException {
        return new QueryDataObject(pf, this, loadData(pf));
    }

    @Override
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
}
