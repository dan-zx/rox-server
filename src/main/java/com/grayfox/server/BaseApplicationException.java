package com.grayfox.server;

import java.util.ArrayList;
import java.util.List;

import com.grayfox.server.util.Messages;

public abstract class BaseApplicationException extends RuntimeException {

    private static final long serialVersionUID = 4065199814273911926L;

    private final BaseBuilder builder;

    protected BaseApplicationException(BaseBuilder builder) {
        super(Messages.get(builder.messageKey, builder.formatArgs.toArray()), builder.cause);
        this.builder = builder;
    }

    public String getMessageKey() {
        return builder.messageKey;
    }

    public Object[] getFormatArgs() {
        return builder.formatArgs.toArray();
    }

    protected static abstract class BaseBuilder {

        private final String messageKey;
        private List<Object> formatArgs;
        private Throwable cause;

        protected BaseBuilder(String messageKey) {
            this.messageKey = messageKey;
            formatArgs = new ArrayList<>();
        }

        public BaseBuilder addFormatArg(Object formatArg) {
            formatArgs.add(formatArg);
            return this;
        }

        public BaseBuilder setCause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public abstract BaseApplicationException build();
    }
}