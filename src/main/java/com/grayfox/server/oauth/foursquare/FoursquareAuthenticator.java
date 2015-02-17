package com.grayfox.server.oauth.foursquare;

import javax.inject.Inject;

import com.foursquare4j.FoursquareApi;
import com.foursquare4j.response.AccessTokenResponse;

import com.grayfox.server.oauth.OAuthException;
import com.grayfox.server.oauth.SocialNetworkAuthenticator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

@Component
public class FoursquareAuthenticator implements SocialNetworkAuthenticator {

    private static final Logger LOGGER = LoggerFactory.getLogger(FoursquareAuthenticator.class);

    @Inject private FoursquareApi foursquareApi;

    @Override
    public String exchangeAccessToken(String authorizationCode) {
        AccessTokenResponse foursquareResponse = foursquareApi.getAccessToken(authorizationCode);
        if (foursquareResponse.getException() != null) {
            LOGGER.error("Foursquare authentication error", foursquareResponse.getException());
            throw new OAuthException.Builder("foursquare.authentication.error").addFormatArg(foursquareResponse.getException().getMessage()).build();
        }
        return foursquareResponse.getAccessToken();
    }
}