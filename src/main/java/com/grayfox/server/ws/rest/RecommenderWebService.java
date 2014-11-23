package com.grayfox.server.ws.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.grayfox.server.service.model.Recommendation;
import com.grayfox.server.service.RecommenderService;

@Named
@Path("v1/recommendations")
public class RecommenderWebService {

    private final RecommenderService recommenderService;

    @Inject
    public RecommenderWebService(RecommenderService recommenderService) {
        this.recommenderService = recommenderService;
    }

    // http://localhost:8080/gray-fox-server/api/v1/recommendations/mock?app-access-token=f3a079bf0f474de3befaeb04ac46f141
    @GET
    @Path("/mock")
    @Produces(MediaType.APPLICATION_JSON)
    public Recommendation doRecommendation(@QueryParam("app-access-token") String appAccessToken) {
        return recommenderService.doRecommendation(appAccessToken);
    }
}