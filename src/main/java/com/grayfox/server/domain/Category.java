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

public class Category implements Serializable {

    private static final long serialVersionUID = -8204143874909029069L;

    private String name;
    private String iconUrl;
    private String foursquareId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getFoursquareId() {
        return foursquareId;
    }

    public void setFoursquareId(String foursquareId) {
        this.foursquareId = foursquareId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, iconUrl, foursquareId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Category other = (Category) obj;
        return Objects.equals(name, other.name) &&
               Objects.equals(iconUrl, other.iconUrl) &&
               Objects.equals(foursquareId, other.foursquareId);
    }

    @Override
    public String toString() {
        return "Category [name=" + name + ", iconUrl=" + iconUrl + ", foursquareId=" + foursquareId + "]";
    }
}