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
package com.grayfox.server.ws.rest;

import java.util.Locale;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseRestComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRestComponent.class);

    @Context private HttpHeaders headers;

    protected Locale getClientLocale() {
        if (headers != null && headers.getAcceptableLanguages() != null && headers.getAcceptableLanguages().size() > 0) {
            for (Locale locale : headers.getAcceptableLanguages()) {
                LOGGER.debug("Client primary language: '{}'", locale.getLanguage());
                return new Locale(locale.getLanguage());
            }
        }

        return null;
    }
}