package com.github.ibronsveld.hippo.angular.behaviors;

import com.github.ibronsveld.hippo.angular.AngularPluginContext;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.handler.ErrorCodeRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public abstract class AbstractCustomPluginBehavior extends AbstractAjaxBehavior {

    private static final Logger log = LoggerFactory.getLogger(AbstractCustomPluginBehavior.class);
    private final AngularPluginContext context;
    private final String componentTag;

    public AbstractCustomPluginBehavior(AngularPluginContext context, String componentTag) {
        this.context = context;
        this.componentTag = componentTag;
    }

    @Override
    public void onRequest() {

        RequestCycle requestCycle = RequestCycle.get();
        WebRequest webRequest = (WebRequest) requestCycle.getRequest();

        PluginRequest pluginRequest = new PluginRequest(webRequest);
        PluginResponse pluginResponse = new PluginResponse();

        this.doRequest(pluginRequest, pluginResponse);

        // Do a check if it was ok or not
        if (pluginResponse.resultCode != 200) {
            requestCycle.scheduleRequestHandlerAfterCurrent(
                    new ErrorCodeRequestHandler(pluginResponse.resultCode, pluginResponse.responseBody));
        } else {
            requestCycle.scheduleRequestHandlerAfterCurrent(
                    new TextRequestHandler("application/json",
                            "UTF-8", pluginResponse.responseBody));
        }
    }

    public abstract void doRequest(PluginRequest pluginRequest, PluginResponse pluginResponse);


    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        tag.put(componentTag, getCallbackUrl());
    }

    protected AngularPluginContext getContext() {
        return this.context;
    }
}
