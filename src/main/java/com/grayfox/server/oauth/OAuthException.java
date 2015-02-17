package com.grayfox.server.oauth;

import com.grayfox.server.BaseApplicationException;

public class OAuthException extends BaseApplicationException {

    private static final long serialVersionUID = -6334172687732382276L;

    private OAuthException(String messageKey, Object[] formatArgs) {
        super(messageKey, formatArgs);
    }

    private OAuthException(String messageKey, Throwable cause, Object[] formatArgs) {
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
        public OAuthException build() {
            if (getCause() == null) return new OAuthException(getMessageKey(), getFormatArgs());
            else return new OAuthException(getMessageKey(), getCause(), getFormatArgs());
        }
    }
}