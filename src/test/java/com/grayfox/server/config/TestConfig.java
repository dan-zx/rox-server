package com.grayfox.server.config;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.sql.DataSource;

import com.foursquare4j.FoursquareApi;
import com.foursquare4j.response.AccessTokenResponse;

import com.grayfox.server.test.DbReseter;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@Import(TestConfig.TestBeanConfig.class)
public class TestConfig {

    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer(ResourceLoader resourceLoader) throws IOException {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ppc.setProperties(configsProperties(resourceLoader).getObject());
        return ppc;
    }

    @Bean
    public static PropertiesFactoryBean configsProperties(ResourceLoader resourceLoader) throws IOException {
        PropertiesFactoryBean props = new PropertiesFactoryBean();
        props.setLocation(resourceLoader.getResource("classpath:configs/test-configs.properties"));
        props.afterPropertiesSet();
        return props;
    }

    @Bean
    public EmbeddedDatabase dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:scripts/schema.sql")
            .build();
    }

    public static class TestBeanConfig extends MainConfig.BeanConfig {

        @Bean
        public DbReseter dbReseter(DataSource dataSource) {
            return new DbReseter(dataSource);
        }

        @Override
        public FoursquareApi foursquareApi(String clientId, String clientSecret) {
            FoursquareApi foursquareApi = mock(FoursquareApi.class);
            when(foursquareApi.getAccessToken(anyString()))
                .thenReturn(new AccessTokenResponse("{\"access_token\":\"fakeAccessToken\"}"));
            return foursquareApi;
        }
    }
}