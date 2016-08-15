package com.github.ibronsveld.hippo.angular.behaviors;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.util.io.IClusterable;
import org.apache.wicket.util.string.StringValue;
import org.hippoecm.frontend.plugins.standards.perspective.Perspective;
import com.github.ibronsveld.hippo.angular.AngularPluginContext;
import com.github.ibronsveld.hippo.angular.perspective.IAngularPerspectiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwitchPerspectiveBehavior extends AbstractDefaultAjaxBehavior {
    private static final Logger log = LoggerFactory.getLogger(SwitchPerspectiveBehavior.class);
    private final AngularPluginContext context;
    private final String componentTag;

    public SwitchPerspectiveBehavior(AngularPluginContext context, String componentTag) {
        this.context = context;
        this.componentTag = componentTag;
    }

    private <T extends IClusterable> T loadService(final String name, final String configServiceId, final Class<T> clazz) {
        final T service = context.getPluginContext().getService(configServiceId, clazz);
        if (service == null) {
            log.info("Could not get service '{}' of type {}", configServiceId, clazz.getName());
        }
        return service;
    }

    @Override
    protected void respond(AjaxRequestTarget ajaxRequestTarget) {
        // Get the perspective service, for now assume it is AngularPerspectiveService
        RequestCycle cycle = RequestCycle.get();
        WebRequest webRequest = (WebRequest) cycle.getRequest();

        StringValue name = webRequest.getQueryParameters().getParameterValue("name");
        StringValue action = webRequest.getQueryParameters().getParameterValue("action");
        StringValue option = webRequest.getQueryParameters().getParameterValue("option");

        if (!name.isEmpty()) {
            String perspectiveName = name.toString();
            log.debug("Loading perspective: " + perspectiveName);
            IAngularPerspectiveService perspectiveService = loadService("Angular Perspective Service",
                    perspectiveName,
                    IAngularPerspectiveService.class);

            log.debug("Invoking perspective: " + perspectiveName);
            perspectiveService.switchToPerspective(action.toString(""), option.toString(""));
            ajaxRequestTarget.add((Perspective)perspectiveService);
        }
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
    }

    @Override
    protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
        super.updateAjaxAttributes(attributes);
        attributes.getExtraParameters().put("name", "__NAME");
        attributes.getExtraParameters().put("action", "__ACTION");
        attributes.getExtraParameters().put("option", "__OPTION");
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        tag.put("switchPerspective", getCallbackScript());
    }

}
