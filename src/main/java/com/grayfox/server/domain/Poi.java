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
import java.util.Set;

public class Poi implements Serializable {

    private static final long serialVersionUID = 7058036287180141517L;

    private String name;
    private Location location;
    private String foursquareId;
    private Double foursquareRating;
    private Set<Category> categories;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getFoursquareId() {
        return foursquareId;
    }

    public void setFoursquareId(String foursquareId) {
        this.foursquareId = foursquareId;
    }

    public Double getFoursquareRating() {
        return foursquareRating;
    }

    public void setFoursquareRating(Double foursquareRating) {
        this.foursquareRating = foursquareRating;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, location, foursquareId, foursquareRating, categories);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Poi other = (Poi) obj;
        return Objects.equals(name, other.name) &&
               Objects.equals(location, other.location) &&
               Objects.equals(foursquareId, other.foursquareId) &&
               Objects.equals(foursquareRating, other.foursquareRating) &&
               Objects.equals(categories, other.categories);
    }

    @Override
    public String toString() {
        return "Poi [name=" + name + ", location=" + location + ", foursquareId=" + foursquareId + ", foursquareRating=" + foursquareRating + ", categories=" + categories + "]";
    }
}