package com.grayfox.server.config;

import java.io.IOException;

import javax.inject.Named;

import fi.foyt.foursquare.api.FoursquareApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(
        basePackages = { "com.grayfox.server" }, 
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Named.class))
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

    @Bean
    public PropertiesFactoryBean urlsProperties(ResourceLoader resourceLoader) throws IOException {
        PropertiesFactoryBean props = new PropertiesFactoryBean();
        props.setLocation(resourceLoader.getResource("/WEB-INF/resources/urls.properties"));
        props.afterPropertiesSet();
        return props;
    }

    @Bean
    @Scope("prototype")
    public FoursquareApi foursquareApi(
            @Value("${foursquare.app.client_id}") String clientId, 
            @Value("${foursquare.app.client_secret}") String clientSecret) {
        return new FoursquareApi(clientId, clientSecret, null);
    }

    
    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public EmbeddedDatabase dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setName("gray-fox")
            .addScript("file:src/main/scripts/h2-schema.sql")
            .addScript("file:src/main/scripts/data-load.sql")
            .setType(EmbeddedDatabaseType.H2)
            .build();
    }
}