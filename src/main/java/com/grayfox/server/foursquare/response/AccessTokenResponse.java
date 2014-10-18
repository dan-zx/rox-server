package com.grayfox.server.foursquare.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.grayfox.server.foursquare.FoursquareException;

import java.util.Objects;

public class AccessTokenResponse extends Response {

    private String accessToken;

    public AccessTokenResponse(String json) {
        JsonElement obj = new JsonParser().parse(json).getAsJsonObject().get("access_token");
        if (obj == null) setException(new FoursquareException(json));
        else accessToken = obj.getAsString();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, getException());
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
        if (getException() == null) {
            if (other.getException() != null) return false;
        } else if (!getException().equals(other.getException())) return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("AccessTokenResponse [accessToken=").append(accessToken)
                .append("]").append(", exception=").append(getException()).toString();
    }
}