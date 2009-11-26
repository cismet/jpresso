/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.gui.options;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.spi.options.OptionsCategory;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

public final class JpressofiletypesOptionsCategory extends OptionsCategory {

    @Override
    public Icon getIcon() {
        return new ImageIcon(Utilities.loadImage("de/cismet/jpresso/project/res/lin_agt_wrench_big.png"));
    }

    public String getCategoryName() {
        return NbBundle.getMessage(JpressofiletypesOptionsCategory.class, "OptionsCategory_Name_Jpressofiletypes");
    }

    public String getTitle() {
        return NbBundle.getMessage(JpressofiletypesOptionsCategory.class, "OptionsCategory_Title_Jpressofiletypes");
    }

    public OptionsPanelController create() {
        return new JpressofiletypesOptionsPanelController();
    }
}
