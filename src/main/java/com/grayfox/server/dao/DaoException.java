package com.grayfox.server.dao;

import com.grayfox.server.BaseApplicationException;

public class DaoException extends BaseApplicationException {

    private static final long serialVersionUID = 6442324698959192799L;

    private DaoException() { }

    private DaoException(String messageKey, Object[] messageArguments, Throwable cause) {
        super(messageKey, messageArguments, cause);
    }

    private DaoException(String messageKey, Object[] messageArguments) {
        super(messageKey, messageArguments);
    }

    private DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    private DaoException(String message) {
        super(message);
    }

    private DaoException(Throwable cause) {
        super(cause);
    }

    public static class Builder extends BaseBuilder<DaoException> {

        @Override
        public DaoException build() {
            if (getMessage() != null) {
                if (getCause() != null) return new DaoException(getMessage(), getCause());
                return new DaoException(getMessage());
            }
            if (getMessageKey() != null) {
                if (getCause() != null) return new DaoException(getMessageKey(), getMessageArguments(), getCause());
                return new DaoException(getMessageKey(), getMessageArguments());
            }
            if (getCause() != null) return new DaoException(getCause());
            return new DaoException();
        }
    }
}