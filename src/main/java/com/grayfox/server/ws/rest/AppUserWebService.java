package com.grayfox.server.ws.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.foursquare4j.response.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.grayfox.server.service.AppUserService;
import com.grayfox.server.ws.model.UserResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@Path("users")
public class AppUserWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppUserWebService.class);

    private AppUserService appUserService;

    @Inject
    public AppUserWebService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GET
    @Path("register")
    @Produces(MediaType.APPLICATION_JSON)
    public String register(@QueryParam("foursquare-authorization-code") String foursquareAuthorizationCode) {
        LOGGER.debug("register({})", foursquareAuthorizationCode);
        String appAccessToken = appUserService.register(foursquareAuthorizationCode);
        JsonObject response = new JsonObject();
        response.addProperty("appAccessToken", appAccessToken);
        return new Gson().toJson(response);
    }

    @GET
    @Path("self")
    @Produces(MediaType.APPLICATION_JSON)
    public UserResponse getSelf(@QueryParam("app-access-token") String appAccessToken) {
        LOGGER.debug("getSelf({})", appAccessToken);
        User foursquareUser = appUserService.getSelf(appAccessToken);
        UserResponse response = new UserResponse();
        response.setId(foursquareUser.getId());
        response.setFirstName(foursquareUser.getFirstName());
        response.setLastName(foursquareUser.getLastName());
        response.setFoursquarePhoto(foursquareUser.getPhoto());
        return response;
    }
}