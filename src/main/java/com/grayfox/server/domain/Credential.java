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

import java.io.Serializable;
import java.util.Objects;

public class Credential implements Serializable {

    private static final long serialVersionUID = 6767036404149116196L;

    private String accessToken;
    private String foursquareAccessToken;
    private boolean isNew;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getFoursquareAccessToken() {
        return foursquareAccessToken;
    }

    public void setFoursquareAccessToken(String foursquareAccessToken) {
        this.foursquareAccessToken = foursquareAccessToken;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, foursquareAccessToken, isNew);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Credential other = (Credential) obj;
        return Objects.equals(accessToken, other.accessToken) &&
               Objects.equals(foursquareAccessToken, other.foursquareAccessToken) &&
               Objects.equals(isNew, other.isNew);
    }

    @Override
    public String toString() {
        return "Credential [accessToken=" + accessToken + ", foursquareAccessToken=" + foursquareAccessToken + ", isNew=" + isNew + "]";
    }
}