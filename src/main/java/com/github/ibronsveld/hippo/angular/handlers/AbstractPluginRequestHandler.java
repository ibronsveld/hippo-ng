package com.github.ibronsveld.hippo.angular.handlers;

import com.github.ibronsveld.hippo.angular.AngularPluginContext;

public abstract class AbstractPluginRequestHandler implements IPluginRequestHandler {

    private final AngularPluginContext pluginContext;

    public AbstractPluginRequestHandler(AngularPluginContext context) {
        this.pluginContext = context;
    }

    protected AngularPluginContext getPluginContext() {
        return this.pluginContext;
    }
}
