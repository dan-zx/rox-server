package com.grayfox.server.ws.rest;

import java.io.Serializable;

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
@Path("v1/registration")
public class RegistrationWebService implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationWebService.class);

    private AppUserService appUserService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register")
    public String register(@QueryParam("authorization_code") String authorizationCode) {
        LOG.debug("register({})", authorizationCode);
        Long id = appUserService.register(authorizationCode);
        JsonObject response = new JsonObject();
        response.addProperty("user_id", id);
        return new Gson().toJson(response);
    }

    @Inject
    public void setAppUserService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }
}