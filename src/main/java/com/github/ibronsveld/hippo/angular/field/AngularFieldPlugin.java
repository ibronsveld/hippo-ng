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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import com.github.ibronsveld.hippo.angular.PluginConstants;

public class AngularFieldPlugin extends AbstractAngularFieldPlugin {

    public AngularFieldPlugin(final IPluginContext context, IPluginConfig config) {
        super(context, config);
        add(new Label("angularfield-caption", getCaptionModel()));
    }

    protected IModel<String> getCaptionModel() {
        final String defaultCaption = new StringResourceModel("angularfield.caption", this, null,
                PluginConstants.DEFAULT_FIELD_CAPTION)
                .getString();
        String caption = getPluginConfig().getString("caption", defaultCaption);
        String captionKey = caption;
        return new StringResourceModel(captionKey, this, null, caption);
    }
}
