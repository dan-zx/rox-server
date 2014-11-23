package com.grayfox.server.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import com.grayfox.server.dao.AppUserDao;

import com.grayfox.server.dao.model.AppUser;
import com.grayfox.server.test.BaseDbReseterTest;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

public class AppUserServiceTest extends BaseDbReseterTest {

    @Inject private AppUserService appUserService;
    @Inject private AppUserDao appUserDao;

    @Override
    public void setUp() {
        super.setUp();
        assertThat(appUserService).isNotNull();
    }

    @Test
    public void registerNew() {
        String appAccessToken = appUserService.register("newAuthorizationCode");

        assertThat(appAccessToken).isNotNull();
    }

    @Test
    public void registerExisting() {
        String expectedAppAccessToken = preInsertAppUser();
        String actualAppAccessToken = appUserService.register("fakeAuthorizationCode");
        
        assertThat(actualAppAccessToken).isNotNull().isEqualTo(expectedAppAccessToken);
    }

    @Transactional
    private String preInsertAppUser() {
        AppUser appUser = new AppUser();
        appUser.setAppAccessToken("fakeAppAccessToken");
        appUser.setFoursquareAccessToken("fakeFourquareAccessToken");
        appUserDao.insert(appUser);
        return appUser.getAppAccessToken();
    }
}