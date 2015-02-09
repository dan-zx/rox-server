package com.grayfox.server;

import java.util.ArrayList;
import java.util.List;

public abstract class GrayFoxException extends RuntimeException {

    private static final long serialVersionUID = 4065199814273911926L;

    private final String messageKey;
    private final Object[] formatArgs;

    protected GrayFoxException(String messageKey, Object[] formatArgs) {
        super(messageKey);
        this.messageKey = messageKey;
        this.formatArgs = formatArgs;
    }

    protected GrayFoxException(String messageKey, Throwable cause, Object[] formatArgs) {
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

    protected static abstract class BaseBuilder {

        private final String messageKey;
        private List<Object> formatArgs;
        private Throwable cause;

        protected BaseBuilder(String messageKey) {
            this.messageKey = messageKey;
            formatArgs = new ArrayList<>();
        }

        protected String getMessageKey() {
            return messageKey;
        }

        protected Object[] getFormatArgs() {
            return formatArgs.toArray();
        }

        public BaseBuilder addFormatArg(Object formatArg) {
            formatArgs.add(formatArg);
            return this;
        }

        protected Throwable getCause() {
            return cause;
        }

        public BaseBuilder setCause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public abstract GrayFoxException build();
    }
}