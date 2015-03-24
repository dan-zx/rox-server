package com.grayfox.server.config;

import static org.mockito.Mockito.spy;

import com.foursquare4j.FoursquareApi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:test-configs.properties")
@Import({MainConfig.DataConfig.class, TestConfig.BeanConfig.class})
public class TestConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Configuration
    public static class BeanConfig extends MainConfig.BeanConfig {

        @Override
        public FoursquareApi foursquareApi(String clientId, String clientSecret) {
            return spy(super.foursquareApi(clientId, clientSecret));
        }
    }
}