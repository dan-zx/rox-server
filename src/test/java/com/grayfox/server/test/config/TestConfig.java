/*
 * Copyright 2014-2015 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.grayfox.server.test.config;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.foursquare4j.FoursquareApi;
import com.grayfox.server.config.MainConfig;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:test-config.properties")
@Import({MainConfig.DataConfig.class, TestConfig.BeanConfig.class})
public class TestConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @EnableAsync
    @Configuration
    @EnableTransactionManagement
    @ComponentScan(basePackages = {
            "com.grayfox.server.dao.jdbc",
            "com.grayfox.server.test.dao.*",
            "com.grayfox.server.oauth.*",
            "com.grayfox.server.service",
            "com.grayfox.server.ws.*"})
    public static class BeanConfig {

        private MockWebServer mockWebServer;

        @PostConstruct
        protected void init() throws Exception {
            mockWebServer = new MockWebServer();
            mockWebServer.start();
        }

        @Bean
        public FoursquareApi foursquareApi() {
            FoursquareApi foursquareApi = mock(FoursquareApi.class, CALLS_REAL_METHODS);
            String mockUrl = mockWebServer.getUrl("/").toString();
            when(foursquareApi.authUrl()).thenReturn(mockUrl);
            when(foursquareApi.apiUrl()).thenReturn(mockUrl);
            return foursquareApi;
        }

        @Bean
        public MockWebServer mockWebServer() {
            return mockWebServer;
        }

        @PreDestroy
        protected void destory() throws Exception {
            mockWebServer.shutdown();
        }
    }
}