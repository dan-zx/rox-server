package com.grayfox.server.data;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String authorizationCode;
    private String accessToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accessToken, authorizationCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        User other = (User) obj;
        if (accessToken == null) {
            if (other.accessToken != null) return false;
        } else if (!accessToken.equals(other.accessToken)) return false;
        if (authorizationCode == null) {
            if (other.authorizationCode != null) return false;
        } else if (!authorizationCode.equals(other.authorizationCode)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("User [id=").append(id).append(", authorizationCode=")
                .append(authorizationCode).append(", accessToken=").append(accessToken).append("]")
                .toString();
    }
}