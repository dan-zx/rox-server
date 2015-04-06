package com.grayfox.server.dao.jdbc;

import java.sql.ResultSet;
import java.util.List;
import java.util.Locale;

import com.grayfox.server.dao.CategoryDao;
import com.grayfox.server.domain.Category;

import org.springframework.stereotype.Repository;

@Repository
public class CategoryJdbcDao extends JdbcDao implements CategoryDao {

    @Override
    public List<Category> fetchLikeName(String partialName, Locale locale) {
        String query = String.format(getQuery("categoriesByPartialName", locale), partialName);
        return getJdbcTemplate().query(query, 
                (ResultSet rs, int i) -> {
                    Category category = new Category();
                    category.setName(rs.getString(1));
                    category.setIconUrl(rs.getString(2));
                    category.setFoursquareId(rs.getString(3));
                    return category;
                });
    }

    @Override
    public List<Category> fetchByPoiFoursquareId(String foursquareId, Locale locale) {
        return getJdbcTemplate().query(getQuery("categoriesByPoiFoursquareId", locale), 
                (ResultSet rs, int i) -> {
                    Category category = new Category();
                    category.setName(rs.getString(1));
                    category.setIconUrl(rs.getString(2));
                    category.setFoursquareId(rs.getString(3));
                    return category;
                }, foursquareId);
    }
}