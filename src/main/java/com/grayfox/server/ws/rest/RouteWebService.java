package com.grayfox.server.ws.rest;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.grayfox.server.domain.Location;
import com.grayfox.server.route.RouteProvider;
import com.grayfox.server.service.RouteService;
import com.grayfox.server.ws.rest.constraints.CheckTransportation;

import org.hibernate.validator.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;

@Controller
@Path("routes")
public class RouteWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteWebService.class);

    @Inject private RouteService routeService;

    @POST
    @Path("build")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Result<List<Location>> buildRoute(
            @NotBlank(message = "access_token.required.error") @QueryParam("access-token") String accessToken, 
            @NotBlank(message = "origin.required.error") @Pattern(message = "location.format.error", regexp = "(\\-?\\d+(\\.\\d+)?),(\\-?\\d+(\\.\\d+)?)") @QueryParam("origin") String originStr,
            @NotBlank(message = "destination.required.error") @Pattern(message = "location.format.error", regexp = "(\\-?\\d+(\\.\\d+)?),(\\-?\\d+(\\.\\d+)?)") @QueryParam("destination") String destinationStr,
            @CheckTransportation @QueryParam("transportation") String transportationStr, 
            Location[] waypoints) {
        LOGGER.debug("buildRoute({}, {}, {}, {}, {})", accessToken, originStr, destinationStr, transportationStr, Arrays.toString(waypoints));
        return new Result<>(routeService.buildRoute(accessToken, parseLocation(originStr), parseLocation(destinationStr), transportationStr == null ? null : RouteProvider.Transportation.valueOf(transportationStr), waypoints));
    }

    private Location parseLocation(String locationString) {
        String[] latLngStr = locationString.split(",");
        Location location = new Location();
        location.setLatitude(Double.parseDouble(latLngStr[0]));
        location.setLongitude(Double.parseDouble(latLngStr[1]));
        return location;
    }
}