package com.grayfox.server.data.dao.impl.jdbc;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import com.grayfox.server.data.AppUser;
import com.grayfox.server.data.dao.AppUserDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

@Named
public class AppUserJdbcDao extends JdbcDaoSupport implements AppUserDao {

    private static final Logger LOG = LoggerFactory.getLogger(AppUserJdbcDao.class);

    @Inject
    public AppUserJdbcDao(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public Long fetchIdByAccessToken(String accessToken) {
        // FIXME: hardcoded SQL statement
        LOG.debug("fetchIdByAccessToken({})", accessToken);
        List<Long> results = getJdbcTemplate().queryForList("SELECT id FROM app_user WHERE access_token = ?", Long.class, accessToken);
        return results == null || results.isEmpty() ? null : results.get(0);
    }

    @Override
    public String fetchAccessTokenById(Long id) {
        // FIXME: hardcoded SQL statement
        LOG.debug("fetchAccessTokenById({})", id);
        List<String> results = getJdbcTemplate().queryForList("SELECT access_token FROM app_user WHERE id = ?", String.class, id);
        return results == null || results.isEmpty() ? null : results.get(0);
    }

    @Override
    public void insert(AppUser appUser) {
        // FIXME: hardcoded SQL statement
        LOG.debug("save({})", appUser);
        getJdbcTemplate().update("INSERT INTO app_user (access_token) VALUES(?)", appUser.getAccessToken());
        long id = fetchIdByAccessToken(appUser.getAccessToken());
        appUser.setId(id);
    }
}