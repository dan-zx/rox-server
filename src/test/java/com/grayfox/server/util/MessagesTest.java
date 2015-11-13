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
package com.grayfox.server.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class MessagesTest {

    @Test
    public void testSimpleMessage() {
        final String messageKey = "test.message";
        final String notExistingMessage = String.format(Constants.Strings.MISSING_RESOURCE_KEY_FORMAT, messageKey);

        assertThat(Messages.get(messageKey)).isNotNull().isNotEmpty().isNotEqualTo(notExistingMessage);
        assertThat(Messages.get(messageKey, Constants.Locales.SPANISH)).isNotNull().isNotEmpty().isNotEqualTo(notExistingMessage);
    }

    @Test
    public void testMessageWithArguments() {
        final String messageKey = "test.message_with_args";
        final String notExistingMessage = String.format(Constants.Strings.MISSING_RESOURCE_KEY_FORMAT, messageKey);
        final Object[] messageArguments = { 54.3, "s" };

        assertThat(Messages.get(messageKey, messageArguments)).isNotNull().isNotEmpty().isNotEqualTo(notExistingMessage).contains(messageArguments[0].toString(), messageArguments[1].toString());
        assertThat(Messages.get(messageKey, Constants.Locales.SPANISH, messageArguments)).isNotNull().isNotEmpty().isNotEqualTo(notExistingMessage).contains(messageArguments[0].toString(), messageArguments[1].toString());
    }

    @Test
    public void testNotExcistingMessage() {
        final String messageKey = "not.existing.message";
        final String notExistingMessage = String.format(Constants.Strings.MISSING_RESOURCE_KEY_FORMAT, messageKey);

        assertThat(Messages.get(messageKey)).isNotNull().isNotEmpty().isEqualTo(notExistingMessage);
        assertThat(Messages.get(messageKey, Constants.Locales.SPANISH)).isNotNull().isNotEmpty().isEqualTo(notExistingMessage);
    }
}