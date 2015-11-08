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
package com.grayfox.server.dao;

import java.util.List;
import java.util.Locale;

import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.User;

public interface UserDao {

    User findCompactByAccessToken(String accessToken);
    String findFoursquareIdByAccessToken(String accessToken);
    List<User> findCompactFriendsByFoursquareId(String foursquareId);
    List<Category> findLikesByFoursquareId(String foursquareId, Locale locale);
    boolean areFriends(String foursquareId1, String foursquareId2);
    boolean exists(String foursquareId);
    void save(User user);
    void update(User user);
    void saveLike(String foursquareId, String categoryFoursquareI);
    void deleteLike(String foursquareId, String categoryFoursquareI);
}