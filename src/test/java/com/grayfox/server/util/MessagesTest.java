package com.grayfox.server.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class MessagesTest {

    private static final String MISSING_RESOURCE_KEY_FORMAT = "???%s???";

    @Test
    public void testSimpleMessage() {
        final String messageKey = "test.message";
        final String notExistingMessage = String.format(MISSING_RESOURCE_KEY_FORMAT, messageKey);

        assertThat(Messages.get(messageKey)).isNotNull().isNotEmpty().isNotEqualTo(notExistingMessage);
        assertThat(Messages.get(messageKey, Constants.Locales.SPANISH)).isNotNull().isNotEmpty().isNotEqualTo(notExistingMessage);
    }

    @Test
    public void testMessageWithArguments() {
        final String messageKey = "test.message_with_args";
        final String notExistingMessage = String.format(MISSING_RESOURCE_KEY_FORMAT, messageKey);
        final Object[] messageArguments = { 54.3, "s" };

        assertThat(Messages.get(messageKey, messageArguments)).isNotNull().isNotEmpty().isNotEqualTo(notExistingMessage).contains(messageArguments[0].toString(), messageArguments[1].toString());
        assertThat(Messages.get(messageKey, Constants.Locales.SPANISH, messageArguments)).isNotNull().isNotEmpty().isNotEqualTo(notExistingMessage).contains(messageArguments[0].toString(), messageArguments[1].toString());
    }

    @Test
    public void testNotExcistingMessage() {
        final String messageKey = "not.existing.message";
        final String notExistingMessage = String.format(MISSING_RESOURCE_KEY_FORMAT, messageKey);

        assertThat(Messages.get(messageKey)).isNotNull().isNotEmpty().isEqualTo(notExistingMessage);
        assertThat(Messages.get(messageKey, Constants.Locales.SPANISH)).isNotNull().isNotEmpty().isEqualTo(notExistingMessage);
    }
}