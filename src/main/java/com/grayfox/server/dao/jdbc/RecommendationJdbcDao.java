package com.grayfox.server.dao.jdbc;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.grayfox.server.domain.Category;

import com.grayfox.server.dao.RecommendationDao;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;
import com.grayfox.server.domain.Recommendation;
import com.grayfox.server.util.Messages;
import org.springframework.stereotype.Repository;

@Repository
public class RecommendationJdbcDao extends JdbcDao implements RecommendationDao {

    @Override
    public List<Recommendation> fetchNearestByRating(Location location, Integer radius, Locale locale) {
        List<Recommendation> recommendations = getJdbcTemplate().query(getQuery("nearestRecommendationsByRating"), 
                (ResultSet rs, int i) -> {
                        Recommendation recommendation = new Recommendation();
                        Poi poi = new Poi();
                        poi.setName(rs.getString(1));
                        poi.setLocation(new Location());
                        poi.getLocation().setLatitude(rs.getDouble(2));
                        poi.getLocation().setLongitude(rs.getDouble(3));
                        poi.setFoursquareId(rs.getString(4));
                        poi.setFoursquareRating(rs.getDouble(5));
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
                    String categoryName = rs.getString(6);
                    if (categoryNames.add(categoryName)) {
                        Recommendation recommendation = new Recommendation();
                        Poi poi = new Poi();
                        poi.setName(rs.getString(1));
                        poi.setLocation(new Location());
                        poi.getLocation().setLatitude(rs.getDouble(2));
                        poi.getLocation().setLongitude(rs.getDouble(3));
                        poi.setFoursquareId(rs.getString(4));
                        poi.setFoursquareRating(rs.getDouble(5));
                        recommendation.setType(Recommendation.Type.SELF);
                        recommendation.setReason(Messages.get("recommendation.self.reason", locale, new Object[] {categoryName}));
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
                    String categoryName = rs.getString(8);
                    if (categoryNames.add(categoryName)) {
                        Recommendation recommendation = new Recommendation();
                        Poi poi = new Poi();
                        poi.setName(rs.getString(1));
                        poi.setLocation(new Location());
                        poi.getLocation().setLatitude(rs.getDouble(2));
                        poi.getLocation().setLongitude(rs.getDouble(3));
                        poi.setFoursquareId(rs.getString(4));
                        poi.setFoursquareRating(rs.getDouble(5));
                        recommendation.setType(Recommendation.Type.SOCIAL);
                        String lastName = rs.getString(7);
                        String friendName = lastName == null || lastName.trim().isEmpty() ? rs.getString(6) : new StringBuilder().append(rs.getString(6)).append(" ").append(lastName).toString();
                        recommendation.setReason(Messages.get("recommendation.social.reason", locale, new Object[] {friendName, categoryName}));
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
                    category.setName(rs.getString(1));
                    category.setIconUrl(rs.getString(2));
                    category.setFoursquareId(rs.getString(3));
                    return category;
                }, foursquareId);
    }
}