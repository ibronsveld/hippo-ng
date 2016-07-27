package org.onehippo.forge.angular.perspective;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.onehippo.forge.angular.AngularPluginContext;
import org.onehippo.forge.angular.PluginConstants;
import org.onehippo.forge.angular.field.AngularFieldPanel;
import org.onehippo.forge.angular.field.AngularFieldPlugin;
import org.onehippo.forge.angular.field.AngularPanel;
import org.onehippo.forge.angular.jcr.JcrModelSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AngularPerspectivePanel extends AngularPanel {

    private static final long serialVersionUID = 1L;

    public AngularPerspectivePanel(String id, String markupId, AngularPluginContext context, String appName) {
        super(id, markupId, context, appName);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-animate.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-aria.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-messages.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-resource.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-material.min.js")));

        // Add the SDK for the Hippo Angular
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "hippoAngularSDK.js")));

        // Add material css
        response.render(CssHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-material.css")));
        // Add material fonts
        // TODO: Change?
        response.render(CssHeaderItem.forUrl("https://fonts.googleapis.com/icon?family=Material+Icons"));

        String[] cssLinks = context.getPluginConfig().getStringArray(PluginConstants.ANGULAR_FIELD_CSS_URLS);
        if (cssLinks != null && cssLinks.length > 0) {
            // Add multiple scripts
            for (int i = 0; i < cssLinks.length; i++) {
                String css = cssLinks[i];
                response.render(CssHeaderItem.forUrl(css));
            }
        }

        String[] fieldJS = context.getPluginConfig().getStringArray(PluginConstants.ANGULAR_FIELD_JAVASCRIPT_URLS);
        if (fieldJS != null && fieldJS.length > 0) {
            // Add multiple scripts
            for (int i = 0; i < fieldJS.length; i++) {
                String javaScript = fieldJS[i];
                response.render(JavaScriptHeaderItem.forUrl(javaScript));
            }
        }
    }
}
