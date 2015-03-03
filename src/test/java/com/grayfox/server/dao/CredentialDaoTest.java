package com.grayfox.server.dao;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import com.grayfox.server.config.TestConfig;
import com.grayfox.server.domain.Credential;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@TransactionConfiguration(defaultRollback = true)
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