package com.grayfox.server;

import java.util.ArrayList;
import java.util.List;

import com.grayfox.server.util.Messages;

public abstract class BaseApplicationException extends RuntimeException {

    private static final long serialVersionUID = 4065199814273911926L;

    private String messageKey;
    private Object[] messageArguments;

    protected BaseApplicationException() { }

    protected BaseApplicationException(String messageKey, Object[] messageArguments, Throwable cause) {
        super(Messages.get(messageKey, messageArguments), cause);
        this.messageKey = messageKey;
        this.messageArguments = messageArguments;
    }

    protected BaseApplicationException(String messageKey, Object[] messageArguments) {
        super(Messages.get(messageKey, messageArguments));
        this.messageKey = messageKey;
        this.messageArguments = messageArguments;
    }

    protected BaseApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    protected BaseApplicationException(String message) {
        super(message);
    }

    protected BaseApplicationException(Throwable cause) {
        super(cause);
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getMessageArguments() {
        return messageArguments;
    }

    protected static abstract class BaseBuilder {

        private String message;
        private String messageKey;
        private List<Object> messageArguments;
        private Throwable cause;

        protected BaseBuilder() {
            messageArguments = new ArrayList<>();
        }

        public BaseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public BaseBuilder messageKey(String messageKey) {
            this.messageKey = messageKey;
            return this;
        }        

        public BaseBuilder addMessageArgument(Object argument) {
            messageArguments.add(argument);
            return this;
        }

        public BaseBuilder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        protected String getMessage() {
            return message;
        }

        protected String getMessageKey() {
            return messageKey;
        }

        protected Object[] getMessageArguments() {
            return messageArguments.toArray();
        }

        protected Throwable getCause() {
            return cause;
        }

        public abstract BaseApplicationException build();
    }
}