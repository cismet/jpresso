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
import org.openide.loaders.MultiDataObject;
import org.openide.util.NbBundle;

import java.io.IOException;

import de.cismet.jpresso.core.data.ProjectOptions;

import de.cismet.jpresso.project.filetypes.JPFileLoader;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class OptionsDataLoader extends JPFileLoader<ProjectOptions> {

    //~ Static fields/initializers ---------------------------------------------

    public static final String REQUIRED_MIME = "text/x-jpresso-options";
    private static final long serialVersionUID = 1L;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new OptionsDataLoader object.
     */
    public OptionsDataLoader() {
        super("de.cismet.jpresso.project.filetypes.options.OptionsDataObject");
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected String defaultDisplayName() {
        return NbBundle.getMessage(OptionsDataLoader.class, "LBL_Options_loader_name");
    }

    @Override
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }

    @Override
    protected MultiDataObject createMultiObject(final FileObject primaryFile) throws DataObjectExistsException,
        IOException {
        return new OptionsDataObject(primaryFile, this, loadData(primaryFile));
    }

    @Override
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
}
