package com.grayfox.server.route;

import com.grayfox.server.BaseApplicationException;

public class RouteException extends BaseApplicationException {

    private static final long serialVersionUID = -6334172687732382276L;

    private RouteException(String messageKey, Object[] formatArgs) {
        super(messageKey, formatArgs);
    }

    private RouteException(String messageKey, Throwable cause, Object[] formatArgs) {
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
        public RouteException build() {
            if (getCause() == null) return new RouteException(getMessageKey(), getFormatArgs());
            else return new RouteException(getMessageKey(), getCause(), getFormatArgs());
        }
    }
}