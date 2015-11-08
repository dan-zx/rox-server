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
package com.grayfox.server.test.dao.jdbc;

import java.util.List;
import java.util.Locale;

import com.grayfox.server.dao.jdbc.CategoryJdbcDao;
import com.grayfox.server.domain.Category;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public class MockCategoryJdbcDao extends CategoryJdbcDao {

    @Override
    public List<Category> findByPartialName(String partialName, Locale locale) {
        insertMockData();
        return super.findByPartialName(partialName, locale);
    }

    private void insertMockData() {
        Category category = new Category();
        category.setFoursquareId("4bf58dd8d48988d188941735");
        category.setIconUrl("https://ss3.4sqi.net/img/categories_v2/arts_entertainment/default_88.png");
        category.setName("Estadio de fútbol");

        getJdbcTemplate().update("CREATE (:Category {defaultName:{1}, spanishName:{2}, iconUrl:{3}, foursquareId:{4}})", category.getName(), category.getName(), category.getIconUrl(), category.getFoursquareId());
        
        category = new Category();
        category.setFoursquareId("4bf58dd8d48988d16a941735");
        category.setIconUrl("https://ss3.4sqi.net/img/categories_v2/food/default_88.png");
        category.setName("Panadería");

        getJdbcTemplate().update("CREATE (:Category {defaultName:{1}, spanishName:{2}, iconUrl:{3}, foursquareId:{4}})", category.getName(), category.getName(), category.getIconUrl(), category.getFoursquareId());
        
        category = new Category();
        category.setFoursquareId("4bf58dd8d48988d18c941735");
        category.setIconUrl("https://ss3.4sqi.net/img/categories_v2/arts_entertainment/default_88.png");
        category.setName("Estadio de béisbol");

        getJdbcTemplate().update("CREATE (:Category {defaultName:{1}, spanishName:{2}, iconUrl:{3}, foursquareId:{4}})", category.getName(), category.getName(), category.getIconUrl(), category.getFoursquareId());
    }
}