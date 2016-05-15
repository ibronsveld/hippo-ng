package org.onehippo.forge.angular;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.onehippo.forge.angular.jcr.JcrModelSerializer;

import javax.jcr.RepositoryException;

public class AngularPluginContext {

    private final IPluginConfig config;
    private final IPluginContext context;
    private final JcrModelSerializer modelSerializer;

    public AngularPluginContext(IPluginContext context, IPluginConfig config, JcrModelSerializer modelSerializer) {
        this.config = config;
        this.context = context;
        this.modelSerializer = modelSerializer;
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




}
