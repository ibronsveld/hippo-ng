package com.github.ibronsveld.hippo.angular.behaviors;

import com.github.ibronsveld.hippo.angular.AngularPluginContext;
import com.github.ibronsveld.hippo.angular.handlers.IPluginRequestHandler;
import com.google.gson.GsonBuilder;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.handler.ErrorCodeRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PluginHandlerBehavior extends AbstractAjaxBehavior {

    private static final Logger log = LoggerFactory.getLogger(PluginHandlerBehavior.class);
    private final AngularPluginContext context;
    private static final String METHOD_TAG = "post";

    public PluginHandlerBehavior(AngularPluginContext context) {
        this.context = context;
    }

    @Override
    public void onRequest() {

        RequestCycle requestCycle = RequestCycle.get();
        WebRequest webRequest = (WebRequest) requestCycle.getRequest();

        PluginRequest pluginRequest = null;
        try {
            pluginRequest = new PluginRequest(webRequest);
        } catch (Exception e) {
            log.error("Request data invalid", e);
        }

        List<PluginResponse> responses = new ArrayList<>();

        // Get all linked plugin request handlers
        IPluginRequestHandler[] pluginRequestHandlers = this.context.getPluginRequestHandlers();
        if (pluginRequestHandlers != null) {
            for (IPluginRequestHandler handler : pluginRequestHandlers) {
                // Check if it can process the request
                if (handler.canProcess(pluginRequest)) {
                    PluginResponse pluginResponse = handler.process(pluginRequest);
                    responses.add(pluginResponse);
                }
            }

            String responseBody = new GsonBuilder().create().toJson(responses);

            requestCycle.scheduleRequestHandlerAfterCurrent(
                    new TextRequestHandler("application/json",
                            "UTF-8", responseBody));
        }
    }



    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        tag.put(METHOD_TAG, getCallbackUrl());
    }

    protected AngularPluginContext getContext() {
        return this.context;
    }
}