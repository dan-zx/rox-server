package com.grayfox.server.data.dao;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import com.grayfox.server.data.AppUser;
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
        AppUser appUser = new AppUser();
        appUser.setAccessToken("fakeAccessToken");
        
        appUserDao.insert(appUser);
        assertThat(appUser.getId()).isNotNull();
        
        String accessToken = appUserDao.fetchAccessTokenById(appUser.getId());
        assertThat(accessToken).isNotNull().isNotEmpty().isEqualTo(appUser.getAccessToken());
        
        Long id = appUserDao.fetchIdByAccessToken(appUser.getAccessToken());
        assertThat(id).isNotNull().isEqualTo(appUser.getId());
    }
}