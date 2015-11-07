/*
 * Copyright 2014-2015 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.grayfox.server.dao.jdbc;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.grayfox.server.dao.RecommendationDao;
import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;
import com.grayfox.server.domain.Recommendation;
import com.grayfox.server.util.Messages;

import org.springframework.stereotype.Repository;

@Repository("recommendationLocalDao")
public class RecommendationJdbcDao extends JdbcDao implements RecommendationDao {

    @Override
    public List<Recommendation> fetchNearestByRating(Location location, Integer radius, Locale locale) {
        List<Recommendation> recommendations = getJdbcTemplate().query(getQuery("nearestRecommendationsByRating"), 
                (ResultSet rs, int i) -> {
                        Recommendation recommendation = new Recommendation();
                        Poi poi = new Poi();
                        int columnIndex = 1;
                        poi.setId(rs.getLong(columnIndex++));
                        poi.setName(rs.getString(columnIndex++));
                        poi.setLocation(new Location());
                        poi.getLocation().setLatitude(rs.getDouble(columnIndex++));
                        poi.getLocation().setLongitude(rs.getDouble(columnIndex++));
                        poi.setFoursquareId(rs.getString(columnIndex++));
                        poi.setFoursquareRating(rs.getDouble(columnIndex++));
                        recommendation.setType(Recommendation.Type.GLOBAL);
                        recommendation.setReason(Messages.get("recommendation.global.reason", locale));
                        recommendation.setPoi(poi);
                        return recommendation;
                }, location.getLatitude(), location.getLongitude(), radius);
        recommendations.forEach(recommendation -> recommendation.getPoi().setCategories(new HashSet<>(fetchCategoriesByPoiFoursquareId(recommendation.getPoi().getFoursquareId(), locale))));
        return recommendations;
    }

    @Override
    public List<Recommendation> fetchNearestByCategoriesLiked(String accessToken, Location location, Integer radius, Locale locale) {
        Set<String> categoryNames = new HashSet<>();
        List<Recommendation> recommendations = getJdbcTemplate().query(getQuery("nearestRecommendationsByCategoriesLiked", locale), 
                (ResultSet rs, int i) -> {
                    String categoryName = rs.getString(7);
                    if (categoryNames.add(categoryName)) {
                        Recommendation recommendation = new Recommendation();
                        Poi poi = new Poi();
                        int columnIndex = 1;
                        poi.setId(rs.getLong(columnIndex++));
                        poi.setName(rs.getString(columnIndex++));
                        poi.setLocation(new Location());
                        poi.getLocation().setLatitude(rs.getDouble(columnIndex++));
                        poi.getLocation().setLongitude(rs.getDouble(columnIndex++));
                        poi.setFoursquareId(rs.getString(columnIndex++));
                        poi.setFoursquareRating(rs.getDouble(columnIndex++));
                        recommendation.setType(Recommendation.Type.SELF);
                        recommendation.setReason(Messages.get("recommendation.self.reason", locale, categoryName));
                        recommendation.setPoi(poi);
                        return recommendation;
                    } else return null;
                }, accessToken, location.getLatitude(), location.getLongitude(), radius);
        recommendations = recommendations.stream().filter(Objects::nonNull).collect(Collectors.toList());
        recommendations.forEach(recommendation -> recommendation.getPoi().setCategories(new HashSet<>(fetchCategoriesByPoiFoursquareId(recommendation.getPoi().getFoursquareId(), locale))));
        return recommendations;
    }

    @Override
    public List<Recommendation> fetchNearestByCategoriesLikedByFriends(String accessToken, Location location, Integer radius, Locale locale) {
        Set<String> categoryNames = new HashSet<>();
        List<Recommendation> recommendations = getJdbcTemplate().query(getQuery("nearestRecommendationsByCategoriesLikedByFriends", locale), 
                (ResultSet rs, int i) -> {
                    String categoryName = rs.getString(9);
                    if (categoryNames.add(categoryName)) {
                        Recommendation recommendation = new Recommendation();
                        Poi poi = new Poi();
                        int columnIndex = 1;
                        poi.setId(rs.getLong(columnIndex++));
                        poi.setName(rs.getString(columnIndex++));
                        poi.setLocation(new Location());
                        poi.getLocation().setLatitude(rs.getDouble(columnIndex++));
                        poi.getLocation().setLongitude(rs.getDouble(columnIndex++));
                        poi.setFoursquareId(rs.getString(columnIndex++));
                        poi.setFoursquareRating(rs.getDouble(columnIndex++));
                        recommendation.setType(Recommendation.Type.SOCIAL);
                        String friendFirstName = rs.getString(columnIndex++);
                        String friendLastName = rs.getString(columnIndex++);
                        String friendFullName = friendLastName == null || friendLastName.trim().isEmpty() ? friendFirstName : friendFirstName + " " +  friendLastName;
                        recommendation.setReason(Messages.get("recommendation.social.reason", locale, friendFullName, categoryName));
                        recommendation.setPoi(poi);
                        return recommendation;
                    } else return null;
                }, accessToken, location.getLatitude(), location.getLongitude(), radius);
        recommendations = recommendations.stream().filter(Objects::nonNull).collect(Collectors.toList());
        recommendations.forEach(recommendation -> recommendation.getPoi().setCategories(new HashSet<>(fetchCategoriesByPoiFoursquareId(recommendation.getPoi().getFoursquareId(), locale))));
        return recommendations;
    }

    private List<Category> fetchCategoriesByPoiFoursquareId(String foursquareId, Locale locale) {
        return getJdbcTemplate().query(getQuery("categoriesByPoiFoursquareId", locale), 
                (ResultSet rs, int i) -> {
                    Category category = new Category();
                    int columnIndex = 1;
                    category.setId(rs.getLong(columnIndex++));
                    category.setName(rs.getString(columnIndex++));
                    category.setIconUrl(rs.getString(columnIndex++));
                    category.setFoursquareId(rs.getString(columnIndex++));
                    return category;
                }, foursquareId);
    }
}