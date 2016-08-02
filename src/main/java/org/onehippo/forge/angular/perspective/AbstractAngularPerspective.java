package org.onehippo.forge.angular.perspective;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.hippoecm.frontend.PluginRequestTarget;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.plugins.standards.image.CachingImage;
import org.hippoecm.frontend.plugins.standards.panelperspective.PanelPlugin;
import org.hippoecm.frontend.plugins.standards.panelperspective.breadcrumb.PanelPluginBreadCrumbLink;
import org.hippoecm.frontend.plugins.standards.perspective.Perspective;
import org.hippoecm.frontend.plugins.yui.layout.WireframeBehavior;
import org.hippoecm.frontend.plugins.yui.layout.WireframeSettings;
import org.hippoecm.frontend.service.IconSize;
import org.hippoecm.frontend.widgets.AbstractView;
import org.onehippo.forge.angular.AngularPluginContext;
import org.onehippo.forge.angular.PluginConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

/**
 * This abstract perspective class can be used to bootstrap an Angular applications
 * It will load the page supplied in the initial URL configuration, or when called through the service,
 * load another URL based on the action + options
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

        // TODO: Is this required?
        IPluginConfig wfConfig = pluginContext.getPluginConfig().getPluginConfig("layout.wireframe");
        if (wfConfig != null) {
            WireframeSettings wfSettings = new WireframeSettings(wfConfig);
            add(new WireframeBehavior(wfSettings));
        }

        //this.add(createPluginPanel());

        // Test strings
        PERSPECTIVE_ID = pluginContext.getPluginConfig().getString(PluginConstants.ANGULAR_PERSPECTIVE_ID);
        PERSPECTIVE_TITLE = pluginContext.getPluginConfig().getString(PluginConstants.ANGULAR_PERSPECTIVE_TITLE, "");
        PERSPECTIVE_ICON = pluginContext.getPluginConfig().getString(PluginConstants.ANGULAR_PERSPECTIVE_ICON, "");
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

    protected AngularPerspectivePanel createAngularPerspectivePanel(String id, String markupId, AngularPluginContext context, String appName) {

        // Create instance of the Model Serializer class
        if (!getPluginConfig().containsKey(PluginConstants.ANGULAR_PERSPECTIVE_PLUGIN_CLASS)) {
            log.warn("no plugin class defined for the perspective");
            return null;
        }

        final String perspectivePanel = getPluginConfig().getString(PluginConstants.ANGULAR_PERSPECTIVE_PLUGIN_CLASS);

        if (perspectivePanel != null && !perspectivePanel.equals("")) {
            try {
                Class dialogClass = Class.forName(perspectivePanel);
                //String id, String markupId, AngularPluginContext context, String appName
                final Constructor constructor = dialogClass.getConstructor(String.class, String.class, AngularPluginContext.class, String.class);

                AngularPerspectivePanel angularPerspectivePanel = (AngularPerspectivePanel) constructor.newInstance(id, markupId, context, appName);
                angularPerspectivePanel.setOutputMarkupId(true);
                return angularPerspectivePanel;

            } catch (ClassNotFoundException e) {
                log.error("Cannot create dialog with name '{}'", perspectivePanel, e);
            } catch (NoSuchMethodException e) {
                log.error("Cannot create dialog with name '{}'", perspectivePanel, e);
            } catch (InvocationTargetException e) {
                log.error("Cannot create dialog with name '{}'", perspectivePanel, e);
            } catch (InstantiationException e) {
                log.error("Cannot create dialog with name '{}'", perspectivePanel, e);
            } catch (IllegalAccessException e) {
                log.error("Cannot create dialog with name '{}'", perspectivePanel, e);
            }
        }
        return null;
    }

    protected AngularPanelPlugin createPluginPanel() {
        AngularPanelPlugin plugin = (AngularPanelPlugin)this.pluginContext.getPluginContext().getService(PLUGIN_NAME, AngularPanelPlugin.class);
        return plugin;
    }

    /**
     * This method will switch to the perspective and load the URL based on the action & options
     *
     * @param action action to invoke on the perspective
     * @param option string or string representation of a json object containing the options to pass to the perspective
     */
    public abstract void switchToPerspective(String action, String option);
}
