package com.grayfox.server.service;

import com.grayfox.server.service.model.Recommendation;

public interface RecommenderService {

    Recommendation doRecommendation(String appAccessToken);
}