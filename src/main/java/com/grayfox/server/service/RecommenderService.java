package com.grayfox.server.service;

import java.util.List;

import com.grayfox.server.service.model.Poi;

public interface RecommenderService {

    List<Poi> recommend(String appAccessToken, String location, int radius);
    List<Poi> search(String appAccessToken, String location, int radius, String category);
}