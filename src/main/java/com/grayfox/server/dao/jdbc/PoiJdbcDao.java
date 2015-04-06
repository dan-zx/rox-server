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
                    Location location = new Location();
                    location.setLatitude(rs.getDouble(2));
                    location.setLongitude(rs.getDouble(3));
                    poi.setLocation(location);
                    poi.setFoursquareId(rs.getString(4));
                    return poi;
                });
    }

    @Override
    public List<Poi> fetchNearestByCategory(Location location, Integer radius, String categoryFoursquareId) {
        return getJdbcTemplate().query(getQuery("nearestPoisByCategory"), 
                (ResultSet rs, int i) -> {
                    Poi poi = new Poi();
                    poi.setName(rs.getString(1));
                    Location poiLocation = new Location();
                    poiLocation.setLatitude(rs.getDouble(2));
                    poiLocation.setLongitude(rs.getDouble(3));
                    poi.setLocation(poiLocation);
                    poi.setFoursquareId(rs.getString(4));
                    return poi;
                }, categoryFoursquareId, location.getLatitude(), location.getLongitude(), radius);
    }
}