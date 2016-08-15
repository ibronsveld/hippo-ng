package com.github.ibronsveld.hippo.angular.behaviors;

import com.github.ibronsveld.hippo.angular.AngularPluginContext;

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