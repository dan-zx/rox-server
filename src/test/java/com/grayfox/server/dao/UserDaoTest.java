package com.grayfox.server.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;

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
    public void testCrud() {
        Credential credential = new Credential();
        credential.setAccessToken("fakeAccessToken");
        credential.setFoursquareAccessToken("fakeFoursquareAccessToken");

        credentialDao.save(credential);

        assertThat(userDao.fetchCompactByAccessToken(credential.getAccessToken())).isNull();

        User expectedUser = new User();
        expectedUser.setName("name");
        expectedUser.setLastName("lastName");
        expectedUser.setPhotoUrl("url");
        expectedUser.setFoursquareId("1");
        expectedUser.setCredential(credential);
        User friend1 = new User();
        friend1.setName("friend1");
        friend1.setLastName("lastName");
        friend1.setPhotoUrl("url");
        friend1.setFoursquareId("2");
        User friend2 = new User();
        friend2.setName("friend2");
        friend2.setLastName("lastName");
        friend2.setPhotoUrl("url");
        friend2.setFoursquareId("3");
        expectedUser.setFriends(new HashSet<>(Arrays.asList(friend1, friend2)));
        expectedUser.setLikes(new HashSet<>());

        userDao.save(expectedUser);

        expectedUser.setCredential(null);
        
        User actualUser = userDao.fetchCompactByAccessToken(credential.getAccessToken());
        assertThat(actualUser).isNotNull();
        
        actualUser.setFriends(new HashSet<>(userDao.fetchCompactFriendsByFoursquareId(actualUser.getFoursquareId())));
        actualUser.setLikes(new HashSet<>(userDao.fetchLikesByFoursquareId(actualUser.getFoursquareId(), null)));

        assertThat(actualUser).isEqualTo(expectedUser);

        expectedUser.setName("othername");
        expectedUser.setLastName("otherlastName");
        expectedUser.setPhotoUrl("otherurl");
        expectedUser.getFriends().remove(friend1);
        User friend3 = new User();
        friend3.setName("friend3");
        friend3.setLastName("lastName");
        friend3.setPhotoUrl("url");
        friend3.setFoursquareId("4");
        expectedUser.getFriends().add(friend3);
        
        userDao.update(expectedUser);
        
        actualUser = userDao.fetchCompactByAccessToken(credential.getAccessToken());
        assertThat(actualUser).isNotNull();
        
        actualUser.setFriends(new HashSet<>(userDao.fetchCompactFriendsByFoursquareId(actualUser.getFoursquareId())));
        actualUser.setLikes(new HashSet<>(userDao.fetchLikesByFoursquareId(actualUser.getFoursquareId(), null)));

        assertThat(actualUser).isEqualTo(expectedUser);
    }
}