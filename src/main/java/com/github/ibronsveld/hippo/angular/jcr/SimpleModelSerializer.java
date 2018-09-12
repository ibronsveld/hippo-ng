package com.github.ibronsveld.hippo.angular.jcr;

import com.google.gson.*;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

/**
 * Simple String Serializer, saves the value as a string to the field
 */
public class SimpleModelSerializer extends JcrModelSerializer {

    private final String propertyName;

    public SimpleModelSerializer(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public Node appendJsonToNode(Node node, String json) throws RepositoryException {
        node.setProperty(this.propertyName, json);
        return node;
    }

    @Override
    public JsonObject convertNodeToJson(JsonObject jsonObject, Node node) throws RepositoryException {
        JsonObject jsonObj = super.convertNodeToJson(jsonObject, node);

        if (node.hasProperty(this.propertyName)) {
            final Property property = node.getProperty(this.propertyName);
            String value = property.getValue().getString();
            jsonObj.addProperty("data", value);
        } else {
            jsonObj.addProperty("data", "");
        }

        return jsonObj;
    }
}
