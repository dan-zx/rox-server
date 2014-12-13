package com.grayfox.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.foursquare4j.FoursquareApi;
import com.foursquare4j.response.ExploreVenueGroups;
import com.foursquare4j.response.ExploreVenueGroups.VenueRecommendation;
import com.foursquare4j.response.Group;
import com.foursquare4j.response.Result;
import com.foursquare4j.response.Venue;

import com.grayfox.server.dao.AppUserDao;
import com.grayfox.server.dao.model.AppUser;
import com.grayfox.server.service.RecommenderService;
import com.grayfox.server.service.model.Location;
import com.grayfox.server.service.model.Poi;

@Named
public class RecommenderServiceImpl implements RecommenderService {

    private final AppUserDao appUserDao;
    private final FoursquareApi foursquareApi;

    @Inject
    public RecommenderServiceImpl(AppUserDao appUserDao, FoursquareApi foursquareApi) {
        this.appUserDao = appUserDao;
        this.foursquareApi = foursquareApi;
    }

    @Override
    public List<Poi> search(String appAccessToken, String location, int radius, String category) {
        AppUser appUser = appUserDao.fetchByAppAccessToken(appAccessToken);
        if (appUser != null) foursquareApi.setAccessToken(appUser.getFoursquareAccessToken());
        Result<ExploreVenueGroups> result = foursquareApi.exploreVenues(location, null, null, null, null, radius, null, category, 4, null, null, null, null, null, null, null, true, null, null, null, null);
        if (result.getMeta().getCode() == 200) {
            List<Poi> pois = new ArrayList<>();
            for(Group<VenueRecommendation> group : result.getResponse().getGroups()) {
                for (VenueRecommendation venueRecommendation : group.getItems()) {
                    pois.add(toPoi(venueRecommendation.getVenue()));
                }
            }
            return pois;
         } else {
            // TODO: Hardcoded exception message
            String message = new StringBuilder()
                .append("Invalid Foursquare request: \"")
                .append(result.getMeta().getErrorType())
                .append("\":\"")
                .append(result.getMeta().getErrorDetail())
                .append("\"")
                .toString();
            throw new ServiceException(message);
        }
    }
    
    private Poi toPoi(Venue venue) {
        Poi poi = new Poi();
        poi.setId(venue.getId());
        poi.setName(venue.getName());
        Location latLng = new Location();
        latLng.setLatitude(venue.getLocation().getLat());
        latLng.setLongitude(venue.getLocation().getLng());
        poi.setLocation(latLng);
        return poi;
    }
}