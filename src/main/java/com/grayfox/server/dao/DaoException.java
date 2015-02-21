package com.grayfox.server.dao;

import com.grayfox.server.BaseApplicationException;

public class DaoException extends BaseApplicationException {

    private static final long serialVersionUID = 6442324698959192799L;

    private DaoException(Builder builder) {
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
        public DaoException build() {
            return new DaoException(this);
        }
    }
}