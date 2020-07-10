package com.itransition.lobach.renbook.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageManager {

    private static String RESOURCE_BUNDLE_NAME = "messages";
    private static final Locale defaulLocale = Locale.ENGLISH;

    private MessageManager() {
        super();
    }

    public static String getMessage(String propertyName) {
        return getMessage(propertyName, defaulLocale);
    }

    public static String getMessage(String propertyName, Locale locale) {
        String message = null;
        try {
            ResourceBundle bundle;
            if (locale != null) {
                bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME, locale);
            } else {
                bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
            }
            message = bundle.getString(propertyName);
        } catch (MissingResourceException e) {
            //logger.error("Resource not found: ", e);
        }
        return message;
    }
}
