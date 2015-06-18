/*
 * Copyright 2014-2015 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.grayfox.server.ws.rest;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;
import com.grayfox.server.domain.Recommendation;
import com.grayfox.server.service.PoiService;
import com.grayfox.server.util.Constants;
import com.grayfox.server.ws.rest.response.Response;

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
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response<List<Poi>> searchPoisByCategory(
            @NotBlank(message = "location.required.error") @Pattern(message = "location.format.error", regexp = Constants.Regexs.LOCATION) @QueryParam("location") String locationStr,
            @NotBlank(message = "radius.required.error") @Pattern(message = "radius.format.error", regexp = Constants.Regexs.POSITIVE_INT) @QueryParam("radius") String radiusStr,
            @NotBlank(message = "category_foursquare_id.required.error") @QueryParam("category_foursquare_id") String categoryFoursquareId) {
        LOGGER.debug("searchPoisByCategory({}, {}, {})", locationStr, radiusStr, categoryFoursquareId);
        return new Response<>(poiService.getNearestPoisByCategory(Location.parse(locationStr), Integer.parseInt(radiusStr), categoryFoursquareId, getClientLocale()));
    }

    @GET
    @Path("route")
    @Produces(MediaType.APPLICATION_JSON)
    public Response<List<Poi>> route(@NotBlank(message = "poi_foursquare_id.required.error") @QueryParam("poi_foursquare_id") String poiFoursquareId) {
        LOGGER.debug("route({})", poiFoursquareId);
        return new Response<>(poiService.buildRoute(poiFoursquareId, getClientLocale()));
    }

    @GET
    @Path("recommend")
    @Produces(MediaType.APPLICATION_JSON)
    public Response<List<Recommendation>> recommend(
            @QueryParam("access_token") String accessToken,
            @NotBlank(message = "location.required.error") @Pattern(message = "location.format.error", regexp = Constants.Regexs.LOCATION) @QueryParam("location") String locationStr,
            @NotBlank(message = "radius.required.error") @Pattern(message = "radius.format.error", regexp = Constants.Regexs.POSITIVE_INT) @QueryParam("radius") String radiusStr) {
        if (accessToken != null && accessToken.trim().isEmpty()) accessToken = null;
        LOGGER.debug("recommend({}, {}, {})", accessToken, locationStr, radiusStr);
        return new Response<>(poiService.recommend(accessToken, Location.parse(locationStr), Integer.parseInt(radiusStr), getClientLocale()));
    }

    @GET
    @Path("categories/like/{partial_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response<List<Category>> categoriesLikeName(@NotBlank(message = "category_name.required.error") @PathParam("partial_name") String partialName) {
        return new Response<>(poiService.getCategoriesLikeName(partialName, getClientLocale()));
    }
}