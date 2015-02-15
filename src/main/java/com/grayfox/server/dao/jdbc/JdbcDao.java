package com.grayfox.server.dao.jdbc;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

abstract class JdbcDao {

    @Inject private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void onPostConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}