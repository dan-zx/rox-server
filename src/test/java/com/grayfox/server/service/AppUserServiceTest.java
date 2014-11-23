package com.grayfox.server.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import com.grayfox.server.data.AppUser;
import com.grayfox.server.data.dao.AppUserDao;
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
        Long id = appUserService.register("newAuthorizationCode");

        assertThat(id).isNotNull();
    }

    @Test
    public void registerExisting() {
        Long expectedId = preInsertAppUser();
        Long id = appUserService.register("fakeAuthorizationCode");
        
        assertThat(id).isNotNull().isEqualTo(expectedId);
    }

    @Transactional
    private Long preInsertAppUser() {
        AppUser appUser = new AppUser();
        appUser.setAccessToken("fakeAccessToken");
        appUserDao.insert(appUser);
        return appUser.getId();
    }
}