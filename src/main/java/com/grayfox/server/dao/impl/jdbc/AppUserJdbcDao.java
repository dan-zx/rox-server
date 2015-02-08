package com.grayfox.server.dao.impl.jdbc;

import java.sql.ResultSet;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import com.grayfox.server.dao.AppUserDao;
import com.grayfox.server.dao.model.AppUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AppUserJdbcDao extends JdbcDaoSupport implements AppUserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppUserJdbcDao.class);

    @Inject
    public AppUserJdbcDao(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public AppUser fetchByFoursquareAccessToken(String foursquareAccessToken) {
        // FIXME: hardcoded SQL statement
        LOGGER.debug("fetchByFoursquareAccessToken({})", foursquareAccessToken);
        List<AppUser> results = getJdbcTemplate().query("SELECT id, app_access_token FROM app_user WHERE foursquare_access_token = ?",  
            (ResultSet rs, int i) -> {
                AppUser appUser = new AppUser();
                appUser.setId(rs.getLong("id"));
                appUser.setAppAccessToken(rs.getString("app_access_token"));
                appUser.setFoursquareAccessToken(foursquareAccessToken);
                return appUser;
            }, foursquareAccessToken);

        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public AppUser fetchByAppAccessToken(String appAccessToken) {
        // FIXME: hardcoded SQL statement
        LOGGER.debug("fetchByAppAccessToken({})", appAccessToken);
        List<AppUser> results = getJdbcTemplate().query("SELECT id, foursquare_access_token FROM app_user WHERE app_access_token = ?",  
            (ResultSet rs, int i) -> {
                AppUser appUser = new AppUser();
                appUser.setId(rs.getLong("id"));
                appUser.setAppAccessToken(appAccessToken);
                appUser.setFoursquareAccessToken(rs.getString("foursquare_access_token"));
                return appUser;
            }, appAccessToken);

        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public boolean isAppAccessTokenUnique(String appAccessToken) {
        return fetchIdByAppAccessToken(appAccessToken) == null;
    }

    @Override
    public void insert(AppUser appUser) {
        // FIXME: hardcoded SQL statement
        LOGGER.debug("save({})", appUser);
        getJdbcTemplate().update("INSERT INTO app_user (app_access_token, foursquare_access_token) VALUES(?, ?)", 
                appUser.getAppAccessToken(), appUser.getFoursquareAccessToken());
        long id = fetchIdByAppAccessToken(appUser.getAppAccessToken());
        appUser.setId(id);
    }

    private Long fetchIdByAppAccessToken(String appAccessToken) {
        // FIXME: hardcoded SQL statement
        LOGGER.debug("fetchIdByAppAccessToken({})", appAccessToken);
        List<Long> results = getJdbcTemplate().queryForList("SELECT id FROM app_user WHERE app_access_token = ?", 
                Long.class, appAccessToken);

        return results.isEmpty() ? null : results.get(0);
    }
}