/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.filetypes.run;

import de.cismet.jpresso.core.data.JPressoRun;
import de.cismet.jpresso.project.filetypes.JPFileLoader;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.util.NbBundle;

public class RunDataLoader extends JPFileLoader<JPressoRun> {

    public static final String REQUIRED_MIME = "text/x-jpresso-run";
    private static final long serialVersionUID = 1L;

    public RunDataLoader() {
        super("de.cismet.jpresso.project.filetypes.run.RunDataObject");
    }

    @Override
    protected String defaultDisplayName() {
        return NbBundle.getMessage(RunDataLoader.class, "LBL_Run_loader_name");
    }

    @Override
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }

    protected MultiDataObject createMultiObject(FileObject pf) throws DataObjectExistsException, IOException {
        return new RunDataObject(pf, this, loadData(pf));
    }

    @Override
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
}
