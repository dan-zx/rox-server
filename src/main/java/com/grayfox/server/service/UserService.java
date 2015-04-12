package com.grayfox.server.service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import com.grayfox.server.dao.CredentialDao;
import com.grayfox.server.dao.SocialNetworkProfileDao;
import com.grayfox.server.dao.UserDao;
import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.Credential;
import com.grayfox.server.domain.User;
import com.grayfox.server.oauth.SocialNetworkAuthenticator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Inject private UserDao userDao;
    @Inject private CredentialDao credentialDao;
    @Inject @Named("foursquareAuthenticator") private SocialNetworkAuthenticator foursquareAuthenticator;
    @Inject @Named("foursquareProfileDao")    private SocialNetworkProfileDao foursquareProfileDao;

    @Transactional
    public Credential registerUsingFoursquare(String authorizationCode) {
        String accessToken = foursquareAuthenticator.exchangeAccessToken(authorizationCode);
        Credential credential = credentialDao.fetchByFoursquareAccessToken(accessToken);
        if (credential != null) {
            LOGGER.debug("Credential already exists");
            return credential;
        } else {
            credential = new Credential();
            credential.setFoursquareAccessToken(accessToken);
            credential.setAccessToken(generateAccessToken());
            credential.setNew(true);
            credentialDao.save(credential);
            LOGGER.debug("New credential created");
            return credential;
        }
    }

    @Async
    @Transactional
    public void generateProfileUsingFoursquare(Credential credential) {
        User user = foursquareProfileDao.collectUserData(credential.getFoursquareAccessToken());
        user.setCredential(credential);
        if (userDao.existsUser(user.getFoursquareId())) userDao.update(user);
        else userDao.save(user);
    }

    @Transactional(readOnly = true)
    public User getCompactSelf(String accessToken) {
        User user = userDao.fetchCompactByAccessToken(accessToken);
        if (user == null) {
            LOGGER.warn("Not existing user attempting to retrive information");
            throw new ServiceException.Builder("user.invalid.error").build();
        }
        return user;
    }

    @Transactional(readOnly = true)
    public List<User> getSelfCompactFriends(String accessToken) {
        String selfFoursquareId = userDao.fetchFoursquareIdByAccessToken(accessToken);
        if (selfFoursquareId == null) {
            LOGGER.warn("Not existing user attempting to retrive information");
            throw new ServiceException.Builder("user.invalid.error").build();
        }
        return userDao.fetchCompactFriendsByFoursquareId(selfFoursquareId);
    }

    @Transactional(readOnly = true)
    public List<Category> getSelfLikes(String accessToken, Locale locale) {
        String selfFoursquareId = userDao.fetchFoursquareIdByAccessToken(accessToken);
        if (selfFoursquareId == null) {
            LOGGER.warn("Not existing user attempting to retrive information");
            throw new ServiceException.Builder("user.invalid.error").build();
        }
        return userDao.fetchLikesByFoursquareId(selfFoursquareId, locale);
    }

    @Transactional(readOnly = true)
    public List<Category> getUserLikes(String accessToken, String friendFoursquareId, Locale locale) {
        String selfFoursquareId = userDao.fetchFoursquareIdByAccessToken(accessToken);
        if (selfFoursquareId == null) {
            LOGGER.warn("Not existing user attempting to retrive information");
            throw new ServiceException.Builder("user.invalid.error").build();
        }
        if (!userDao.existsUser(friendFoursquareId)) {
            LOGGER.warn("User with Foursquare Id [{}] does not exist", friendFoursquareId);
            throw new ServiceException.Builder("user.not_exist.error").addFormatArg(friendFoursquareId).build();
        }
        if (!userDao.areFriends(selfFoursquareId, friendFoursquareId) && !selfFoursquareId.equals(friendFoursquareId)) {
            LOGGER.warn("Requested user with Foursquare id [{}] is not a friend", friendFoursquareId);
            throw new ServiceException.Builder("not_friends.error").addFormatArg(friendFoursquareId).build();
        }
        return userDao.fetchLikesByFoursquareId(friendFoursquareId, locale);
    }

    @Transactional
    public void addLike(String accessToken, Category like) {
        String selfFoursquareId = userDao.fetchFoursquareIdByAccessToken(accessToken);
        if (selfFoursquareId == null) {
            LOGGER.warn("Not existing user attempting to modify information");
            throw new ServiceException.Builder("user.invalid.error").build();
        }
        userDao.saveLike(selfFoursquareId, like);
    }

    @Transactional
    public void removeLike(String accessToken, Category like) {
        String selfFoursquareId = userDao.fetchFoursquareIdByAccessToken(accessToken);
        if (selfFoursquareId == null) {
            LOGGER.warn("Not existing user attempting to modify information");
            throw new ServiceException.Builder("user.invalid.error").build();
        }
        userDao.deleteLike(selfFoursquareId, like);
    }

    private String generateAccessToken() {
        String accessToken = null;
        do accessToken = UUID.randomUUID().toString().replaceAll("-", ""); while (credentialDao.existsAccessToken(accessToken));
        return accessToken;
    }
}