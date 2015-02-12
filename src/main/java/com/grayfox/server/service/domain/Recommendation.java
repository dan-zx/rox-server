package com.grayfox.server.service.domain;

import java.io.Serializable;
import java.util.List;

import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;

public class Recommendation implements Serializable {

    private static final long serialVersionUID = -5900961085377923024L;

    private final List<Poi> pois;
    private final List<Location> route;

    public Recommendation(List<Poi> pois, List<Location> route) {
        this.pois = pois;
        this.route = route;
    }

    public List<Poi> getPois() {
        return pois;
    }

    public List<Location> getRoute() {
        return route;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pois == null) ? 0 : pois.hashCode());
        result = prime * result + ((route == null) ? 0 : route.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Recommendation other = (Recommendation) obj;
        if (pois == null) {
            if (other.pois != null) return false;
        } else if (!pois.equals(other.pois)) return false;
        if (route == null) {
            if (other.route != null) return false;
        } else if (!route.equals(other.route)) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Recommendation [pois=").append(pois).append(", route=").append(route).append("]");
        return builder.toString();
    }
}