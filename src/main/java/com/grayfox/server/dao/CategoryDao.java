package com.grayfox.server.dao;

import java.util.List;
import java.util.Locale;

import com.grayfox.server.domain.Category;

public interface CategoryDao {

    List<Category> fetchLikeName(String partialName, Locale locale);
    List<Category> fetchByPoiFoursquareId(String foursquareId, Locale locale);
}