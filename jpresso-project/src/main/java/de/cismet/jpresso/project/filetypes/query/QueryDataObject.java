/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.filetypes.query;

import de.cismet.jpresso.project.gui.AbstractJPTopComponent;
import de.cismet.jpresso.project.gui.query.QueryTopComponent;
import de.cismet.jpresso.core.data.DatabaseConnection;
import de.cismet.jpresso.core.data.Query;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.project.filetypes.AbstractJPDataObject;
import de.cismet.jpresso.project.filetypes.connection.ConnectionDataObject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.text.DataEditorSupport;

public final class QueryDataObject extends AbstractJPDataObject<Query> implements PropertyChangeListener {

    private ConnectionDataObject conData;

    public QueryDataObject(final FileObject pf, final QueryDataLoader loader, final Query query) throws DataObjectExistsException, IOException {
        super(pf, loader, query);
        //das entsprechende dataobject für die connection finden..wenn vorhanden ;)
        //es setzen, hierbei wird man sein listener
        setConData(findConnectionDataDependence());
        //die eigene Connection mit dem dataobject abgleichen...
        if (conData != null) {
            getData().setConnection(conData.getData());
        } else {
            getData().setConnection(new DatabaseConnection());
        }
        //TODO bei exception abfangen und durchgestrichenes symbol oder sowas anzeigen...
        final CookieSet cookies = getCookieSet();
        cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));
    }

    @Override
    protected Node createNodeDelegate() {
        return new QueryDataNode(this, getLookup());
    }

    //setzt ein connectiondataobject, bei dem man sich als listener registriert.  mögliche alte registrierung wird gelöscht
    public void setConData(ConnectionDataObject conData) {
        if (conData == this.conData) {
            //wenn wir das objekt schon referenzieren gibts nix zu tun sind..nix tun
            return;
        }
        if (this.conData != null) {
            this.conData.removePropertyChangeListener(this);
        }
        if (conData != null && conData.getData() != null) {
            getData().setConnection(conData.getData());
        } else {
            getData().setConnection(new DatabaseConnection());
        }
        if (conData != null) {
            conData.addPropertyChangeListener(this);
        }
        this.conData = conData;
    }

    private ConnectionDataObject findConnectionDataDependence() throws DataObjectNotFoundException {
        ConnectionDataObject ret = null;
        final FileObject primFile = getPrimaryFile();
        if (primFile != null && primFile.getParent() != null && primFile.getParent().getParent() != null) {
            final FileObject con = getPrimaryFile().getParent().getParent().getFileObject("/" + JPressoFileManager.DIR_CON + "/" + getData().getConnectionFile());
            if (con != null) {
                final DataObject dob = DataObject.find(con);
                if (dob instanceof ConnectionDataObject) {
                    ret = (ConnectionDataObject) dob;
                }
            }
        }
        return ret;
    }

    @Override
    protected AbstractJPTopComponent<QueryDataObject> createTopComponent() {
        return new QueryTopComponent(this);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ConnectionDataObject.class.getCanonicalName())) {
            getData().setConnection(conData.getData());
            notifyChangeListeners();
        }
    }
}
