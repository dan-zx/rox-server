package com.grayfox.server.service.impl;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;

import com.grayfox.server.foursquare.AccessTokenResponse;
import com.grayfox.server.data.AppUser;
import com.grayfox.server.data.dao.AppUserDao;
import com.grayfox.server.foursquare.FoursquareAuthenticator;
import com.grayfox.server.service.AppUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.transaction.annotation.Transactional;

@Named
public class AppUserServiceImpl implements AppUserService, Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private FoursquareAuthenticator foursquareAuthenticator;
    private AppUserDao appUserDao;

    @Override
    @Transactional(noRollbackFor = ServiceException.class)
    public Long register(String authorizationCode) {
        AccessTokenResponse response = foursquareAuthenticator.getAccessToken(authorizationCode);
        if (response.getException() != null) throw new ServiceException(response.getException().getMessage());
        Long id = appUserDao.fetchIdByAccessToken(response.getAccessToken());
        if (id != null) {
            LOG.debug("User already exists -> id={}", id);
            return id;
        } else {
            AppUser appUser = new AppUser();
            appUser.setAccessToken(response.getAccessToken());
            appUserDao.save(appUser);
            LOG.debug("New user -> user={}", appUser);
            return appUser.getId();
        }
    }

    @Inject
    public void setFoursquareAuthenticator(FoursquareAuthenticator foursquareAuthenticator) {
        this.foursquareAuthenticator = foursquareAuthenticator;
    }

    @Inject
    public void setAppUserDao(AppUserDao appUserDao) {
        this.appUserDao = appUserDao;
    }
}