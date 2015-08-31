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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;

import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.Poi;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UtilJdbcDao {

    @Inject private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    protected void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveCategory(Category category) {
        jdbcTemplate.update("CREATE (:Category {defaultName:{1}, spanishName:{2}, iconUrl:{3}, foursquareId:{4}})", category.getName(), category.getName(), category.getIconUrl(), category.getFoursquareId());
    }

    public void saveCategories(List<Category> categories) {
        categories.forEach(category -> saveCategory(category));
    }

    public void savePoi(Poi poi) {
        jdbcTemplate.update("CREATE (:Poi {name:{1}, latitude:{2}, longitude:{3}, foursquareId:{4}, foursquareRating:{5}})", poi.getName(), poi.getLocation().getLatitude(), poi.getLocation().getLongitude(), poi.getFoursquareId(), poi.getFoursquareRating());
    }

    public void savePois(List<Poi> pois) {
        pois.forEach(poi -> savePoi(poi));
    }
}