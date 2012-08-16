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
package de.cismet.jpresso.project.gui.options;

import org.netbeans.spi.options.OptionsCategory;
import org.netbeans.spi.options.OptionsPanelController;

import org.openide.util.NbBundle;
import org.openide.util.Utilities;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class JpressofiletypesOptionsCategory extends OptionsCategory {

    //~ Methods ----------------------------------------------------------------

    @Override
    public Icon getIcon() {
        return new ImageIcon(Utilities.loadImage("de/cismet/jpresso/project/res/lin_agt_wrench_big.png"));
    }

    @Override
    public String getCategoryName() {
        return NbBundle.getMessage(JpressofiletypesOptionsCategory.class, "OptionsCategory_Name_Jpressofiletypes");
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(JpressofiletypesOptionsCategory.class, "OptionsCategory_Title_Jpressofiletypes");
    }

    @Override
    public OptionsPanelController create() {
        return new JpressofiletypesOptionsPanelController();
    }
}
