package com.grayfox.server.dao.jdbc;

import java.sql.ResultSet;
import java.util.List;

import com.grayfox.server.dao.PoiDao;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;

import org.springframework.stereotype.Repository;

@Repository
public class PoiJdbcDao extends JdbcDao implements PoiDao {

    @Override
    public List<Poi> fetchAll() {
        return getJdbcTemplate().query(getQuery("allPois"), 
                (ResultSet rs, int i) -> {
                    Poi poi = new Poi();
                    poi.setName(rs.getString(1));
                    poi.setLocation(new Location());
                    poi.getLocation().setLatitude(rs.getDouble(2));
                    poi.getLocation().setLongitude(rs.getDouble(3));
                    poi.setFoursquareId(rs.getString(4));
                    poi.setFoursquareRating(rs.getDouble(5));
                    return poi;
                });
    }

    @Override
    public List<Poi> fetchNearestByCategory(Location location, Integer radius, String categoryFoursquareId) {
        return getJdbcTemplate().query(getQuery("nearestPoisByCategory"), 
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
    }
}