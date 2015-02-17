package com.grayfox.server.domain;

import java.io.Serializable;
import java.util.List;

public class Recommendation implements Serializable {

    public static enum Type { SOCIAL, SELF }

    private static final long serialVersionUID = -5900961085377923024L;

    private Type type;
    private String reason;
    private List<Poi> poiSequence;
    private List<Location> routePoints;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<Poi> getPoiSequence() {
        return poiSequence;
    }

    public void setPoiSequence(List<Poi> poiSequence) {
        this.poiSequence = poiSequence;
    }

    public List<Location> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(List<Location> routePoints) {
        this.routePoints = routePoints;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((poiSequence == null) ? 0 : poiSequence.hashCode());
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        result = prime * result + ((routePoints == null) ? 0 : routePoints.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Recommendation other = (Recommendation) obj;
        if (poiSequence == null) {
            if (other.poiSequence != null) return false;
        } else if (!poiSequence.equals(other.poiSequence)) return false;
        if (reason == null) {
            if (other.reason != null) return false;
        } else if (!reason.equals(other.reason)) return false;
        if (routePoints == null) {
            if (other.routePoints != null) return false;
        } else if (!routePoints.equals(other.routePoints)) return false;
        if (type != other.type) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Recommendation [type=").append(type).append(", reason=").append(reason).append(", poiSequence=").append(poiSequence).append(", routePoints=").append(routePoints).append("]");
        return builder.toString();
    }
}