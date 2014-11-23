package com.grayfox.server.ws.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.grayfox.server.service.AppUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@Path("v1/users")
public class AppUserWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppUserWebService.class);

    private AppUserService appUserService;

    @Inject
    public AppUserWebService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GET
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public String register(@QueryParam("foursquare-authorization-code") String foursquareAuthorizationCode) {
        LOGGER.debug("register({})", foursquareAuthorizationCode);
        String appAccessToken = appUserService.register(foursquareAuthorizationCode);
        JsonObject response = new JsonObject();
        response.addProperty("appAccessToken", appAccessToken);
        return new Gson().toJson(response);
    }
}