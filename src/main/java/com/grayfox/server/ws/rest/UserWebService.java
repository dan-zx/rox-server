package com.grayfox.server.ws.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.Credential;
import com.grayfox.server.domain.User;
import com.grayfox.server.service.UserService;

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
    public String registerUsingFoursquare(@NotBlank(message = "authorization_code.required.error") @QueryParam("authorization-code") String authorizationCode) {
        LOGGER.debug("registerUsingFoursquare({})", authorizationCode);
        Credential credential = userService.registerUsingFoursquare(authorizationCode);
        if (credential.isNew()) userService.generateProfileUsingFoursquare(credential);
        JsonObject response = new JsonObject();
        JsonObject accessTokenElement = new JsonObject();
        accessTokenElement.addProperty("accessToken", credential.getAccessToken());
        response.add("response", accessTokenElement);
        return new Gson().toJson(response);
    }

    @GET
    @Path("self")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<User> getSelf(@NotBlank(message = "access_token.required.error") @QueryParam("access-token") String accessToken) {
        LOGGER.debug("getSelf({})", accessToken);
        return new Result<>(userService.getSelf(accessToken));
    }

    @GET
    @Path("self/friends")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<List<User>> getSelfFriends(@NotBlank(message = "access_token.required.error") @QueryParam("access-token") String accessToken) {
        LOGGER.debug("getSelfFriends({})", accessToken);
        return new Result<>(userService.getSelfFriends(accessToken));
    }

    @GET
    @Path("self/likes")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<List<Category>> getSelfLikes(@NotBlank(message = "access_token.required.error") @QueryParam("access-token") String accessToken) {
        LOGGER.debug("getSelfLikes({})", accessToken);
        return new Result<>(userService.getSelfLikes(accessToken, getClientLocale()));
    }

    @GET
    @Path("{foursquareId}/likes")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<List<Category>> getUserLikes(
            @QueryParam("access-token") @NotBlank(message = "access_token.required.error") String accessToken,
            @PathParam("foursquareId") @NotBlank(message = "foursquare_id.required.error") String foursquareId) {
        LOGGER.debug("getUserLikes({}, {})", accessToken, foursquareId);
        return new Result<>(userService.getUserLikes(accessToken, foursquareId, getClientLocale()));
    }

    @POST
    @Path("self/update/addlike")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Result<UpdateResult> addLike(
            @NotBlank(message = "access_token.required.error") @QueryParam("access-token") String accessToken,
            Category like) {
        LOGGER.debug("addLike({}, {})", accessToken, like);
        userService.addLike(accessToken, like);
        return new Result<UpdateResult>(new UpdateResult(true));
    }

    @POST
    @Path("self/update/removelike")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Result<UpdateResult> removeLike(
            @NotBlank(message = "access_token.required.error") @QueryParam("access-token") String accessToken,
            Category like) {
        LOGGER.debug("removeLike({}, {})", accessToken, like);
        userService.removeLike(accessToken, like);
        return new Result<UpdateResult>(new UpdateResult(true));
    }
}