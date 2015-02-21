package com.grayfox.server.datasource;

import com.grayfox.server.BaseApplicationException;

public class DataSourceException extends BaseApplicationException {

    private static final long serialVersionUID = 1496010188766789298L;

    private DataSourceException(Builder builder) {
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
        public DataSourceException build() {
            return new DataSourceException(this);
        }
    }
}