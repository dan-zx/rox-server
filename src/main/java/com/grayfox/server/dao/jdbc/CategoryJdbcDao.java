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
package com.grayfox.server.dao.jdbc;

import java.sql.ResultSet;
import java.util.List;
import java.util.Locale;

import com.grayfox.server.dao.CategoryDao;
import com.grayfox.server.domain.Category;

import org.springframework.stereotype.Repository;

@Repository("categoryLocalDao")
public class CategoryJdbcDao extends JdbcDao implements CategoryDao {

    @Override
    public List<Category> findByPartialName(String partialName, Locale locale) {
        String query = String.format(getQuery("Category.findByPartialName", locale), partialName);
        return getJdbcTemplate().query(query, 
                (ResultSet rs, int i) -> {
                    Category category = new Category();
                    int columnIndex = 1;
                    category.setId(rs.getLong(columnIndex++));
                    category.setName(rs.getString(columnIndex++));
                    category.setIconUrl(rs.getString(columnIndex++));
                    category.setFoursquareId(rs.getString(columnIndex++));
                    return category;
                });
    }
}