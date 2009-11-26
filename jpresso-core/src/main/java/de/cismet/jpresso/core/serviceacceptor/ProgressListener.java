/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.serviceacceptor;

/**
 * Interface for classes that can handle progress values.
 * 
 * @author stefan
 */
public interface ProgressListener {
    
    /**
     * 
     * @param name
     */
    public void start(String name);
    
    /**
     * 
     * @param name
     * @param workUnits
     */
    public void start(String name, int workUnits);
    
    /**
     * 
     */
    public void finish();
    
    /**
     * 
     * @param progress
     */
    public void switchToDeterminate(int progress);
    
    /**
     * 
     */
    public void switchToIndeterminate();
    
    /**
     * 
     * @param progress
     */
    public void progress(int progress);
}
