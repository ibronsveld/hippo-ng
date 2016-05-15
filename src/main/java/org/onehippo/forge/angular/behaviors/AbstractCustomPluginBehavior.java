package org.onehippo.forge.angular.behaviors;

import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.util.io.IClusterable;
import org.onehippo.forge.angular.AngularPluginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractCustomPluginBehavior extends AbstractAjaxBehavior {

    private static final Logger log = LoggerFactory.getLogger(AbstractCustomPluginBehavior.class);
    private final AngularPluginContext context;
    private final String componentTag;

    public AbstractCustomPluginBehavior(AngularPluginContext context, String componentTag) {
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
    public void onRequest() {

    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        tag.put(componentTag, getCallbackUrl());
    }
}
