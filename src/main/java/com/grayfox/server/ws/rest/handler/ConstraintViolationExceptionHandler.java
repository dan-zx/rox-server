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

import java.util.Locale;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.grayfox.server.util.Messages;
import com.grayfox.server.ws.rest.BaseRestComponent;
import com.grayfox.server.ws.rest.response.ErrorResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class ConstraintViolationExceptionHandler extends BaseRestComponent implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstraintViolationExceptionHandler.class);

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        StringBuilder messageBuilder = new StringBuilder();
        StringBuilder logMessageBuilder = new StringBuilder();
        Locale clientLocale = getClientLocale();
        for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
            String message = Messages.get(constraintViolation.getMessageTemplate(), clientLocale, constraintViolation.getInvalidValue());
            messageBuilder.append(message).append(", ");
            logMessageBuilder.append(constraintViolation.getMessageTemplate()).append("->[")
                .append(constraintViolation.getInvalidValue()).append("], ");
        }
        logMessageBuilder.delete(logMessageBuilder.length()-2, logMessageBuilder.length());
        messageBuilder.delete(messageBuilder.length()-2, messageBuilder.length());
        LOGGER.error("Constraints violated in arguments: {}", logMessageBuilder);
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse("param.validation.error", messageBuilder.toString()).toJson())
                .build();
    }
}