/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.execution;

import de.cismet.jpresso.core.serviceprovider.AntUniversalExecutor;

/**
 *
 * @author srichter
 */
public abstract class ProjectPlanBase {

    public static AntUniversalExecutor EXECUTOR;

    public abstract void start();
}
