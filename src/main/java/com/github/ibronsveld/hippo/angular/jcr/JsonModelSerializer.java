package com.github.ibronsveld.hippo.angular.jcr;

import com.google.gson.*;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.util.ArrayList;

public class JsonModelSerializer extends JcrModelSerializer {

    private final String propertyName;

    public JsonModelSerializer(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public Node appendJsonToNode(Node node, String json) throws RepositoryException {

        JsonParser parser = new JsonParser();
        JsonArray jsonObject = parser.parse(json).getAsJsonArray();

        //TODO: test

        if (jsonObject.isJsonArray())
        {
            ArrayList<String> stringArrayList = new ArrayList<>();
            JsonArray array = jsonObject.getAsJsonArray();
            if (array != null)
                for (JsonElement elem : array) {
                    stringArrayList.add(elem.toString());
                }
            node.setProperty(this.propertyName, stringArrayList.toArray(new String[stringArrayList.size()]));
        } else {
            node.setProperty(this.propertyName, jsonObject.toString());
        }



//        if (node.hasProperty(this.propertyName)) {
//            if (node.getProperty(this.propertyName).isMultiple()) {
//                if (jsonObject.isJsonArray()) {
//                    ArrayList<String> stringArrayList = new ArrayList<>();
//                    JsonArray array = jsonObject.getAsJsonArray();
//                    for (JsonElement elem : array) {
//                        stringArrayList.add(elem.toString());
//                    }
//                    node.setProperty(this.propertyName, stringArrayList.toArray(new String[stringArrayList.size()]));
//                } else {
//                    node.setProperty(this.propertyName, jsonObject.toString());
//                }
//            } else {
//                if (!jsonObject.isJsonArray()) {
//                    node.setProperty(this.propertyName, jsonObject.toString());
//                } else {
//                    JsonArray array = jsonObject.getAsJsonArray();
//                    node.setProperty(this.propertyName, array.get(0).toString());
//                }
//            }
//        }

        return node;
    }

    @Override
    public JsonObject convertNodeToJson(JsonObject jsonObject, Node node) throws RepositoryException {
        JsonObject jsonObj = super.convertNodeToJson(jsonObject, node);

        if (node.hasProperty(this.propertyName)) {
            Value[] values = null;
            final Property property = node.getProperty(this.propertyName);
            if (property.isMultiple()) {
                values = property.getValues();

            } else {
                values = new Value[1];
                values[0] = property.getValue();
            }

            // Create array of strings
            JsonArray array = new JsonArray();
            JsonParser jsonParser = new JsonParser();

            for (int i = 0; i < values.length; i++) {
                if (values[i] != null && !values[i].getString().equals("")) {
                    array.add(jsonParser.parse(values[i].getString()).getAsJsonObject());
                }
            }

            String json = new GsonBuilder().create().toJson(array);
            jsonObj.addProperty("data", json);
            return jsonObj;
        }
        return new JsonObject();
    }
}
