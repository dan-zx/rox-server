package com.grayfox.server.dao;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import com.grayfox.server.dao.model.AppUser;
import com.grayfox.server.test.BaseDbReseterTest;

import org.junit.Test;

import org.springframework.transaction.annotation.Transactional;

public class AppUserDaoTest extends BaseDbReseterTest {

    @Inject private AppUserDao appUserDao;

    @Override
    public void setUp() {
        super.setUp();
        assertThat(appUserDao).isNotNull();
    }

    @Test
    @Transactional
    public void insertAndFetch() {
        AppUser expectedAppUser = new AppUser();
        expectedAppUser.setAppAccessToken("fakeAppAccessToken");
        expectedAppUser.setFoursquareAccessToken("fakeFoursquareAccessToken");
        
        appUserDao.insert(expectedAppUser);
        assertThat(expectedAppUser.getId()).isNotNull();
        
        AppUser actualAppUser = appUserDao.fetchByFoursquareAccessToken(expectedAppUser.getFoursquareAccessToken());
        assertThat(actualAppUser).isNotNull().isEqualTo(expectedAppUser);
        
        actualAppUser = appUserDao.fetchByAppAccessToken(expectedAppUser.getAppAccessToken());
        assertThat(actualAppUser).isNotNull().isEqualTo(expectedAppUser);
        
        assertThat(appUserDao.isAppAccessTokenUnique("fakeAppAccessToken")).isFalse();
        assertThat(appUserDao.isAppAccessTokenUnique("otherFakeAppAccessToken")).isTrue();
    }
}