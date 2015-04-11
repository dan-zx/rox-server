package com.grayfox.server.dao.jdbc;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import com.grayfox.server.domain.Category;
import com.grayfox.server.dao.PoiDao;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;
import org.springframework.stereotype.Repository;

@Repository
public class PoiJdbcDao extends JdbcDao implements PoiDao {

    @Override
    public List<Poi> fetchNearestByCategory(Location location, Integer radius, String categoryFoursquareId, Locale locale) {
        List<Poi> pois = getJdbcTemplate().query(getQuery("nearestPoisByCategory"), 
                (ResultSet rs, int i) -> {
                    Poi poi = new Poi();
                    poi.setName(rs.getString(1));
                    poi.setLocation(new Location());
                    poi.getLocation().setLatitude(rs.getDouble(2));
                    poi.getLocation().setLongitude(rs.getDouble(3));
                    poi.setFoursquareId(rs.getString(4));
                    poi.setFoursquareRating(rs.getDouble(5));
                    return poi;
                }, categoryFoursquareId, location.getLatitude(), location.getLongitude(), radius);
        pois.forEach(poi -> poi.setCategories(new HashSet<>(fetchCategoriesByPoiFoursquareId(poi.getFoursquareId(), locale))));
        return pois;
    }

    private List<Category> fetchCategoriesByPoiFoursquareId(String foursquareId, Locale locale) {
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