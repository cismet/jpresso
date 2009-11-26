/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.filetypes.sql;

import de.cismet.jpresso.project.gui.AbstractJPTopComponent;
import de.cismet.jpresso.project.gui.sql.SQLTopComponent;
import de.cismet.jpresso.core.data.SQLRun;
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
import org.openide.text.DataEditorSupport;

public final class SQLDataObject extends AbstractJPDataObject<SQLRun> implements RunCookie {

    public SQLDataObject(final FileObject pf, final SQLDataLoader loader, final SQLRun sql) throws DataObjectExistsException, IOException {
        super(pf, loader, sql);
        final CookieSet cookies = getCookieSet();
        cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));
    }

    @Override
    protected Node createNodeDelegate() {
        return new SQLDataNode(this, getLookup());
    }

    /**
     * 
     */
    public void startRun() {
        final Project p = FileOwnerQuery.getOwner(getPrimaryFile());
        if (p != null) {
            AntHandler.startSingleRun(p.getProjectDirectory().getFileObject("/" + JPressoFileManager.BUILD_XML), this);
        } else {
            //TODO
        }
    }

    @Override
    protected AbstractJPTopComponent<SQLDataObject> createTopComponent() {
        return new SQLTopComponent(this);
    }
}
