package com.grayfox.server.test.dao.foursquare;

import java.util.List;
import java.util.Locale;

import com.grayfox.server.dao.foursquare.PoiFoursquareDao;

import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;
import org.springframework.stereotype.Repository;

@Repository("poiFoursquareDao")
public class MockPoiFoursquareDao extends PoiFoursquareDao {

    @Override
    public List<Poi> fetchNext(String poiFoursquareId, int limit, Locale locale) {
        return null;
    }

    @Override
    public List<Poi> fetchNearestByCategory(Location location, Integer radius, String categoryFoursquareId, Locale locale) {
        return null;
    }
}