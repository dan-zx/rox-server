package com.grayfox.server.dao.jdbc;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;

import com.grayfox.server.dao.PoiDao;
import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;

import org.springframework.stereotype.Repository;

@Repository
public class PoiJdbcDao extends JdbcDao implements PoiDao {

    @Override
    public List<Poi> fetchNearestByCategoriesLiked(String accessToken, Location location, Integer radius) {
        List<Poi> pois = getJdbcTemplate().query(CypherQueries.NEAREAST_POIS_BY_CATEGORIES_LIKED, 
                (ResultSet rs, int i) -> {
                    Poi poi = new Poi();
                    poi.setName(rs.getString(1));
                    poi.setLocation(new Location());
                    poi.getLocation().setLatitude(rs.getDouble(2));
                    poi.getLocation().setLongitude(rs.getDouble(3));
                    poi.setFoursquareId(rs.getString(4));
                    return poi;
                }, accessToken, location.getLatitude(), location.getLongitude(), radius);
        pois.forEach(poi -> poi.setCategories(new HashSet<>(fetchCategoriesByPoiFoursquareId(poi.getFoursquareId()))));
        return pois;
    }

    @Override
    public List<Poi> fetchNearestByCategoriesLikedByFriends(String accessToken, Location location, Integer radius) {
        List<Poi> pois = getJdbcTemplate().query(CypherQueries.NEAREAST_POIS_BY_CATEGORIES_LIKED_BY_FRIENDS, 
                (ResultSet rs, int i) -> {
                    Poi poi = new Poi();
                    poi.setName(rs.getString(1));
                    poi.setLocation(new Location());
                    poi.getLocation().setLatitude(rs.getDouble(2));
                    poi.getLocation().setLongitude(rs.getDouble(3));
                    poi.setFoursquareId(rs.getString(4));
                    return poi;
                }, accessToken, location.getLatitude(), location.getLongitude(), radius);
        pois.forEach(poi -> poi.setCategories(new HashSet<>(fetchCategoriesByPoiFoursquareId(poi.getFoursquareId()))));
        return pois;
    }

    private List<Category> fetchCategoriesByPoiFoursquareId(String foursquareId) {
        return getJdbcTemplate().query(CypherQueries.CATEGORIES_BY_POI_FOURSQUARE_ID, 
                (ResultSet rs, int i) -> {
                    Category category = new Category();
                    category.setName(rs.getString(1));
                    category.setFoursquareId(rs.getString(2));
                    return category;
                }, foursquareId);
    }
}