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
package com.grayfox.server.ws.rest.handler;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.grayfox.server.exception.BaseApplicationException;
import com.grayfox.server.util.Messages;
import com.grayfox.server.ws.rest.BaseRestComponent;
import com.grayfox.server.ws.rest.response.ErrorResponse;

@Provider
public class ApplicationExceptionHandler extends BaseRestComponent implements ExceptionMapper<BaseApplicationException> {

    @Override
    public Response toResponse(BaseApplicationException exception) {
        if (exception.getMessageKey() == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new ErrorResponse("server.internal.error", exception.getMessage()).toJson())
                    .build();
        }
        String message = Messages.get(exception.getMessageKey(), getClientLocale(), exception.getMessageArguments());
        switch (exception.getMessageKey()) {
            case "user.invalid.error":
                return Response.status(Response.Status.UNAUTHORIZED)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(new ErrorResponse(exception.getMessageKey(), message).toJson())
                        .build();
            case "location.is_null.error":
            case "location.format.error":
                return Response.status(Response.Status.BAD_REQUEST)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(new ErrorResponse("param.validation.error", message).toJson())
                        .build();
            default: 
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(new ErrorResponse(exception.getMessageKey(), message).toJson())
                        .build();
        }
    }
}