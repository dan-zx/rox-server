package com.grayfox.server.dao;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.HashSet;

import javax.inject.Inject;

import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.User;
import com.grayfox.server.test.config.TestConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SocialNetworkProfileDaoTest {

    @Inject private SocialNetworkProfileDao socialNetworkProfileDao;

    @Before
    public void setUp() {
        assertThat(socialNetworkProfileDao).isNotNull();
    }

    @Test
    public void testCollectUserData() {
        User expectedUser = new User();
        expectedUser.setName("John");
        expectedUser.setLastName("Doe");
        expectedUser.setPhotoUrl("https://irs3.4sqi.net/img/user/300x300/photo.jpg");
        expectedUser.setFoursquareId("34468234");
        Category selfLike = new Category();
        selfLike.setFoursquareId("4bf58dd8d48988d190941735");
        expectedUser.setLikes(new HashSet<>(Arrays.asList(selfLike)));
        User friend = new User();
        friend.setName("Jane");
        friend.setLastName("Doe");
        friend.setPhotoUrl("https://irs1.4sqi.net/img/user/300x300/photo.jpg");
        friend.setFoursquareId("94578235");
        Category friendLike = new Category();
        friendLike.setFoursquareId("4bf58dd8d48988d175941735");
        friend.setLikes(new HashSet<>(Arrays.asList(friendLike)));
        expectedUser.setFriends(new HashSet<>(Arrays.asList(friend)));

        assertThat(socialNetworkProfileDao.collectUserData("fakeToken")).isNotNull().isEqualTo(expectedUser);
    }

    @Test
    public void testErrorInCollectUserData() {
        assertThatThrownBy(() -> socialNetworkProfileDao.collectUserData("invalidToken"))
            .isInstanceOf(DaoException.class);
    }
}