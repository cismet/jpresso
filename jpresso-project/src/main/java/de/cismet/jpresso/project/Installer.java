/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project;

import de.cismet.jpresso.core.data.ProjectOptions;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.project.filetypes.AntHandler;
import de.cismet.jpresso.core.log4j.config.Log4jEasyConfigurator;
import java.io.IOException;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.Repository;
import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all. This is executed before the module is installed.
 * We use it to prepare our logger.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        Log4jEasyConfigurator.configLog4j();
        final FileSystem fs = Repository.getDefault().getDefaultFileSystem();
        FileObject result = fs.findResource(JPressoFileManager.PROJECT_OPTIONS);
        if (result == null) {
            try {
                result = fs.getRoot().createData(JPressoFileManager.PROJECT_OPTIONS);
                final ProjectOptions po = new ProjectOptions();
                JPressoFileManager.getDefault().persist(FileUtil.toFile(result), po);
            } catch (IOException ex) {

                ErrorManager.getDefault().notify(ex);
            }
        }
        final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(getClass());
        if (!AntHandler.defaultPropertiesExist()) {
            try {
                log.info("Could not find Ant-properties. Create them...");
                AntHandler.writeDefaultProperties();
            } catch (IOException ex) {
                log.warn(ex, ex);
            }
        }
    }
}
