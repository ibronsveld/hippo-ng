package org.onehippo.forge.angular.perspective;

import com.google.gson.*;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.ComponentDetachableModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.hippoecm.frontend.PluginRequestTarget;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.plugins.standards.perspective.Perspective;
import org.hippoecm.frontend.plugins.yui.layout.WireframeBehavior;
import org.hippoecm.frontend.plugins.yui.layout.WireframeSettings;
import org.onehippo.forge.angular.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * This perspective is used to bootstrap an Angular application in a custom iFrame
 * It will load the page supplied in the initial URL configuration, or when called through the service,
 * load another URL based on the action + options
 */
public class AngularPerspective extends Perspective implements IAngularPerspectiveService {

    private WebMarkupContainer appPanel;
    private IModel<String> srcModel;
    private String perspectiveUrl;
    private final String PERSPECTIVE_TITLE;
    private final String PERSPECTIVE_ID;

    private static final String EVENT_PERSPECTIVE_DEACTIVATED = "angular-perspective-deactivated";
    private static final String EVENT_PERSPECTIVE_SWITCHED = "angular-perspective-switched";
    private static final String EVENT_ID = "angular";
    private static final Logger log = LoggerFactory.getLogger(AngularPerspective.class);

    public AngularPerspective(IPluginContext context, IPluginConfig config) {
        super(context, config, EVENT_ID);
        this.setOutputMarkupId(true);

        // TODO: Is this required?
        IPluginConfig wfConfig = config.getPluginConfig("layout.wireframe");
        if (wfConfig != null) {
            WireframeSettings wfSettings = new WireframeSettings(wfConfig);
            add(new WireframeBehavior(wfSettings));
        }

        perspectiveUrl = "";
        srcModel = new Model<String>() {
            @Override
            public String getObject() {
                return AngularPerspective.this.perspectiveUrl;
            }
        };

        PERSPECTIVE_ID = config.getString(PluginConstants.ANGULAR_PERSPECTIVE_ID);
        PERSPECTIVE_TITLE = config.getString(PluginConstants.ANGULAR_PERSPECTIVE_TITLE);
        context.registerService(this, PERSPECTIVE_ID);

        appPanel = new WebMarkupContainer("perspective-iframe");
        appPanel.setOutputMarkupId(true);
        add(appPanel);
    }

    @Override
    public IModel<String> getTitle() {
        return new StringResourceModel("perspective-title", this, Model.of(PERSPECTIVE_TITLE));
    }

    @Override
    protected void onDeactivated() {
        super.onDeactivated();
        publishEvent(EVENT_PERSPECTIVE_DEACTIVATED);
    }

    @Override
    public void render(final PluginRequestTarget target) {
        super.render(target);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);
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
