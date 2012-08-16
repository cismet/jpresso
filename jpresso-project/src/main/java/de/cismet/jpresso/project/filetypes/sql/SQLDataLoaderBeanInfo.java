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

import org.openide.loaders.UniFileLoader;
import org.openide.util.Utilities;

import java.awt.Image;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.SimpleBeanInfo;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class SQLDataLoaderBeanInfo extends SimpleBeanInfo {

    //~ Methods ----------------------------------------------------------------

    @Override
    public BeanInfo[] getAdditionalBeanInfo() {
        try {
            return new BeanInfo[] { Introspector.getBeanInfo(UniFileLoader.class) };
        } catch (IntrospectionException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public Image getIcon(final int type) {
        return super.getIcon(type); // TODO add a custom icon here: Utilities.loadImage(..., true)
    }
}
