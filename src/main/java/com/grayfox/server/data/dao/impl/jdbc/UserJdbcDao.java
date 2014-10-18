package com.grayfox.server.data.dao.impl.jdbc;

import com.grayfox.server.data.User;
import com.grayfox.server.data.dao.UserDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

@Named
public class UserJdbcDao extends JdbcDaoSupport implements UserDao {

    private static final Logger LOG = LoggerFactory.getLogger(UserJdbcDao.class);

    @Inject
    public UserJdbcDao(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public Long fetchIdByAccessToken(String accessToken) {
        // FIXME: hardcoded SQL statement
        LOG.debug("fetchIdByAccessToken({})", accessToken);
        List<Long> results = getJdbcTemplate().queryForList("SELECT id FROM app_user WHERE access_token = ?", Long.class, accessToken);
        return results == null || results.isEmpty() ? null : results.get(0);
    }
    
    @Override
    public void save(User user) {
        // FIXME: hardcoded SQL statement
        LOG.debug("save({})", user);
        getJdbcTemplate().update("INSERT INTO app_user (access_token) VALUES(?)", user.getAccessToken());
        long id = fetchIdByAccessToken(user.getAccessToken());
        user.setId(id);
    }
}