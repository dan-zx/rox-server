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
package com.grayfox.server.dao;

import com.grayfox.server.BaseApplicationException;

public class DaoException extends BaseApplicationException {

    private static final long serialVersionUID = 6442324698959192799L;

    private DaoException() { }

    private DaoException(String messageKey, Object[] messageArguments, Throwable cause) {
        super(messageKey, messageArguments, cause);
    }

    private DaoException(String messageKey, Object[] messageArguments) {
        super(messageKey, messageArguments);
    }

    private DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    private DaoException(String message) {
        super(message);
    }

    private DaoException(Throwable cause) {
        super(cause);
    }

    public static class Builder extends BaseBuilder<DaoException> {

        @Override
        public DaoException build() {
            if (getMessage() != null) {
                if (getCause() != null) return new DaoException(getMessage(), getCause());
                return new DaoException(getMessage());
            }
            if (getMessageKey() != null) {
                if (getCause() != null) return new DaoException(getMessageKey(), getMessageArguments(), getCause());
                return new DaoException(getMessageKey(), getMessageArguments());
            }
            if (getCause() != null) return new DaoException(getCause());
            return new DaoException();
        }
    }
}