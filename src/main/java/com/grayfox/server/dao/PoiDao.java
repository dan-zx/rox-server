package com.grayfox.server.dao;

import java.util.List;

import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;

public interface PoiDao {

    List<Poi> fetchNearestByCategoriesLiked(String accessToken, Location location, Integer radius);
    List<Poi> fetchNearestByCategoriesLikedByFriends(String accessToken, Location location, Integer radius);
}