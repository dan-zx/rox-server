package com.grayfox.server.route.google;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import com.grayfox.server.domain.Location;
import com.grayfox.server.route.RouteException;
import com.grayfox.server.route.RouteProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

@Component
public class RouteGoogleMapsProvider implements RouteProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteGoogleMapsProvider.class);

    @Inject private GeoApiContext geoApiContext;

    @Override
    public List<Location> createRoute(Location origin, Location destination, Transportation transportation, Location... waypoints) {
        DirectionsApiRequest directionRequest = DirectionsApi.newRequest(geoApiContext)
                .origin(toDirectionsPointString(origin))
                .destination(toDirectionsPointString(destination))
                .mode(toTravelMode(transportation));
        if (waypoints != null && waypoints.length > 0) directionRequest.waypoints(toRequestWaypoints(waypoints));
        try {
            DirectionsRoute[] routes = directionRequest.await();
            if (routes != null && routes.length > 0) return getPath(routes[0]);
            else {
                LOGGER.warn("Google failed to create a vaild route");
                throw new RouteException.Builder("route.unavailable.error").build();
            }
        } catch (Exception ex) {
            LOGGER.error("Google error while requesting route", ex);
            throw new RouteException.Builder("google.request.error").setCause(ex).build();
        }
    }

    private TravelMode toTravelMode(Transportation transportation) {
        if (transportation == null) return TravelMode.DRIVING;
        switch (transportation) {
            case DRIVING:
                return TravelMode.DRIVING;
            case WALKING:
                return TravelMode.WALKING;
            case BICYCLING:
                return TravelMode.BICYCLING;
            case TRANSIT:
                return TravelMode.TRANSIT;
            default:
                return TravelMode.UNKNOWN;
        }
    }

    private String toDirectionsPointString(Location location) {
        return new StringBuilder().append(location.getLatitude())
                .append(',').append(location.getLongitude()).toString();
    }

    private String[] toRequestWaypoints(Location[] waypoints) {
        String[] requestWaypoints = new String[waypoints.length];
        for (int index = 0; index < requestWaypoints.length; index++) requestWaypoints[index] = toDirectionsPointString(waypoints[index]);
        return requestWaypoints;
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
}