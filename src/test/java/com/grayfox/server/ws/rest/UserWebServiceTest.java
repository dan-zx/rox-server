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

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import com.grayfox.server.test.BaseWebServiceTest;
import com.grayfox.server.ws.rest.response.AccessTokenResponse;
import com.grayfox.server.ws.rest.response.ApiResponse;
import com.grayfox.server.ws.rest.response.ErrorResponse;

import org.junit.Test;

public class UserWebServiceTest extends BaseWebServiceTest {

    @Test
    public void testService() {
        Response response = target("users/register/foursquare").queryParam("authorization_code", "fakeCode").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        ApiResponse<AccessTokenResponse> accesTokenResponse = new Gson().fromJson(response.readEntity(String.class), new TypeToken<ApiResponse<AccessTokenResponse>>() {}.getType());
        assertThat(accesTokenResponse).isNotNull();
        assertThat(accesTokenResponse.getResponse()).isNotNull();
        assertThat(accesTokenResponse.getResponse().getAccessToken()).isNotNull().isNotEmpty();
    }

    @Test
    public void testAuthenticationError() {
        Response response = target("users/register/foursquare").queryParam("authorization_code", "invalidCode").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("foursquare.authentication.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }

    @Test
    public void testMissingParamsWhenRegistering() {
        Response response = target("users/register/foursquare").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        
        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("param.validation.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }

    @Test
    public void testMissingParamsWhenRequestingSelf() {
        Response response = target("users/self").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        
        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("param.validation.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }

    @Test
    public void testAuthenticationErrorWhenRequestingSelf() {
        Response response = target("users/self").queryParam("access_token", "invalidToken").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        
        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("user.invalid.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }

    @Test
    public void testMissingParamsWhenRequestingSelfFriends() {
        Response response = target("users/self/friends").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        
        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("param.validation.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }

    @Test
    public void testAuthenticationErrorWhenRequestingSelfFriends() {
        Response response = target("users/self/friends").queryParam("access_token", "invalidToken").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        
        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("user.invalid.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }

    @Test
    public void testMissingParamsWhenRequestingSelfLikes() {
        Response response = target("users/self/likes").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        
        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("param.validation.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }

    @Test
    public void testAuthenticationErrorWhenRequestingSelfLikes() {
        Response response = target("users/self/likes").queryParam("access_token", "invalidToken").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        
        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("user.invalid.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }

    @Test
    public void testMissingParamsWhenAddingLike() {
        Response response = target("users/self/update/addlike").request().put(Entity.text(""));
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        
        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("param.validation.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }

    @Test
    public void testAuthenticationErrorWhenAddingLike() {
        Response response = target("users/self/update/addlike").queryParam("access_token", "invalidToken").queryParam("category_foursquare_id", "invalidId").request().put(Entity.text(""));
        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        
        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("user.invalid.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }

    @Test
    public void testMissingParamsWhenRemovingLike() {
        Response response = target("users/self/update/removelike").request().delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        
        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("param.validation.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }

    @Test
    public void testAuthenticationErrorWhenRemovingLike() {
        Response response = target("users/self/update/removelike").queryParam("access_token", "invalidToken").queryParam("category_foursquare_id", "anyId").request().delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        
        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(gson.fromJson(response.readEntity(String.class), JsonObject.class).get("error"), ErrorResponse.class);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isNotNull().isNotEmpty().isEqualTo("user.invalid.error");
        assertThat(errorResponse.getErrorMessage()).isNotNull().isNotEmpty();
    }
}