package org.onehippo.forge.angular;

import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;

public class AngularPluginContext {

    private final IPluginConfig config;
    private final IPluginContext context;

    public AngularPluginContext(IPluginContext context, IPluginConfig config) {
        this.config = config;
        this.context = context;
    }

    /**
     * Returns the plugin config instance.
     * @return IPluginConfig instance
     */
    public IPluginConfig getPluginConfig() {
        return config;
    }

    /**
     * Returns the plugin context instance.
     * @return IPluginContext instance
     */
    public IPluginContext getPluginContext() {
        return context;
    }
}
