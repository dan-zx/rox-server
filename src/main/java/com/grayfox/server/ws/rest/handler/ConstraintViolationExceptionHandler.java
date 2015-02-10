package com.grayfox.server.ws.rest.handler;

import java.util.Locale;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.grayfox.server.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class ConstraintViolationExceptionHandler extends BaseExceptionHandler<ConstraintViolationException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstraintViolationExceptionHandler.class);

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Locale clientLocale = getClientLocale(Messages.SUPPORTED_LOCALES, Messages.DEFAULT_LOCALE);
        StringBuilder messageBuilder = new StringBuilder();
        StringBuilder logMessageBuilder = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
            String message = Messages.get(constraintViolation.getMessageTemplate(), clientLocale, new Object[] {constraintViolation.getInvalidValue()});
            messageBuilder.append(message).append(", ");
            logMessageBuilder.append(constraintViolation.getMessageTemplate()).append("->[")
                .append(constraintViolation.getInvalidValue()).append("], ");
        }
        logMessageBuilder.delete(logMessageBuilder.length()-2, logMessageBuilder.length());
        messageBuilder.delete(messageBuilder.length()-2, messageBuilder.length());
        LOGGER.error("Constraints violated in arguments: {}", logMessageBuilder);
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResult("param.validation.error", messageBuilder.toString()))
                .build();
    }
}