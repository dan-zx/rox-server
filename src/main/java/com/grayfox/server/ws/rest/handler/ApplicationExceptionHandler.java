package com.grayfox.server.ws.rest.handler;

import java.util.Locale;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.grayfox.server.BaseApplicationException;
import com.grayfox.server.Messages;

@Provider
public class ApplicationExceptionHandler extends BaseExceptionHandler<BaseApplicationException> {

    @Override
    public Response toResponse(BaseApplicationException exception) {
        Locale clientLocale = getClientLocale(Messages.SUPPORTED_LOCALES, Messages.DEFAULT_LOCALE);
        String message = Messages.get(exception.getMessageKey(), clientLocale, exception.getFormatArgs());
        switch (exception.getMessageKey()) {
            case "route.unavailable.error": 
                return Response.status(Response.Status.BAD_REQUEST)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(new ErrorResult(exception.getMessageKey(), message).toJson())
                        .build();
            case "user.invalid.error":
                return Response.status(Response.Status.UNAUTHORIZED)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(new ErrorResult(exception.getMessageKey(), message).toJson())
                        .build();
            default: 
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(new ErrorResult(exception.getMessageKey(), message).toJson())
                        .build();
        }
    }
}