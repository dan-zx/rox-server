package com.grayfox.server.oauth;

import com.grayfox.server.BaseApplicationException;

public class OAuthException extends BaseApplicationException {

    private static final long serialVersionUID = 1301884314371840181L;

    private OAuthException() { }

    private OAuthException(String messageKey, Object[] messageArguments, Throwable cause) {
        super(messageKey, messageArguments, cause);
    }

    private OAuthException(String messageKey, Object[] messageArguments) {
        super(messageKey, messageArguments);
    }

    private OAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    private OAuthException(String message) {
        super(message);
    }

    private OAuthException(Throwable cause) {
        super(cause);
    }

    public static class Builder extends BaseBuilder {

        @Override
        public Builder message(String message) {
            return (Builder) super.message(message);
        }

        @Override
        public Builder messageKey(String messageKey) {
            return (Builder) super.messageKey(messageKey);
        }

        @Override
        public Builder addMessageArgument(Object argument) {
            return (Builder) super.addMessageArgument(argument);
        }
        
        @Override
        public Builder cause(Throwable cause) {
            return (Builder) super.cause(cause);
        }

        @Override
        public OAuthException build() {
            if (getMessage() != null) {
                if (getCause() != null) return new OAuthException(getMessage(), getCause());
                return new OAuthException(getMessage());
            }
            if (getMessageKey() != null) {
                if (getCause() != null) return new OAuthException(getMessageKey(), getMessageArguments(), getCause());
                return new OAuthException(getMessageKey(), getMessageArguments());
            }
            if (getCause() != null) return new OAuthException(getCause());
            return new OAuthException();
        }
    }
}