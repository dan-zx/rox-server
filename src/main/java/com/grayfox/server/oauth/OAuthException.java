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

    public static class Builder extends BaseBuilder<OAuthException> {

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