package com.grayfox.server.service;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import com.grayfox.server.dao.CredentialDao;
import com.grayfox.server.dao.RecommendationDao;
import com.grayfox.server.datasource.PoiDataSource;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;
import com.grayfox.server.domain.Recommendation;
import com.grayfox.server.route.RouteProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecommenderService {

    private static final int DEFAULT_RADIUS = 20_000;
    private static final int MAX_POIS_PER_ROUTE = 6;
    private static final Logger LOGGER = LoggerFactory.getLogger(RecommenderService.class);

    @Inject private RecommendationDao recommendationDao;
    @Inject private CredentialDao credentialDao;
    @Inject private PoiDataSource poiDataSource;
    @Inject private RouteProvider routeProvider;

    @Transactional(readOnly = true)
    public List<Recommendation> recommendByLikes(String accessToken, Location location, Integer radius, RouteProvider.Transportation transportation, Locale locale) {
        if (!credentialDao.existsAccessToken(accessToken)) {
            LOGGER.warn("Not existing user attempting to retrive information");
            throw new ServiceException.Builder("user.invalid.error").build();
        }
        int trueRadius = radius != null ? radius.intValue() : DEFAULT_RADIUS;
        List<Recommendation> recommendations = recommendationDao.fetchNearestByCategoriesLiked(accessToken, location, trueRadius, locale);        
        for (Recommendation recommendation : recommendations) {
            recommendation.getPoiSequence().addAll(poiDataSource.nextPois(recommendation.getPoiSequence().get(0), MAX_POIS_PER_ROUTE, locale));
            Location destination = recommendation.getPoiSequence().get(recommendation.getPoiSequence().size()-1).getLocation();
            Location[] waypoints = toWaypoints(recommendation.getPoiSequence());
            recommendation.setRoutePoints(routeProvider.createRoute(location, destination, transportation, waypoints));
        }
        return recommendations;
    }

    @Transactional(readOnly = true)
    public List<Recommendation> recommendByFriendsLikes(String accessToken, Location location, Integer radius, RouteProvider.Transportation transportation, Locale locale) {
        if (!credentialDao.existsAccessToken(accessToken)) {
            LOGGER.warn("Not existing user attempting to retrive information");
            throw new ServiceException.Builder("user.invalid.error").build();
        }
        int trueRadius = radius != null ? radius.intValue() : DEFAULT_RADIUS;
        List<Recommendation> recommendations = recommendationDao.fetchNearestByCategoriesLikedByFriends(accessToken, location, trueRadius, locale);        
        for (Recommendation recommendation : recommendations) {
            recommendation.getPoiSequence().addAll(poiDataSource.nextPois(recommendation.getPoiSequence().get(0), MAX_POIS_PER_ROUTE, locale));
            Location destination = recommendation.getPoiSequence().get(recommendation.getPoiSequence().size()-1).getLocation();
            Location[] waypoints = toWaypoints(recommendation.getPoiSequence());
            recommendation.setRoutePoints(routeProvider.createRoute(location, destination, transportation, waypoints));
        }
        return recommendations;
    }

    private Location[] toWaypoints(List<Poi> pois) {
        Location[] waypoints = new Location[pois.size()-1];
        for (int i = 0; i < waypoints.length; i++) waypoints[i] = pois.get(i).getLocation();
        return waypoints;
    }
}