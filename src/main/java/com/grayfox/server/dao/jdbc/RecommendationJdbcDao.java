package com.grayfox.server.dao.jdbc;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import com.grayfox.server.dao.RecommendationDao;
import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;
import com.grayfox.server.domain.Recommendation;
import com.grayfox.server.util.Messages;

import org.springframework.stereotype.Repository;

@Repository
public class RecommendationJdbcDao extends JdbcDao implements RecommendationDao {

    @Override
    public List<Recommendation> fetchNearestByCategoriesLiked(String accessToken, Location location, Integer radius, Locale locale) {
        List<Recommendation> recommendations = getJdbcTemplate().query(CypherQueries.NEAREAST_RECOMMENDATIONS_BY_CATEGORIES_LIKED, 
                (ResultSet rs, int i) -> {
                    Recommendation recommendation = new Recommendation();
                    Poi poi = new Poi();
                    poi.setName(rs.getString(1));
                    poi.setLocation(new Location());
                    poi.getLocation().setLatitude(rs.getDouble(2));
                    poi.getLocation().setLongitude(rs.getDouble(3));
                    poi.setFoursquareId(rs.getString(4));
                    recommendation.setPoiSequence(new ArrayList<>());
                    recommendation.getPoiSequence().add(poi);
                    recommendation.setType(Recommendation.Type.SELF);
                    recommendation.setReason(Messages.get("recommendation.self.reason", locale, new Object[] {rs.getString(5)}));
                    return recommendation;
                }, accessToken, location.getLatitude(), location.getLongitude(), radius);
        recommendations.forEach(recommendation -> recommendation.getPoiSequence().get(0).setCategories(new HashSet<>(fetchCategoriesByPoiFoursquareId(recommendation.getPoiSequence().get(0).getFoursquareId()))));
        return recommendations;
    }

    @Override
    public List<Recommendation> fetchNearestByCategoriesLikedByFriends(String accessToken, Location location, Integer radius, Locale locale) {
        List<Recommendation> recommendations = getJdbcTemplate().query(CypherQueries.NEAREAST_RECOMMENDATIONS_BY_CATEGORIES_LIKED_BY_FRIENDS, 
                (ResultSet rs, int i) -> {
                    Recommendation recommendation = new Recommendation();
                    Poi poi = new Poi();
                    poi.setName(rs.getString(1));
                    poi.setLocation(new Location());
                    poi.getLocation().setLatitude(rs.getDouble(2));
                    poi.getLocation().setLongitude(rs.getDouble(3));
                    poi.setFoursquareId(rs.getString(4));
                    recommendation.setPoiSequence(new ArrayList<>());
                    recommendation.getPoiSequence().add(poi);
                    recommendation.setType(Recommendation.Type.SOCIAL);
                    String lastName = rs.getString(6);
                    String friendName = lastName == null || lastName.trim().isEmpty() ? rs.getString(5) : new StringBuilder().append(rs.getString(5)).append(" ").append(lastName).toString();
                    recommendation.setReason(Messages.get("recommendation.social.reason", locale, new Object[] {friendName, rs.getString(7)}));
                    return recommendation;
                }, accessToken, location.getLatitude(), location.getLongitude(), radius);
        recommendations.forEach(recommendation -> recommendation.getPoiSequence().get(0).setCategories(new HashSet<>(fetchCategoriesByPoiFoursquareId(recommendation.getPoiSequence().get(0).getFoursquareId()))));
        return recommendations;
    }

    private List<Category> fetchCategoriesByPoiFoursquareId(String foursquareId) {
        return getJdbcTemplate().query(CypherQueries.CATEGORIES_BY_POI_FOURSQUARE_ID, 
                (ResultSet rs, int i) -> {
                    Category category = new Category();
                    category.setName(rs.getString(1));
                    category.setFoursquareId(rs.getString(2));
                    return category;
                }, foursquareId);
    }
}