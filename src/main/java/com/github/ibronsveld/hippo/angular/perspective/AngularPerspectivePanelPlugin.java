package com.github.ibronsveld.hippo.angular.perspective;

import com.github.ibronsveld.hippo.angular.perspective.AngularPanelPlugin;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AngularPerspectivePanelPlugin extends AngularPanelPlugin {

    protected static final Logger log = LoggerFactory.getLogger(AngularPerspectivePanelPlugin.class);

    public AngularPerspectivePanelPlugin(IPluginContext context, IPluginConfig config) {
        super(context, config);

        //this.add(new GetFieldBehavior(this.getAngularPluginContext(), "getField"));
    }
}

