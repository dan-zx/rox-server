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
package com.grayfox.server.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.grayfox.server.dao.DaoException;
import com.grayfox.server.oauth.OAuthException;
import com.grayfox.server.service.ServiceException;
import com.grayfox.server.util.Messages;

import org.junit.Test;

public class ExceptionTest {

    private static final Object[] MESSAGE_ARGS = {99, 29.6};
    private static final String MESSAGE_KEY = "test.message";
    private static final String MESSAGE_WITH_ARGS_KEY = "test.message_with_args";
    private static final String MESSAGE = Messages.get(MESSAGE_KEY);
    private static final String MESSAGE_WITH_ARGS = Messages.get(MESSAGE_WITH_ARGS_KEY, MESSAGE_ARGS);
    private static final Throwable CAUSE = new IllegalArgumentException("A cause");
    
    @Test
    public void testDaoExceptionCreation() {
        DaoException exception = new DaoException.Builder().build();
        assertThat(exception).hasNoCause().hasMessage(null);
        assertThat(exception.getMessageKey()).isNull();
        assertThat(exception.getMessageArguments()).isNotNull().isEmpty();
        
        exception = new DaoException.Builder()
            .message(MESSAGE)
            .build();
        assertThat(exception).hasNoCause().hasMessage(MESSAGE);
        assertThat(exception.getMessageKey()).isNull();
        assertThat(exception.getMessageArguments()).isNotNull().isEmpty();

        exception = new DaoException.Builder()
            .message(MESSAGE)
            .cause(CAUSE)
            .build();
        assertThat(exception).hasCause(CAUSE).hasMessage(MESSAGE);
        assertThat(exception.getMessageKey()).isNull();
        assertThat(exception.getMessageArguments()).isNotNull().isEmpty();
 
        exception = new DaoException.Builder()
            .messageKey(MESSAGE_KEY)
            .build();
        assertThat(exception).hasNoCause().hasMessage(MESSAGE);
        assertThat(exception.getMessageKey()).isNotNull().isNotEmpty().isEqualTo(MESSAGE_KEY);
        assertThat(exception.getMessageArguments()).isNotNull().isEmpty();
        
        exception = new DaoException.Builder()
            .messageKey(MESSAGE_WITH_ARGS_KEY)
            .addMessageArgument(MESSAGE_ARGS[0])
            .addMessageArgument(MESSAGE_ARGS[1])
            .build();
        assertThat(exception).hasNoCause().hasMessage(MESSAGE_WITH_ARGS);
        assertThat(exception.getMessageKey()).isNotNull().isNotEmpty().isEqualTo(MESSAGE_WITH_ARGS_KEY);
        assertThat(exception.getMessageArguments()).isNotNull().isNotEmpty().hasSameSizeAs(MESSAGE_ARGS).isEqualTo(MESSAGE_ARGS);

        exception = new DaoException.Builder()
            .messageKey(MESSAGE_WITH_ARGS_KEY)
            .addMessageArgument(MESSAGE_ARGS[0])
            .addMessageArgument(MESSAGE_ARGS[1])
            .cause(CAUSE)
            .build();
        assertThat(exception).hasCause(CAUSE).hasMessage(MESSAGE_WITH_ARGS);
        assertThat(exception.getMessageKey()).isNotNull().isNotEmpty().isEqualTo(MESSAGE_WITH_ARGS_KEY);
        assertThat(exception.getMessageArguments()).isNotNull().isNotEmpty().hasSameSizeAs(MESSAGE_ARGS).isEqualTo(MESSAGE_ARGS);
    }

    @Test
    public void testOAuthExceptionCreation() {
        OAuthException exception = new OAuthException.Builder().build();
        assertThat(exception).hasNoCause().hasMessage(null);
        assertThat(exception.getMessageKey()).isNull();
        assertThat(exception.getMessageArguments()).isNotNull().isEmpty();
        
        exception = new OAuthException.Builder()
            .message(MESSAGE)
            .build();
        assertThat(exception).hasNoCause().hasMessage(MESSAGE);
        assertThat(exception.getMessageKey()).isNull();
        assertThat(exception.getMessageArguments()).isNotNull().isEmpty();

        exception = new OAuthException.Builder()
            .message(MESSAGE)
            .cause(CAUSE)
            .build();
        assertThat(exception).hasCause(CAUSE).hasMessage(MESSAGE);
        assertThat(exception.getMessageKey()).isNull();
        assertThat(exception.getMessageArguments()).isNotNull().isEmpty();

        exception = new OAuthException.Builder()
            .messageKey(MESSAGE_KEY)
            .build();
        assertThat(exception).hasNoCause().hasMessage(MESSAGE);
        assertThat(exception.getMessageKey()).isNotNull().isNotEmpty().isEqualTo(MESSAGE_KEY);
        assertThat(exception.getMessageArguments()).isNotNull().isEmpty();
        
        exception = new OAuthException.Builder()
            .messageKey(MESSAGE_WITH_ARGS_KEY)
            .addMessageArgument(MESSAGE_ARGS[0])
            .addMessageArgument(MESSAGE_ARGS[1])
            .build();
        assertThat(exception).hasNoCause().hasMessage(MESSAGE_WITH_ARGS);
        assertThat(exception.getMessageKey()).isNotNull().isNotEmpty().isEqualTo(MESSAGE_WITH_ARGS_KEY);
        assertThat(exception.getMessageArguments()).isNotNull().isNotEmpty().hasSameSizeAs(MESSAGE_ARGS).isEqualTo(MESSAGE_ARGS);

        exception = new OAuthException.Builder()
            .messageKey(MESSAGE_WITH_ARGS_KEY)
            .addMessageArgument(MESSAGE_ARGS[0])
            .addMessageArgument(MESSAGE_ARGS[1])
            .cause(CAUSE)
            .build();
        assertThat(exception).hasCause(CAUSE).hasMessage(MESSAGE_WITH_ARGS);
        assertThat(exception.getMessageKey()).isNotNull().isNotEmpty().isEqualTo(MESSAGE_WITH_ARGS_KEY);
        assertThat(exception.getMessageArguments()).isNotNull().isNotEmpty().hasSameSizeAs(MESSAGE_ARGS).isEqualTo(MESSAGE_ARGS);
    }

    @Test
    public void testServiceExceptionCreation() {
        ServiceException exception = new ServiceException.Builder().build();
        assertThat(exception).hasNoCause().hasMessage(null);
        assertThat(exception.getMessageKey()).isNull();
        assertThat(exception.getMessageArguments()).isNotNull().isEmpty();
        
        exception = new ServiceException.Builder()
            .message(MESSAGE)
            .build();
        assertThat(exception).hasNoCause().hasMessage(MESSAGE);
        assertThat(exception.getMessageKey()).isNull();
        assertThat(exception.getMessageArguments()).isNotNull().isEmpty();

        exception = new ServiceException.Builder()
            .message(MESSAGE)
            .cause(CAUSE)
            .build();
        assertThat(exception).hasCause(CAUSE).hasMessage(MESSAGE);
        assertThat(exception.getMessageKey()).isNull();
        assertThat(exception.getMessageArguments()).isNotNull().isEmpty();

        exception = new ServiceException.Builder()
            .messageKey(MESSAGE_KEY)
            .build();
        assertThat(exception).hasNoCause().hasMessage(MESSAGE);
        assertThat(exception.getMessageKey()).isNotNull().isNotEmpty().isEqualTo(MESSAGE_KEY);
        assertThat(exception.getMessageArguments()).isNotNull().isEmpty();
        
        exception = new ServiceException.Builder()
            .messageKey(MESSAGE_WITH_ARGS_KEY)
            .addMessageArgument(MESSAGE_ARGS[0])
            .addMessageArgument(MESSAGE_ARGS[1])
            .build();
        assertThat(exception).hasNoCause().hasMessage(MESSAGE_WITH_ARGS);
        assertThat(exception.getMessageKey()).isNotNull().isNotEmpty().isEqualTo(MESSAGE_WITH_ARGS_KEY);
        assertThat(exception.getMessageArguments()).isNotNull().isNotEmpty().hasSameSizeAs(MESSAGE_ARGS).isEqualTo(MESSAGE_ARGS);

        exception = new ServiceException.Builder()
            .messageKey(MESSAGE_WITH_ARGS_KEY)
            .addMessageArgument(MESSAGE_ARGS[0])
            .addMessageArgument(MESSAGE_ARGS[1])
            .cause(CAUSE)
            .build();
        assertThat(exception).hasCause(CAUSE).hasMessage(MESSAGE_WITH_ARGS);
        assertThat(exception.getMessageKey()).isNotNull().isNotEmpty().isEqualTo(MESSAGE_WITH_ARGS_KEY);
        assertThat(exception.getMessageArguments()).isNotNull().isNotEmpty().hasSameSizeAs(MESSAGE_ARGS).isEqualTo(MESSAGE_ARGS);
    }
}