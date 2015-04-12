package com.grayfox.server.dao;

import com.grayfox.server.domain.User;

public interface SocialNetworkProfileDao {

    User collectUserData(String accessToken);
}