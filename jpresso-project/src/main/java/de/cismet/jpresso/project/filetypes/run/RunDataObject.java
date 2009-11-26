/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.filetypes.run;

import de.cismet.jpresso.project.gui.AbstractJPTopComponent;
import de.cismet.jpresso.project.gui.run.RunTopComponent;
import de.cismet.jpresso.core.data.JPressoRun;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.project.filetypes.AbstractJPDataObject;
import de.cismet.jpresso.project.filetypes.AntHandler;
import de.cismet.jpresso.project.filetypes.cookies.RunCookie;
import java.io.IOException;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;

public final class RunDataObject extends AbstractJPDataObject<JPressoRun> implements RunCookie {

    public RunDataObject(final FileObject pf, final RunDataLoader loader, JPressoRun run) throws DataObjectExistsException, IOException {
        super(pf, loader, run);
        final CookieSet cookies = getCookieSet();
        //cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));
        cookies.add(this);

    }

    @Override
    protected Node createNodeDelegate() {
        return new RunDataNode(this, getLookup());
    }

    public void startRun() {
        final Project p = FileOwnerQuery.getOwner(getPrimaryFile());
        if (p != null) {
            AntHandler.startSingleRun(p.getProjectDirectory().getFileObject("/" + JPressoFileManager.BUILD_XML), this);
        } else {
            //TODO
        }
    }

    @Override
    protected AbstractJPTopComponent<RunDataObject> createTopComponent() {
        return new RunTopComponent(this);
    }
}
