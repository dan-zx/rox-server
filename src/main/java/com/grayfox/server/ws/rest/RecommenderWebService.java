package com.grayfox.server.ws.rest;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Recommendation;
import com.grayfox.server.route.RouteProvider;
import com.grayfox.server.service.RecommenderService;
import com.grayfox.server.ws.rest.constraints.CheckTransportation;

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
    @Path("bylikes")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<List<Recommendation>> recommendByLikes(
            @NotBlank(message = "access_token.required.error") @QueryParam("access-token") String accessToken,
            @NotBlank(message = "location.required.error") @Pattern(message = "location.format.error", regexp = "(\\-?\\d+(\\.\\d+)?),(\\-?\\d+(\\.\\d+)?)") @QueryParam("location") String locationString,
            @Pattern(message = "radius.format.error", regexp = "[1-9]\\d*") @QueryParam("radius") String radiusStr,
            @NotNull(message = "transportation.required.error") @CheckTransportation @QueryParam("transportation") String transportationStr) {
        LOGGER.debug("recommendByLikes({}, {}, {}, {})", accessToken, locationString, radiusStr, transportationStr);
        Integer radius = radiusStr == null || radiusStr.trim().isEmpty() ? null : Integer.parseInt(radiusStr);
        recommenderService.setLocale(getClientLocale());
        return new Result<>(recommenderService.recommendByLikes(accessToken, parseLocation(locationString), radius, RouteProvider.Transportation.valueOf(transportationStr)));
    }

    @GET
    @Path("byfriendslikes")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<List<Recommendation>> recommendByFriendsLikes(
            @NotBlank(message = "access_token.required.error") @QueryParam("access-token") String accessToken,
            @NotBlank(message = "location.required.error") @Pattern(message = "location.format.error", regexp = "(\\-?\\d+(\\.\\d+)?),(\\-?\\d+(\\.\\d+)?)") @QueryParam("location") String locationString,
            @Pattern(message = "radius.format.error", regexp = "[1-9]\\d*") @QueryParam("radius") String radiusStr,
            @NotNull(message = "transportation.required.error") @CheckTransportation @QueryParam("transportation") String transportationStr) {
        LOGGER.debug("recommendByFriendsLikes({}, {}, {}, {})", accessToken, locationString, radiusStr, transportationStr);
        Integer radius = radiusStr == null || radiusStr.trim().isEmpty() ? null : Integer.parseInt(radiusStr);
        recommenderService.setLocale(getClientLocale());
        return new Result<>(recommenderService.recommendByFriendsLikes(accessToken, parseLocation(locationString), radius, RouteProvider.Transportation.valueOf(transportationStr)));
    }

    private Location parseLocation(String locationString) {
        String[] latLngStr = locationString.split(",");
        Location location = new Location();
        location.setLatitude(Double.parseDouble(latLngStr[0]));
        location.setLongitude(Double.parseDouble(latLngStr[1]));
        return location;
    }
}