package com.grayfox.server.data.dao;

import com.grayfox.server.data.User;

public interface UserDao {

    Long fetchIdByAccessToken(String accessToken);
    void save(User user);
}