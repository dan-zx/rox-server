package com.grayfox.server.dao.foursquare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import com.foursquare4j.FoursquareApi;
import com.foursquare4j.response.Group;
import com.foursquare4j.response.Result;
import com.foursquare4j.response.Venue;

import com.grayfox.server.dao.DaoException;
import com.grayfox.server.dao.PoiDao;
import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class PoiFoursquareDao implements PoiDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PoiFoursquareDao.class);

    @Value("${foursquare.app.client.id}")     private String clientId; 
    @Value("${foursquare.app.client.secret}") private String clientSecret;

    @Override
    public List<Poi> fetchNext(String poiFoursquareId, int limit, Locale locale) {
        FoursquareApi foursquareApi = new FoursquareApi(clientId, clientSecret);
        foursquareApi.setLocale(locale);
        Result<Venue> venueResult = foursquareApi.getVenue(poiFoursquareId);
        if (venueResult.getMeta().getCode() == 200) {
            Poi seed = toPoi(venueResult.getResponse());
            Set<String> categoryIds = new HashSet<>();
            categoryIds.addAll(seed.getCategories().stream().map(Category::getFoursquareId).collect(Collectors.toSet()));
            List<Poi> pois = new ArrayList<>(limit);
            Poi currentPoi = seed;
            for (int numberOfPois = 0; numberOfPois < limit-1; numberOfPois++) {
                Result<Group<Venue>> nextVenues = foursquareApi.getNextVenues(currentPoi.getFoursquareId());
                if (nextVenues.getMeta().getCode() == 200) {
                    for (Venue venue : nextVenues.getResponse().getItems()) {
                        currentPoi = toPoi(venue);
                        final String currentFoursquarId = venue.getId();
                        List<Poi> matchingPois = pois.stream().filter(poi -> poi.getFoursquareId().equals(currentFoursquarId)).collect(Collectors.toList());
                        if (matchingPois.isEmpty()) {
                            boolean existsCategory = false;
                            for (Category currentPoiCategory : currentPoi.getCategories()) {
                                if (!categoryIds.add(currentPoiCategory.getFoursquareId())) {
                                    existsCategory = true;
                                    break;
                                }
                            }
                            if (!existsCategory) {
                                pois.add(currentPoi);
                                break;
                            }
                        }
                    }
                } else {
                    LOGGER.error("Foursquare error while requesting [venues/{}/nextvenues] [code={}, errorType={}, errorDetail={}]", currentPoi.getFoursquareId(), nextVenues.getMeta().getCode(), nextVenues.getMeta().getErrorType(), nextVenues.getMeta().getErrorDetail());
                    throw new DaoException.Builder()
                        .messageKey("foursquare.request.error")
                        .addMessageArgument(nextVenues.getMeta().getErrorDetail())
                        .build();
                }
            }
            return pois;
        } else {
            LOGGER.error("Foursquare error while requesting [venues/{}] [code={}, errorType={}, errorDetail={}]", poiFoursquareId, venueResult.getMeta().getCode(), venueResult.getMeta().getErrorType(), venueResult.getMeta().getErrorDetail());
            throw new DaoException.Builder()
                .messageKey("foursquare.request.error")
                .addMessageArgument(venueResult.getMeta().getErrorDetail())
                .build();
        }
    }

    @Override
    public List<Poi> fetchNearestByCategory(Location location, Integer radius, String categoryFoursquareId, Locale locale) {
        FoursquareApi foursquareApi = new FoursquareApi(clientId, clientSecret);
        foursquareApi.setLocale(locale);
        Result<Venue[]> venuesResult = foursquareApi.searchVenues(location.stringValues(), null, null, null, null, null, null, null, radius, null, null, categoryFoursquareId, null, null, null);
        if (venuesResult.getMeta().getCode() == 200) {
            List<Poi> pois = new ArrayList<>(venuesResult.getResponse().length);
            Arrays.stream(venuesResult.getResponse()).forEach(venue -> pois.add(toPoi(venue)));
            return pois;
        } else {
            LOGGER.error("Foursquare error while requesting [venues/search] [code={}, errorType={}, errorDetail={}]", venuesResult.getMeta().getCode(), venuesResult.getMeta().getErrorType(), venuesResult.getMeta().getErrorDetail());
            throw new DaoException.Builder()
                .messageKey("foursquare.request.error")
                .addMessageArgument(venuesResult.getMeta().getErrorDetail())
                .build();
        }
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
            myCategory.setIconUrl(new StringBuilder().append(foursquareCategory.getIcon().getPrefix()).append("88").append(foursquareCategory.getIcon().getSuffix()).toString());
            myCategory.setFoursquareId(foursquareCategory.getId());
            poi.getCategories().add(myCategory);
        }
        return poi;
    }
}