package com.github.ibronsveld.hippo.angular.behaviors;

import com.google.gson.*;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class PluginRequest {

    protected final WebRequest webRequest;
    protected final HttpServletRequest httpServletRequest;
    protected final JsonObject requestBody;

    protected PluginRequest(WebRequest webRequest) {
        this.webRequest = webRequest;
        this.httpServletRequest = (HttpServletRequest) webRequest.getContainerRequest();

        // TODO: Figure out a way to handle mime types?
        // For now, assume it is JSON
        requestBody = (JsonObject) this.getRequestBodyAs(JsonObject.class);
    }

    public String getAction() {
        return this.getAsString("action");
    }

    public String getAsString(String attribute) {
        return this.get(attribute).getAsString();
    }

    public JsonElement get(String attribute) {
        if (requestBody.has(attribute)) {
            return requestBody.get(attribute);
        }
        return null;
    }

    @Deprecated
    private String getRequestBodyAsString() {
        try {
            BufferedReader br = httpServletRequest.getReader();
            String contents = br.readLine();
            br.close();

            return contents;

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    private Object getRequestBodyAs(Class clazz) {
        try {
            String contents = this.getRequestBodyAsString();

            if ((contents == null) || contents.isEmpty()) {
                // No JSON
                return null;
            } else {
                // Got JSON
                Gson gson = new GsonBuilder().create();
                Object result = gson.fromJson(contents, clazz);

                return result;
            }
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex);
        }
    }
}
