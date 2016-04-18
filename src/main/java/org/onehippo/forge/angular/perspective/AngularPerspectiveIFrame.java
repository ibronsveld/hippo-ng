package org.onehippo.forge.angular.perspective;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * This panel is used to render the iFrame with the application
 */
public class AngularPerspectiveIFrame extends WebComponent {

    public AngularPerspectiveIFrame(String id, IModel model) {
        super(id, model);
    }

    protected void onComponentTag(ComponentTag tag) {
        checkComponentTag(tag, "iframe");
        tag.put("src", getDefaultModelObjectAsString());
        // since Wicket 1.4 you need to use getDefaultModelObjectAsString() instead of getModelObjectAsString()
        super.onComponentTag(tag);
    }
}
