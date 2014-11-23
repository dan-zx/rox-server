package com.grayfox.server.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.Test;

import com.grayfox.server.test.BaseDbReseterTest;

public class AccessTokenServiceTest extends BaseDbReseterTest  {

    @Inject private AccessTokenService accessTokenService;

    @Override
    public void setUp() {
        super.setUp();
        assertThat(accessTokenService).isNotNull();
    }

    @Test
    public void generateToken() {
        assertThat(accessTokenService.generate()).isNotNull().isNotEmpty();
    }
}