package com.grayfox.server.dao;

import com.grayfox.server.domain.User;

public interface UserDao {

    User fetchCompactByAccessToken(String accessToken);
    void save(User user);
}