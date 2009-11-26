/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.filetypes.options;

import de.cismet.jpresso.core.data.ProjectOptions;
import de.cismet.jpresso.project.filetypes.JPFileLoader;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.util.NbBundle;

public class OptionsDataLoader extends JPFileLoader<ProjectOptions> {

    public static final String REQUIRED_MIME = "text/x-jpresso-options";
    private static final long serialVersionUID = 1L;

    public OptionsDataLoader() {
        super("de.cismet.jpresso.project.filetypes.options.OptionsDataObject");
    }

    @Override
    protected String defaultDisplayName() {
        return NbBundle.getMessage(OptionsDataLoader.class, "LBL_Options_loader_name");
    }

    @Override
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }

    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        return new OptionsDataObject(primaryFile, this, loadData(primaryFile));
    }

    @Override
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
}
