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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.Credential;
import com.grayfox.server.domain.User;
import com.grayfox.server.service.UserService;
import com.grayfox.server.ws.rest.response.AccessTokenResponse;
import com.grayfox.server.ws.rest.response.ApiResponse;
import com.grayfox.server.ws.rest.response.UpdateResponse;

import org.hibernate.validator.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;

@Controller
@Path("users")
public class UserWebService extends BaseRestComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserWebService.class);

    @Inject private UserService userService;

    @GET
    @Path("register/foursquare")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<AccessTokenResponse> registerUsingFoursquare(@NotBlank(message = "authorization_code.required.error") @QueryParam("authorization_code") String authorizationCode) {
        LOGGER.debug("registerUsingFoursquare({})", authorizationCode);
        Credential credential = userService.registerUsingFoursquare(authorizationCode);
        if (credential.isNew()) userService.generateProfileUsingFoursquare(credential);
        return new ApiResponse<>(new AccessTokenResponse(credential.getAccessToken()));
    }

    @GET
    @Path("self")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<User> self(@NotBlank(message = "access_token.required.error") @QueryParam("access_token") String accessToken) {
        LOGGER.debug("getSelf({})", accessToken);
        return new ApiResponse<>(userService.getCompactSelf(accessToken));
    }

    @GET
    @Path("self/friends")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<List<User>> selfFriends(@NotBlank(message = "access_token.required.error") @QueryParam("access_token") String accessToken) {
        LOGGER.debug("getSelfFriends({})", accessToken);
        return new ApiResponse<>(userService.getSelfCompactFriends(accessToken));
    }

    @GET
    @Path("{user_foursquare_id}/likes")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<List<Category>> userLikes(
            @NotBlank(message = "user_foursquare_id.required.error") @PathParam("user_foursquare_id") String userFoursquareId,
            @NotBlank(message = "access_token.required.error") @QueryParam("access_token") String accessToken) {
        LOGGER.debug("getUserLikes({}, {})", accessToken, userFoursquareId);
        if (userFoursquareId.equals("self")) return new ApiResponse<>(userService.getSelfLikes(accessToken, getClientLocale()));
        return new ApiResponse<>(userService.getUserLikes(accessToken, userFoursquareId, getClientLocale()));
    }

    @PUT
    @Path("self/update/addlike")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<UpdateResponse> addLike(
            @NotBlank(message = "access_token.required.error") @QueryParam("access_token") String accessToken,
            @NotBlank(message = "category_foursquare_id.required.error") @QueryParam("category_foursquare_id") String categoryFoursquareId) {
        LOGGER.debug("addLike({}, {})", accessToken, categoryFoursquareId);
        userService.addLike(accessToken, categoryFoursquareId);
        return new ApiResponse<>(new UpdateResponse(true));
    }

    @DELETE
    @Path("self/update/removelike")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<UpdateResponse> removeLike(
            @NotBlank(message = "access_token.required.error") @QueryParam("access_token") String accessToken,
            @NotBlank(message = "category_foursquare_id.required.error") @QueryParam("category_foursquare_id") String categoryFoursquareId) {
        LOGGER.debug("removeLike({}, {})", accessToken, categoryFoursquareId);
        userService.removeLike(accessToken, categoryFoursquareId);
        return new ApiResponse<>(new UpdateResponse(true));
    }
}