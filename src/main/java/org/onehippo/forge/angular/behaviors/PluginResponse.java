package org.onehippo.forge.angular.behaviors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PluginResponse {

    protected String responseBody;

    protected PluginResponse() {

    }

    public void addResponseBody(String contents) {
        responseBody = contents;
    }

    public void addResponseBody(Object o) {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(o);

        this.addResponseBody(json);
    }
}
