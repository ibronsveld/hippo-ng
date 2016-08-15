package com.github.ibronsveld.hippo.angular;

import org.apache.wicket.request.http.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class AngularPluginUtils {

    public static String generateUniqueComponentId(String appName, int maxLength) {
        int range = (Integer.MAX_VALUE - 0) + 1;
        int intValue = (int) (Math.random() * range) + 0;

        String randomValue = Integer.toString(intValue).substring(0, maxLength);
        return "ng-" + appName + "-" + randomValue;
    }

    @Deprecated
    public static String getContentFromRequest(WebRequest wr) {
        HttpServletRequest hsr =
                (HttpServletRequest) wr.getContainerRequest();

        try {
            BufferedReader br = hsr.getReader();
            String contents = br.readLine();
            br.close();

            if ((contents == null) || contents.isEmpty()) {
                // No JSON
                return null;
            } else {
                // Got JSON
                return contents;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
