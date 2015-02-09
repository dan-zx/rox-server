package com.grayfox.server.ws.rest;

import java.util.ArrayList;
import java.util.List;

public class InvalidFormatException extends IllegalArgumentException {

    private static final long serialVersionUID = 959701599755126040L;

    private final String messageKey;
    private final Object[] formatArgs;

    private InvalidFormatException(String messageKey, Object[] formatArgs) {
        super(messageKey);
        this.messageKey = messageKey;
        this.formatArgs = formatArgs;
    }

    private InvalidFormatException(String messageKey, Throwable cause, Object[] formatArgs) {
        super(messageKey, cause);
        this.messageKey = messageKey;
        this.formatArgs = formatArgs;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getFormatArgs() {
        return formatArgs;
    }

    public static class Builder {

        private final String messageKey;
        private List<Object> formatArgs;
        private Throwable cause;

        public Builder(String messageKey) {
            this.messageKey = messageKey;
            formatArgs = new ArrayList<>();
        }

        public Builder addFormatArg(Object formatArg) {
            formatArgs.add(formatArg);
            return this;
        }

        public Builder setCause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public InvalidFormatException build() {
            if (cause == null) return new InvalidFormatException(messageKey, formatArgs.toArray());
            else return new InvalidFormatException(messageKey, cause, formatArgs.toArray());
        }
    }
}