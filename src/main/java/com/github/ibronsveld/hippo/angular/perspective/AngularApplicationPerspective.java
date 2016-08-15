package com.github.ibronsveld.hippo.angular.perspective;

import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This perspective is embedding an angular app
 */
public class AngularApplicationPerspective extends AbstractAngularPerspective {

    private static final Logger log = LoggerFactory.getLogger(AngularPerspectivePanel.class);

    public AngularApplicationPerspective(IPluginContext context, IPluginConfig config) {
        super(context,config);
        //addExtensionPoint("panel");
    }

    @Override
    public void switchToPerspective(String action, String option) {
        // Depending on action, we could do other things
    }
}
