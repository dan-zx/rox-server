package com.grayfox.server.service.impl;

import com.grayfox.server.data.User;
import com.grayfox.server.data.dao.UserDao;
import com.grayfox.server.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class UserDefaultService implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserDefaultService.class);

    private UserDao userDao;
    
    @Override
    @Transactional
    public Long register(User user) {
        LOG.debug("<--{}", user);
        // TODO: retrieve access token from Foursquare
        user.setAccessToken("sdflskjdfkljsdf");
        userDao.save(user);
        LOG.debug("saved user -> {}", user);
        return user.getId();
    }

    @Inject
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}