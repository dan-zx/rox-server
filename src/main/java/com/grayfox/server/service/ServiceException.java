package com.grayfox.server.service;

import com.grayfox.server.BaseApplicationException;

public class ServiceException extends BaseApplicationException {

    private static final long serialVersionUID = -6334172687732382276L;

    private ServiceException(String messageKey, Object[] formatArgs) {
        super(messageKey, formatArgs);
    }

    private ServiceException(String messageKey, Throwable cause, Object[] formatArgs) {
        super(messageKey, cause, formatArgs);
    }

    public static class Builder extends BaseBuilder {

        public Builder(String messageKey) {
            super(messageKey);
        }

        @Override
        public Builder addFormatArg(Object formatArg) {
            return (Builder) super.addFormatArg(formatArg);
        }
        
        @Override
        public Builder setCause(Throwable cause) {
            return (Builder) super.setCause(cause);
        }

        @Override
        public ServiceException build() {
            if (getCause() == null) return new ServiceException(getMessageKey(), getFormatArgs());
            else return new ServiceException(getMessageKey(), getCause(), getFormatArgs());
        }
    }
}