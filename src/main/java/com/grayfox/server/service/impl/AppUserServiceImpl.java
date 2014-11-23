package com.grayfox.server.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import com.foursquare4j.FoursquareApi;
import com.foursquare4j.response.AccessTokenResponse;

import com.grayfox.server.dao.AppUserDao;
import com.grayfox.server.dao.model.AppUser;
import com.grayfox.server.service.AccessTokenService;
import com.grayfox.server.service.AppUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.transaction.annotation.Transactional;

@Named
public class AppUserServiceImpl implements AppUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private final AppUserDao appUserDao;
    private final FoursquareApi foursquareApi;
    private final AccessTokenService accessTokenService;

    @Inject
    public AppUserServiceImpl(AppUserDao appUserDao, FoursquareApi foursquareApi, AccessTokenService accessTokenService) {
        this.appUserDao = appUserDao;
        this.foursquareApi = foursquareApi;
        this.accessTokenService = accessTokenService;
    }

    @Override
    @Transactional(noRollbackFor = ServiceException.class)
    public String register(String foursquareAuthorizationCode) {
        AccessTokenResponse foursquareResponse = foursquareApi.getAccessToken(foursquareAuthorizationCode);
        if (foursquareResponse.getException() != null) throw new ServiceException(foursquareResponse.getException().getMessage());
        AppUser appUser = appUserDao.fetchByFoursquareAccessToken(foursquareResponse.getAccessToken());
        if (appUser != null) {
            LOGGER.debug("User already exists -> {}", appUser);
            return appUser.getAppAccessToken();
        } else {
            appUser = new AppUser();
            appUser.setAppAccessToken(accessTokenService.generate());
            appUser.setFoursquareAccessToken(foursquareResponse.getAccessToken());
            appUserDao.insert(appUser);
            LOGGER.debug("New user -> {}", appUser);
            return appUser.getAppAccessToken();
        }
    }
}