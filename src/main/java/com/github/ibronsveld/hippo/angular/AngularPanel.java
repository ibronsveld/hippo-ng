package com.github.ibronsveld.hippo.angular;

import com.github.ibronsveld.hippo.angular.field.AngularFieldPlugin;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Panel that renders the angular directive + bootstraps the directive as a separate application to
 * enable multiple fields in the editor.
 * WARNING: When another application bootstraps without this script, before this one renders it will
 * not work.
 */
public abstract class AngularPanel extends Panel {

    protected final AngularPluginContext context;

    protected final String appName;

    protected final String id;
    protected final String markupId;
    private static final Logger log = LoggerFactory.getLogger(AngularPanel.class);

    public AngularPanel(String id, String markupId, AngularPluginContext context, String appName) {
        super(id);

        this.context = context;
        this.appName = appName;
        this.id = id;
        this.markupId = markupId;

        MarkupContainer angularField = new WebMarkupContainer("angularfield-directive");
        angularField.setMarkupId("angularfield-directive");

        String pickerDirective = context.getPluginConfig().getString(PluginConstants.PLUGIN_DIRECTIVE);
        if (pickerDirective != null && !pickerDirective.equals("")) {
            angularField.add(new AttributeModifier(pickerDirective, ""));
        }

        this.add(angularField);
    }

    private void renderAngular(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-animate.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-aria.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-messages.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-resource.js")));
    }

    protected void renderAngularMaterial(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-material.min.js")));

        // Add material css
        response.render(CssHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-material.css")));

        // Add material fonts
        // TODO: Change?
        response.render(CssHeaderItem.forUrl("https://fonts.googleapis.com/icon?family=Material+Icons"));
    }

    protected AngularPluginContext getContext() {
        return context;
    }

    protected void renderPluginScripts(IHeaderResponse response) {
        // Add the SDK for the Hippo Angular
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "hippoAngularSDK.js")));

        String[] cssLinks = context.getPluginConfig().getStringArray(PluginConstants.PLUGIN_CSS_URLS);
        if (cssLinks != null && cssLinks.length > 0) {
            // Add multiple scripts
            for (int i = 0; i < cssLinks.length; i++) {
                String css = cssLinks[i];
                response.render(CssHeaderItem.forUrl(css));
            }
        }

        String[] fieldJS = context.getPluginConfig().getStringArray(PluginConstants.PLUGIN_JAVASCRIPT_URLS);
        if (fieldJS != null && fieldJS.length > 0) {
            // Add multiple scripts
            for (int i = 0; i < fieldJS.length; i++) {
                String javaScript = fieldJS[i];
                response.render(JavaScriptHeaderItem.forUrl(javaScript));
            }
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {

        renderAngular(response);
        renderAngularMaterial(response);
        renderPluginScripts(response);
    }
}
