package com.grayfox.server.dao.jdbc;

import java.sql.ResultSet;
import java.util.List;

import com.grayfox.server.dao.DaoException;
import com.grayfox.server.dao.UserDao;
import com.grayfox.server.domain.User;

import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcDao extends JdbcDao implements UserDao {

    @Override
    public User fetchCompactByAccessToken(String accessToken) {
        List<User> users = getJdbcTemplate().query(CypherQueries.COMPACT_USER_BY_ACCESS_TOKEN, 
                (ResultSet rs, int i) -> {
                    User user = new User();
                    user.setName(rs.getString(1));
                    user.setLastName(rs.getString(2));
                    user.setPhotoUrl(rs.getString(3));
                    user.setFoursquareId(rs.getString(4));
                    return user;
                },
                accessToken);
        if (users.size() > 1) throw new DaoException.Builder("data.integrity.error").build();
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public void saveOrUpdate(User user) {
        if (!exists(user)) getJdbcTemplate().update(CypherQueries.CREATE_USER, user.getCredential().getAccessToken(), user.getName(), user.getLastName(), user.getPhotoUrl(), user.getFoursquareId());
        else getJdbcTemplate().update(CypherQueries.UPDATE_USER, user.getFoursquareId(), user.getName(), user.getLastName(), user.getPhotoUrl());
        if (user.getLikes() != null) user.getLikes().forEach(category -> getJdbcTemplate().update(CypherQueries.CREATE_LIKES_RELATION, user.getFoursquareId(), category.getFoursquareId()));
        if (user.getFriends() != null) for (User friend : user.getFriends()) {
            if (!exists(friend)) getJdbcTemplate().update(CypherQueries.CREATE_FRIEND, user.getFoursquareId(), friend.getName(), friend.getLastName(), friend.getPhotoUrl(), friend.getFoursquareId());
            else getJdbcTemplate().update(CypherQueries.CREATE_FRIENDS_RELATION, user.getFoursquareId(), friend.getFoursquareId());
            if (friend.getLikes() != null) friend.getLikes().forEach(category -> getJdbcTemplate().update(CypherQueries.CREATE_LIKES_RELATION, friend.getFoursquareId(), category.getFoursquareId()));
        }
    }

    private boolean exists(User user) {
        List<Boolean> exists = getJdbcTemplate().queryForList(CypherQueries.EXISTS_USER, Boolean.class, user.getFoursquareId());
        return !exists.isEmpty();
    }
}