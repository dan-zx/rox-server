package com.grayfox.server.domain;

import java.io.Serializable;
import java.util.Set;

public class Poi implements Serializable {

    private static final long serialVersionUID = 7058036287180141517L;

    private String name;
    private Location location;
    private String foursquareId;
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

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((categories == null) ? 0 : categories.hashCode());
        result = prime * result + ((foursquareId == null) ? 0 : foursquareId.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Poi other = (Poi) obj;
        if (categories == null) {
            if (other.categories != null) return false;
        } else if (!categories.equals(other.categories)) return false;
        if (foursquareId == null) {
            if (other.foursquareId != null) return false;
        } else if (!foursquareId.equals(other.foursquareId)) return false;
        if (location == null) {
            if (other.location != null) return false;
        } else if (!location.equals(other.location)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Poi [name=").append(name).append(", location=").append(location).append(", foursquareId=").append(foursquareId).append(", categories=").append(categories).append("]");
        return builder.toString();
    }
}