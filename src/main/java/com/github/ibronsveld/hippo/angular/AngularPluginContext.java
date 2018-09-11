package com.github.ibronsveld.hippo.angular;

import com.github.ibronsveld.hippo.angular.field.AbstractAngularFieldPlugin;
import com.github.ibronsveld.hippo.angular.handlers.DefaultPluginRequestHandler;
import com.github.ibronsveld.hippo.angular.handlers.IPluginRequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import com.github.ibronsveld.hippo.angular.jcr.JcrModelSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class AngularPluginContext {

    private final IPluginConfig config;
    private final IPluginContext context;
    private final JcrModelSerializer modelSerializer;
    private final IPluginRequestHandler[] pluginRequestHandlers;
    private static final Logger log = LoggerFactory.getLogger(AngularPluginContext.class);
    private JcrNodeModel documentModel = null;
    private JcrNodeModel compareBaseDocumentModel = null;


    public AngularPluginContext(IPluginContext context, IPluginConfig config, JcrModelSerializer modelSerializer) {
        this.config = config;
        this.context = context;
        this.modelSerializer = modelSerializer;
        this.pluginRequestHandlers = createAndInstantiateHandlers();
    }

    public AngularPluginContext(IPluginContext context, IPluginConfig config) {
        this.config = config;
        this.context = context;
        this.modelSerializer = null;
        this.pluginRequestHandlers = createAndInstantiateHandlers();
    }

    public AngularPluginContext(IPluginContext context, IPluginConfig config, JcrModelSerializer modelSerializer, JcrNodeModel documentModel, JcrNodeModel compareBaseDocumentModel) {
        this.config = config;
        this.context = context;
        this.modelSerializer = modelSerializer;
        this.pluginRequestHandlers = createAndInstantiateHandlers();
        this.documentModel = documentModel;
        this.compareBaseDocumentModel = compareBaseDocumentModel;
    }

    /**
     * Returns the plugin config instance.
     *
     * @return IPluginConfig instance
     */
    public IPluginConfig getPluginConfig() {
        return config;
    }

    /**
     * Returns the plugin context instance.
     *
     * @return IPluginContext instance
     */
    public IPluginContext getPluginContext() {
        return context;
    }

    /**
     * Returns the associated model serializer, giving access to the plugin model
     *
     * @return JcrModelSerializer instance
     */
    public JcrModelSerializer getModelSerializer() {
        return modelSerializer;
    }

    /**
     * Gets the configuration of the plugin
     *
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

    /**
     * Get all plugin request handlers that are registered
     *
     * @return
     */
    public IPluginRequestHandler[] getPluginRequestHandlers() {
        return pluginRequestHandlers;
    }

    /**
     * Creates instances for all plugin request handlers
     * @return
     */
    private IPluginRequestHandler[] createAndInstantiateHandlers() {
        // Start with basic handler
        String[] handlers = this.getPluginConfig().getStringArray(PluginConstants.PLUGIN_REQUEST_HANDLERS);
        if (handlers != null && handlers.length > 0) {

            // There is always the default one
            ArrayList<IPluginRequestHandler> requestHandlerList = new ArrayList<>();

            requestHandlerList.add(new DefaultPluginRequestHandler(this));

            for (int i=0; i < handlers.length; i++) {
                // Because of the default handler, check the right index
                String handler = handlers[i];
                if (!handler.equals("")) {
                    try {
                        Class handlerClass = Class.forName(handler);
                        IPluginRequestHandler.class.isAssignableFrom(handlerClass);

                        final Constructor constructor = handlerClass.getConstructor(AngularPluginContext.class);
                        IPluginRequestHandler pluginRequestHandler = (IPluginRequestHandler) constructor.newInstance(this);
                        if (pluginRequestHandler != null) {
                            requestHandlerList.add(pluginRequestHandler);
                        }

                    } catch (ClassNotFoundException e) {
                        log.error("Cannot create handler with name '{}'", handler, e);
                    } catch (NoSuchMethodException e) {
                        log.error("Cannot create handler with name '{}'", handler, e);
                    } catch (IllegalAccessException e) {
                        log.error("Cannot create handler with name '{}'", handler, e);
                    } catch (InstantiationException e) {
                        log.error("Cannot create handler with name '{}'", handler, e);
                    } catch (InvocationTargetException e) {
                        log.error("Cannot create handler with name '{}'", handler, e);
                    }
                }
            }

            IPluginRequestHandler[] pluginRequestHandlers = new IPluginRequestHandler[requestHandlerList.size()];
            return requestHandlerList.toArray(pluginRequestHandlers);

        } else {
            IPluginRequestHandler[] pluginRequestHandlers = new IPluginRequestHandler[1];
            pluginRequestHandlers[0] = new DefaultPluginRequestHandler(this);
            return pluginRequestHandlers;
        }
    }

    /**
     * Returns the model for the document
     * @return JcrNodeModel instance, null if not set
     */
    public JcrNodeModel getDocumentModel() {
        return this.documentModel;
    }

    /**
     * Returns the compare to model
     * @return JcrNodeModel instance, null if not set
     */
    public JcrNodeModel getCompareBaseDocumentModel() {
        return this.compareBaseDocumentModel;
    }

    /**
     * Returns the model as a JsonObject
     * @return
     */
    public JsonObject getModelAsJson() {
        JsonObject jsonObject = new JsonObject();

        try {
            JsonObject modelObject = new JsonObject();
            modelObject = this.getModelSerializer().convertNodeToJson(modelObject, documentModel.getNode());
            jsonObject.add("model", modelObject);

            if (compareBaseDocumentModel != null) {
                JsonObject compareObject = new JsonObject();
                compareObject = this.getModelSerializer().convertNodeToJson(compareObject, compareBaseDocumentModel.getNode());
                jsonObject.add("compareModel", compareObject);
            } else {
                //jsonObject.add("compareModel", "");
            }

        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
