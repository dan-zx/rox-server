/*
 * Copyright 2014-2015 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

@Repository("userLocalDao")
public class UserJdbcDao extends JdbcDao implements UserDao {

    @Override
    public User findCompactByAccessToken(String accessToken) {
        List<User> users = getJdbcTemplate().query(getQuery("User.findByAccessToken"), 
                (ResultSet rs, int i) -> {
                    User user = new User();
                    int columnIndex = 1;
                    user.setId(rs.getLong(columnIndex++));
                    user.setName(rs.getString(columnIndex++));
                    user.setLastName(rs.getString(columnIndex++));
                    user.setPhotoUrl(rs.getString(columnIndex++));
                    user.setFoursquareId(rs.getString(columnIndex++));
                    return user;
                },
                accessToken);
        if (users.size() > 1) {
            throw new DaoException.Builder()
                .messageKey("data.integrity.error")
                .build();
        }
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public String findFoursquareIdByAccessToken(String accessToken) {
        List<String> foursquareIds = getJdbcTemplate().queryForList(getQuery("User.findFoursquareIdByAccessToken"), String.class, accessToken);
        if (foursquareIds.size() > 1) {
            throw new DaoException.Builder()
                .messageKey("data.integrity.error")
                .build();
        }
        return foursquareIds.isEmpty() ? null : foursquareIds.get(0);
    }

    @Override
    public List<User> findCompactFriendsByFoursquareId(String foursquareId) {
        return getJdbcTemplate().query(getQuery("User.findFriendsByUserFoursquareId"), 
                (ResultSet rs, int i) -> {
                    User user = new User();
                    int columnIndex = 1;
                    user.setId(rs.getLong(columnIndex++));
                    user.setName(rs.getString(columnIndex++));
                    user.setLastName(rs.getString(columnIndex++));
                    user.setPhotoUrl(rs.getString(columnIndex++));
                    user.setFoursquareId(rs.getString(columnIndex++));
                    return user;
                }, foursquareId);
    }

    @Override
    public List<Category> findLikesByFoursquareId(String foursquareId, Locale locale) {
        return getJdbcTemplate().query(getQuery("User.findLikesByUserFoursquareId", locale), 
                (ResultSet rs, int i) -> {
                    Category category = new Category();
                    int columnIndex = 1;
                    category.setId(rs.getLong(columnIndex++));
                    category.setName(rs.getString(columnIndex++));
                    category.setIconUrl(rs.getString(columnIndex++));
                    category.setFoursquareId(rs.getString(columnIndex++));
                    return category;
                }, foursquareId);
    }

    @Override
    public boolean areFriends(String foursquareId1, String foursquareId2) {
        List<Boolean> exists = getJdbcTemplate().queryForList(getQuery("User.areFriends"), Boolean.class, foursquareId1, foursquareId2);
        return !exists.isEmpty();
    }

    @Override
    public boolean exists(String foursquareId) {
        List<Boolean> exists = getJdbcTemplate().queryForList(getQuery("User.exists"), Boolean.class, foursquareId);
        return !exists.isEmpty();
    }

    @Override
    public void save(User user) {
        getJdbcTemplate().update(getQuery("User.create"), user.getName(), user.getLastName(), user.getPhotoUrl(), user.getFoursquareId());
        if (user.getCredential() != null) getJdbcTemplate().update(getQuery("User.createHasRelationship"), user.getFoursquareId(), user.getCredential().getAccessToken());
        if (user.getLikes() != null) user.getLikes().forEach(like -> saveLike(user.getFoursquareId(), like.getFoursquareId()));
        if (user.getFriends() != null) {
            user.getFriends().forEach(friend -> {
                if (!exists(friend.getFoursquareId())) {
                    getJdbcTemplate().update(getQuery("User.create"), friend.getName(), friend.getLastName(), friend.getPhotoUrl(), friend.getFoursquareId());
                    getJdbcTemplate().update(getQuery("User.createFriendsRelationship"), user.getFoursquareId(), friend.getFoursquareId());
                    if (friend.getLikes() != null) friend.getLikes().forEach(like -> saveLike(friend.getFoursquareId(), like.getFoursquareId()));
                    friend.setId(findIdByFoursquareId(friend.getFoursquareId()));
                } else getJdbcTemplate().update(getQuery("User.createFriendsRelationship"), user.getFoursquareId(), friend.getFoursquareId());
            });
        }
        user.setId(findIdByFoursquareId(user.getFoursquareId()));
    }

    @Override
    public void update(User user) {
        getJdbcTemplate().update(getQuery("User.update"), user.getFoursquareId(), user.getName(), user.getLastName(), user.getPhotoUrl());
        if (user.getCredential() != null) {
            getJdbcTemplate().update(getQuery("Credential.delete"), user.getFoursquareId());
            getJdbcTemplate().update(getQuery("User.createHasRelationship"), user.getFoursquareId(), user.getCredential().getAccessToken());
        }
        if (user.getFriends() != null) {
            List<String> oldFriendsIds = fetchFriendsIds(user.getFoursquareId());
            List<String> intersection = user.getFriends().stream().filter(friend -> oldFriendsIds.contains(friend.getFoursquareId())).map(User::getFoursquareId).collect(Collectors.toList());
            List<User> newFriends = user.getFriends().stream().filter(friend -> !intersection.contains(friend.getFoursquareId())).collect(Collectors.toList());
            oldFriendsIds.removeAll(intersection);
            oldFriendsIds.forEach(friendFoursquareId -> getJdbcTemplate().update(getQuery("User.deleteFriendsRelationship"), user.getFoursquareId(), friendFoursquareId));
            newFriends.forEach(friend -> {
                if (!exists(friend.getFoursquareId())) {
                    getJdbcTemplate().update(getQuery("User.create"), friend.getName(), friend.getLastName(), friend.getPhotoUrl(), friend.getFoursquareId());
                    getJdbcTemplate().update(getQuery("User.createFriendsRelationship"), user.getFoursquareId(), friend.getFoursquareId());
                    if (friend.getLikes() != null) friend.getLikes().forEach(like -> saveLike(friend.getFoursquareId(), like.getFoursquareId()));
                    friend.setId(findIdByFoursquareId(friend.getFoursquareId()));
                } else getJdbcTemplate().update(getQuery("User.createFriendsRelationship"), user.getFoursquareId(), friend.getFoursquareId());
            });
        }
        if (user.getLikes() != null) {
            List<String> oldLikesIds = fetchLikesIds(user.getFoursquareId());
            List<String> intersection = user.getLikes().stream().filter(like -> oldLikesIds.contains(like.getFoursquareId())).map(Category::getFoursquareId).collect(Collectors.toList());
            List<Category> newLikes = user.getLikes().stream().filter(like -> !intersection.contains(like.getFoursquareId())).collect(Collectors.toList());
            oldLikesIds.removeAll(intersection);
            oldLikesIds.forEach(likeFoursquareId -> getJdbcTemplate().update(getQuery("User.deleteLikesRelationship"), user.getFoursquareId(), likeFoursquareId));
            newLikes.forEach(like -> saveLike(user.getFoursquareId(), like.getFoursquareId()));
        }
    }

    @Override
    public void saveLike(String foursquareId, String categoryFoursquareI) {
        getJdbcTemplate().update(getQuery("User.createLikesRelationship"), foursquareId, categoryFoursquareI);
    }

    @Override
    public void deleteLike(String foursquareId, String categoryFoursquareI) {
        getJdbcTemplate().update(getQuery("User.deleteLikesRelationship"), foursquareId, categoryFoursquareI);
    }

    private List<String> fetchFriendsIds(String foursquareId) {
        return getJdbcTemplate().queryForList(getQuery("User.findFriendsFoursquareIdsByUserFoursquareId"), String.class, foursquareId);
    }

    private List<String> fetchLikesIds(String foursquareId) {
        return getJdbcTemplate().queryForList(getQuery("User.findLikesFoursquareIdsByUserFoursquareId"), String.class, foursquareId);
    }

    private Long findIdByFoursquareId (String foursquareId) {
        return getJdbcTemplate().queryForObject(getQuery("User.findIdByFoursquareId"), Long.class, foursquareId);
    }
}