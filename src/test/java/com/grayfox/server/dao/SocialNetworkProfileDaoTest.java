package com.grayfox.server.dao;

import static org.assertj.core.api.StrictAssertions.assertThat;

import javax.inject.Inject;

import org.junit.Test;

import com.grayfox.server.test.config.TestConfig;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SocialNetworkProfileDaoTest {

    @Inject private SocialNetworkProfileDao socialNetworkProfileDao;

    @Before
    public void setUp() {
        assertThat(socialNetworkProfileDao).isNotNull();
    }

    @Test
    public void collectUserDataTest() {
        assertThat(socialNetworkProfileDao.collectUserData("fakeToken")).isNotNull();
    }
}