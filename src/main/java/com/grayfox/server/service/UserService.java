package com.grayfox.server.service;

import java.util.UUID;

import javax.inject.Inject;

import com.foursquare4j.FoursquareApi;
import com.foursquare4j.response.AccessTokenResponse;

import com.grayfox.server.dao.CredentialDao;
import com.grayfox.server.dao.UserDao;
import com.grayfox.server.datasource.ProfileFoursquareDataSource;
import com.grayfox.server.domain.Credential;
import com.grayfox.server.domain.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Inject private UserDao userDao;
    @Inject private CredentialDao credentialDao;
    @Inject private ProfileFoursquareDataSource profileFoursquareDataSource;
    @Inject private FoursquareApi foursquareApi;

    @Transactional
    public Credential registerUsingFoursquare(String foursquareAuthorizationCode) {
        AccessTokenResponse foursquareResponse = foursquareApi.getAccessToken(foursquareAuthorizationCode);
        if (foursquareResponse.getException() != null) {
            LOGGER.error("Foursquare authentication error", foursquareResponse.getException());
            throw new ServiceException.Builder("foursquare.authentication.error").setCause(foursquareResponse.getException()).build();
        }
        Credential credential = credentialDao.fetchByFoursquareAccessToken(foursquareResponse.getAccessToken());
        if (credential != null) {
            LOGGER.debug("Credential already exists");
            return credential;
        } else {
            credential = new Credential();
            credential.setFoursquareAccessToken(foursquareResponse.getAccessToken());
            credential.setAccessToken(generateAccessToken());
            credential.setNew(true);
            credentialDao.create(credential);
            LOGGER.debug("New credential created");
            return credential;
        }
    }

    @Async
    @Transactional
    public void generateProfileUsingFoursquare(Credential credential) {
        User user = profileFoursquareDataSource.collectUserData(credential.getFoursquareAccessToken());
        user.setLikes(profileFoursquareDataSource.collectLikes(credential.getFoursquareAccessToken()));
        user.setFriends(profileFoursquareDataSource.collectFriendsAndLikes(credential.getFoursquareAccessToken()));
        user.setCredential(credential);
        userDao.create(user);
    }

    @Transactional
    public User getCompactSelf(String accessToken) {
        if (!credentialDao.existsAccessToken(accessToken)) {
            LOGGER.error("Not existing user attempting to retrive information");
            throw new ServiceException.Builder("user.invalid.error").build();
        }
        return userDao.fetchCompactByAccessToken(accessToken);
    }

    private String generateAccessToken() {
        String accessToken = null;
        do accessToken = UUID.randomUUID().toString().replaceAll("-", ""); while (credentialDao.existsAccessToken(accessToken));
        return accessToken;
    }
}