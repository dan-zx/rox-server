package com.grayfox.server.ws.rest.handler;

import java.util.Locale;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.grayfox.server.Messages;
import com.grayfox.server.ws.rest.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class InvalidFormatExceptionHandler extends ExceptionHandler<InvalidFormatException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidFormatExceptionHandler.class);

    @Override
    public Response toResponse(InvalidFormatException exception) {
        LOGGER.error("Invalid format in arguments", exception);
        Locale clientLocale = getClientLocale(Messages.SUPPORTED_LOCALES, Messages.DEFAULT_LOCALE);
        String message = Messages.get(exception.getMessageKey(), clientLocale, exception.getFormatArgs());
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResult(exception.getMessageKey(), message).toJson())
                .build();
    }
}