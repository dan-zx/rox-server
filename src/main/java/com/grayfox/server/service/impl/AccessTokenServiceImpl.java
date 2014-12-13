package com.grayfox.server.service.impl;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import com.grayfox.server.dao.AppUserDao;
import com.grayfox.server.service.AccessTokenService;

import org.springframework.transaction.annotation.Transactional;

@Named
public class AccessTokenServiceImpl implements AccessTokenService {

    private final AppUserDao appUserDao;

    @Inject
    public AccessTokenServiceImpl(AppUserDao appUserDao) {
        this.appUserDao = appUserDao;
    }

    @Override
    @Transactional
    public String generate() {
        String accessToken = null;
        do {
            accessToken = UUID.randomUUID().toString().replaceAll("-", "");
        } while(!appUserDao.isAppAccessTokenUnique(accessToken));
        return accessToken;  
    }
}