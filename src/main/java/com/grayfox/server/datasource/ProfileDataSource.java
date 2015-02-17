package com.grayfox.server.datasource;

import java.util.Set;

import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.User;

public interface ProfileDataSource {

    User collectUserData(); 
    Set<Category> collectLikes();
    Set<User> collectFriendsAndLikes();
    void setAccessToken(String accessToken);
}