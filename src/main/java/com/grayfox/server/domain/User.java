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
package com.grayfox.server.domain;

import java.util.Objects;
import java.util.Set;

public class User extends Entity<Long> {

    private String name;
    private String lastName;
    private String photoUrl;
    private String foursquareId;
    private Credential credential;
    private Set<Category> likes;
    private Set<User> friends;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getFoursquareId() {
        return foursquareId;
    }

    public void setFoursquareId(String foursquareId) {
        this.foursquareId = foursquareId;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public Set<Category> getLikes() {
        return likes;
    }

    public void setLikes(Set<Category> likes) {
        this.likes = likes;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastName, photoUrl, foursquareId, credential, likes, friends);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return Objects.equals(name, other.name) &&
               Objects.equals(lastName, other.lastName) &&
               Objects.equals(photoUrl, other.photoUrl) &&
               Objects.equals(foursquareId, other.foursquareId) &&
               Objects.equals(credential, other.credential) &&
               Objects.equals(likes, other.likes) &&
               Objects.equals(friends, other.friends);
    }

    @Override
    public String toString() {
        return "User [id=" + getId() + ", name=" + name + ", lastName=" + lastName + ", photoUrl=" + photoUrl + ", foursquareId=" + foursquareId + ", credential=" + credential + ", likes=" + likes + ", friends=" + friends + "]";
    }
}