package com.grayfox.server.ws.rest.handler;

import java.util.Locale;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.grayfox.server.Messages;
import com.grayfox.server.ws.rest.RequiredArgException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class RequiredArgExceptionHandler extends ExceptionHandler<RequiredArgException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequiredArgExceptionHandler.class);

    @Override
    public Response toResponse(RequiredArgException exception) {
        LOGGER.error("Required arguments", exception);
        String messageKey = "arg.required.error";
        Locale clientLocale = getClientLocale(Messages.SUPPORTED_LOCALES, Messages.DEFAULT_LOCALE);
        
        StringBuilder messageBuilder = new StringBuilder();
        for (int index = 0; ; index++) {
            messageBuilder.append(Messages.get(messageKey, clientLocale, new Object[] {exception.getRequiredArgs()[index]}));
            if (index == exception.getRequiredArgs().length-1) break;
            messageBuilder.append(", ");
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResult(messageKey, messageBuilder.toString()).toJson())
                .build();
    }
}