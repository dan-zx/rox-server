package com.grayfox.server.dao;

import com.grayfox.server.domain.Credential;

public interface CredentialDao {

    Credential fetchByFoursquareAccessToken(String foursquareAccessToken);
    boolean existsAccessToken(String accessToken);
    void save(Credential credential);
}