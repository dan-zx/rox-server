package com.grayfox.server.ws.rest.handler;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.grayfox.server.util.Messages;
import com.grayfox.server.ws.rest.BaseRestComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataAccessException;

@Provider
public class DataAccessExceptionHandler extends BaseRestComponent implements ExceptionMapper<DataAccessException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataAccessExceptionHandler.class);

    @Override
    public Response toResponse(DataAccessException exception) {
        LOGGER.error("Data access failure", exception);
        String messageKey = "data.internal.error";
        String message = Messages.get(messageKey, getClientLocale());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResult(messageKey, message).toJson())
                .build();
    }
}