package com.grayfox.server.dao;

import com.grayfox.server.BaseApplicationException;

public class DaoException extends BaseApplicationException {

    private static final long serialVersionUID = -6334172687732382276L;

    private DaoException(String messageKey, Object[] formatArgs) {
        super(messageKey, formatArgs);
    }

    private DaoException(String messageKey, Throwable cause, Object[] formatArgs) {
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
        public DaoException build() {
            if (getCause() == null) return new DaoException(getMessageKey(), getFormatArgs());
            else return new DaoException(getMessageKey(), getCause(), getFormatArgs());
        }
    }
}