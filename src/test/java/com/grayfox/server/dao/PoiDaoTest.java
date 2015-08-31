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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;
import com.grayfox.server.test.config.TestConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class PoiDaoTest {

    @Inject private PoiDao poiDao;

    @Before
    public void setUp() {
        assertThat(poiDao).isNotNull();
    }

    @Test
    public void testFetchNext() {
        Poi p1 = new Poi();
        p1.setFoursquareId("4bad0850f964a52082263be3");
        p1.setName("Cinépolis");
        p1.setLocation(Location.parse("19.032099226143384,-98.23300838470459"));
        Category category = new Category();
        category.setFoursquareId("4bf58dd8d48988d180941735");
        category.setName("Multicine");
        category.setIconUrl("https://ss3.4sqi.net/img/categories_v2/arts_entertainment/movietheater_88.png");
        p1.setCategories(new HashSet<>(Arrays.asList(category)));
        
        Poi p2 = new Poi();
        p2.setFoursquareId("4c2b7f8257a9c9b6affff567");
        p2.setName("Liverpool");
        p2.setLocation(Location.parse("19.03174044908608,-98.23100973086046"));
        category = new Category();
        category.setFoursquareId("4bf58dd8d48988d1f6941735");
        category.setName("Gran tienda");
        category.setIconUrl("https://ss3.4sqi.net/img/categories_v2/shops/departmentstore_88.png");
        p2.setCategories(new HashSet<>(Arrays.asList(category)));

        List<Poi> expectedPois = Arrays.asList(p1, p2);
        List<Poi> actualPois = poiDao.fetchNext("4c09270ea1b32d7f172297f0", 3, Locale.ROOT);

        assertThat(actualPois).isNotNull().isNotEmpty().doesNotContainNull().hasSameSizeAs(expectedPois).containsExactlyElementsOf(expectedPois);
        assertThatThrownBy(() -> poiDao.fetchNext("invalidId", 3, Locale.ROOT))
            .isInstanceOf(DaoException.class);
    }

    @Test
    public void testFetchNearestByCategory() {
        Category category = new Category();
        category.setFoursquareId("4bf58dd8d48988d151941735");
        category.setName("Taco Place");
        category.setIconUrl("https://ss3.4sqi.net/img/categories_v2/food/taco_88.png");

        Poi p1 = new Poi();
        p1.setFoursquareId("4cdd6a06930af04d92fb9597");
        p1.setName("Taquería Los Ángeles");
        p1.setLocation(Location.parse("19.04336700060403,-98.19716334342957"));
        p1.setCategories(new HashSet<>(Arrays.asList(category)));

        Poi p2 = new Poi();
        p2.setFoursquareId("4c3ce8087c1ee21ebd388d71");
        p2.setName("Antigua Taquería La Oriental");
        p2.setLocation(Location.parse("19.044926274591635,-98.19751471281052"));
        p2.setCategories(new HashSet<>(Arrays.asList(category)));

        List<Poi> expectedPois = Arrays.asList(p1, p2);
        List<Poi> actualPois = poiDao.fetchNearestByCategory(Location.parse("19.04365,-98.197968"), 800, category.getFoursquareId(), Locale.ROOT);

        assertThat(actualPois).isNotNull().isNotEmpty().doesNotContainNull().hasSameSizeAs(expectedPois).containsOnlyElementsOf(expectedPois);
        assertThatThrownBy(() -> poiDao.fetchNearestByCategory(Location.parse("19.04365,-98.197968"), 800, "invalidId", Locale.ROOT))
            .isInstanceOf(DaoException.class);
    }
}