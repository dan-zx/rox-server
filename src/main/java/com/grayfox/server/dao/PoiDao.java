package com.grayfox.server.dao;

import java.util.List;

import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;

public interface PoiDao {

    List<Poi> fetchAll();
    List<Poi> fetchNearestByCategory(Location location, Integer radius, String categoryFoursquareId);
}