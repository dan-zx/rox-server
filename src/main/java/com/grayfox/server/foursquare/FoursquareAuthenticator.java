package com.grayfox.server.foursquare;

import java.io.Serializable;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;

import com.grayfox.server.http.Header;
import com.grayfox.server.http.Method;
import com.grayfox.server.http.RequestBuilder;
import com.grayfox.server.util.URIs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;

@Named
public class FoursquareAuthenticator implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(FoursquareAuthenticator.class);

    private final String clientId;
    private final String clientSecret;

    @Inject @Named("urlsProperties") private Properties urlProperties;

    @Inject
    public FoursquareAuthenticator(
            @Value("${foursquare.app.client_id}") String clientId, 
            @Value("${foursquare.app.client_secret}") String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public AccessTokenResponse getAccessToken(String authorizationCode) {
        String url = URIs.builder(urlProperties.getProperty("foursquare.auth.url"))
            .addParameter("client_id", clientId)
            .addParameter("client_secret", clientSecret)
            .addParameter("grant_type", urlProperties.getProperty("foursquare.auth.param.grant_type.value"))
            .addParameter("code", authorizationCode)
            .toString();
        String json = new RequestBuilder(url)
            .setMethod(Method.GET)
            .setDefaultHeaders()
            .addHeader(Header.ACCEPT, "application/json")
            .addHeader(Header.ACCEPT_CHARSET, "utf-8")
            .callForResult();
        LOG.debug("Response: {}", json);
        return new AccessTokenResponse(json);
    }
}