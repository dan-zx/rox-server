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
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.Credential;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;
import com.grayfox.server.domain.Recommendation;
import com.grayfox.server.domain.User;
import com.grayfox.server.test.config.TestConfig;
import com.grayfox.server.test.dao.jdbc.UtilJdbcDao;
import com.grayfox.server.util.Messages;

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
public class RecommendationDaoTest {

    @Inject private UtilJdbcDao utilJdbcDao;
    @Inject private CredentialDao credentialDao;
    @Inject private UserDao userDao;
    @Inject private RecommendationDao recommendationDao;

    @Before
    public void setUp() {
        assertThat(utilJdbcDao).isNotNull();
        assertThat(credentialDao).isNotNull();
        assertThat(userDao).isNotNull();
        assertThat(recommendationDao).isNotNull();
    }

    @Test
    @Transactional
    public void testFetchNearestByRating() {
        loadMockDataForFetchNearestByRating();

        Category c = new Category();
        c.setFoursquareId("1");
        c.setIconUrl("url");
        c.setName("CAT_1");

        Poi p1 = new Poi();
        p1.setFoursquareId("1");
        p1.setFoursquareRating(9.2);
        p1.setLocation(Location.parse("19.044,-98.197753"));
        p1.setName("POI_1");
        p1.setCategories(new HashSet<>(Arrays.asList(c)));

        Recommendation r1 = new Recommendation();
        r1.setPoi(p1);
        r1.setType(Recommendation.Type.GLOBAL);
        r1.setReason(Messages.get("recommendation.global.reason"));

        List<Recommendation> expectedRecommendations = Arrays.asList(r1);
        List<Recommendation> actualRecommendations = recommendationDao.findNearestWithHighRating(Location.parse("19.043635,-98.197947"), 100, Locale.ROOT);

        assertThat(actualRecommendations).isNotNull().isNotEmpty().doesNotContainNull().hasSameSizeAs(expectedRecommendations).containsExactlyElementsOf(expectedRecommendations);
    }

    @Test
    @Transactional
    public void testFetchNearestByCategoriesLiked() {
        loadMockDataForFetchNearestByCategoriesLiked();

        Category c1 = new Category();
        c1.setFoursquareId("1");
        c1.setIconUrl("url1");
        c1.setName("CAT_1");

        Category c3 = new Category();
        c3.setFoursquareId("3");
        c3.setIconUrl("url3");
        c3.setName("CAT_3");

        Poi p1 = new Poi();
        p1.setFoursquareId("1");
        p1.setFoursquareRating(9.2);
        p1.setLocation(Location.parse("19.044,-98.197753"));
        p1.setName("POI_1");
        p1.setCategories(new HashSet<>(Arrays.asList(c1)));

        Poi p2 = new Poi();
        p2.setFoursquareId("2");
        p2.setFoursquareRating(8.5);
        p2.setLocation(Location.parse("19.043148,-98.198354"));
        p2.setName("POI_2");
        p2.setCategories(new HashSet<>(Arrays.asList(c3)));

        Recommendation r1 = new Recommendation();
        r1.setPoi(p1);
        r1.setType(Recommendation.Type.SELF);
        r1.setReason(Messages.get("recommendation.self.reason", c1.getName()));

        Recommendation r2 = new Recommendation();
        r2.setPoi(p2);
        r2.setType(Recommendation.Type.SELF);
        r2.setReason(Messages.get("recommendation.self.reason", c3.getName()));

        List<Recommendation> expectedRecommendations = Arrays.asList(r1, r2);
        List<Recommendation> actualRecommendations = recommendationDao.findNearestByCategoriesLiked("fakeToken", Location.parse("19.043635,-98.197947"), 100, Locale.ROOT);

        assertThat(actualRecommendations).isNotNull().isNotEmpty().doesNotContainNull().hasSameSizeAs(expectedRecommendations).containsExactlyElementsOf(expectedRecommendations);
    }

    @Test
    @Transactional
    public void testFetchNearestByCategoriesLikedByFriends() {
        loadMockDataFetchNearestByCategoriesLikedByFriends();

        Category c1 = new Category();
        c1.setFoursquareId("1");
        c1.setIconUrl("url1");
        c1.setName("CAT_1");

        Category c2 = new Category();
        c2.setFoursquareId("2");
        c2.setIconUrl("url2");
        c2.setName("CAT_2");

        Poi p1 = new Poi();
        p1.setFoursquareId("1");
        p1.setFoursquareRating(9.2);
        p1.setLocation(Location.parse("19.044,-98.197753"));
        p1.setName("POI_1");
        p1.setCategories(new HashSet<>(Arrays.asList(c1)));

        Poi p3 = new Poi();
        p3.setFoursquareId("3");
        p3.setFoursquareRating(9d);
        p3.setLocation(Location.parse("19.04329,-98.197432"));
        p3.setName("POI_3");
        p3.setCategories(new HashSet<>(Arrays.asList(c2)));

        Recommendation r1 = new Recommendation();
        r1.setPoi(p1);
        r1.setType(Recommendation.Type.SOCIAL);
        r1.setReason(Messages.get("recommendation.social.reason", "John2 Doe2", c1.getName()));

        Recommendation r2 = new Recommendation();
        r2.setPoi(p3);
        r2.setType(Recommendation.Type.SOCIAL);
        r2.setReason(Messages.get("recommendation.social.reason", "John3 Doe3", c2.getName()));

        List<Recommendation> expectedRecommendations = Arrays.asList(r1, r2);
        List<Recommendation> actualRecommendations = recommendationDao.findNearestByCategoriesLikedByFriends("fakeToken", Location.parse("19.043635,-98.197947"), 100, Locale.ROOT);

        assertThat(actualRecommendations).isNotNull().isNotEmpty().doesNotContainNull().hasSameSizeAs(expectedRecommendations).containsExactlyElementsOf(expectedRecommendations);
    }

    private void loadMockDataForFetchNearestByRating() {
        Category c = new Category();
        c.setFoursquareId("1");
        c.setIconUrl("url");
        c.setName("CAT_1");

        Poi p1 = new Poi();
        p1.setFoursquareId("1");
        p1.setFoursquareRating(9.2);
        p1.setLocation(Location.parse("19.044,-98.197753"));
        p1.setName("POI_1");
        p1.setCategories(new HashSet<>(Arrays.asList(c)));

        Poi p2 = new Poi();
        p2.setFoursquareId("2");
        p2.setFoursquareRating(8.5);
        p2.setLocation(Location.parse("19.043148,-98.198354"));
        p2.setName("POI_2");
        p2.setCategories(new HashSet<>(Arrays.asList(c)));

        Poi p3 = new Poi();
        p3.setFoursquareId("3");
        p3.setFoursquareRating(9d);
        p3.setLocation(Location.parse("19.04258,-98.190608"));
        p3.setName("POI_3");
        p3.setCategories(new HashSet<>(Arrays.asList(c)));

        utilJdbcDao.saveCategory(c);
        utilJdbcDao.savePois(Arrays.asList(p1, p2, p3));
    }

    private void loadMockDataForFetchNearestByCategoriesLiked() {
        Category c1 = new Category();
        c1.setFoursquareId("1");
        c1.setIconUrl("url1");
        c1.setName("CAT_1");

        Category c2 = new Category();
        c2.setFoursquareId("2");
        c2.setIconUrl("url2");
        c2.setName("CAT_2");

        Category c3 = new Category();
        c3.setFoursquareId("3");
        c3.setIconUrl("url3");
        c3.setName("CAT_3");

        Poi p1 = new Poi();
        p1.setFoursquareId("1");
        p1.setFoursquareRating(9.2);
        p1.setLocation(Location.parse("19.044,-98.197753"));
        p1.setName("POI_1");
        p1.setCategories(new HashSet<>(Arrays.asList(c1)));

        Poi p2 = new Poi();
        p2.setFoursquareId("2");
        p2.setFoursquareRating(8.5);
        p2.setLocation(Location.parse("19.043148,-98.198354"));
        p2.setName("POI_2");
        p2.setCategories(new HashSet<>(Arrays.asList(c3)));

        Poi p3 = new Poi();
        p3.setFoursquareId("3");
        p3.setFoursquareRating(9d);
        p3.setLocation(Location.parse("19.04258,-98.190608"));
        p3.setName("POI_3");
        p3.setCategories(new HashSet<>(Arrays.asList(c2)));

        Poi p4 = new Poi();
        p4.setFoursquareId("4");
        p4.setFoursquareRating(9d);
        p4.setLocation(Location.parse("19.043983,-98.198805"));
        p4.setName("POI_4");
        p4.setCategories(new HashSet<>(Arrays.asList(c1)));

        Credential c = new Credential();
        c.setAccessToken("fakeToken");

        User u = new User();
        u.setFoursquareId("1");
        u.setName("John");
        u.setLastName("Doe");
        u.setPhotoUrl("url");
        u.setCredential(c);
        u.setLikes(new HashSet<>(Arrays.asList(c1, c2, c3)));

        utilJdbcDao.saveCategories(Arrays.asList(c1, c2, c3));
        utilJdbcDao.savePois(Arrays.asList(p1, p2, p3, p4));
        credentialDao.save(c);
        userDao.save(u);
    }

    private void loadMockDataFetchNearestByCategoriesLikedByFriends() {
        Category c1 = new Category();
        c1.setFoursquareId("1");
        c1.setIconUrl("url1");
        c1.setName("CAT_1");

        Category c2 = new Category();
        c2.setFoursquareId("2");
        c2.setIconUrl("url2");
        c2.setName("CAT_2");

        Category c3 = new Category();
        c3.setFoursquareId("3");
        c3.setIconUrl("url3");
        c3.setName("CAT_3");

        Poi p1 = new Poi();
        p1.setFoursquareId("1");
        p1.setFoursquareRating(9.2);
        p1.setLocation(Location.parse("19.044,-98.197753"));
        p1.setName("POI_1");
        p1.setCategories(new HashSet<>(Arrays.asList(c1)));

        Poi p2 = new Poi();
        p2.setFoursquareId("2");
        p2.setFoursquareRating(8.5);
        p2.setLocation(Location.parse("19.043148,-98.198354"));
        p2.setName("POI_2");
        p2.setCategories(new HashSet<>(Arrays.asList(c3)));

        Poi p3 = new Poi();
        p3.setFoursquareId("3");
        p3.setFoursquareRating(9d);
        p3.setLocation(Location.parse("19.04329,-98.197432"));
        p3.setName("POI_3");
        p3.setCategories(new HashSet<>(Arrays.asList(c2)));

        Poi p4 = new Poi();
        p4.setFoursquareId("4");
        p4.setFoursquareRating(9d);
        p4.setLocation(Location.parse("19.043983,-98.198805"));
        p4.setName("POI_4");
        p4.setCategories(new HashSet<>(Arrays.asList(c1)));

        Credential c = new Credential();
        c.setAccessToken("fakeToken");

        User f1 = new User();
        f1.setFoursquareId("2");
        f1.setName("John2");
        f1.setLastName("Doe2");
        f1.setPhotoUrl("url2");
        f1.setLikes(new HashSet<>(Arrays.asList(c1, c2)));

        User f2 = new User();
        f2.setFoursquareId("3");
        f2.setName("John3");
        f2.setLastName("Doe3");
        f2.setPhotoUrl("url3");
        f2.setLikes(new HashSet<>(Arrays.asList(c2)));

        User u = new User();
        u.setFoursquareId("1");
        u.setName("John1");
        u.setLastName("Doe1");
        u.setPhotoUrl("url1");
        u.setCredential(c);
        u.setLikes(new HashSet<>(Arrays.asList(c3)));
        u.setFriends(new HashSet<>(Arrays.asList(f1, f2)));

        utilJdbcDao.saveCategories(Arrays.asList(c1, c2, c3));
        utilJdbcDao.savePois(Arrays.asList(p1, p2, p3, p4));
        credentialDao.save(c);
        userDao.save(u);
    }
}