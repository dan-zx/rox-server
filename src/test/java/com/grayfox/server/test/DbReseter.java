package com.grayfox.server.test;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class DbReseter extends JdbcDaoSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbReseter.class);

    public DbReseter(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Transactional
    public void deleteAllData() {
        deleteAppUserTableData();
    }

    private void deleteAppUserTableData() {
        LOGGER.debug("Delete app_user table data");
        getJdbcTemplate().execute("TRUNCATE TABLE app_user");
        LOGGER.debug("Restart app_user table id");
        getJdbcTemplate().execute("ALTER TABLE app_user ALTER COLUMN id RESTART WITH 1");
    }
}