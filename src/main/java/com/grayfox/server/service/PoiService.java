package com.grayfox.server.service;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import com.grayfox.server.dao.CategoryDao;
import com.grayfox.server.dao.PoiDao;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PoiService {

    @Inject private PoiDao poiDao;
    @Inject private CategoryDao categoryDao;

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
}