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
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import com.grayfox.server.domain.Category;
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
public class CategoryDaoTest {

    @Inject private CategoryDao categoryDao;

    @Before
    public void setUp() {
        assertThat(categoryDao).isNotNull();
    }

    @Test
    @Transactional
    public void testFetchLikeName() {
        Category c1 = new Category();
        c1.setFoursquareId("4bf58dd8d48988d188941735");
        c1.setIconUrl("https://ss3.4sqi.net/img/categories_v2/arts_entertainment/default_88.png");
        c1.setName("Estadio de fútbol");
        
        Category c2 = new Category();
        c2.setFoursquareId("4bf58dd8d48988d18c941735");
        c2.setIconUrl("https://ss3.4sqi.net/img/categories_v2/arts_entertainment/default_88.png");
        c2.setName("Estadio de béisbol");

        List<Category> expectedCategories = Arrays.asList(c1, c2);
        
        assertThat(categoryDao.fetchLikeName("estadio", Locale.ROOT)).isNotNull().isNotEmpty().containsOnlyElementsOf(expectedCategories);
    }
}