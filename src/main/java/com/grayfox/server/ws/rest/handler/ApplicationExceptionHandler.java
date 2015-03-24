package com.grayfox.server.ws.rest.handler;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.grayfox.server.BaseApplicationException;
import com.grayfox.server.util.Messages;
import com.grayfox.server.ws.rest.BaseRestComponent;

@Provider
public class ApplicationExceptionHandler extends BaseRestComponent implements ExceptionMapper<BaseApplicationException> {

    @Override
    public Response toResponse(BaseApplicationException exception) {
        String message = Messages.get(exception.getMessageKey(), getClientLocale(), exception.getFormatArgs());
        switch (exception.getMessageKey()) {
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