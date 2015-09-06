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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;
import com.grayfox.server.domain.Recommendation;
import com.grayfox.server.test.BaseWebServiceTest;
import com.grayfox.server.ws.rest.response.ApiResponse;
import com.grayfox.server.ws.rest.response.ErrorResponse;

import org.junit.Ignore;
import org.junit.Test;

public class PoiWebServiceTest extends BaseWebServiceTest {

    @Test
    public void testSearchPoisByCategory() {
        Category category = new Category();
        category.setFoursquareId("4bf58dd8d48988d151941735");
        category.setName("Taco Place");
        category.setIconUrl("https://ss3.4sqi.net/img/categories_v2/food/taco_88.png");

        Poi p1 = new Poi();
        p1.setFoursquareId("4cdd6a06930af04d92fb9597");
        p1.setName("Taquería Los Ángeles");
        p1.setLocation(Location.parse("19.04336700060403,-98.19716334342957"));
        p1.setCategories(new HashSet<>(Arrays.asList(category)));

        Poi p2 = new Poi();
        p2.setFoursquareId("4c3ce8087c1ee21ebd388d71");
        p2.setName("Antigua Taquería La Oriental");
        p2.setLocation(Location.parse("19.044926274591635,-98.19751471281052"));
        p2.setCategories(new HashSet<>(Arrays.asList(category)));

        List<Poi> expectedPois = Arrays.asList(p1, p2);

        Response response = target("pois/search").queryParam("location", "19.04365,-98.197968").queryParam("radius", 100).queryParam("category_foursquare_id", "4bf58dd8d48988d151941735").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        ApiResponse<List<Poi>> apiResponse = new Gson().fromJson(response.readEntity(String.class), new TypeToken<ApiResponse<List<Poi>>>() {}.getType());
        assertThat(apiResponse).isNotNull();
        assertThat(apiResponse.getResponse()).isNotNull().isNotNull().isNotEmpty().containsOnlyElementsOf(expectedPois);
    }

    @Test
    @Ignore
    public void testRoute() {
        Poi p1 = new Poi();
        p1.setFoursquareId("4bad0850f964a52082263be3");
        p1.setName("Cinépolis");
        p1.setLocation(Location.parse("19.032099226143384,-98.23300838470459"));
        Category category = new Category();
        category.setFoursquareId("4bf58dd8d48988d180941735");
        category.setName("Multicine");
        category.setIconUrl("https://ss3.4sqi.net/img/categories_v2/arts_entertainment/movietheater_88.png");
        p1.setCategories(new HashSet<>(Arrays.asList(category)));
        
        Poi p2 = new Poi();
        p2.setFoursquareId("4c2b7f8257a9c9b6affff567");
        p2.setName("Liverpool");
        p2.setLocation(Location.parse("19.03174044908608,-98.23100973086046"));
        category = new Category();
        category.setFoursquareId("4bf58dd8d48988d1f6941735");
        category.setName("Gran tienda");
        category.setIconUrl("https://ss3.4sqi.net/img/categories_v2/shops/departmentstore_88.png");
        p2.setCategories(new HashSet<>(Arrays.asList(category)));

        List<Poi> expectedPois = Arrays.asList(p1, p2);

        Response response = target("pois/route").queryParam("poi_foursquare_id", "4c09270ea1b32d7f172297f0").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        ApiResponse<List<Poi>> apiResponse = new Gson().fromJson(response.readEntity(String.class), new TypeToken<ApiResponse<List<Poi>>>() {}.getType());
        assertThat(apiResponse).isNotNull();
        assertThat(apiResponse.getResponse()).isNotNull().isNotNull().isNotEmpty().containsOnlyElementsOf(expectedPois);
    }

    @Test
    public void testRecommend() {
        Response response = target("pois/recommend").queryParam("location", "19.04,-98.19").queryParam("radius", 100).request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        ApiResponse<List<Recommendation>> apiResponse = new Gson().fromJson(response.readEntity(String.class), new TypeToken<ApiResponse<List<Recommendation>>>() {}.getType());
        assertThat(apiResponse).isNotNull();
        assertThat(apiResponse.getResponse()).isNotNull();
    }

    @Test
    @Ignore
    public void testSearchCategoriesLikeName() {
        Category c1 = new Category();
        c1.setFoursquareId("4bf58dd8d48988d188941735");
        c1.setIconUrl("https://ss3.4sqi.net/img/categories_v2/arts_entertainment/default_88.png");
        c1.setName("Estadio de fútbol");
        
        Category c2 = new Category();
        c2.setFoursquareId("4bf58dd8d48988d18c941735");
        c2.setIconUrl("https://ss3.4sqi.net/img/categories_v2/arts_entertainment/default_88.png");
        c2.setName("Estadio de béisbol");
        
        List<Category> expectedCategories = Arrays.asList(c1, c2);

        Response response = target("pois/categories/like/estadio").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        ApiResponse<List<Category>> apiResponse = new Gson().fromJson(response.readEntity(String.class), new TypeToken<ApiResponse<List<Category>>>() {}.getType());
        assertThat(apiResponse).isNotNull();
        assertThat(apiResponse.getResponse()).isNotNull().isNotEmpty().containsOnlyElementsOf(expectedCategories);
    }

    @Test
    public void testMissingParmsWhenSearchingPois() {
        Response response = target("pois/search").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());

        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("param.validation.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }

    @Test
    public void testErrorInParmsWhenSearchingPois() {
        Response response = target("pois/search").queryParam("location", "xx,-xx").queryParam("radius", "xx").queryParam("category_foursquare_id", "anyId").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());

        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("param.validation.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }

    @Test
    public void testMissingParmsWhenRequestingRoutes() {
        Response response = target("pois/route").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());

        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("param.validation.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }

    @Test
    public void testMissingParmsWhenRequestingRecommendations() {
        Response response = target("pois/recommend").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());

        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("param.validation.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }

    @Test
    public void testErrorInParmsWhenRequestingRecommendations() {
        Response response = target("pois/recommend").queryParam("location", "xx,-xx").queryParam("radius", "-23.3").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());

        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("param.validation.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }

    @Test
    public void testMissingParmsWhenRequestingCategories() {
        Response response = target("pois/categories/like/ ").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());

        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("param.validation.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }
}