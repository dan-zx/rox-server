package com.grayfox.server.service;

import java.util.List;

import com.grayfox.server.service.model.Location;
import com.grayfox.server.service.model.Poi;

public interface RouteService {

    List<Location> createRoute(List<Poi> pois, Transportation transportation);

    enum Transportation { DRIVING, WALKING, BICYCLING, TRANSIT }
}