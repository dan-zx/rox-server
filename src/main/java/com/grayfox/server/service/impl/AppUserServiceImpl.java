package com.grayfox.server.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import com.foursquare4j.FoursquareApi;
import com.foursquare4j.response.AccessTokenResponse;

import com.grayfox.server.data.AppUser;
import com.grayfox.server.data.dao.AppUserDao;
import com.grayfox.server.service.AppUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.transaction.annotation.Transactional;

@Named
public class AppUserServiceImpl implements AppUserService {

    private static final Logger LOG = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private final AppUserDao appUserDao;
    private final FoursquareApi foursquareApi;

    @Inject
    public AppUserServiceImpl(AppUserDao appUserDao, FoursquareApi foursquareApi) {
        this.appUserDao = appUserDao;
        this.foursquareApi = foursquareApi;
    }

    @Override
    @Transactional(noRollbackFor = ServiceException.class)
    public Long register(String authorizationCode) {
        AccessTokenResponse response = foursquareApi.getAccessToken(authorizationCode);
        if (response.getException() != null) throw new ServiceException(response.getException().getMessage());
        Long id = appUserDao.fetchIdByAccessToken(response.getAccessToken());
        if (id != null) {
            LOG.debug("User already exists -> id={}", id);
            return id;
        } else {
            AppUser appUser = new AppUser();
            appUser.setAccessToken(response.getAccessToken());
            appUserDao.insert(appUser);
            LOG.debug("New user -> user={}", appUser);
            return appUser.getId();
        }
    }
}