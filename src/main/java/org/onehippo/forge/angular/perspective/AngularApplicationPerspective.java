package org.onehippo.forge.angular.perspective;

import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.onehippo.forge.angular.AngularPluginContext;
import org.onehippo.forge.angular.AngularPluginUtils;
import org.onehippo.forge.angular.PluginConstants;
import org.onehippo.forge.angular.behaviors.GetPluginConfigurationBehavior;
import org.onehippo.forge.angular.behaviors.SwitchPerspectiveBehavior;
import org.onehippo.forge.angular.jcr.JcrModelSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
