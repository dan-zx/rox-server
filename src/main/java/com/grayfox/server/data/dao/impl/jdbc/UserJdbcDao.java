package com.grayfox.server.data.dao.impl.jdbc;

import com.grayfox.server.data.User;
import com.grayfox.server.data.dao.UserDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

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

    @Override
    public void save(User user) {
        // FIXME: hardcoded SQL statement
        String sql ="INSERT INTO app_user (authorization_code, access_token) VALUES(?, ?)";
        LOG.debug("save->{}", user);
        LOG.debug("sql->{}", sql);
        getJdbcTemplate().update(sql, user.getAuthorizationCode(), user.getAccessToken());
        long id = getJdbcTemplate().queryForObject("SELECT id FROM app_user WHERE authorization_code = ? AND access_token = ?", Long.class, user.getAuthorizationCode(), user.getAccessToken());
        user.setId(id);
    }
}