package com.grayfox.server.route;

import java.util.List;

import com.grayfox.server.domain.Location;

public interface RouteProvider {

    enum Transportation { DRIVING, WALKING, BICYCLING, TRANSIT }

    List<Location> createRoute(Location origin, Location destination, Transportation transportation, Location... waypoints);
}