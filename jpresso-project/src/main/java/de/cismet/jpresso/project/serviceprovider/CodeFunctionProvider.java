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
package de.cismet.jpresso.project.serviceprovider;

import java.util.List;

/**
 * Provides a list of available ´public-static methods to use in MappingEditor's mappings.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface CodeFunctionProvider {

    //~ Methods ----------------------------------------------------------------

    /**
     * Provides a list of available ´public-static methods to use in MappingEditor's mappings.
     *
     * @return  list of all currently available public-static methods.
     */
    List<String> getFunctionList();
}
