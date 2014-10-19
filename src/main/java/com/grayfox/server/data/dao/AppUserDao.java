package com.grayfox.server.data.dao;

import com.grayfox.server.data.AppUser;

public interface AppUserDao {

    Long fetchIdByAccessToken(String accessToken);
    String fetchAccessTokenById(Long id);
    void save(AppUser appUser);
}