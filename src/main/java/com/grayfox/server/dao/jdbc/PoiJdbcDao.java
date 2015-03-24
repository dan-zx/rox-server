package com.grayfox.server.dao.jdbc;

import static com.grayfox.server.dao.jdbc.CypherQueries.*;

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
        return getJdbcTemplate().query(POIS, 
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
}