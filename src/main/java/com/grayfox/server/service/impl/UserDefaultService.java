package com.grayfox.server.service.impl;

import com.grayfox.server.data.User;
import com.grayfox.server.data.dao.UserDao;
import com.grayfox.server.foursquare.response.AccessTokenResponse;
import com.grayfox.server.http.Header;
import com.grayfox.server.http.Method;
import com.grayfox.server.http.RequestBuilder;
import com.grayfox.server.service.UserService;
import com.grayfox.server.util.URIs;

import org.apache.http.client.utils.URIBuilder;
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
    @Transactional(noRollbackFor = ServiceException.class)
    public Long register(String authorizationCode) {
        LOG.debug("register({})", authorizationCode);
        AccessTokenResponse response = retrieveAccessTokenFromFoursquare(authorizationCode);
        if (response.getException() != null) throw new ServiceException(response.getException().getMessage());
        Long id = userDao.fetchIdByAccessToken(response.getAccessToken());
        if (id != null) {
            LOG.debug("User already exists -> id={}", id);
            return id;
        }
        else {
            User user = new User();
            user.setAccessToken(response.getAccessToken());
            userDao.save(user);
            LOG.debug("New user -> user={}", user);
            return user.getId();
        }
    }

    private AccessTokenResponse retrieveAccessTokenFromFoursquare(String accessCode) {
        // FIXME: hardcoded url and parameters
        String url = new URIBuilder(URIs.toUri("https://foursquare.com/oauth2/access_token"))
            .addParameter("client_id", "B4BRRJR2DUYMKFHJEYFUF24QXWSJKA4XGMHOELJEQOW2XTAG")
            .addParameter("client_secret", "ZNVGEZGTIGNDGTDQSD3ZYAKZWS0HTJNE3DMFIMOPS4WWEXVX")
            .addParameter("grant_type", "authorization_code")
            .addParameter("code", accessCode)
            .toString();
        // FIXME: hardcoded headers
        String json = new RequestBuilder(url)
            .setMethod(Method.GET)
            .setDefaultHeaders()
            .addHeader(Header.ACCEPT, "application/json")
            .addHeader(Header.ACCEPT_CHARSET, "utf-8")
            .callForResult();
        LOG.debug("Response: {}", json);
        return new AccessTokenResponse(json);
    }

    @Inject
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}