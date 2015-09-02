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

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import com.grayfox.server.domain.Credential;
import com.grayfox.server.test.config.TestConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class CredentialDaoTest {

    @Inject private CredentialDao credentialDao;

    @Before
    public void setUp() {
        assertThat(credentialDao).isNotNull();
    }

    @Test
    @Transactional
    public void testCrud() {
        Credential expectedCredential = new Credential();
        expectedCredential.setAccessToken("fakeAccessToken");
        expectedCredential.setFoursquareAccessToken("fakeFoursquareAccessToken");
        
        assertThat(credentialDao.existsAccessToken(expectedCredential.getAccessToken())).isFalse();
        assertThat(credentialDao.fetchByFoursquareAccessToken(expectedCredential.getFoursquareAccessToken())).isNull();

        credentialDao.save(expectedCredential);

        assertThat(credentialDao.existsAccessToken(expectedCredential.getAccessToken())).isTrue();
        assertThat(credentialDao.fetchByFoursquareAccessToken(expectedCredential.getFoursquareAccessToken())).isNotNull().isEqualTo(expectedCredential);
    }
}