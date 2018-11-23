/**
 * Copyright 2014 Hippo B.V. (http://www.onehippo.com)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.ibronsveld.hippo.angular.field;

import com.github.ibronsveld.hippo.angular.behaviors.*;
import com.google.gson.*;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.panel.Panel;
import org.hippoecm.frontend.model.IModelReference;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.model.event.IObserver;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.service.IEditor;
import org.hippoecm.frontend.service.render.RenderPlugin;
import com.github.ibronsveld.hippo.angular.AngularPluginContext;
import com.github.ibronsveld.hippo.angular.AngularPluginUtils;
import com.github.ibronsveld.hippo.angular.PluginConstants;
import com.github.ibronsveld.hippo.angular.jcr.JcrModelSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractAngularFieldPlugin extends RenderPlugin<Node> implements IObserver {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(AbstractAngularFieldPlugin.class);

    private final AngularPluginContext fieldContext;
    private final String APP_NAME;
    private final String UNIQUE_COMPONENT_ID;

    protected Panel angularFieldPanel;

    public AbstractAngularFieldPlugin(final IPluginContext context, IPluginConfig config) {
        super(context, config);
        setOutputMarkupId(true);

        APP_NAME = getPluginConfig().getString(PluginConstants.PLUGIN_APPNAME);
        JcrNodeModel documentModel = (JcrNodeModel) getModel();
        JcrNodeModel compareBaseDocumentModel = null;

        if (isCompareMode()) {

            if (!getPluginConfig().containsKey("model.compareTo")) {
                log.warn("no base model service configured");
            } else {
                //JcrNodeModel compareBaseDocumentModel = null;
                IModelReference compareBaseRef = getPluginContext()
                        .getService(getPluginConfig().getString("model.compareTo"), IModelReference.class);

                if (compareBaseRef == null) {
                    log.warn("no base model service available");
                    compareBaseDocumentModel = null;
                } else {
                    compareBaseDocumentModel = new JcrNodeModel(new StringBuilder()
                            .append(((JcrNodeModel) compareBaseRef.getModel()).getItemModel().getPath())
                            .toString());
                }
            }
        } else {
            compareBaseDocumentModel = null;
        }

        // Store the Model Serializer to use
        JcrModelSerializer jcrModelSerializer = createModelSerializerInstance();
        fieldContext = new AngularPluginContext(context, config, jcrModelSerializer, documentModel, compareBaseDocumentModel);

        UNIQUE_COMPONENT_ID = AngularPluginUtils.generateUniqueComponentId(APP_NAME, 6);

        angularFieldPanel = new AngularFieldPanel("angularfield-container", UNIQUE_COMPONENT_ID, fieldContext, APP_NAME);

        // Processing request handlers
        angularFieldPanel.add(new PluginHandlerBehavior(fieldContext));
        // Adding a perspective switch behavior
        angularFieldPanel.add(new SwitchPerspectiveBehavior(fieldContext, "switchPerspective"));


        if (isEditMode()) {
            angularFieldPanel.add(new AttributeModifier("mode", "edit"));
        } else if (isCompareMode()) {
            angularFieldPanel.add(new AttributeModifier("mode", "compare"));
        }

        add(angularFieldPanel);
    }

    private JcrModelSerializer createModelSerializerInstance() {

        // Create instance of the Model Serializer class
        if (!getPluginConfig().containsKey(PluginConstants.MODEL_SERIALIZER)) {
            log.warn("no model serializer configured for this field, using default");
        }
        final String modelSerializer = getPluginConfig().getString(PluginConstants.MODEL_SERIALIZER, PluginConstants.DEFAULT_MODEL_SERIALIZER);

        try {
            Class dialogClass = Class.forName(modelSerializer);
            final Constructor constructor = dialogClass.getConstructor(String.class);
            String fieldName = getPluginConfig().getString(PluginConstants.PLUGIN_FIELD_NAME);
            JcrModelSerializer jcrModelSerializer = (JcrModelSerializer) constructor.newInstance(fieldName);

            return jcrModelSerializer;

        } catch (ClassNotFoundException e) {
            log.error("Cannot create serializer with name '{}'", modelSerializer, e);
        } catch (NoSuchMethodException e) {
            log.error("Cannot create serializer with name '{}'", modelSerializer, e);
        } catch (InvocationTargetException e) {
            log.error("Cannot create serializer with name '{}'", modelSerializer, e);
        } catch (InstantiationException e) {
            log.error("Cannot create serializer with name '{}'", modelSerializer, e);
        } catch (IllegalAccessException e) {
            log.error("Cannot create serializer with name '{}'", modelSerializer, e);
        }

        return null;
    }

    protected Panel getAngularFieldPanel() {
        return angularFieldPanel;
    }


    protected boolean isEditMode() {
        return IEditor.Mode.EDIT.equals(IEditor.Mode.fromString(getPluginConfig().getString("mode", "view")));
    }

    protected boolean isCompareMode() {
        return IEditor.Mode.COMPARE.equals(IEditor.Mode.fromString(getPluginConfig()
                .getString("mode", "view")));
    }

    protected String getAngularPluginConfiguration(String key) {
        if (key == null || key.equals("")) {
            key = PluginConstants.PLUGIN_CONFIGURATION;
        }

        // Get the data from getPluginConfig().getString(key);
        return this.getPluginConfig().getString(key, "{}");
    }

}
