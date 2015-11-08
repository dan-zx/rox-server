package com.grayfox.server.dao.jdbc;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import com.grayfox.server.dao.DaoException;
import com.grayfox.server.dao.PoiDao;
import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;
import org.springframework.stereotype.Repository;

@Repository("poiLocalDao")
public class PoiJdbcDao extends JdbcDao implements PoiDao {

    @Override
    public List<Poi> findNext(String poiFoursquareId, int limit, Locale locale) {
        throw new DaoException.Builder()
            .messageKey("unsuported.operation.error")
            .build();
    }

    @Override
    public List<Poi> findNearestByCategory(Location location, Integer radius, String categoryFoursquareId, Locale locale) {
        List<Poi> pois = getJdbcTemplate().query(getQuery("Poi.findNearestByCategoryFoursquareId"), 
                (ResultSet rs, int i) -> {
                    Poi poi = new Poi();
                    int columnIndex = 1;
                    poi.setId(rs.getLong(columnIndex++));
                    poi.setName(rs.getString(columnIndex++));
                    poi.setLocation(new Location());
                    poi.getLocation().setLatitude(rs.getDouble(columnIndex++));
                    poi.getLocation().setLongitude(rs.getDouble(columnIndex++));
                    poi.setFoursquareId(rs.getString(columnIndex++));
                    poi.setFoursquareRating(rs.getDouble(columnIndex++));
                    return poi;
                }, location.getLatitude(), location.getLongitude(), radius, categoryFoursquareId);
        Category category = findCategoryByFoursquareId(categoryFoursquareId, locale);
        pois.forEach(poi -> poi.setCategories(new HashSet<>(Arrays.asList(category))));
        return pois;
    }

    private Category findCategoryByFoursquareId(String foursquareId, Locale locale) {
        return getJdbcTemplate().queryForObject(getQuery("Category.findByFoursquareId", locale), 
                (ResultSet rs, int i) -> {
                    Category category = new Category();
                    int columnIndex = 1;
                    category.setId(rs.getLong(columnIndex++));
                    category.setName(rs.getString(columnIndex++));
                    category.setIconUrl(rs.getString(columnIndex++));
                    category.setFoursquareId(foursquareId);
                    return category;
                }, foursquareId);
    }
}