package com.grayfox.server.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.foursquare4j.FoursquareApi;
import com.foursquare4j.response.Group;
import com.foursquare4j.response.Result;
import com.foursquare4j.response.Venue;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.grayfox.server.dao.CredentialDao;
import com.grayfox.server.dao.RecommendationDao;
import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;
import com.grayfox.server.domain.Recommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecommenderService {

    public static enum Transportation { DRIVING, WALKING, BICYCLING, TRANSIT }

    private static final int DEFAULT_RADIUS = 20_000;
    private static final int MAX_POIS_PER_ROUTE = 6;
    private static final Logger LOGGER = LoggerFactory.getLogger(RecommenderService.class);

    @Inject private RecommendationDao recommendationDao;
    @Inject private CredentialDao credentialDao;
    @Inject private FoursquareApi foursquareApi;
    @Inject private GeoApiContext geoApiContext;

    @Transactional(readOnly = true)
    public List<Recommendation> recommendByLikes(String accessToken, Location location, Integer radius, Transportation transportation) {
        if (!credentialDao.existsAccessToken(accessToken)) {
            LOGGER.error("Not existing user attempting to retrive information");
            throw new ServiceException.Builder("user.invalid.error").build();
        }
        int trueRadius = radius != null ? radius.intValue() : DEFAULT_RADIUS;
        List<Recommendation> recommendations = recommendationDao.fetchNearestByCategoriesLiked(accessToken, location, trueRadius);        
        for (Recommendation recommendation : recommendations) {
            recommendation.getPoiSequence().addAll(nextPois(recommendation.getPoiSequence().get(0)));
            recommendation.setRoutePoints(createRoute(location, recommendation.getPoiSequence(), transportation));
        }
        return recommendations;
    }

    @Transactional(readOnly = true)
    public List<Recommendation> recommendByFriendsLikes(String accessToken, Location location, Integer radius, Transportation transportation) {
        if (!credentialDao.existsAccessToken(accessToken)) {
            LOGGER.error("Not existing user attempting to retrive information");
            throw new ServiceException.Builder("user.invalid.error").build();
        }
        int trueRadius = radius != null ? radius.intValue() : DEFAULT_RADIUS;
        List<Recommendation> recommendations = recommendationDao.fetchNearestByCategoriesLikedByFriends(accessToken, location, trueRadius);        
        for (Recommendation recommendation : recommendations) {
            recommendation.getPoiSequence().addAll(nextPois(recommendation.getPoiSequence().get(0)));
            recommendation.setRoutePoints(createRoute(location, recommendation.getPoiSequence(), transportation));
        }
        return recommendations;
    }

    private Poi toPoi(Venue venue) {
        Poi poi = new Poi();
        poi.setName(venue.getName());
        poi.setLocation(new Location());
        poi.getLocation().setLatitude(venue.getLocation().getLat());
        poi.getLocation().setLongitude(venue.getLocation().getLng());
        poi.setFoursquareId(venue.getId());
        poi.setCategories(new HashSet<>());
        for (com.foursquare4j.response.Category foursquareCategory : venue.getCategories()) {
            Category myCategory = new Category();
            myCategory.setName(foursquareCategory.getName());
            myCategory.setFoursquareId(foursquareCategory.getId());
            poi.getCategories().add(myCategory);
        }
        return poi;
    }

    private List<Poi> nextPois(Poi originPoi) {
        List<Poi> pois = new ArrayList<>(MAX_POIS_PER_ROUTE);
        Poi currentPoi = originPoi;
        for (int numberOfPois = 0; numberOfPois < MAX_POIS_PER_ROUTE-1; numberOfPois++) {
            Result<Group<Venue>> nextVenues = foursquareApi.getNextVenues(currentPoi.getFoursquareId());
            if (nextVenues.getMeta().getCode() == 200) {
                for (Venue venue : nextVenues.getResponse().getItems()) {
                    currentPoi = toPoi(venue);
                    final String currentFoursquarId = venue.getId();
                    List<Poi> matchingPois = pois.stream().filter(poi -> poi.getFoursquareId().equals(currentFoursquarId)).collect(Collectors.toList());
                    if (matchingPois.isEmpty()) {
                        pois.add(currentPoi);
                        break;
                    }
                }
            } else {
                LOGGER.error("Foursquare error while requesting [venues/{}/nextvenues] [code={}, errorType={}, errorDetail={}]", currentPoi.getFoursquareId(), nextVenues.getMeta().getCode(), nextVenues.getMeta().getErrorType(), nextVenues.getMeta().getErrorDetail());
                throw new ServiceException.Builder("foursquare.request.error").build();
            }
        }
        return pois;
    }

    private List<Location> createRoute(Location origin, List<Poi> pois, Transportation transportation) {
        if (pois.size() > 0) {
            DirectionsApiRequest directionRequest = DirectionsApi.newRequest(geoApiContext)
                .origin(toDirectionsPointString(origin))
                .destination(toDirectionsPointString(pois.get(pois.size()-1).getLocation()))
                .mode(toTravelMode(transportation));
    
            String[] waypoints = new String[pois.size()-1]; 
            for (int index = 0; index < pois.size()-1; index++) waypoints[index] = toDirectionsPointString(pois.get(index).getLocation());
    
            try {
                DirectionsRoute[] routes = directionRequest.waypoints(waypoints).await();
                if (routes.length > 0) return getPath(routes[0]);
                else {
                    LOGGER.error("Google failed to create a vaild route");
                    throw new ServiceException.Builder("route.unavailable.error").build();
                }
            } catch (Exception ex) {
                LOGGER.error("Google error while requesting route", ex);
                throw new ServiceException.Builder("google.request.error").setCause(ex).build();
            }
        } else return new ArrayList<>(0);
    }

    private TravelMode toTravelMode(Transportation transportation) {
        if (transportation == null) return TravelMode.DRIVING;
        switch (transportation) {
            case DRIVING: return TravelMode.DRIVING;
            case WALKING: return TravelMode.WALKING;
            case BICYCLING: return TravelMode.BICYCLING;
            case TRANSIT: return TravelMode.TRANSIT;
            default: return TravelMode.UNKNOWN;
        }
    }
    
    private List<Location> getPath(DirectionsRoute route) {
        if (route.overviewPolyline != null) {
            List<LatLng> polyline = route.overviewPolyline.decodePath();
            if (!polyline.isEmpty()) {
                List<Location> path = new ArrayList<>(polyline.size()); 
                polyline.forEach((point) -> {
                    Location location = new Location();
                    location.setLatitude(point.lat);
                    location.setLongitude(point.lng);
                    path.add(location);
                });
                return path;
            }
        }
        return new ArrayList<>(0);
    }

    private String toDirectionsPointString(Location location) {
        return new StringBuilder().append(location.getLatitude()).append(',')
                .append(location.getLongitude()).toString();
    }

    public void setLocale(Locale locale) {
        recommendationDao.setLocale(locale);
        foursquareApi.setLocale(locale);
    }
}