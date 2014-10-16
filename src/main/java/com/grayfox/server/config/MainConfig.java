package com.grayfox.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Named;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {
        "com.grayfox.server.data.dao.impl",
        "com.grayfox.server.service.impl",
        "com.grayfox.server.ws.rest"
    },
    includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Named.class))
public class MainConfig {

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public EmbeddedDatabase dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setName("gray-fox")
            .addScript("file:src/main/scripts/h2-schema.sql")
            .setType(EmbeddedDatabaseType.H2)
            .build();
    }
}