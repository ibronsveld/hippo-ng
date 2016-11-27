package com.github.ibronsveld.hippo.angular.handlers;

import com.github.ibronsveld.hippo.angular.AngularPluginContext;
import com.github.ibronsveld.hippo.angular.behaviors.PluginRequest;
import com.github.ibronsveld.hippo.angular.behaviors.PluginResponse;
import com.github.ibronsveld.hippo.angular.field.AbstractAngularFieldPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;

public class DefaultPluginRequestHandler extends AbstractPluginRequestHandler {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DefaultPluginRequestHandler.class);

    private static final String ACTION_CONFIG = "config";
    private static final String ACTION_UPDATE_MODEL = "setModel";
    private static final String ACTION_GET_MODEL = "getModel";

    private static final String MODEL_KEY = "model";

    public DefaultPluginRequestHandler(AngularPluginContext context) {
        super(context);
    }

    private PluginResponse getPluginConfig(PluginRequest pluginRequest) {
        PluginResponse pluginResponse = new PluginResponse();

        if (pluginRequest.get("key") != null && !pluginRequest.getAsString("key").equals("")) {
            String config = this.getPluginContext().getAngularPluginConfiguration(pluginRequest.getAsString("key"));
            if (config != null) {
                pluginResponse.addResponseBody(config);
            } else {
                pluginResponse.setError(404, "Not found");
            }
        }
        return pluginResponse;
    }

    private PluginResponse updateModel(PluginRequest pluginRequest) {
        PluginResponse pluginResponse = new PluginResponse();

        String jsonString = pluginRequest.getAsString(MODEL_KEY);
        try {
            this.getPluginContext().getModelSerializer().appendJsonToNode(
                    this.getPluginContext().getDocumentModel().getNode(),
                    jsonString
            );
        } catch (RepositoryException e) {
            log.error("Error storing data in repository", e);
        }

        final String fieldJson = this.getPluginContext().getModelAsJson().toString();
        pluginResponse.addResponseBody(fieldJson);
        return pluginResponse;
    }

    private PluginResponse getModel(PluginRequest pluginRequest) {
        PluginResponse pluginResponse = new PluginResponse();

        final String fieldJson = this.getPluginContext().getModelAsJson().toString();
        pluginResponse.addResponseBody(fieldJson);
        return pluginResponse;
    }

    @Override
    public boolean canProcess(PluginRequest request) {
        if (request.getAction() != null && !request.getAction().equals("")) {
            // Validate the action
            switch(request.getAction().toLowerCase()) {
                case DefaultPluginRequestHandler.ACTION_CONFIG:
                    return true;
                case DefaultPluginRequestHandler.ACTION_UPDATE_MODEL:
                    return true;
                case DefaultPluginRequestHandler.ACTION_GET_MODEL:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    @Override
    public PluginResponse process(PluginRequest request) {
        if (request.getAction() != null && !request.getAction().equals("")) {
            // Validate the action
            switch(request.getAction().toLowerCase()) {
                case DefaultPluginRequestHandler.ACTION_CONFIG:
                    // Get config
                    return getPluginConfig(request);
                case DefaultPluginRequestHandler.ACTION_UPDATE_MODEL:
                    // Store model
                    return updateModel(request);
                case DefaultPluginRequestHandler.ACTION_GET_MODEL:
                    // Store model
                    return getModel(request);
                default:
                    return null;
            }
        }
        return null;
    }
}