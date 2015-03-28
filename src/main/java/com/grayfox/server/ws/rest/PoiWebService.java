package com.grayfox.server.ws.rest;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;
import com.grayfox.server.service.PoiService;

import org.hibernate.validator.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;

@Controller
@Path("pois")
public class PoiWebService extends BaseRestComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(PoiWebService.class);

    @Inject private PoiService poiService;

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<List<Poi>> getPois() {
        return new Result<>(poiService.getPois(getClientLocale()));
    }

    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<List<Poi>> searchPoisByCategory(
            @NotBlank(message = "location.required.error") @Pattern(message = "location.format.error", regexp = "(\\-?\\d+(\\.\\d+)?),(\\-?\\d+(\\.\\d+)?)") @QueryParam("location") String locationString,
            @Pattern(message = "radius.format.error", regexp = "[1-9]\\d*") @QueryParam("radius") String radiusStr,
            @NotBlank(message = "category_foursquare_id.required.error") @QueryParam("category-foursquare-id") String categoryFoursquareId) {
        LOGGER.debug("searchPoisByCategory({}, {}, {})", locationString, radiusStr, categoryFoursquareId);
        Integer radius = radiusStr == null || radiusStr.trim().isEmpty() ? null : Integer.parseInt(radiusStr);
        return new Result<>(poiService.getNearestPoisByCategory(Location.parse(locationString), radius, categoryFoursquareId, getClientLocale()));
    }
}