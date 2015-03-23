package com.grayfox.server.dao.jdbc;

import static com.grayfox.server.dao.jdbc.CypherQueries.*;

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
        String query = String.format(getQueryFrom(CATEGORIES_LIKE_NAME_I18N, locale), partialName);
        return getJdbcTemplate().query(query, 
                (ResultSet rs, int i) -> {
                    Category category = new Category();
                    category.setName(rs.getString(1));
                    category.setIconUrl(rs.getString(2));
                    category.setFoursquareId(rs.getString(3));
                    return category;
                });
    }
}