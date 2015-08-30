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

import java.util.Arrays;
import java.util.HashSet;

import javax.inject.Inject;

import com.grayfox.server.domain.Credential;
import com.grayfox.server.domain.User;
import com.grayfox.server.test.config.TestConfig;

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

        assertThat(userDao.fetchCompactByAccessToken(credential.getAccessToken())).isNull();
        assertThat(userDao.existsUser(expectedUser.getFoursquareId())).isNotNull().isFalse();
        assertThat(userDao.fetchFoursquareIdByAccessToken(expectedUser.getCredential().getAccessToken())).isNull();

        userDao.save(expectedUser);

        expectedUser.setCredential(null);

        User actualUser = userDao.fetchCompactByAccessToken(credential.getAccessToken());

        assertThat(actualUser).isNotNull();
        assertThat(userDao.existsUser(expectedUser.getFoursquareId())).isNotNull().isTrue();
        assertThat(userDao.fetchFoursquareIdByAccessToken(credential.getAccessToken())).isNotNull().isNotEmpty().isEqualTo(expectedUser.getFoursquareId());
        assertThat(userDao.areFriends(expectedUser.getFoursquareId(), friend1.getFoursquareId())).isNotNull().isTrue();
        assertThat(userDao.areFriends(expectedUser.getFoursquareId(), friend2.getFoursquareId())).isNotNull().isTrue();

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
        assertThat(userDao.areFriends(expectedUser.getFoursquareId(), friend1.getFoursquareId())).isNotNull().isFalse();
        assertThat(userDao.areFriends(expectedUser.getFoursquareId(), friend3.getFoursquareId())).isNotNull().isTrue();

        actualUser.setFriends(new HashSet<>(userDao.fetchCompactFriendsByFoursquareId(actualUser.getFoursquareId())));
        actualUser.setLikes(new HashSet<>(userDao.fetchLikesByFoursquareId(actualUser.getFoursquareId(), null)));

        assertThat(actualUser).isEqualTo(expectedUser);
    }
}