package com.grayfox.server.data;

import java.io.Serializable;

public class AppUser implements Serializable {

    private static final long serialVersionUID = -754162508775020641L;

    private Long id;
    private String appAccessToken;
    private String foursquareAccessToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppAccessToken() {
        return appAccessToken;
    }

    public void setAppAccessToken(String appAccessToken) {
        this.appAccessToken = appAccessToken;
    }

    public String getFoursquareAccessToken() {
        return foursquareAccessToken;
    }

    public void setFoursquareAccessToken(String fourquareAccessToken) {
        this.foursquareAccessToken = fourquareAccessToken;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((appAccessToken == null) ? 0 : appAccessToken.hashCode());
        result = prime * result + ((foursquareAccessToken == null) ? 0 : foursquareAccessToken.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AppUser other = (AppUser) obj;
        if (appAccessToken == null) {
            if (other.appAccessToken != null) return false;
        } else if (!appAccessToken.equals(other.appAccessToken)) return false;
        if (foursquareAccessToken == null) {
            if (other.foursquareAccessToken != null) return false;
        } else if (!foursquareAccessToken.equals(other.foursquareAccessToken)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("AppUser [id=").append(id).append(", appAccessToken=")
                .append(appAccessToken).append(", foursquareAccessToken=")
                .append(foursquareAccessToken).append("]").toString();
    }
}