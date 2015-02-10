package com.grayfox.server.ws.rest.handler;

import java.util.Collection;
import java.util.Locale;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.ExceptionMapper;

abstract class BaseExceptionHandler<E extends Throwable> implements ExceptionMapper<E> {

    @Context private HttpHeaders headers;

    protected Locale getClientLocale(Collection<Locale> supportedLocales, Locale defaultLocale) {
        if (headers != null && headers.getAcceptableLanguages() != null && headers.getAcceptableLanguages().size() > 0) {
            for (Locale locale : headers.getAcceptableLanguages()) {
                if (supportedLocales.contains(locale)) {
                    return locale;
                }
            }
        }

        return defaultLocale;
    }
}