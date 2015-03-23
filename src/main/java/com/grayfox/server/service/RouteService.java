package com.grayfox.server.service;

import java.util.List;

import javax.inject.Inject;

import com.grayfox.server.dao.CredentialDao;
import com.grayfox.server.domain.Location;
import com.grayfox.server.route.RouteProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RouteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteService.class);

    @Inject private CredentialDao credentialDao;
    @Inject private RouteProvider routeProvider;

    @Transactional(readOnly = true)
    public List<Location> buildRoute(String accessToken, Location origin, Location destination, RouteProvider.Transportation transportation, Location... waypoints) {
        if (!credentialDao.existsAccessToken(accessToken)) {
            LOGGER.warn("Not existing user attempting to retrive information");
            throw new ServiceException.Builder("user.invalid.error").build();
        }
        return routeProvider.createRoute(origin, destination, transportation, waypoints);
    }
}