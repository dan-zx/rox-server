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
                return locale;
            }
        }

        return null;
    }

    protected HttpHeaders getHeaders() {
        return headers;
    }
}