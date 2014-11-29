package com.grayfox.server.ws.rest;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.grayfox.server.service.RecommenderService;
import com.grayfox.server.service.RouteService;
import com.grayfox.server.service.model.Location;
import com.grayfox.server.service.model.Poi;
import com.grayfox.server.ws.model.Recommendation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@Path("v1/recommendations")
public class RecommenderWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommenderWebService.class);

    private final RecommenderService recommenderService;
    private final RouteService routeService;

    @Inject
    public RecommenderWebService(RecommenderService recommenderService, RouteService routeService) {
        super();
        this.recommenderService = recommenderService;
        this.routeService = routeService;
    }

    @GET
    @Path("/recommend")
    @Produces(MediaType.APPLICATION_JSON)
    public Recommendation recommend(
            @QueryParam("app-access-token") String appAccessToken,
            @QueryParam("location") String location,
            @QueryParam("radius") int radius,
            @QueryParam("transportation") RouteService.Transportation transportation) {
        LOGGER.debug("recommend({}, {}, {}, {})", appAccessToken, location, radius, transportation);
        List<Poi> pois = recommenderService.recommend(appAccessToken, location, radius);
        List<Location> routePoints = routeService.createRoute(pois, transportation);
        Recommendation recommendation = new Recommendation();
        recommendation.setPois(pois);
        recommendation.setRoutePoints(routePoints);
        return recommendation;
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Recommendation search(
            @QueryParam("app-access-token") String appAccessToken,
            @QueryParam("location") String location,
            @QueryParam("radius") int radius,
            @QueryParam("category") String category,
            @QueryParam("transportation") RouteService.Transportation transportation) {
        LOGGER.debug("search({}, {}, {}, {}, {})", appAccessToken, location, radius, category, transportation);
        List<Poi> pois = recommenderService.search(appAccessToken, location, radius, category);
        List<Location> routePoints = routeService.createRoute(pois, transportation);
        Recommendation recommendation = new Recommendation();
        recommendation.setPois(pois);
        recommendation.setRoutePoints(routePoints);
        return recommendation;
    }
}