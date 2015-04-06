package com.grayfox.server.util;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Messages {

    private static final Collection<Locale> SUPPORTED_LOCALES = Collections.singletonList(new Locale("es"));
    private static final String RESOURCE_BUNDLE_BASE_NAME = "com.grayfox.server.messages";
    private static final String MISSING_RESOURCE_KEY_FORMAT = "???%s???";
    private static final Logger LOGGER = LoggerFactory.getLogger(Messages.class);

    private Messages() {
        throw new IllegalAccessError("This class cannot be instantiated nor extended");
    }

    public static String get(String key) {
        try {
            return ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, Locale.ROOT).getString(key);
        } catch (MissingResourceException ex) {
            LOGGER.warn("Can't find message for key: [{}]", key, ex);
            return String.format(MISSING_RESOURCE_KEY_FORMAT, key);
        }
    }

    public static String get(String key, Locale locale) {
        try {
            ResourceBundle bundle = SUPPORTED_LOCALES.contains(locale) ? ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, locale) : ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, Locale.ROOT);
            return bundle.getString(key);
        } catch (MissingResourceException ex) {
            LOGGER.warn("Can't find message for key: [{}]", key, ex);
            return String.format(MISSING_RESOURCE_KEY_FORMAT, key);
        }
    }

    public static String get(String key, Object[] formatArgs) {
        String unformattedMessage = get(key);
        if (formatArgs != null && formatArgs.length > 0) {
            try {
                return MessageFormat.format(unformattedMessage, formatArgs);
            } catch (IllegalArgumentException ex) {
                LOGGER.warn("Can't format message: [{}] with args: {}", unformattedMessage, Arrays.deepToString(formatArgs), ex);
            }
        }
        return unformattedMessage;
    }

    public static String get(String key, Locale locale, Object[] formatArgs) {
        String unformattedMessage = get(key, locale);
        if (formatArgs != null && formatArgs.length > 0) {
            try {
                return MessageFormat.format(unformattedMessage, formatArgs);
            } catch (IllegalArgumentException ex) {
                LOGGER.warn("Can't format message: [{}] with args: {}", unformattedMessage, Arrays.deepToString(formatArgs), ex);
            }
        }
        return unformattedMessage;
    }
}