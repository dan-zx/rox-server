package com.grayfox.server.ws.rest.response;

import java.io.Serializable;

public class AccessTokenResponse implements Serializable {

    private static final long serialVersionUID = 9144120001670895369L;

    private final String accessToken;

    public AccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accessToken == null) ? 0 : accessToken.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AccessTokenResponse other = (AccessTokenResponse) obj;
        if (accessToken == null) {
            if (other.accessToken != null) return false;
        } else if (!accessToken.equals(other.accessToken)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "AccessTokenResponse [accessToken=" + accessToken + "]";
    }
}