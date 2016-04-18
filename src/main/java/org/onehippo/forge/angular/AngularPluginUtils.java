package org.onehippo.forge.angular;

public class AngularPluginUtils {

    public static String generateUniqueComponentId(String appName, int maxLength) {
        int range = (Integer.MAX_VALUE - 0) + 1;
        int intValue = (int) (Math.random() * range) + 0;

        String randomValue = Integer.toString(intValue).substring(0, maxLength);
        return "ng-" + appName + "-" + randomValue;
    }
}
