package com.grayfox.server.ws.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.grayfox.server.domain.Location;
import com.grayfox.server.service.RecommenderService;
import com.grayfox.server.service.RecommenderService.Transportation;
import com.grayfox.server.service.domain.Recommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Path("recommendations")
public class RecommenderWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommenderWebService.class);
    
    @Inject private RecommenderService recommenderService;

    @GET
    @Path("bylikes")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<List<Recommendation>> recommendByLikes(
            @QueryParam("access-token") String accessToken,
            @QueryParam("location") String locationString,
            @QueryParam("radius") Integer radius,
            @QueryParam("transportation") Transportation transportation) {
        LOGGER.debug("recommendByLikes({}, {}, {}, {})", accessToken, locationString, radius, transportation);

        RequiredArgException.Builder requiredArgExceptionBuilder = new RequiredArgException.Builder();
        if (accessToken == null || accessToken.trim().isEmpty()) requiredArgExceptionBuilder.addRequiredArg("access-token");
        if (locationString == null || locationString.trim().isEmpty()) requiredArgExceptionBuilder.addRequiredArg("location");
        if (transportation == null) requiredArgExceptionBuilder.addRequiredArg("transportation");
        requiredArgExceptionBuilder.throwIfNotEmpty();

        return new Result<>(recommenderService.recommendByLikes(accessToken, parseLocation(locationString), radius, transportation));
    }

    @GET
    @Path("byfriendslikes")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<List<Recommendation>> recommendByFriendsLikes(
            @QueryParam("access-token") String accessToken,
            @QueryParam("location") String locationString,
            @QueryParam("radius") Integer radius,
            @QueryParam("transportation") Transportation transportation) {
        LOGGER.debug("recommendByFriendsLikes({}, {}, {}, {})", accessToken, locationString, radius, transportation);

        RequiredArgException.Builder requiredArgExceptionBuilder = new RequiredArgException.Builder();
        if (accessToken == null || accessToken.trim().isEmpty()) requiredArgExceptionBuilder.addRequiredArg("access-token");
        if (locationString == null || locationString.trim().isEmpty()) requiredArgExceptionBuilder.addRequiredArg("location");
        if (transportation == null) requiredArgExceptionBuilder.addRequiredArg("transportation");
        requiredArgExceptionBuilder.throwIfNotEmpty();

        return new Result<>(recommenderService.recommendByFriendsLikes(accessToken, parseLocation(locationString), radius, transportation));
    }

    private Location parseLocation(String locationString) {
        String[] latLngStr = locationString.split(",");
        if (latLngStr.length == 0 || latLngStr.length > 2) {
            LOGGER.error("Incorrect location format [{}]", locationString);
            throw new InvalidFormatException.Builder("latlng.format.error")
                .addFormatArg(locationString)
                .build();
        } else {
            try {
                return new Location(Double.parseDouble(latLngStr[0]), Double.parseDouble(latLngStr[1]));
            } catch (Exception ex) {
                LOGGER.error("Incorrect location format [{}]", locationString);
                throw new InvalidFormatException.Builder("latlng.format.error")
                    .addFormatArg(locationString)
                    .build();
            }
        }
    }
}