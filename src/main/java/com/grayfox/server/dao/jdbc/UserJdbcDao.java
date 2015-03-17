package com.grayfox.server.dao.jdbc;

import java.sql.ResultSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.grayfox.server.dao.DaoException;
import com.grayfox.server.dao.UserDao;
import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.User;

import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcDao extends JdbcDao implements UserDao {

    @Override
    public User fetchByAccessToken(String accessToken) {
        List<User> users = getJdbcTemplate().query(CypherQueries.USER, 
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
    public String fetchFoursquareIdByAccessToken(String accessToken) {
        List<String> foursquareIds = getJdbcTemplate().queryForList(CypherQueries.USER_FOURSQUARE_ID, String.class, accessToken);
        if (foursquareIds.size() > 1) throw new DaoException.Builder("data.integrity.error").build();
        return foursquareIds.isEmpty() ? null : foursquareIds.get(0);
    }

    @Override
    public List<User> fetchFriendsByFoursquareId(String foursquareId) {
        return getJdbcTemplate().query(CypherQueries.USER_FRIENDS, 
                (ResultSet rs, int i) -> {
                    User user = new User();
                    user.setName(rs.getString(1));
                    user.setLastName(rs.getString(2));
                    user.setPhotoUrl(rs.getString(3));
                    user.setFoursquareId(rs.getString(4));
                    return user;
                }, foursquareId);
    }

    @Override
    public List<Category> fetchLikesByFoursquareId(String foursquareId, Locale locale) {
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

    @Override
    public boolean areFriends(String foursquareId1, String foursquareId2) {
        List<Boolean> exists = getJdbcTemplate().queryForList(CypherQueries.ARE_FRIENDS, Boolean.class, foursquareId1, foursquareId2);
        return !exists.isEmpty();
    }

    @Override
    public boolean existsUser(String foursquareId) {
        List<Boolean> exists = getJdbcTemplate().queryForList(CypherQueries.EXISTS_USER, Boolean.class, foursquareId);
        return !exists.isEmpty();
    }

    @Override
    public void save(User user) {
        getJdbcTemplate().update(CypherQueries.CREATE_USER, user.getCredential().getAccessToken(), user.getName(), user.getLastName(), user.getPhotoUrl(), user.getFoursquareId());
        if (user.getLikes() != null) user.getLikes().forEach(like -> saveLike(user.getFoursquareId(), like));
        if (user.getFriends() != null) {
            user.getFriends().forEach(friend -> {
                if (!existsUser(friend.getFoursquareId())) {
                    getJdbcTemplate().update(CypherQueries.CREATE_FRIEND, user.getFoursquareId(), friend.getName(), friend.getLastName(), friend.getPhotoUrl(), friend.getFoursquareId());
                    if (friend.getLikes() != null) friend.getLikes().forEach(like -> saveLike(friend.getFoursquareId(), like));
                } else getJdbcTemplate().update(CypherQueries.CREATE_FRIENDS_RELATION, user.getFoursquareId(), friend.getFoursquareId());
            });
        }
    }

    @Override
    public void update(User user) {
        getJdbcTemplate().update(CypherQueries.UPDATE_USER, user.getFoursquareId(), user.getName(), user.getLastName(), user.getPhotoUrl());
        if (user.getCredential() != null) {
            getJdbcTemplate().update(CypherQueries.DELETE_CREDENTIALS, user.getFoursquareId());
            getJdbcTemplate().update(CypherQueries.CREATE_HAS_CREDENTIAL_RELATION, user.getFoursquareId(), user.getCredential().getAccessToken());
        }
        if (user.getFriends() != null) {
            List<String> oldFriendsIds = fetchFriendsIds(user.getFoursquareId());
            List<String> intersection = user.getFriends().stream().filter(friend -> oldFriendsIds.contains(friend.getFoursquareId())).map(User::getFoursquareId).collect(Collectors.toList());
            List<User> newFriends = user.getFriends().stream().filter(friend -> !intersection.contains(friend.getFoursquareId())).collect(Collectors.toList());
            oldFriendsIds.removeAll(intersection);
            oldFriendsIds.forEach(friendFoursquareId -> getJdbcTemplate().update(CypherQueries.DELETE_FRIENDS_RELATION, user.getFoursquareId(), friendFoursquareId));
            newFriends.forEach(friend -> {
                if (!existsUser(friend.getFoursquareId())) {
                    getJdbcTemplate().update(CypherQueries.CREATE_FRIEND, user.getFoursquareId(), friend.getName(), friend.getLastName(), friend.getPhotoUrl(), friend.getFoursquareId());
                    if (friend.getLikes() != null) friend.getLikes().forEach(like -> saveLike(friend.getFoursquareId(), like));
                } else getJdbcTemplate().update(CypherQueries.CREATE_FRIENDS_RELATION, user.getFoursquareId(), friend.getFoursquareId());
            });
        }
        if (user.getLikes() != null) {
            List<String> oldLikesIds = fetchLikesIds(user.getFoursquareId());
            List<String> intersection = user.getLikes().stream().filter(like -> oldLikesIds.contains(like.getFoursquareId())).map(Category::getFoursquareId).collect(Collectors.toList());
            List<Category> newLikes = user.getLikes().stream().filter(like -> !intersection.contains(like.getFoursquareId())).collect(Collectors.toList());
            oldLikesIds.removeAll(intersection);
            oldLikesIds.forEach(likeFoursquareId -> getJdbcTemplate().update(CypherQueries.DELETE_LIKES_RELATION, user.getFoursquareId(), likeFoursquareId));
            newLikes.forEach(like -> saveLike(user.getFoursquareId(), like));
        }
    }

    @Override
    public void saveLike(String foursquareId, Category like) {
        getJdbcTemplate().update(CypherQueries.CREATE_LIKES_RELATION, foursquareId, like.getFoursquareId());
    }

    @Override
    public void deleteLike(String foursquareId, Category like) {
        getJdbcTemplate().update(CypherQueries.DELETE_LIKES_RELATION, foursquareId, like.getFoursquareId());
    }

    private List<String> fetchFriendsIds(String foursquareId) {
        return getJdbcTemplate().queryForList(CypherQueries.USER_FRIENDS_FOURSQUARE_IDS, String.class, foursquareId);
    }

    private List<String> fetchLikesIds(String foursquareId) {
        return getJdbcTemplate().queryForList(CypherQueries.USER_LIKES_FOURSQUARE_IDS, String.class, foursquareId);
    }
}