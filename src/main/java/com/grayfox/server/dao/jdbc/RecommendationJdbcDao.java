package com.grayfox.server.dao.jdbc;

import static com.grayfox.server.dao.jdbc.CypherQueries.*;

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

@Repository
public class RecommendationJdbcDao extends JdbcDao implements RecommendationDao {

    private static final int SIZE_LIMIT = 5;

    @Override
    public List<Recommendation> fetchNearestByCategoriesLiked(String accessToken, Location location, Integer radius, Locale locale) {
        Set<String> categories = new HashSet<>();
        List<Recommendation> recommendations = getJdbcTemplate().query(getQueryFrom(NEAREAST_RECOMMENDATIONS_BY_CATEGORIES_LIKED_I18N, locale), 
                (ResultSet rs, int i) -> {
                    String category = rs.getString(5);
                    if (categories.add(category)) {
                        Recommendation recommendation = new Recommendation();
                        Poi poi = new Poi();
                        poi.setName(rs.getString(1));
                        poi.setLocation(new Location());
                        poi.getLocation().setLatitude(rs.getDouble(2));
                        poi.getLocation().setLongitude(rs.getDouble(3));
                        poi.setFoursquareId(rs.getString(4));
                        recommendation.setType(Recommendation.Type.SELF);
                        recommendation.setReason(Messages.get("recommendation.self.reason", locale, new Object[] {category}));
                        recommendation.setPoi(poi);
                        return recommendation;
                    } else return null;
                }, accessToken, location.getLatitude(), location.getLongitude(), radius);
        recommendations = recommendations.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (recommendations.size() > SIZE_LIMIT) recommendations = recommendations.subList(0, SIZE_LIMIT-1);
        recommendations.forEach(recommendation -> recommendation.getPoi().setCategories(new HashSet<>(fetchCategoriesByPoiFoursquareId(recommendation.getPoi().getFoursquareId(), locale))));
        return recommendations;
    }

    @Override
    public List<Recommendation> fetchNearestByCategoriesLikedByFriends(String accessToken, Location location, Integer radius, Locale locale) {
        Set<String> categories = new HashSet<>();
        List<Recommendation> recommendations = getJdbcTemplate().query(getQueryFrom(NEAREAST_RECOMMENDATIONS_BY_CATEGORIES_LIKED_BY_FRIENDS_I18N, locale), 
                (ResultSet rs, int i) -> {
                    String category = rs.getString(7);
                    if (categories.add(category)) {
                        Recommendation recommendation = new Recommendation();
                        Poi poi = new Poi();
                        poi.setName(rs.getString(1));
                        poi.setLocation(new Location());
                        poi.getLocation().setLatitude(rs.getDouble(2));
                        poi.getLocation().setLongitude(rs.getDouble(3));
                        poi.setFoursquareId(rs.getString(4));
                        recommendation.setType(Recommendation.Type.SOCIAL);
                        String lastName = rs.getString(6);
                        String friendName = lastName == null || lastName.trim().isEmpty() ? rs.getString(5) : new StringBuilder().append(rs.getString(5)).append(" ").append(lastName).toString();
                        recommendation.setReason(Messages.get("recommendation.social.reason", locale, new Object[] {friendName, category}));
                        recommendation.setPoi(poi);
                        return recommendation;
                    } else return null;
                }, accessToken, location.getLatitude(), location.getLongitude(), radius);
        recommendations = recommendations.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (recommendations.size() > SIZE_LIMIT) recommendations = recommendations.subList(0, SIZE_LIMIT-1); 
        recommendations.forEach(recommendation -> recommendation.getPoi().setCategories(new HashSet<>(fetchCategoriesByPoiFoursquareId(recommendation.getPoi().getFoursquareId(), locale))));
        return recommendations;
    }

    private List<Category> fetchCategoriesByPoiFoursquareId(String foursquareId, Locale locale) {
        return getJdbcTemplate().query(getQueryFrom(CATEGORIES_BY_POI_FOURSQUARE_ID_I18N, locale), 
                (ResultSet rs, int i) -> {
                    Category category = new Category();
                    category.setName(rs.getString(1));
                    category.setIconUrl(rs.getString(2));
                    category.setFoursquareId(rs.getString(3));
                    return category;
                }, foursquareId);
    }
}