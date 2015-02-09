package com.grayfox.server.datasource;

import com.grayfox.server.GrayFoxException;

public class DataSourceException extends GrayFoxException {

    private static final long serialVersionUID = -6334172687732382276L;

    private DataSourceException(String messageKey, Object[] formatArgs) {
        super(messageKey, formatArgs);
    }

    private DataSourceException(String messageKey, Throwable cause, Object[] formatArgs) {
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
        public DataSourceException build() {
            if (getCause() == null) return new DataSourceException(getMessageKey(), getFormatArgs());
            else return new DataSourceException(getMessageKey(), getCause(), getFormatArgs());
        }
    }
}