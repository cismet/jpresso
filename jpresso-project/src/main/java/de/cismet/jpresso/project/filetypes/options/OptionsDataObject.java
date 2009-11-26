/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.filetypes.options;

import de.cismet.jpresso.project.gui.AbstractJPTopComponent;
import de.cismet.jpresso.project.gui.options.OptionsTopComponent;
import de.cismet.jpresso.core.data.ProjectOptions;
import de.cismet.jpresso.project.filetypes.AbstractJPDataObject;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.text.DataEditorSupport;

public final class OptionsDataObject extends AbstractJPDataObject<ProjectOptions> {

    public OptionsDataObject(final FileObject pf, final OptionsDataLoader loader, ProjectOptions po) throws DataObjectExistsException, IOException {
        super(pf, loader, po);
        CookieSet cookies = getCookieSet();
        cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));
    }

    @Override
    protected Node createNodeDelegate() {
        return new OptionsDataNode(this, getLookup());
    }

    @Override
    protected AbstractJPTopComponent<OptionsDataObject> createTopComponent() {
        return new OptionsTopComponent(this);
    }
}
