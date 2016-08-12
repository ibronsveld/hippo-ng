package org.onehippo.forge.angular.perspective;

import org.apache.wicket.AttributeModifier;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.service.render.RenderPlugin;
import org.onehippo.forge.angular.AngularPluginContext;
import org.onehippo.forge.angular.AngularPluginUtils;
import org.onehippo.forge.angular.PluginConstants;
import org.onehippo.forge.angular.behaviors.GetPluginConfigurationBehavior;
import org.onehippo.forge.angular.behaviors.SwitchPerspectiveBehavior;

public abstract class AngularPanelPlugin extends RenderPlugin {

    private final String id;
    private final String appName;

    private final AngularPluginContext pluginContext;

    protected AngularPerspectivePanel angularPerspectivePanel;

    public AngularPanelPlugin(IPluginContext context, IPluginConfig config) {
        //String id, AngularPluginContext pluginContext, String appName
        super(context, config);
        this.pluginContext = new AngularPluginContext(context, config);

        appName = pluginContext.getPluginConfig().getString(PluginConstants.PLUGIN_APPNAME);
        id = AngularPluginUtils.generateUniqueComponentId(appName, 6);

        angularPerspectivePanel = new AngularPerspectivePanel("angular-panel", id, pluginContext, appName);

        // Add the default behaviours to the mix
        this.add(new SwitchPerspectiveBehavior(pluginContext, "switchPerspective"));
        this.add(new GetPluginConfigurationBehavior(pluginContext, "getPluginConfig"));

        this.add(new AttributeModifier(PluginConstants.PLUGIN_SDK_DIRECTIVE, ""));
        this.add(new AttributeModifier("ng-app", appName));

        add(angularPerspectivePanel);
    }

    public AngularPluginContext getAngularPluginContext() {
        return pluginContext;
    }
}
