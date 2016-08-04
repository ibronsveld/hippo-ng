package org.onehippo.forge.angular.behaviors;

import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.http.WebRequest;
import org.onehippo.forge.angular.AngularPluginContext;
import org.onehippo.forge.angular.AngularPluginUtils;

public class GetPluginConfigurationBehavior extends AbstractCustomPluginBehavior {
    private static final long serialVersionUID = 1L;

    public GetPluginConfigurationBehavior(AngularPluginContext context, String componentTag) {
        super(context, componentTag);
    }


    @Override
    public void doRequest(PluginRequest pluginRequest, PluginResponse pluginResponse) {
        String key = pluginRequest.getRequestBodyAsString();
        String config = this.getContext().getAngularPluginConfiguration(key);
        pluginResponse.addResponseBody(config);
    }

//    @Override
//    public void onRequest() {
//        RequestCycle requestCycle = RequestCycle.get();
//        WebRequest wr = (WebRequest) requestCycle.getRequest();
//
//        String key = AngularPluginUtils.getContentFromRequest(wr);
//        String config = this.getContext().getAngularPluginConfiguration(key);
//
//        requestCycle.scheduleRequestHandlerAfterCurrent(
//                new TextRequestHandler("application/json",
//                        "UTF-8", config));
//    }
}