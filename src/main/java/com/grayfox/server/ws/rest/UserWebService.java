package com.grayfox.server.ws.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.grayfox.server.domain.Credential;
import com.grayfox.server.domain.User;
import com.grayfox.server.service.UserService;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Path("users")
public class UserWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserWebService.class);

    @Inject private UserService userService;

    @GET
    @Path("register/foursquare")
    @Produces(MediaType.APPLICATION_JSON)
    public String registerUsingFoursquare(@NotBlank(message = "foursquare_authorization_code.required.error") @QueryParam("foursquare-authorization-code") String foursquareAuthorizationCode) {
        LOGGER.debug("registerUsingFoursquare({})", foursquareAuthorizationCode);
        Credential credential = userService.registerUsingFoursquare(foursquareAuthorizationCode);
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
        return new Result<>(userService.getCompactSelf(accessToken));
    }
}