package com.grayfox.server.data;

import java.io.Serializable;
import java.util.Objects;

public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String accessToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accessToken);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AppUser other = (AppUser) obj;
        if (accessToken == null) {
            if (other.accessToken != null) return false;
        } else if (!accessToken.equals(other.accessToken)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("User [id=").append(id).append(", accessToken=")
                .append(accessToken).append("]")
                .toString();
    }
}