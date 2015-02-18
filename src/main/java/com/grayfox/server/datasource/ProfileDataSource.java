package com.grayfox.server.datasource;

import com.grayfox.server.domain.User;

public interface ProfileDataSource {

    User collectUserData(String accessToken); 
}