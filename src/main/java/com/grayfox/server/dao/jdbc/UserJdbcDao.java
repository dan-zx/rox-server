package com.grayfox.server.dao.jdbc;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import com.grayfox.server.dao.DaoException;
import com.grayfox.server.dao.UserDao;
import com.grayfox.server.domain.Category;
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
    public User fetchCompleteByAccessToken(String accessToken, Locale locale) {
        User user = fetchCompactByAccessToken(accessToken);
        if (user != null) {
            user.setFriends(new HashSet<>(fetchFriends(user.getFoursquareId())));
            user.setLikes(new HashSet<>(fetchLikes(user.getFoursquareId(), locale)));
        }
        return user;
    }

    @Override
    public void saveOrUpdate(User user) {
        if (!exists(user)) getJdbcTemplate().update(CypherQueries.CREATE_USER, user.getCredential().getAccessToken(), user.getName(), user.getLastName(), user.getPhotoUrl(), user.getFoursquareId());
        else getJdbcTemplate().update(CypherQueries.UPDATE_USER, user.getFoursquareId(), user.getName(), user.getLastName(), user.getPhotoUrl());
        fetchLikesIds(user.getFoursquareId()).forEach(categoryId -> getJdbcTemplate().update(CypherQueries.DELETE_LIKES_RELATION, user.getFoursquareId(), categoryId));
        if (user.getLikes() != null) user.getLikes().forEach(category -> getJdbcTemplate().update(CypherQueries.CREATE_LIKES_RELATION, user.getFoursquareId(), category.getFoursquareId()));
        fetchFriendsIds(user.getFoursquareId()).forEach(friendFoursquareId -> getJdbcTemplate().update(CypherQueries.DELETE_FRIENDS_RELATION, user.getFoursquareId(), friendFoursquareId));
        if (user.getFriends() != null) for (User friend : user.getFriends()) {
            if (!exists(friend)) getJdbcTemplate().update(CypherQueries.CREATE_FRIEND, user.getFoursquareId(), friend.getName(), friend.getLastName(), friend.getPhotoUrl(), friend.getFoursquareId());
            else getJdbcTemplate().update(CypherQueries.CREATE_FRIENDS_RELATION, user.getFoursquareId(), friend.getFoursquareId());
            if (friend.getLikes() != null) friend.getLikes().forEach(category -> getJdbcTemplate().update(CypherQueries.CREATE_LIKES_RELATION, friend.getFoursquareId(), category.getFoursquareId()));
        }
    }

    @Override
    public void saveOrUpdateLikes(String accessToken, Collection<String> newLikes) {
        String userFoursquareId = fetchUserFoursquareId(accessToken);
        if (userFoursquareId != null) {
            fetchLikesIds(userFoursquareId).forEach(categoryId -> getJdbcTemplate().update(CypherQueries.DELETE_LIKES_RELATION, userFoursquareId, categoryId));
            newLikes.forEach(categoryId -> getJdbcTemplate().update(CypherQueries.CREATE_LIKES_RELATION, userFoursquareId, categoryId));
        }
    }

    private boolean exists(User user) {
        List<Boolean> exists = getJdbcTemplate().queryForList(CypherQueries.EXISTS_USER, Boolean.class, user.getFoursquareId());
        return !exists.isEmpty();
    }

    private String fetchUserFoursquareId(String accessToken) {
        List<String> userFoursquareIds = getJdbcTemplate().queryForList(CypherQueries.USER_FOURSQUARE_ID_BY_ACCESS_TOKEN, String.class, accessToken);
        if (userFoursquareIds.size() > 1) throw new DaoException.Builder("data.integrity.error").build();
        return userFoursquareIds.isEmpty() ? null : userFoursquareIds.get(0);
    }
    private List<User> fetchFriends(String foursquareId) {
        return getJdbcTemplate().query(CypherQueries.USER_FRIENDS_BY_FOURSQUARE_ID, 
                (ResultSet rs, int i) -> {
                    User user = new User();
                    user.setName(rs.getString(1));
                    user.setLastName(rs.getString(2));
                    user.setPhotoUrl(rs.getString(3));
                    user.setFoursquareId(rs.getString(4));
                    return user;
                }, foursquareId);
    }

    private List<String> fetchFriendsIds(String foursquareId) {
        return getJdbcTemplate().queryForList(CypherQueries.USER_FRIENDS_IDS_BY_FOURSQUARE_ID, String.class, foursquareId);
    }

    private List<Category> fetchLikes(String foursquareId, Locale locale) {
        String query;
        if (locale != null) {
            switch(locale.getLanguage()) {
                case "es": 
                    query = CypherQueries.USER_LIKES_SPANISH;
                    break;
                default: query = CypherQueries.USER_LIKES;
            }
        } else query = CypherQueries.USER_LIKES;
        return getJdbcTemplate().query(query, 
                (ResultSet rs, int i) -> {
                    Category category = new Category();
                    category.setName(rs.getString(1));
                    category.setIconUrl(rs.getString(2));
                    category.setFoursquareId(rs.getString(3));
                    return category;
                }, foursquareId);
    }

    private List<String> fetchLikesIds(String foursquareId) {
        return getJdbcTemplate().queryForList(CypherQueries.USER_LIKES_IDS, String.class, foursquareId);
    }
}