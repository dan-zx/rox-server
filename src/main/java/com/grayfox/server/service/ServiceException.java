package com.grayfox.server.service;

import com.grayfox.server.BaseApplicationException;

public class ServiceException extends BaseApplicationException {

    private static final long serialVersionUID = 474365738037258460L;

    private ServiceException(Builder builder) {
        super(builder);
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
            return new ServiceException(this);
        }
    }
}