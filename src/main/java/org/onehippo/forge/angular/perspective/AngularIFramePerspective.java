package org.onehippo.forge.angular.perspective;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.onehippo.forge.angular.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * This perspective is used to bootstrap an Angular application in a custom iFrame
 * It will load the page supplied in the initial URL configuration, or when called through the service,
 * load another URL based on the action + options
 */
public class AngularIFramePerspective extends AbstractAngularPerspective implements IAngularPerspectiveService {

    private WebMarkupContainer appPanel;
    private IModel<String> srcModel;
    private String perspectiveUrl;

    private static final String EVENT_PERSPECTIVE_DEACTIVATED = "angular-perspective-deactivated";
    private static final String EVENT_PERSPECTIVE_SWITCHED = "angular-perspective-switched";
    private static final String EVENT_ID = "angular";
    private static final Logger log = LoggerFactory.getLogger(AngularIFramePerspective.class);

    public AngularIFramePerspective(IPluginContext context, IPluginConfig config) {
        super(context, config);
        this.setOutputMarkupId(true);

        perspectiveUrl = config.getString(PluginConstants.PLUGIN_PERSPECTIVE_URL);
        srcModel = new Model<String>() {
            @Override
            public String getObject() {
                return AngularIFramePerspective.this.perspectiveUrl;
            }
        };

        appPanel = new WebMarkupContainer("perspective-iframe");
        appPanel.setOutputMarkupId(true);
        appPanel.add(new AttributeModifier("src", srcModel));
        add(appPanel);
    }

    @Override
    public void onEvent(Iterator event) {
        if (event instanceof AjaxRequestTarget) {
            ((AjaxRequestTarget)event).add(appPanel);
        }
        super.onEvent(event);
    }



    /**
     * This method will switch to the perspective and load the URL based on the action & options
     *
     * @param action action to invoke on the perspective
     * @param option string or string representation of a json object containing the options to pass to the perspective
     */
    @Override
    public void switchToPerspective(String action, String option) {
        // TODO: Based on action and options, pick right thing to show from config
        log.debug("Attempting to open perspective for ACTION: " + action + " and OPTS: " + option);

//        // We will also publish an event here
//        publishEvent(EVENT_PERSPECTIVE_SWITCHED);

        // If action is open, the option parameter is string
        if (action.toLowerCase().equals("open")) {
            perspectiveUrl = option;
            appPanel.add(new AttributeModifier("src", srcModel));
        }
        focus(null);
    }
}
