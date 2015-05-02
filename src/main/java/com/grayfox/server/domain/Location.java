package com.grayfox.server.domain;

import java.io.Serializable;

public class Location implements Serializable {

    private static final long serialVersionUID = 665032830432086830L;

    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String stringValues() {
        return new StringBuilder().append(latitude).append(',').append(longitude).toString();
    }

    public static Location parse(String locationString) {
        String[] latLng = locationString.split(",");
        if (latLng == null || latLng.length != 2) throw new IllegalArgumentException("Incorrect location format. It must be '##.##,##.##'"); 
        try {
            Location location = new Location();
            location.setLatitude(Double.parseDouble(latLng[0]));
            location.setLongitude(Double.parseDouble(latLng[1]));
            return location;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Incorrect location format. It must be '##.##,##.##'");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Location other = (Location) obj;
        if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude)) return false;
        if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Location [latitude=" + latitude + ", longitude=" + longitude + "]";
    }
}