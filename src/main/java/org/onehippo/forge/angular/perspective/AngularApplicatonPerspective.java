package org.onehippo.forge.angular.perspective;

import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.onehippo.forge.angular.AngularPluginContext;
import org.onehippo.forge.angular.AngularPluginUtils;
import org.onehippo.forge.angular.PluginConstants;
import org.onehippo.forge.angular.behaviors.GetPluginConfigurationBehavior;
import org.onehippo.forge.angular.behaviors.SwitchPerspectiveBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This perspective is embedding an angular app
 */
public class AngularApplicatonPerspective extends AbstractAngularPerspective {

    private static final Logger log = LoggerFactory.getLogger(AngularPerspectivePanel.class);

    private JcrNodeModel documentModel;
    private JcrNodeModel compareBaseDocumentModel;


    private final AngularPluginContext perspectiveContext;
    private final String APP_NAME;
    private final String UNIQUE_COMPONENT_ID;

    protected AngularPerspectivePanel angularPerspectivePanel;

    public AngularApplicatonPerspective(IPluginContext context, IPluginConfig config) {
        super(context,config);

        APP_NAME = pluginContext.getPluginConfig().getString(PluginConstants.ANGULAR_FIELD_APPNAME);
        perspectiveContext = new AngularPluginContext(context, config);

        UNIQUE_COMPONENT_ID = AngularPluginUtils.generateUniqueComponentId(APP_NAME, 6);

        // We could load the class if needed here to remove the complexity for html stuff


        angularPerspectivePanel = new AngularPerspectivePanel("angularfield-container", UNIQUE_COMPONENT_ID, perspectiveContext, APP_NAME);
        angularPerspectivePanel.add(new SwitchPerspectiveBehavior(perspectiveContext, "switchPerspective"));
        angularPerspectivePanel.add(new GetPluginConfigurationBehavior(perspectiveContext, "getPluginConfig"));

        add(angularPerspectivePanel);
    }

    protected AngularPerspectivePanel getAngularPerspectivePanel() {
        return this.angularPerspectivePanel;
    }

    protected AngularPluginContext getPerspectiveContext() {
        return this.perspectiveContext;
    }

    @Override
    public void switchToPerspective(String action, String option) {
        // Depending on action, we could do other things
    }



}
