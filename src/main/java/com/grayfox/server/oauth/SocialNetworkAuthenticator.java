package com.grayfox.server.oauth;

public interface SocialNetworkAuthenticator {

    String exchangeAccessToken(String code);
}