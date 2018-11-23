package com.github.ibronsveld.hippo.angular;

import com.github.ibronsveld.hippo.angular.field.AngularFieldPlugin;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.resource.PackageResourceReference;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class AngularPluginUtils {

    public static String generateUniqueComponentId(String appName, int maxLength) {
        int range = (Integer.MAX_VALUE - 0) + 1;
        int intValue = (int) (Math.random() * range) + 0;

        String randomValue = Integer.toString(intValue).substring(0, maxLength);
        return "ng-" + appName + "-" + randomValue;
    }

    @Deprecated
    public static String getContentFromRequest(WebRequest wr) {
        HttpServletRequest hsr =
                (HttpServletRequest) wr.getContainerRequest();

        try {
            BufferedReader br = hsr.getReader();
            String contents = br.readLine();
            br.close();

            if ((contents == null) || contents.isEmpty()) {
                // No JSON
                return null;
            } else {
                // Got JSON
                return contents;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void renderPluginScripts(AngularPluginContext context, IHeaderResponse response) {

        String[] cssLinks = context.getPluginConfig().getStringArray(PluginConstants.PLUGIN_CSS_URLS);
        if (cssLinks != null && cssLinks.length > 0) {
            // Add multiple scripts
            for (int i = 0; i < cssLinks.length; i++) {
                String css = cssLinks[i];
                if (css != null && css.length() > 0)
                    response.render(CssHeaderItem.forUrl(css));
            }
        }

        String[] fieldJS = context.getPluginConfig().getStringArray(PluginConstants.PLUGIN_JAVASCRIPT_URLS);
        if (fieldJS != null && fieldJS.length > 0) {
            // Add multiple scripts
            for (int i = 0; i < fieldJS.length; i++) {
                String javaScript = fieldJS[i];
                if (javaScript != null && javaScript.length() > 0)
                    response.render(JavaScriptHeaderItem.forUrl(javaScript));
            }
        }

        // Add the SDK for the Hippo Angular
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "hippoAngularSDK.js")));
    }


    public static void renderAngular(IHeaderResponse response) {
        // TODO: Replace with CDN links?

//        response.render(JavaScriptHeaderItem.forUrl ()new (AngularFieldPlugin.class, "angular.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular.min.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-animate.min.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-aria.min.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-messages.min.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-resource.min.js")));
    }

    public static void renderAngularMaterial(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-material.min.js")));

        // Add material css
        response.render(CssHeaderItem.forReference(new PackageResourceReference(AngularFieldPlugin.class, "angular-material.css")));

        // Add material fonts
        // TODO: Change?
        response.render(CssHeaderItem.forUrl("https://fonts.googleapis.com/icon?family=Material+Icons"));
    }
}
