package com.grayfox.server.data.dao;

import com.grayfox.server.data.AppUser;

public interface AppUserDao {

    AppUser fetchByFoursquareAccessToken(String foursquareAccessToken);
    boolean isAppAccessTokenUnique(String appAccessToken);
    void insert(AppUser appUser);
}