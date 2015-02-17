package com.grayfox.server.domain;

import java.io.Serializable;
import java.util.Set;

public class User implements Serializable {

    private static final long serialVersionUID = -8778694523067866298L;

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
        final int prime = 31;
        int result = 1;
        result = prime * result + ((credential == null) ? 0 : credential.hashCode());
        result = prime * result + ((foursquareId == null) ? 0 : foursquareId.hashCode());
        result = prime * result + ((friends == null) ? 0 : friends.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((likes == null) ? 0 : likes.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((photoUrl == null) ? 0 : photoUrl.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        User other = (User) obj;
        if (credential == null) {
            if (other.credential != null) return false;
        } else if (!credential.equals(other.credential)) return false;
        if (foursquareId == null) {
            if (other.foursquareId != null) return false;
        } else if (!foursquareId.equals(other.foursquareId)) return false;
        if (friends == null) {
            if (other.friends != null) return false;
        } else if (!friends.equals(other.friends)) return false;
        if (lastName == null) {
            if (other.lastName != null) return false;
        } else if (!lastName.equals(other.lastName)) return false;
        if (likes == null) {
            if (other.likes != null) return false;
        } else if (!likes.equals(other.likes)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (photoUrl == null) {
            if (other.photoUrl != null) return false;
        } else if (!photoUrl.equals(other.photoUrl)) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("User [name=").append(name).append(", lastName=").append(lastName).append(", photoUrl=").append(photoUrl).append(", foursquareId=").append(foursquareId).append(", credential=").append(credential).append(", likes=").append(likes).append(", friends=").append(friends).append("]");
        return builder.toString();
    }
}