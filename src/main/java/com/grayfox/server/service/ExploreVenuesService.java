package com.grayfox.server.service;

import java.util.List;

import fi.foyt.foursquare.api.entities.CompactVenue;

public interface ExploreVenuesService {

    List<CompactVenue> recommendVenues(Long id, String latLng);
}
