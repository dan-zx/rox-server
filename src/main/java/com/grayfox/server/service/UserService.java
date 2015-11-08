/*
 * Copyright 2014-2015 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        Credential credential = credentialDao.findByFoursquareAccessToken(accessToken);
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
        if (userDao.exists(user.getFoursquareId())) userDao.update(user);
        else userDao.save(user);
    }

    @Transactional(readOnly = true)
    public User getCompactSelf(String accessToken) {
        User user = userDao.findCompactByAccessToken(accessToken);
        if (user == null) {
            LOGGER.warn("Not existing user attempting to retrive information");
            throw new ServiceException.Builder()
                .messageKey("user.invalid.error")
                .build();
        }
        return user;
    }

    @Transactional(readOnly = true)
    public List<User> getSelfCompactFriends(String accessToken) {
        String selfFoursquareId = userDao.findFoursquareIdByAccessToken(accessToken);
        if (selfFoursquareId == null) {
            LOGGER.warn("Not existing user attempting to retrive information");
            throw new ServiceException.Builder()
                .messageKey("user.invalid.error")
                .build();
        }
        return userDao.findCompactFriendsByFoursquareId(selfFoursquareId);
    }

    @Transactional(readOnly = true)
    public List<Category> getSelfLikes(String accessToken, Locale locale) {
        String selfFoursquareId = userDao.findFoursquareIdByAccessToken(accessToken);
        if (selfFoursquareId == null) {
            LOGGER.warn("Not existing user attempting to retrive information");
            throw new ServiceException.Builder()
                .messageKey("user.invalid.error")
                .build();
        }
        return userDao.findLikesByFoursquareId(selfFoursquareId, locale);
    }

    @Transactional(readOnly = true)
    public List<Category> getUserLikes(String accessToken, String friendFoursquareId, Locale locale) {
        String selfFoursquareId = userDao.findFoursquareIdByAccessToken(accessToken);
        if (selfFoursquareId == null) {
            LOGGER.warn("Not existing user attempting to retrive information");
            throw new ServiceException.Builder()
                .messageKey("user.invalid.error")
                .build();
        }
        if (!userDao.exists(friendFoursquareId)) {
            LOGGER.warn("User with Foursquare Id [{}] does not exist", friendFoursquareId);
            throw new ServiceException.Builder()
                .messageKey("user.not_exist.error")
                .addMessageArgument(friendFoursquareId)
                .build();
        }
        if (!userDao.areFriends(selfFoursquareId, friendFoursquareId) && !selfFoursquareId.equals(friendFoursquareId)) {
            LOGGER.warn("Requested user with Foursquare id [{}] is not a friend", friendFoursquareId);
            throw new ServiceException.Builder()
                .messageKey("not_friends.error")
                .addMessageArgument(friendFoursquareId)
                .build();
        }
        return userDao.findLikesByFoursquareId(friendFoursquareId, locale);
    }

    @Transactional
    public void addLike(String accessToken, String categoryFoursquareId) {
        String selfFoursquareId = userDao.findFoursquareIdByAccessToken(accessToken);
        if (selfFoursquareId == null) {
            LOGGER.warn("Not existing user attempting to modify information");
            throw new ServiceException.Builder()
                .messageKey("user.invalid.error")
                .build();
        }
        userDao.saveLike(selfFoursquareId, categoryFoursquareId);
    }

    @Transactional
    public void removeLike(String accessToken, String categoryFoursquareId) {
        String selfFoursquareId = userDao.findFoursquareIdByAccessToken(accessToken);
        if (selfFoursquareId == null) {
            LOGGER.warn("Not existing user attempting to modify information");
            throw new ServiceException.Builder()
                .messageKey("user.invalid.error")
                .build();
        }
        userDao.deleteLike(selfFoursquareId, categoryFoursquareId);
    }

    private String generateAccessToken() {
        String accessToken = null;
        do accessToken = UUID.randomUUID().toString().replaceAll("-", ""); while (credentialDao.exists(accessToken));
        return accessToken;
    }
}