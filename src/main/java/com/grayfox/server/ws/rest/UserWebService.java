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
import com.grayfox.server.ws.rest.response.Response;
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
    public Response<AccessTokenResponse> registerUsingFoursquare(@NotBlank(message = "authorization_code.required.error") @QueryParam("authorization_code") String authorizationCode) {
        LOGGER.debug("registerUsingFoursquare({})", authorizationCode);
        Credential credential = userService.registerUsingFoursquare(authorizationCode);
        if (credential.isNew()) userService.generateProfileUsingFoursquare(credential);
        return new Response<>(new AccessTokenResponse(credential.getAccessToken()));
    }

    @GET
    @Path("self")
    @Produces(MediaType.APPLICATION_JSON)
    public Response<User> self(@NotBlank(message = "access_token.required.error") @QueryParam("access_token") String accessToken) {
        LOGGER.debug("getSelf({})", accessToken);
        return new Response<>(userService.getCompactSelf(accessToken));
    }

    @GET
    @Path("self/friends")
    @Produces(MediaType.APPLICATION_JSON)
    public Response<List<User>> selfFriends(@NotBlank(message = "access_token.required.error") @QueryParam("access_token") String accessToken) {
        LOGGER.debug("getSelfFriends({})", accessToken);
        return new Response<>(userService.getSelfCompactFriends(accessToken));
    }

    @GET
    @Path("{user_foursquare_id}/likes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response<List<Category>> userLikes(
            @NotBlank(message = "user_foursquare_id.required.error") @PathParam("user_foursquare_id") String userFoursquareId,
            @NotBlank(message = "access_token.required.error") @QueryParam("access_token") String accessToken) {
        LOGGER.debug("getUserLikes({}, {})", accessToken, userFoursquareId);
        if (userFoursquareId.equals("self")) return new Response<>(userService.getSelfLikes(accessToken, getClientLocale()));
        return new Response<>(userService.getUserLikes(accessToken, userFoursquareId, getClientLocale()));
    }

    @PUT
    @Path("self/update/addlike")
    @Produces(MediaType.APPLICATION_JSON)
    public Response<UpdateResponse> addLike(
            @NotBlank(message = "access_token.required.error") @QueryParam("access_token") String accessToken,
            @NotBlank(message = "category_foursquare_id.required.error") @QueryParam("category_foursquare_id") String categoryFoursquareId) {
        LOGGER.debug("addLike({}, {})", accessToken, categoryFoursquareId);
        userService.addLike(accessToken, categoryFoursquareId);
        return new Response<>(new UpdateResponse(true));
    }

    @DELETE
    @Path("self/update/removelike")
    @Produces(MediaType.APPLICATION_JSON)
    public Response<UpdateResponse> removeLike(
            @NotBlank(message = "access_token.required.error") @QueryParam("access_token") String accessToken,
            @NotBlank(message = "category_foursquare_id.required.error") @QueryParam("category_foursquare_id") String categoryFoursquareId) {
        LOGGER.debug("removeLike({}, {})", accessToken, categoryFoursquareId);
        userService.removeLike(accessToken, categoryFoursquareId);
        return new Response<>(new UpdateResponse(true));
    }
}