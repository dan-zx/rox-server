/*
 * Copyright 2014-2015 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    public static abstract class BaseBuilder<T extends BaseApplicationException> {

        private String message;
        private String messageKey;
        private List<Object> messageArguments;
        private Throwable cause;

        protected BaseBuilder() {
            messageArguments = new ArrayList<>();
        }

        public BaseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public BaseBuilder<T> messageKey(String messageKey) {
            this.messageKey = messageKey;
            return this;
        }        

        public BaseBuilder<T> addMessageArgument(Object argument) {
            messageArguments.add(argument);
            return this;
        }

        public BaseBuilder<T> cause(Throwable cause) {
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

        public abstract T build();
    }
}