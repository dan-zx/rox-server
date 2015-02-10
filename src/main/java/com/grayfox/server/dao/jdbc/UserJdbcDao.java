package com.grayfox.server.dao.jdbc;

import java.sql.ResultSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;

import com.grayfox.server.dao.UserDao;
import com.grayfox.server.domain.User;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserJdbcDao implements UserDao {

    @Inject private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void onPostConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User fetchCompactByAccessToken(String accessToken) {
        List<User> users = jdbcTemplate.query(CypherQueries.COMPACT_USER_BY_ACCESS_TOKEN, 
                (ResultSet rs, int i) -> {
                    User user = new User();
                    user.setName(rs.getString(1));
                    user.setLastName(rs.getString(2));
                    user.setPhotoUrl(rs.getString(3));
                    user.setFoursquareId(rs.getString(4));
                    return user;
                },
                accessToken);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public void create(User user) {
        jdbcTemplate.update(CypherQueries.CREATE_USER, user.getCredential().getAccessToken(), user.getName(), user.getLastName(), user.getPhotoUrl(), user.getFoursquareId());
        user.getLikes().forEach(category -> jdbcTemplate.update(CypherQueries.CREATE_LIKES_RELATION, user.getFoursquareId(), category.getFoursquareId()));
        for (User friend : user.getFriends()) {
            List<Boolean> exists = jdbcTemplate.queryForList(CypherQueries.EXISTS_FRIEND, Boolean.class, friend.getFoursquareId());
            if (exists.isEmpty()) {
                jdbcTemplate.update(CypherQueries.CREATE_FRIEND, user.getFoursquareId(), friend.getName(), friend.getLastName(), friend.getPhotoUrl(), friend.getFoursquareId());
                friend.getLikes().forEach(category -> jdbcTemplate.update(CypherQueries.CREATE_LIKES_RELATION, friend.getFoursquareId(), category.getFoursquareId()));
            } else jdbcTemplate.update(CypherQueries.CREATE_FRIENDS_RELATION, user.getFoursquareId(), friend.getFoursquareId());
        }
    }
}