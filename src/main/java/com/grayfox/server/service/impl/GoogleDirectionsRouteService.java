package com.grayfox.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import com.grayfox.server.service.RouteService;
import com.grayfox.server.service.model.Location;
import com.grayfox.server.service.model.Poi;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GoogleDirectionsRouteService implements RouteService {

    private final GeoApiContext geoApiContext;

    @Inject
    public GoogleDirectionsRouteService(GeoApiContext geoApiContext) {
        this.geoApiContext = geoApiContext;
    }

    @Override
    public List<Location> createRoute(List<Poi> pois, Transportation transportation) {
        if (pois.size() > 1) {
            DirectionsApiRequest directionRequest = DirectionsApi.newRequest(geoApiContext)
                .origin(pois.get(0).getLocation().stringValues())
                .destination(pois.get(pois.size()-1).getLocation().stringValues())
                .mode(toTravelMode(transportation));
    
            String[] waypoints = new String[pois.size()-2]; 
            for (int index = 1; index < pois.size()-1; index++) waypoints[index-1] = pois.get(index).getLocation().stringValues();
    
            try {
                DirectionsRoute[] routes = directionRequest.waypoints(waypoints).await();
                if (routes.length > 0) return getPath(routes[0]);
                else return new ArrayList<>(0); // TODO: Handle case when route is unavailable
            } catch (Exception ex) {
                // FIXME: Hardcoded exception message
                throw new ServiceException("Route creation error", ex);
            }
        } else return new ArrayList<>(0);
    }

    private TravelMode toTravelMode(Transportation transportation) {
        if (transportation == null) return TravelMode.DRIVING;
        switch (transportation) {
            case DRIVING: return TravelMode.DRIVING;
            case WALKING: return TravelMode.WALKING;
            case BICYCLING: return TravelMode.BICYCLING;
            // case TRANSIT: return TravelMode.TRANSIT;
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
}