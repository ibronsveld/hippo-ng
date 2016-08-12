package org.onehippo.forge.angular;

import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.onehippo.forge.angular.jcr.JcrModelSerializer;

public class AngularPluginContext {

    private final IPluginConfig config;
    private final IPluginContext context;
    private final JcrModelSerializer modelSerializer;

    public AngularPluginContext(IPluginContext context, IPluginConfig config, JcrModelSerializer modelSerializer) {
        this.config = config;
        this.context = context;
        this.modelSerializer = modelSerializer;
    }

    public AngularPluginContext(IPluginContext context, IPluginConfig config) {
        this.config = config;
        this.context = context;
        this.modelSerializer = null;
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

    /**
     * Returns the associated model serializer, giving access to the plugin model
     * @return JcrModelSerializer instance
     */
    public JcrModelSerializer getModelSerializer() {
        return modelSerializer;
    }

    /**
     * Gets the configuration of the plugin
     * @param key (optional) key can be overridden if plugin wants to use a different value
     * @return value of the CMS
     */
    public String getAngularPluginConfiguration(String key) {
        if (key == null || key.equals("")) {
            key = PluginConstants.PLUGIN_CONFIGURATION;
        }

        // Get the data from getPluginConfig().getString(key);
        return this.getPluginConfig().getString(key, "{}");
    }

}
