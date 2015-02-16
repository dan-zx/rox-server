package com.grayfox.server.dao;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import com.grayfox.server.config.TestConfig;
import com.grayfox.server.domain.Credential;
import com.grayfox.server.domain.User;
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
public class UserDaoTest {

    @Inject private CredentialDao credentialDao;
    @Inject private UserDao userDao;

    @Before
    public void setUp() {
        assertThat(credentialDao).isNotNull();
        assertThat(userDao).isNotNull();
    }

    @Test
    @Transactional
    public void testSaveAndFetch() {
        Credential credential = new Credential();
        credential.setAccessToken("fakeAccessToken");
        credential.setFoursquareAccessToken("fakeFoursquareAccessToken");

        credentialDao.save(credential);

        assertThat(userDao.fetchCompactByAccessToken(credential.getAccessToken())).isNull();

        User expectedUser = new User();
        expectedUser.setName("name");
        expectedUser.setLastName("lastName");
        expectedUser.setPhotoUrl("url");
        expectedUser.setFoursquareId("id");
        expectedUser.setCredential(credential);

        userDao.saveOrUpdate(expectedUser);

        expectedUser.setCredential(null);
        assertThat(userDao.fetchCompactByAccessToken(credential.getAccessToken())).isNotNull().isEqualTo(expectedUser);
    }
}