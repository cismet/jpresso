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
package de.cismet.jpresso.core.starter;

import java.io.IOException;

import de.cismet.jpresso.core.exceptions.InvalidFormatFileException;
import de.cismet.jpresso.core.io.Converter;
import de.cismet.jpresso.core.log4j.config.Log4jEasyConfigurator;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class StartConvert {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        if (args.length >= 2) {
            Log4jEasyConfigurator.configLog4j();
            try {
                Converter.convertOldImportFile(args[0], args[1], args[2]);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InvalidFormatFileException ex) {
                ex.printStackTrace();
            }
        } else {
            System.err.println("Error: incorrect arguments.");
        }
    }
}
