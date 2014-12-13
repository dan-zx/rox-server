package com.grayfox.server.service;

import com.foursquare4j.response.User;

public interface AppUserService {

    String register(String foursquareAuthorizationCode);
    User getSelf(String appAccessToken);
}