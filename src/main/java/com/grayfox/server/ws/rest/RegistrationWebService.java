package com.grayfox.server.ws.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.grayfox.server.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Named
@Path("v1/registration")
public class RegistrationWebService implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationWebService.class);

    private UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register.json")
    public String register(@QueryParam("authorization_code") String authorizationCode) {
        LOG.debug("register({})", authorizationCode);
        Long id = userService.register(authorizationCode);
        JsonObject response = new JsonObject();
        response.addProperty("user_id", id);
        return new Gson().toJson(response);
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}