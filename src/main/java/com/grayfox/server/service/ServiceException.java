package com.grayfox.server.service;

import com.grayfox.server.BaseApplicationException;

public class ServiceException extends BaseApplicationException {

    private static final long serialVersionUID = 474365738037258460L;

    private ServiceException() { }

    private ServiceException(String messageKey, Object[] messageArguments, Throwable cause) {
        super(messageKey, messageArguments, cause);
    }

    private ServiceException(String messageKey, Object[] messageArguments) {
        super(messageKey, messageArguments);
    }

    private ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    private ServiceException(String message) {
        super(message);
    }

    private ServiceException(Throwable cause) {
        super(cause);
    }

    public static class Builder extends BaseBuilder<ServiceException> {

        @Override
        public ServiceException build() {
            if (getMessage() != null) {
                if (getCause() != null) return new ServiceException(getMessage(), getCause());
                return new ServiceException(getMessage());
            }
            if (getMessageKey() != null) {
                if (getCause() != null) return new ServiceException(getMessageKey(), getMessageArguments(), getCause());
                return new ServiceException(getMessageKey(), getMessageArguments());
            }
            if (getCause() != null) return new ServiceException(getCause());
            return new ServiceException();
        }
    }
}