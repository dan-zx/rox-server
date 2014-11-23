package com.grayfox.server.test;

import static org.assertj.core.api.Assertions.assertThat;
import javax.inject.Inject;

import org.junit.Before;

import com.grayfox.server.config.TestConfig;
import org.junit.After;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public abstract class BaseDbReseterTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDbReseterTest.class);

    @Inject private DbReseter dbReseter;

    @Before
    public void setUp() {
        assertThat(dbReseter).isNotNull();
    }
    
    @After
    public void tearDown() {
        LOGGER.trace("Cleaning tables...");
        dbReseter.deleteAllData();
    }
}