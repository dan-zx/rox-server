package com.grayfox.server.ws.rest.handler;

import java.util.Locale;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.grayfox.server.util.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.transaction.TransactionException;

@Provider
public class TransactionExceptionHandler extends BaseExceptionHandler<TransactionException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionExceptionHandler.class);

    @Override
    public Response toResponse(TransactionException exception) {
        LOGGER.error("Transaction failure", exception);
        String messageKey = "transaction.internal.error";
        Locale clientLocale = getClientLocale(Messages.SUPPORTED_LOCALES, Messages.DEFAULT_LOCALE);
        String message = Messages.get(messageKey, clientLocale);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResult(messageKey, message).toJson())
                .build();
    }
}