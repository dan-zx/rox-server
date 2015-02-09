package com.grayfox.server.ws.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.grayfox.server.domain.User;
import com.grayfox.server.service.UserService;
import com.grayfox.server.service.domain.CredentialResult;
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
    public String registerUsingFoursquare(@QueryParam("foursquare-authorization-code") String foursquareAuthorizationCode) {
        LOGGER.debug("registerUsingFoursquare({})", foursquareAuthorizationCode);
        
        RequiredArgException.Builder requiredArgExceptionBuilder = new RequiredArgException.Builder();
        if (foursquareAuthorizationCode == null || foursquareAuthorizationCode.trim().isEmpty()) requiredArgExceptionBuilder.addRequiredArg("foursquare-authorization-code");
        requiredArgExceptionBuilder.throwIfNotEmpty();
        
        CredentialResult credentialResult = userService.registerUsingFoursquare(foursquareAuthorizationCode);
        if (credentialResult.isNewUser()) userService.generateProfileUsingFoursquare(credentialResult.getCredential());
        JsonObject response = new JsonObject();
        JsonObject accessTokenElement = new JsonObject();
        accessTokenElement.addProperty("accessToken", credentialResult.getCredential().getAccessToken());
        response.add("response", accessTokenElement);
        return new Gson().toJson(response);
    }

    @GET
    @Path("self")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<User> getSelf(@QueryParam("access-token") String accessToken) {
        LOGGER.debug("getSelf({})", accessToken);
        
        RequiredArgException.Builder requiredArgExceptionBuilder = new RequiredArgException.Builder();
        if (accessToken == null || accessToken.trim().isEmpty()) requiredArgExceptionBuilder.addRequiredArg("access-token");
        requiredArgExceptionBuilder.throwIfNotEmpty();
        return new Result<>(userService.getCompactSelf(accessToken));
    }
}