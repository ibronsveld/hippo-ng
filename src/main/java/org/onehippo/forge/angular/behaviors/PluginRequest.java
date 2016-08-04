package org.onehippo.forge.angular.behaviors;

import com.google.gson.*;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class PluginRequest {

    protected final WebRequest webRequest;
    protected final HttpServletRequest httpServletRequest;

    protected PluginRequest(WebRequest webRequest) {
        this.webRequest = webRequest;
        this.httpServletRequest = (HttpServletRequest) webRequest.getContainerRequest();
    }

    public String getRequestBodyAsString() {
        try {
            BufferedReader br = httpServletRequest.getReader();
            String contents = br.readLine();
            br.close();

            return contents;

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Object getRequestBodyAs(Class clazz) {
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
