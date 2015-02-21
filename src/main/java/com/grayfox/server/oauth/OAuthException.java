package com.grayfox.server.oauth;

import com.grayfox.server.BaseApplicationException;

public class OAuthException extends BaseApplicationException {

    private static final long serialVersionUID = 1301884314371840181L;

    private OAuthException(Builder builder) {
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
        public OAuthException build() {
            return new OAuthException(this);
        }
    }
}