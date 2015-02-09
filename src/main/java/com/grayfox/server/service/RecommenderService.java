package com.grayfox.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.grayfox.server.dao.CredentialDao;
import com.grayfox.server.domain.Category;
import org.springframework.transaction.annotation.Transactional;
import com.grayfox.server.service.domain.Recommendation;
import com.foursquare4j.response.Group;
import com.foursquare4j.response.Result;
import com.foursquare4j.FoursquareApi;
import com.foursquare4j.response.Venue;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.grayfox.server.dao.PoiDao;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RecommenderService {

    public static enum Transportation { DRIVING, WALKING, BICYCLING, TRANSIT }

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommenderService.class);

    @Inject private PoiDao poiDao;
    @Inject private CredentialDao credentialDao;
    @Inject private FoursquareApi foursquareApi;
    @Inject private GeoApiContext geoApiContext;

    @Transactional
    public List<Recommendation> recommendByLikes(String accessToken, Location location, Integer radius, Transportation transportation) {
        if (!credentialDao.existsAccessToken(accessToken)) {
            LOGGER.error("Not existing user attempting to retrive information");
            throw new ServiceException.Builder("user.invalid.error").build();
        }
        int trueRadius = radius != null ? radius.intValue() : 20_000;
        List<Poi> recommendPois = poiDao.fetchNearestByCategoriesLiked(accessToken, location, trueRadius);
        List<Recommendation> recommendations = new ArrayList<>(recommendPois.size());
        for (Poi recommendPoi : recommendPois) {
            Result<Group<Venue>> nextVenues = foursquareApi.getNextVenues(recommendPoi.getFoursquareId());
            if (nextVenues.getMeta().getCode() == 200) {
                List<Poi> pois = new ArrayList<>(nextVenues.getResponse().getItems().length+1);
                pois.add(recommendPoi);
                Arrays.stream(nextVenues.getResponse().getItems()).forEach(venue -> pois.add(toPoi(venue)));
                List<Location> route = createRoute(pois, transportation);
                recommendations.add(new Recommendation(pois, route));
            } else {
                LOGGER.error("Foursquare error while requesting [venues/{}/nextvenues] [code={}, errorType={}, errorDetail={}]", recommendPoi.getFoursquareId(), nextVenues.getMeta().getCode(), nextVenues.getMeta().getErrorType(), nextVenues.getMeta().getErrorDetail());
                throw new ServiceException.Builder("foursquare.request.error").build();
            }
        }
        return recommendations;
    }

    @Transactional
    public List<Recommendation> recommendByFriendsLikes(String accessToken, Location location, Integer radius, Transportation transportation) {
        if (!credentialDao.existsAccessToken(accessToken)) {
            LOGGER.error("Not existing user attempting to retrive information");
            throw new ServiceException.Builder("user.invalid.error").build();
        }
        int trueRadius = radius != null ? radius.intValue() : 20_000;
        List<Poi> recommendPois = poiDao.fetchNearestByCategoriesLikedByFriends(accessToken, location, trueRadius);
        List<Recommendation> recommendations = new ArrayList<>(recommendPois.size());
        for (Poi recommendPoi : recommendPois) {
            Result<Group<Venue>> nextVenues = foursquareApi.getNextVenues(recommendPoi.getFoursquareId());
            if (nextVenues.getMeta().getCode() == 200) {
                List<Poi> pois = new ArrayList<>(nextVenues.getResponse().getItems().length+1);
                pois.add(recommendPoi);
                Arrays.stream(nextVenues.getResponse().getItems()).forEach(venue -> pois.add(toPoi(venue)));
                List<Location> route = createRoute(pois, transportation);
                recommendations.add(new Recommendation(pois, route));
            } else {
                LOGGER.error("Foursquare error while requesting [venues/{}/nextvenues] [code={}, errorType={}, errorDetail={}]", recommendPoi.getFoursquareId(), nextVenues.getMeta().getCode(), nextVenues.getMeta().getErrorType(), nextVenues.getMeta().getErrorDetail());
                throw new ServiceException.Builder("foursquare.request.error").build();
            }
        }
        return recommendations;
    }

    private Poi toPoi(Venue venue) {
        Poi poi = new Poi();
        poi.setName(venue.getName());
        poi.setLocation(new Location(venue.getLocation().getLat(), venue.getLocation().getLng()));
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

    private List<Location> createRoute(List<Poi> pois, Transportation transportation) {
        if (pois.size() > 1) {
            DirectionsApiRequest directionRequest = DirectionsApi.newRequest(geoApiContext)
                .origin(toDirectionsPointString(pois.get(0).getLocation()))
                .destination(toDirectionsPointString(pois.get(pois.size()-1).getLocation()))
                .mode(toTravelMode(transportation));
    
            String[] waypoints = new String[pois.size()-2]; 
            for (int index = 1; index < pois.size()-1; index++) waypoints[index-1] = toDirectionsPointString(pois.get(index).getLocation());
    
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
                polyline.forEach((point) -> path.add(new Location(point.lat, point.lng)));
                return path;
            }
        }
        return new ArrayList<>(0);
    }

    private String toDirectionsPointString(Location location) {
        return new StringBuilder().append(location.getLatitude()).append(',')
                .append(location.getLongitude()).toString();
    }
}