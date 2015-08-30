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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.inject.Inject;

import com.grayfox.server.test.config.TestConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SocialNetworkAuthenticatorTest {

    @Inject private SocialNetworkAuthenticator socialNetworkAuthenticator;

    @Before
    public void setUp() {
        assertThat(socialNetworkAuthenticator).isNotNull();
    }

    @Test
    public void testExchangeAccessToken() {
        assertThat(socialNetworkAuthenticator.exchangeAccessToken("fakeCode")).isNotNull().isNotEmpty().isEqualTo("fakeToken");
        assertThatThrownBy(() -> socialNetworkAuthenticator.exchangeAccessToken("invalidCode")).isInstanceOf(OAuthException.class);
    }
}