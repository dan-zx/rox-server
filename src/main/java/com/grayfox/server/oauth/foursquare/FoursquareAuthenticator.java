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
        AccessTokenResponse foursquareResponse = foursquareApi.getAccessToken(null, authorizationCode);
        if (foursquareResponse.getException() != null) {
            LOGGER.error("Foursquare authentication error", foursquareResponse.getException());
            throw new OAuthException.Builder()
                .messageKey("foursquare.authentication.error")
                .addMessageArgument(foursquareResponse.getException().getMessage())
                .cause(foursquareResponse.getException())
                .build();
        }
        return foursquareResponse.getAccessToken();
    }
}