package com.grayfox.server.config;

import java.beans.PropertyVetoException;
import java.io.IOException;

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
                    "com.grayfox.server.data.dao.impl.jdbc",
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
                @Value("${jdbc.url}") String url,
                @Value("${jdbc.user}") String user,
                @Value("${jdbc.password}") String password,
                @Value("${jdbc.pool_size.min}") int minPoolSize,
                @Value("${jdbc.pool_size.max}") int maxPoolSize,
                @Value("${jdbc.acquire_increment}") int acquireIncrement) throws PropertyVetoException {
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(driverClass);
            dataSource.setJdbcUrl(url);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            dataSource.setMinPoolSize(minPoolSize);
            dataSource.setMaxPoolSize(maxPoolSize);
            dataSource.setAcquireIncrement(acquireIncrement);
            return dataSource;
        }
    }
}