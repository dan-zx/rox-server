package com.grayfox.server.ws.rest.handler;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.grayfox.server.util.Messages;
import com.grayfox.server.ws.rest.BaseRestComponent;
import com.grayfox.server.ws.rest.response.ErrorResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.transaction.TransactionException;

@Provider
public class TransactionExceptionHandler extends BaseRestComponent implements ExceptionMapper<TransactionException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionExceptionHandler.class);

    @Override
    public Response toResponse(TransactionException exception) {
        LOGGER.error("Transaction failure", exception);
        String messageKey = "transaction.internal.error";
        String message = Messages.get(messageKey, getClientLocale());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse(messageKey, message).toJson())
                .build();
    }
}