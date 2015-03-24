package com.grayfox.server.ws.rest;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.grayfox.server.domain.Poi;

import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Recommendation;
import com.grayfox.server.service.RecommenderService;

import org.hibernate.validator.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;

@Controller
@Path("recommendations")
public class RecommenderWebService extends BaseRestComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommenderWebService.class);

    @Inject private RecommenderService recommenderService;

    @GET
    @Path("byall")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<List<Recommendation>> recommendByAll(
            @NotBlank(message = "access_token.required.error") @QueryParam("access-token") String accessToken,
            @NotBlank(message = "location.required.error") @Pattern(message = "location.format.error", regexp = "(\\-?\\d+(\\.\\d+)?),(\\-?\\d+(\\.\\d+)?)") @QueryParam("location") String locationString,
            @Pattern(message = "radius.format.error", regexp = "[1-9]\\d*") @QueryParam("radius") String radiusStr) {
        LOGGER.debug("recommendByLikes({}, {}, {})", accessToken, locationString, radiusStr);
        Integer radius = radiusStr == null || radiusStr.trim().isEmpty() ? null : Integer.parseInt(radiusStr);
        return new Result<>(recommenderService.recommendByAll(accessToken, parseLocation(locationString), radius, getClientLocale()));
    }

    @GET
    @Path("bylikes")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<List<Recommendation>> recommendByLikes(
            @NotBlank(message = "access_token.required.error") @QueryParam("access-token") String accessToken,
            @NotBlank(message = "location.required.error") @Pattern(message = "location.format.error", regexp = "(\\-?\\d+(\\.\\d+)?),(\\-?\\d+(\\.\\d+)?)") @QueryParam("location") String locationString,
            @Pattern(message = "radius.format.error", regexp = "[1-9]\\d*") @QueryParam("radius") String radiusStr) {
        LOGGER.debug("recommendByLikes({}, {}, {})", accessToken, locationString, radiusStr);
        Integer radius = radiusStr == null || radiusStr.trim().isEmpty() ? null : Integer.parseInt(radiusStr);
        return new Result<>(recommenderService.recommendByLikes(accessToken, parseLocation(locationString), radius, getClientLocale()));
    }

    @GET
    @Path("byfriendslikes")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<List<Recommendation>> recommendByFriendsLikes(
            @NotBlank(message = "access_token.required.error") @QueryParam("access-token") String accessToken,
            @NotBlank(message = "location.required.error") @Pattern(message = "location.format.error", regexp = "(\\-?\\d+(\\.\\d+)?),(\\-?\\d+(\\.\\d+)?)") @QueryParam("location") String locationString,
            @Pattern(message = "radius.format.error", regexp = "[1-9]\\d*") @QueryParam("radius") String radiusStr) {
        LOGGER.debug("recommendByFriendsLikes({}, {}, {})", accessToken, locationString, radiusStr);
        Integer radius = radiusStr == null || radiusStr.trim().isEmpty() ? null : Integer.parseInt(radiusStr);
        return new Result<>(recommenderService.recommendByFriendsLikes(accessToken, parseLocation(locationString), radius, getClientLocale()));
    }

    @POST
    @Path("nextpois")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Result<List<Poi>> nextPois(
            @NotBlank(message = "access_token.required.error") @QueryParam("access-token") String accessToken,
            Poi seed) {
        LOGGER.debug("nextPois({}, {})", accessToken, seed);
        return new Result<>(recommenderService.nextPois(accessToken, seed, getClientLocale()));
    }

    private Location parseLocation(String locationString) {
        String[] latLngStr = locationString.split(",");
        Location location = new Location();
        location.setLatitude(Double.parseDouble(latLngStr[0]));
        location.setLongitude(Double.parseDouble(latLngStr[1]));
        return location;
    }
}