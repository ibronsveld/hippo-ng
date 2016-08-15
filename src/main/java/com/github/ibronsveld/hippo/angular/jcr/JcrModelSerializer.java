package com.github.ibronsveld.hippo.angular.jcr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.jcr.Node;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

public abstract class JcrModelSerializer {

    private static String DOCUMENT_FIELDS = "hippostd:holder,hippostdpubwf:createdBy,hippostdpubwf:lastModifiedBy,hippostdpubwf:creationDate,hippostdpubwf:lastModificationDate,hippotranslation:locale";

    public JsonObject convertNodeToJson(JsonObject jsonObject, Node node) throws RepositoryException {
        JsonObject base = new JsonObject();

        Gson gson = new GsonBuilder().create();

        // Attach all the important document attributes as well
        String[] documentFields = DOCUMENT_FIELDS.split(",");
        for (String docField : documentFields) {
            if (node.hasProperty(docField)) {
                final Value jcrValue = node.getProperty(docField).getValue();
                switch (jcrValue.getType()) {
                    case PropertyType.STRING:
                        base.addProperty(docField, jcrValue.getString());
                        break;
                    case PropertyType.DATE:
                        String date = gson.toJson(jcrValue.getDate());
                        base.addProperty(docField, date);
                        break;
                }
            }
        }

        jsonObject.add("document", base);

        return jsonObject;
    }

    public abstract Node appendJsonToNode(Node node, String json) throws RepositoryException;
}
