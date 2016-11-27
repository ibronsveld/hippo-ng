package com.github.ibronsveld.hippo.angular.handlers;

import com.github.ibronsveld.hippo.angular.behaviors.PluginRequest;
import com.github.ibronsveld.hippo.angular.behaviors.PluginResponse;

public interface IPluginRequestHandler {
    boolean canProcess(PluginRequest request);
    PluginResponse process(PluginRequest request);
}
