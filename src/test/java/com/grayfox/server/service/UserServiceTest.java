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
package com.grayfox.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import com.grayfox.server.dao.UserDao;
import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.Credential;
import com.grayfox.server.domain.User;
import com.grayfox.server.test.config.TestConfig;
import com.grayfox.server.test.dao.jdbc.UtilJdbcDao;

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
public class UserServiceTest {

    @Inject private UserService userService;
    @Inject private UtilJdbcDao utilJdbcDao;
    @Inject private UserDao userDao;

    @Before
    public void setUp() {
        assertThat(userService).isNotNull();
        assertThat(utilJdbcDao).isNotNull();
        assertThat(userDao).isNotNull();
    }

    @Test
    @Transactional
    public void integrationTest() {
        loadMockdata();

        Credential expectedCredential = new Credential();
        expectedCredential.setFoursquareAccessToken("fakeToken");
        expectedCredential.setNew(true);

        Credential credential = userService.registerUsingFoursquare("fakeCode");

        assertThat(credential).isNotNull().isEqualToIgnoringGivenFields(expectedCredential, "accessToken");
        assertThat(credential.getAccessToken()).isNotNull().isNotEmpty();
        assertThatThrownBy(() -> userService.getCompactSelf(credential.getAccessToken())).isInstanceOf(ServiceException.class);
        assertThatThrownBy(() -> userService.getSelfLikes(credential.getAccessToken(), Locale.ROOT)).isInstanceOf(ServiceException.class);
        assertThatThrownBy(() -> userService.getSelfCompactFriends(credential.getAccessToken())).isInstanceOf(ServiceException.class);
        assertThatThrownBy(() -> userService.getUserLikes(credential.getAccessToken(), "invalidId", Locale.ROOT)).isInstanceOf(ServiceException.class);
        assertThatThrownBy(() -> userService.addLike(credential.getAccessToken(), "id")).isInstanceOf(ServiceException.class);
        assertThatThrownBy(() -> userService.removeLike(credential.getAccessToken(), "id")).isInstanceOf(ServiceException.class);

        userService.generateProfileUsingFoursquare(credential);
        credential.setNew(false);

        assertThat(userService.registerUsingFoursquare("fakeCode")).isNotNull().isEqualTo(credential);
        
        User expectedUser = new User();
        expectedUser.setName("John");
        expectedUser.setLastName("Doe");
        expectedUser.setPhotoUrl("https://irs3.4sqi.net/img/user/300x300/photo.jpg");
        expectedUser.setFoursquareId("34468234");

        assertThat(userService.getCompactSelf(credential.getAccessToken())).isNotNull().isEqualTo(expectedUser);
        
        Category selfLike = new Category();
        selfLike.setFoursquareId("4bf58dd8d48988d190941735");
        selfLike.setName("Museo histórico");
        selfLike.setIconUrl("https://ss3.4sqi.net/img/categories_v2/arts_entertainment/museum_history_88.png");
        List<Category> expectedSelfLikes = new ArrayList<>(Arrays.asList(selfLike));

        assertThat(userService.getSelfLikes(credential.getAccessToken(), Locale.ROOT)).isNotNull().isNotEmpty().hasSameSizeAs(expectedSelfLikes).containsExactlyElementsOf(expectedSelfLikes);
        assertThat(userService.getUserLikes(credential.getAccessToken(), expectedUser.getFoursquareId(), Locale.ROOT)).isNotNull().isNotEmpty().hasSameSizeAs(expectedSelfLikes).containsExactlyElementsOf(expectedSelfLikes);

        User friend = new User();
        friend.setName("Jane");
        friend.setLastName("Doe");
        friend.setPhotoUrl("https://irs1.4sqi.net/img/user/300x300/photo.jpg");
        friend.setFoursquareId("94578235");
        List<User> expectedFriends = Arrays.asList(friend);

        assertThat(userService.getSelfCompactFriends(credential.getAccessToken())).isNotNull().isNotEmpty().hasSameSizeAs(expectedFriends).containsExactlyElementsOf(expectedFriends);

        Category friendLike = new Category();
        friendLike.setFoursquareId("4bf58dd8d48988d175941735");
        friendLike.setName("Gimnasio/Centro de fitness");
        friendLike.setIconUrl("https://ss3.4sqi.net/img/categories_v2/building/gym_88.png");
        List<Category> expectedFriendLikes = Arrays.asList(friendLike);

        assertThatThrownBy(() -> userService.getUserLikes(credential.getAccessToken(), "invalidId", Locale.ROOT)).isInstanceOf(ServiceException.class);
        assertThatThrownBy(() -> userService.getUserLikes(credential.getAccessToken(), "46jk35", Locale.ROOT)).isInstanceOf(ServiceException.class);
        assertThat(userService.getUserLikes(credential.getAccessToken(), friend.getFoursquareId(), Locale.ROOT)).isNotNull().isNotEmpty().hasSameSizeAs(expectedFriendLikes).containsExactlyElementsOf(expectedFriendLikes);

        Category newSelfLike = new Category();
        newSelfLike.setFoursquareId("4bf58dd8d48988d151941735");
        newSelfLike.setName("Taco Place");
        newSelfLike.setIconUrl("https://ss3.4sqi.net/img/categories_v2/food/taco_88.png");

        userService.addLike(credential.getAccessToken(), newSelfLike.getFoursquareId());
        userService.removeLike(credential.getAccessToken(), selfLike.getFoursquareId());
        expectedSelfLikes.add(newSelfLike);
        expectedSelfLikes.remove(selfLike);

        assertThat(userService.getSelfLikes(credential.getAccessToken(), Locale.ROOT)).isNotNull().isNotEmpty().hasSameSizeAs(expectedSelfLikes).containsExactlyElementsOf(expectedSelfLikes);
    }

    private void loadMockdata() {
        Category c1 = new Category();
        c1.setFoursquareId("4bf58dd8d48988d190941735");
        c1.setName("Museo histórico");
        c1.setIconUrl("https://ss3.4sqi.net/img/categories_v2/arts_entertainment/museum_history_88.png");

        Category c2 = new Category();
        c2.setFoursquareId("4bf58dd8d48988d175941735");
        c2.setName("Gimnasio/Centro de fitness");
        c2.setIconUrl("https://ss3.4sqi.net/img/categories_v2/building/gym_88.png");

        Category c3 = new Category();
        c3.setFoursquareId("4bf58dd8d48988d151941735");
        c3.setName("Taco Place");
        c3.setIconUrl("https://ss3.4sqi.net/img/categories_v2/food/taco_88.png");

        utilJdbcDao.saveCategories(Arrays.asList(c1, c2, c3));

        User user = new User();
        user.setFoursquareId("46jk35");

        userDao.save(user);
    }
}