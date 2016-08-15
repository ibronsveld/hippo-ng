package com.github.ibronsveld.hippo.angular.perspective;

import org.apache.wicket.util.io.IClusterable;

/**
 * Method to invoke the perspective from other parts of the system directly
 */
public interface IAngularPerspectiveService extends IClusterable {
    /**
     *
     * @param action action to invoke on the perspective
     * @param options string or string representation of a json object containing the options to pass to the perspective
     */
    void switchToPerspective(String action, String options);
}
