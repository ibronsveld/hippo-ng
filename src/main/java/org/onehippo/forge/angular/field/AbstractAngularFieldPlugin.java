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
package org.onehippo.forge.angular.field;

import com.google.gson.*;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.http.WebRequest;
import org.hippoecm.frontend.model.IModelReference;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.model.event.IObserver;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.service.IEditor;
import org.hippoecm.frontend.service.render.RenderPlugin;
import org.onehippo.forge.angular.AngularPluginContext;
import org.onehippo.forge.angular.AngularPluginUtils;
import org.onehippo.forge.angular.PluginConstants;
import org.onehippo.forge.angular.behaviors.AbstractCustomPluginBehavior;
import org.onehippo.forge.angular.jcr.JcrModelSerializer;
import org.onehippo.forge.angular.behaviors.SwitchPerspectiveBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractAngularFieldPlugin extends RenderPlugin<Node> implements IObserver {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(AbstractAngularFieldPlugin.class);

    private JcrNodeModel documentModel;
    private JcrNodeModel compareBaseDocumentModel;

    private final JcrModelSerializer jcrModelSerializer;

    private final AngularPluginContext fieldContext;
    private final String APP_NAME;
    private final String UNIQUE_COMPONENT_ID;

    protected Panel angularFieldPanel;

    public AbstractAngularFieldPlugin(final IPluginContext context, IPluginConfig config) {
        super(context, config);
        setOutputMarkupId(true);

        APP_NAME = getPluginConfig().getString(PluginConstants.ANGULAR_FIELD_APPNAME);
        documentModel = (JcrNodeModel) getModel();

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
        jcrModelSerializer = createModelSerializerInstance();
        fieldContext = new AngularPluginContext(context, config, jcrModelSerializer);

        UNIQUE_COMPONENT_ID = AngularPluginUtils.generateUniqueComponentId(APP_NAME, 6);

        angularFieldPanel = new AngularFieldPanel("angularfield-container", UNIQUE_COMPONENT_ID, fieldContext, APP_NAME);
        angularFieldPanel.add(new UpdateModelDataBehaviour(fieldContext, "setModel"));
        angularFieldPanel.add(new GetModelDataBehaviour(fieldContext, "getModel"));
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
            String fieldName = getPluginConfig().getString(PluginConstants.ANGULAR_FIELD_NAME);
            JcrModelSerializer jcrModelSerializer = (JcrModelSerializer) constructor.newInstance(fieldName);

            return jcrModelSerializer;

        } catch (ClassNotFoundException e) {
            log.error("Cannot create dialog with name '{}'", modelSerializer, e);
        } catch (NoSuchMethodException e) {
            log.error("Cannot create dialog with name '{}'", modelSerializer, e);
        } catch (InvocationTargetException e) {
            log.error("Cannot create dialog with name '{}'", modelSerializer, e);
        } catch (InstantiationException e) {
            log.error("Cannot create dialog with name '{}'", modelSerializer, e);
        } catch (IllegalAccessException e) {
            log.error("Cannot create dialog with name '{}'", modelSerializer, e);
        }

        return null;
    }

    protected Panel getAngularFieldPanel() {
        return angularFieldPanel;
    }

    protected JcrNodeModel getCompareBaseDocumentModel() {
        return compareBaseDocumentModel;
    }

    protected JcrNodeModel getDocumentModel() {
        return documentModel;
    }

    public String getModelAsJson() {
        JsonObject jsonObject = new JsonObject();
        final Gson gson = new GsonBuilder().create();

        try {
            JsonObject modelObject = new JsonObject();
            modelObject = AbstractAngularFieldPlugin.this.jcrModelSerializer.convertNodeToJson(modelObject, documentModel.getNode());
            jsonObject.add("model", modelObject);

            if (compareBaseDocumentModel != null) {
                JsonObject compareObject = new JsonObject();
                compareObject = AbstractAngularFieldPlugin.this.jcrModelSerializer.convertNodeToJson(compareObject, compareBaseDocumentModel.getNode());
                jsonObject.add("compareModel", compareObject);
            } else {
                //jsonObject.add("compareModel", "");
            }

        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    protected boolean isEditMode() {
        return IEditor.Mode.EDIT.equals(IEditor.Mode.fromString(getPluginConfig().getString("mode", "view")));
    }

    protected boolean isCompareMode() {
        return IEditor.Mode.COMPARE.equals(IEditor.Mode.fromString(getPluginConfig()
                .getString("mode", "view")));
    }

    protected String getJsonFromRequest(WebRequest wr) {
        HttpServletRequest hsr =
                (HttpServletRequest) wr.getContainerRequest();

        try {
            BufferedReader br = hsr.getReader();
            String jsonString = br.readLine();
            br.close();

            if ((jsonString == null) || jsonString.isEmpty()) {
                // No JSON
                return null;
            } else {
                // Got JSON
                return jsonString;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private class GetModelDataBehaviour extends AbstractCustomPluginBehavior {
        private static final long serialVersionUID = 1L;

        private AngularPluginContext context;
        private String componentTag;
        private JcrNodeModel model;

        public GetModelDataBehaviour(AngularPluginContext context, String componentTag) {
            super(context, componentTag);
        }

        @Override
        public void onRequest() {
            RequestCycle requestCycle = RequestCycle.get();

            // Serialize the model data in a JSON object
            JsonObject jsonObject = new JsonObject();
            final Gson gson = new GsonBuilder().create();

            final String fieldJson = AbstractAngularFieldPlugin.this.getModelAsJson();

            requestCycle.scheduleRequestHandlerAfterCurrent(
                    new TextRequestHandler("application/json",
                            "UTF-8", ""));
        }
    }
    private final class UpdateModelDataBehaviour extends AbstractCustomPluginBehavior {
        private static final long serialVersionUID = 1L;

        public UpdateModelDataBehaviour(AngularPluginContext context, String componentTag) {
            super(context, componentTag);
        }

        @Override
        public void onRequest() {
            RequestCycle requestCycle = RequestCycle.get();
            WebRequest wr = (WebRequest) requestCycle.getRequest();

            String jsonString = AbstractAngularFieldPlugin.this.getJsonFromRequest(wr);
            try {
                AbstractAngularFieldPlugin.this.jcrModelSerializer.appendJsonToNode(
                        AbstractAngularFieldPlugin.this.documentModel.getNode(),
                        jsonString
                );
            } catch (RepositoryException e) {
                e.printStackTrace();
            }

            final String fieldJson = AbstractAngularFieldPlugin.this.getModelAsJson();


            requestCycle.scheduleRequestHandlerAfterCurrent(
                    new TextRequestHandler("application/json",
                            "UTF-8", fieldJson));
        }
    }
}
