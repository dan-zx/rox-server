package com.grayfox.server.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.foursquare4j.FoursquareApi;
import com.foursquare4j.response.Result;
import com.foursquare4j.response.Venue;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.LatLng;

import com.grayfox.server.dao.AppUserDao;
import com.grayfox.server.dao.model.AppUser;
import com.grayfox.server.service.RecommenderService;
import com.grayfox.server.service.model.Recommendation;

import org.springframework.transaction.annotation.Transactional;

@Named
public class MockRecommenderService implements RecommenderService {

    // TODO: Implement a real RecommenderService

    private final AppUserDao appUserDao;
    private final FoursquareApi foursquareApi;
    private final GeoApiContext geoApiContext;

    @Inject
    public MockRecommenderService(AppUserDao appUserDao, FoursquareApi foursquareApi, GeoApiContext geoApiContext) {
        this.appUserDao = appUserDao;
        this.foursquareApi = foursquareApi;
        this.geoApiContext = geoApiContext;
    }

    @Override
    @Transactional
    public Recommendation doRecommendation(String appAccessToken) {
        AppUser appUser = appUserDao.fetchByAppAccessToken(appAccessToken);
        // FIXME: hardcoded exception message
        if (appUser == null) throw new ServiceException("Invalid user");
        foursquareApi.setAccessToken(appUser.getFoursquareAccessToken());
        List<Recommendation.Poi> recommendedPois = mockRecommendation();
        DirectionsApiRequest directionRequest = DirectionsApi
            .newRequest(geoApiContext)
            .origin(recommendedPois.get(0).getLocation())
            .destination(recommendedPois.get(recommendedPois.size()-1).getLocation());

        String[] waypoints = new String[recommendedPois.size()-2]; 
        for (int index = 1; index < recommendedPois.size()-1; index++) {
            waypoints[index-1] = latLngToString(recommendedPois.get(index).getLocation());
        }

        try {
            DirectionsRoute route = directionRequest.waypoints(waypoints).await()[0];
            List<LatLng> routePoints = routePointsFromRoute(route);
            Recommendation recommendation = new Recommendation();
            recommendation.setPois(recommendedPois);
            recommendation.setRoutePoints(routePoints);
            return recommendation;
        } catch (Exception ex) {
            // FIXME: hardcoded exception message
            throw new ServiceException("Route creation error", ex);
        }
    }

    private List<Recommendation.Poi> mockRecommendation() {
        return Arrays.asList(poiFromFoursquareVenue(foursquareApi.getVenue("4e0f9573ae603a50b54e2fc4")),
                poiFromFoursquareVenue(foursquareApi.getVenue("51edf3fb498e7eebb027f3e6")),
                poiFromFoursquareVenue(foursquareApi.getVenue("4c95128e72dd224b0131a091")), 
                poiFromFoursquareVenue(foursquareApi.getVenue("512bb30cf31c08b32ec6861a")));
    }

    private Recommendation.Poi poiFromFoursquareVenue(Result<Venue> venue) {
        Recommendation.Poi poi = new Recommendation.Poi();
        poi.setId(venue.getResponse().getId());
        poi.setName(venue.getResponse().getName());
        poi.setLocation(new LatLng(venue.getResponse().getLocation().getLat(), venue.getResponse().getLocation().getLng()));
        return poi;
    }

    private String latLngToString(LatLng latLng) {
        return new StringBuilder()
            .append(latLng.lat)
            .append(',')
            .append(latLng.lng)
            .toString();
    }

    private List<LatLng> routePointsFromRoute(DirectionsRoute route) {
        ArrayList<LatLng> routePoints = new ArrayList<>(); 
        for (DirectionsLeg leg : route.legs) {
            for (DirectionsStep step : leg.steps) {
                routePoints.add(step.startLocation);
                routePoints.add(step.endLocation);
            }
        }
        return routePoints;
    }
}