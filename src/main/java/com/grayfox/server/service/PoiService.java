package com.grayfox.server.service;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import com.grayfox.server.dao.CategoryDao;
import com.grayfox.server.dao.PoiDao;
import com.grayfox.server.datasource.PoiDataSource;
import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PoiService {

    private static final int MAX_POIS_PER_ROUTE = 6;

    @Inject private PoiDao poiDao;
    @Inject private CategoryDao categoryDao;
    @Inject private PoiDataSource poiDataSource;

    @Transactional(readOnly = true)
    public List<Poi> getPois(Locale locale) {
        List<Poi> pois = poiDao.fetchAll();
        pois.forEach(poi -> poi.setCategories(new HashSet<>(categoryDao.fetchByPoiFoursquareId(poi.getFoursquareId(), locale))));
        return pois;
    }

    @Transactional(readOnly = true)
    public List<Poi> getNearestPoisByCategory(Location location, int radius, String categoryFoursquareId, Locale locale) {
        List<Poi> pois = poiDao.fetchNearestByCategory(location, radius, categoryFoursquareId);
        pois.forEach(poi -> poi.setCategories(new HashSet<>(categoryDao.fetchByPoiFoursquareId(poi.getFoursquareId(), locale))));
        return pois;
    }

    public List<Poi> nextPois(Poi seed, Locale locale) {
        return poiDataSource.nextPois(seed, MAX_POIS_PER_ROUTE, locale);
    }

    @Transactional(readOnly = true)
    public List<Category> getCategoriesLikeName(String partialName, Locale locale) {
        return categoryDao.fetchLikeName(partialName, locale);
    }
}