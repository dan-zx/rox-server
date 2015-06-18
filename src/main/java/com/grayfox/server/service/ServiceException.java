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
package com.grayfox.server.service;

import com.grayfox.server.BaseApplicationException;

public class ServiceException extends BaseApplicationException {

    private static final long serialVersionUID = 474365738037258460L;

    private ServiceException() { }

    private ServiceException(String messageKey, Object[] messageArguments, Throwable cause) {
        super(messageKey, messageArguments, cause);
    }

    private ServiceException(String messageKey, Object[] messageArguments) {
        super(messageKey, messageArguments);
    }

    private ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    private ServiceException(String message) {
        super(message);
    }

    private ServiceException(Throwable cause) {
        super(cause);
    }

    public static class Builder extends BaseBuilder<ServiceException> {

        @Override
        public ServiceException build() {
            if (getMessage() != null) {
                if (getCause() != null) return new ServiceException(getMessage(), getCause());
                return new ServiceException(getMessage());
            }
            if (getMessageKey() != null) {
                if (getCause() != null) return new ServiceException(getMessageKey(), getMessageArguments(), getCause());
                return new ServiceException(getMessageKey(), getMessageArguments());
            }
            if (getCause() != null) return new ServiceException(getCause());
            return new ServiceException();
        }
    }
}