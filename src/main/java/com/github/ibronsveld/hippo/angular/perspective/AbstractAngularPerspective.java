package com.github.ibronsveld.hippo.angular.perspective;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.hippoecm.frontend.PluginRequestTarget;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.plugins.standards.perspective.Perspective;
import org.hippoecm.frontend.plugins.yui.layout.WireframeBehavior;
import org.hippoecm.frontend.plugins.yui.layout.WireframeSettings;
import org.hippoecm.frontend.service.IconSize;
import com.github.ibronsveld.hippo.angular.AngularPluginContext;
import com.github.ibronsveld.hippo.angular.PluginConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This abstract perspective class can be used to bootstrap an Angular applications
 *
 */
public abstract class AbstractAngularPerspective extends Perspective implements IAngularPerspectiveService {

    private WebMarkupContainer appPanel;

    private final String PERSPECTIVE_TITLE;
    private final String PERSPECTIVE_ICON;
    private final String PERSPECTIVE_ID;
    private final String PLUGIN_NAME = "angular-panel";

    protected final AngularPluginContext pluginContext;

    private static final String EVENT_PERSPECTIVE_DEACTIVATED = "angular-perspective-deactivated";
    private static final String EVENT_PERSPECTIVE_SWITCHED = "angular-perspective-switched";
    private static final String EVENT_ID = "angular";
    private static final Logger log = LoggerFactory.getLogger(AbstractAngularPerspective.class);


    public AbstractAngularPerspective(IPluginContext context, IPluginConfig config) {
        super(context, config, EVENT_ID);

        pluginContext = new AngularPluginContext(context, config);
        this.setOutputMarkupId(true);

        IPluginConfig wfConfig = pluginContext.getPluginConfig().getPluginConfig("layout.wireframe");
        if (wfConfig != null) {
            WireframeSettings wfSettings = new WireframeSettings(wfConfig);
            add(new WireframeBehavior(wfSettings));
        }

        // Test strings
        PERSPECTIVE_ID = pluginContext.getPluginConfig().getString(PluginConstants.PLUGIN_PERSPECTIVE_ID);
        PERSPECTIVE_TITLE = pluginContext.getPluginConfig().getString(PluginConstants.PLUGIN_PERSPECTIVE_TITLE, "");
        PERSPECTIVE_ICON = pluginContext.getPluginConfig().getString(PluginConstants.PLUGIN_PERSPECTIVE_ICON, "");
        context.registerService(this, PERSPECTIVE_ID);
    }

    @Override
    public IModel<String> getTitle() {
        return Model.of(PERSPECTIVE_TITLE);
    }

    @Override
    public ResourceReference getIcon(IconSize size) {
        //return super.getIcon(size);
        // To Full URL
        Url url = Url.parse(PERSPECTIVE_ICON);

        UrlResourceReference urlResourceReference = new UrlResourceReference(url);
        return urlResourceReference;
    }

    @Override
    protected void onDeactivated() {
        super.onDeactivated();
        publishEvent(EVENT_PERSPECTIVE_DEACTIVATED);
    }

    protected AngularPluginContext getPerspectiveContext() {
        return this.pluginContext;
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

    /**
     * This method will switch to the perspective and load the URL based on the action & options
     *
     * @param action action to invoke on the perspective
     * @param option string or string representation of a json object containing the options to pass to the perspective
     */
    public abstract void switchToPerspective(String action, String option);
}
