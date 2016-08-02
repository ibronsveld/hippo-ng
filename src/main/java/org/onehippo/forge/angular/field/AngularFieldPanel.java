package org.onehippo.forge.angular.field;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.hippoecm.frontend.service.IEditor;
import org.onehippo.forge.angular.AngularPanel;
import org.onehippo.forge.angular.AngularPluginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AngularFieldPanel extends AngularPanel {

    private static final Logger log = LoggerFactory.getLogger(AngularFieldPanel.class);

    public AngularFieldPanel(String id, String markupId, AngularPluginContext context, String appName) {
        super(id, markupId, context, appName);

        this.setMarkupId(this.markupId);
        this.add(new AttributeModifier("ng-field", ""));
    }

    protected boolean isEditMode() {
        return IEditor.Mode.EDIT.equals(IEditor.Mode.fromString(context.getPluginConfig().getString("mode", "view")));
    }

    protected boolean isCompareMode() {
        return IEditor.Mode.COMPARE.equals(IEditor.Mode.fromString(context.getPluginConfig()
                .getString("mode", "view")));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(OnDomReadyHeaderItem.forScript("var rootElement = document.querySelector('#" + this.markupId + "');\n" +
                "var element = angular.element(rootElement);\n" +
                "var isInitialized = element.injector();\n" +
                "if (!isInitialized) {\n" +
                "\tangular.bootstrap(element, ['" + appName + "']);\n" +
                "}\n"));

    }
}
