package com.grayfox.server.dao;

import java.util.Collection;
import java.util.Locale;

import com.grayfox.server.domain.User;

public interface UserDao {

    User fetchCompactByAccessToken(String accessToken);
    User fetchCompleteByAccessToken(String accessToken, Locale locale);
    void saveOrUpdate(User user);
    void saveOrUpdateLikes(String accessToken, Collection<String> newLikes);
}