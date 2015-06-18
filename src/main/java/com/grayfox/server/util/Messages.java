/*
 * Copyright 2014-2015 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    private static final Collection<Locale> SUPPORTED_LOCALES = Collections.singletonList(Constants.Locales.SPANISH);
    private static final String RESOURCE_BUNDLE_BASE_NAME = "com.grayfox.server.messages";
    private static final String MISSING_RESOURCE_KEY_FORMAT = "???%s???";
    private static final Logger LOGGER = LoggerFactory.getLogger(Messages.class);

    private Messages() {
        throw new IllegalAccessError("This class cannot be instantiated nor extended");
    }

    public static String get(String key, Object... messageArguments) {
        String unformattedMessage;
        try {
            unformattedMessage = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, Locale.ROOT).getString(key);
        } catch (MissingResourceException ex) {
            LOGGER.warn("Can't find message for key: [{}]", key, ex);
            return String.format(MISSING_RESOURCE_KEY_FORMAT, key);
        }
        if (messageArguments.length > 0) {
            try {
                return MessageFormat.format(unformattedMessage, messageArguments);
            } catch (IllegalArgumentException ex) {
                LOGGER.warn("Can't format message: [{}] with arguments: {}", unformattedMessage, Arrays.deepToString(messageArguments), ex);
            }
        }
        return unformattedMessage;
    }

    public static String get(String key, Locale locale, Object... messageArguments) {
        String unformattedMessage;
        try {
            ResourceBundle bundle = SUPPORTED_LOCALES.contains(locale) ? ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, locale) : ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, Locale.ROOT);
            unformattedMessage = bundle.getString(key);
        } catch (MissingResourceException ex) {
            LOGGER.warn("Can't find message for key: [{}]", key, ex);
            return String.format(MISSING_RESOURCE_KEY_FORMAT, key);
        }
        if (messageArguments.length > 0) {
            try {
                return MessageFormat.format(unformattedMessage, messageArguments);
            } catch (IllegalArgumentException ex) {
                LOGGER.warn("Can't format message: [{}] with arguments: {}", unformattedMessage, Arrays.deepToString(messageArguments), ex);
            }
        }
        return unformattedMessage;
    }
}