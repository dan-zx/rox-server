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
    public List<Category> fetchLikeName(String partialName, Locale locale) {
        insertMockData();
        return super.fetchLikeName(partialName, locale);
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