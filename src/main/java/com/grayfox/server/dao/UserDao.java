package com.grayfox.server.dao;

import java.util.List;
import java.util.Locale;

import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.User;

public interface UserDao {

    User fetchCompactByAccessToken(String accessToken);
    String fetchFoursquareIdByAccessToken(String accessToken);
    List<User> fetchCompactFriendsByFoursquareId(String foursquareId);
    List<Category> fetchLikesByFoursquareId(String foursquareId, Locale locale);
    boolean areFriends(String foursquareId1, String foursquareId2);
    boolean existsUser(String foursquareId);
    void save(User user);
    void update(User user);
    void saveLike(String foursquareId, String categoryFoursquareI);
    void deleteLike(String foursquareId, String categoryFoursquareI);
}