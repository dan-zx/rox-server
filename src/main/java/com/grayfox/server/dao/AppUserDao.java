package com.grayfox.server.dao;

import com.grayfox.server.dao.model.AppUser;

public interface AppUserDao {

    AppUser fetchByFoursquareAccessToken(String foursquareAccessToken);
    AppUser fetchByAppAccessToken(String appAccessToken);
    boolean isAppAccessTokenUnique(String appAccessToken);
    void insert(AppUser appUser);
}