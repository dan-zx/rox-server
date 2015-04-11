package com.grayfox.server.dao;

import java.util.List;
import java.util.Locale;

import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Recommendation;

public interface RecommendationDao {

    List<Recommendation> fetchNearestByRating(Location location, Integer radius, Locale locale);
    List<Recommendation> fetchNearestByCategoriesLiked(String accessToken, Location location, Integer radius, Locale locale);
    List<Recommendation> fetchNearestByCategoriesLikedByFriends(String accessToken, Location location, Integer radius, Locale locale);
}