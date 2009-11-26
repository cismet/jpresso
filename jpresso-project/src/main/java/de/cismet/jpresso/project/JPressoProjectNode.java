/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project;

import de.cismet.jpresso.project.nodes.actions.ExportProjectAction;
import de.cismet.jpresso.project.nodes.actions.StartProjectAction;
import de.cismet.jpresso.project.wizard.convert.ConvertWizardAction;
import java.awt.Image;
import java.util.Arrays;
import javax.swing.Action;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * The top/head node of a JPresso Project in the LogicalView.
 * 
 * @author srichter
 */
final class JPressoProjectNode extends FilterNode {

    public JPressoProjectNode(final Node node, final JPressoProject project) {
        super(node, new JPressoTopNodeChildren(project), new ProxyLookup(new Lookup[]{Lookups.singleton(project), node.getLookup()}));
//        this.jpressoLogicalView = jpressoLogicalView;
        this.project = project;

    }
//    private final JPressoLogicalView jpressoLogicalView;
    private final JPressoProject project;
//    private Lookup lookup;
    @Override
    public Image getIcon(int type) {
        return Utilities.loadImage(
                "de/cismet/jpresso/project/res/launch.png");
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    public String getName() {
        return getProject().getProjectDirectory().getName();
    }

    @Override
    public Action[] getActions(boolean b) {
        final Action[] result = new Action[]{ //                CallableSystemAction.get(ConnectAction.class), null,
            //                CallableSystemAction.get(NewUserWizardAction.class), // ???
            //                CallableSystemAction.get(NewUsergroupWizardAction.class), null, // ???
            SystemAction.get(StartProjectAction.class),
            SystemAction.get(ConvertWizardAction.class),
            SystemAction.get(ExportProjectAction.class),
            CommonProjectActions.closeProjectAction(), null,
            CommonProjectActions.setAsMainProjectAction(), null,
            CommonProjectActions.customizeProjectAction(),
            CommonProjectActions.deleteProjectAction(),
            CommonProjectActions.copyProjectAction(),
            CommonProjectActions.moveProjectAction(),
            CommonProjectActions.renameProjectAction()
        };
        return result;

    }

    @Override
    public String getHtmlDisplayName() {
        return "<font color='!textText'>" + getName() + "</font><font color='!controlShadow'> [JPresso] </font>";

    }

    @Override
    public String getShortDescription() {
        return FileUtil.toFile(getProject().getProjectDirectory()).getAbsolutePath();
    }

    public JPressoProject getProject() {
        return project;
    }
}

final class JPressoTopNodeChildren extends Children.Array {

    public JPressoTopNodeChildren(final JPressoProject project) {
        super(Arrays.asList(ProjectSubnodesFactory.createProjectNodes(project)));
    }
}
