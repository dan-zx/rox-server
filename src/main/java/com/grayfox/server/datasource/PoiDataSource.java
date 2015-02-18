package com.grayfox.server.datasource;

import java.util.List;
import java.util.Locale;

import com.grayfox.server.domain.Poi;

public interface PoiDataSource {

    List<Poi> nextPois(Poi originPoi, int limit, Locale locale);
}