package com.github.ibronsveld.hippo.angular.behaviors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PluginResponse {

    protected String responseBody;
    protected int resultCode;
    protected PluginResponse() {
        resultCode = 200;
    }

    public void addResponseBody(String contents) {
        responseBody = contents;
    }

    public void addResponseBody(Object o) {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(o);

        this.addResponseBody(json);
    }

    public void setError(int code, String message) {
        responseBody = message;
        resultCode = code;
    }
}
