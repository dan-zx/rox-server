package com.grayfox.server.config;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Named;
import javax.sql.DataSource;

import com.foursquare4j.FoursquareApi;

import com.google.maps.GeoApiContext;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Import({MainConfig.DataSourceConfig.class, MainConfig.BeanConfig.class})
public class MainConfig {

    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer(ResourceLoader resourceLoader) throws IOException {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ppc.setProperties(configsProperties(resourceLoader).getObject());
        return ppc;
    }

    @Bean
    public static PropertiesFactoryBean configsProperties(ResourceLoader resourceLoader) throws IOException {
        PropertiesFactoryBean props = new PropertiesFactoryBean();
        props.setLocation(resourceLoader.getResource("/WEB-INF/resources/configs.properties"));
        props.afterPropertiesSet();
        return props;
    }

    @Configuration
    @EnableTransactionManagement
    @ComponentScan(
            basePackages = { 
                    "com.grayfox.server.dao.impl.jdbc",
                    "com.grayfox.server.service.impl",
                    "com.grayfox.server.ws.rest" }, 
            includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Named.class))
    public static class BeanConfig {

        @Bean
        @Scope("prototype")
        public FoursquareApi foursquareApi(
                @Value("${foursquare.app.client.id}") String clientId, 
                @Value("${foursquare.app.client.secret}") String clientSecret) {
            return new FoursquareApi(clientId, clientSecret);
        }

        @Bean
        public GeoApiContext geoApiContext(@Value("${google.api.key}") String apiKey) {
            return new GeoApiContext().setApiKey(apiKey);
        }

        @Bean
        public DataSourceTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }

    @Configuration
    public static class DataSourceConfig {

        @Bean(destroyMethod = "close")
        public ComboPooledDataSource dataSource(
                @Value("${jdbc.driver.class}") String driverClass,
                @Value("${jdbc.url}") String url) throws URISyntaxException, PropertyVetoException {
            URI databaseUri = new URI(System.getenv("DATABASE_URL"));
            String[] userInfo = databaseUri.getUserInfo().split(":");
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(driverClass);
            dataSource.setJdbcUrl(String.format(url, databaseUri.getHost(), databaseUri.getPort(), databaseUri.getPath()));
            dataSource.setUser(userInfo[0]);
            dataSource.setPassword(userInfo[1]);
            dataSource.setMinPoolSize(1);
            dataSource.setMaxPoolSize(50);
            dataSource.setAcquireIncrement(5);
            return dataSource;
        }
    }
}