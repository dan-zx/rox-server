package com.grayfox.server.route;

import com.grayfox.server.BaseApplicationException;

public class RouteException extends BaseApplicationException {

    private static final long serialVersionUID = -4028153845430353925L;

    private RouteException(Builder builder) {
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
        public RouteException build() {
            return new RouteException(this);
        }
    }
}