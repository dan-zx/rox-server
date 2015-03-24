package com.grayfox.server.datasource.foursquare;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import com.foursquare4j.FoursquareApi;
import com.foursquare4j.response.Group;
import com.foursquare4j.response.Result;
import com.foursquare4j.response.Venue;

import com.grayfox.server.datasource.DataSourceException;
import com.grayfox.server.datasource.PoiDataSource;
import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class PoiFoursquareDataSource implements PoiDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PoiFoursquareDataSource.class);

    @Value("${foursquare.app.client.id}")     private String clientId; 
    @Value("${foursquare.app.client.secret}") private String clientSecret;

    @Override
    public List<Poi> nextPois(Poi originPoi, int limit, Locale locale) {
        FoursquareApi foursquareApi = new FoursquareApi(clientId, clientSecret);
        Set<String> categoryIds = new HashSet<>();
        categoryIds.addAll(originPoi.getCategories().stream().map(Category::getFoursquareId).collect(Collectors.toSet()));
        foursquareApi.setLocale(locale);
        List<Poi> pois = new ArrayList<>(limit);
        Poi currentPoi = originPoi;
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
                throw new DataSourceException.Builder("foursquare.request.error").build();
            }
        }
        return pois;
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