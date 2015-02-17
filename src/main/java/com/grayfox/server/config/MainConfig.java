package com.grayfox.server.config;

import javax.sql.DataSource;

import com.foursquare4j.FoursquareApi;

import com.google.maps.GeoApiContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("/WEB-INF/resources/configs.properties")
@Import({MainConfig.DataConfig.class, MainConfig.BeanConfig.class})
public class MainConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @EnableAsync
    @Configuration
    @EnableTransactionManagement
    @ComponentScan(basePackages = { 
            "com.grayfox.server.dao.jdbc",
            "com.grayfox.server.datasource.foursquare",
            "com.grayfox.server.oauth.foursquare",
            "com.grayfox.server.service",
            "com.grayfox.server.ws.rest"})
    public static class BeanConfig {

        @Bean
        @Scope(BeanDefinition.SCOPE_PROTOTYPE)
        public FoursquareApi foursquareApi(
                @Value("${foursquare.app.client.id}") String clientId, 
                @Value("${foursquare.app.client.secret}") String clientSecret) {
            return new FoursquareApi(clientId, clientSecret);
        }

        @Bean
        public GeoApiContext geoApiContext(@Value("${google.api.key}") String apiKey) {
            return new GeoApiContext().setApiKey(apiKey);
        }
    }

    @Configuration
    public static class DataConfig {

        @Bean
        public DataSource dataSource(
                @Value("${jdbc.driver.class}") String driverClass,
                @Value("${jdbc.url}") String url,
                @Value("${jdbc.user}") String user,
                @Value("${jdbc.password}") String password) {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(driverClass);
            dataSource.setUrl(url);
            dataSource.setUsername(user);
            dataSource.setPassword(password);
            return dataSource;
        }

        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }
}