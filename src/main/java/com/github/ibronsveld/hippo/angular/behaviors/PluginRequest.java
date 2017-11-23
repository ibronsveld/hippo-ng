package com.github.ibronsveld.hippo.angular.behaviors;

import com.google.gson.*;
import org.apache.wicket.request.http.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class PluginRequest {

    protected final WebRequest webRequest;
    protected final HttpServletRequest httpServletRequest;
    protected final JsonObject requestBody;
    protected final JsonElement requestData;

    protected PluginRequest(WebRequest webRequest) throws Exception {
        this.webRequest = webRequest;
        this.httpServletRequest = (HttpServletRequest) webRequest.getContainerRequest();

        // TODO: Figure out a way to handle mime types?
        // For now, assume it is JSON
        JsonElement elem = this.getAsJsonElement();
        if (elem.isJsonObject()) {
            requestBody = (JsonObject) elem.getAsJsonObject();

            if (requestBody.has("data")) {
                requestData = requestBody.get("data");
            } else {
                requestData = null;
            }
        } else {
            throw new Exception("No data present");
        }

    }

    public String getAction() {
        if (requestBody.has("action")) {
            return requestBody.get("action").getAsString();
        }
        return null;
    }

    public String getAsString(String attribute) {
        return this.get(attribute).getAsString();
    }

    public String getAsString() {
        if (requestData != null) {
            return requestData.toString();
        }
        return null;
    }

    /**
     * Converts the data to a certain type
     *
     * @param clazz type to convert to
     * @return instance of the object
     */
    public Object getAsObject(Class clazz) {
        if (requestData != null) {
            return this.getRequestBodyAs(requestData, clazz);
        } else {
            return null;
        }
    }

    /**
     * Returns the JsonElement as part of the data element.
     *
     * @param attribute
     * @return
     */
    public JsonElement get(String attribute) {
        // The SDK will always embed the actual request in the 'data' attribute.
        if (requestData != null) {
            if (requestData.isJsonObject()) {
                JsonObject jsonObject = requestData.getAsJsonObject();
                if (jsonObject.has(attribute)) {
                    return jsonObject.get(attribute);
                }
            }
        }
        return null;
    }

    private JsonElement getAsJsonElement() {
        try {
            JsonParser jsonParser = new JsonParser();

            BufferedReader br = httpServletRequest.getReader();
            String contents = br.readLine();
            br.close();

            return jsonParser.parse(contents);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Object getRequestBodyAs(JsonElement elem, Class clazz) {
        try {
            if (elem != null) {
                String contents = elem.getAsString();

                if ((contents == null) || contents.isEmpty()) {
                    // No JSON
                    return null;
                } else {
                    // Got JSON
                    Gson gson = new GsonBuilder().create();
                    Object result = gson.fromJson(contents, clazz);

                    return result;
                }
            }
            return null;
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex);
        }
    }
}
