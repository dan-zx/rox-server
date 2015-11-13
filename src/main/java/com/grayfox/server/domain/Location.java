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
        return latitude + "," + longitude;
    }

    public static Location parse(String locationString) {
        if (locationString == null) {
            throw new DomainException.Builder()
                .messageKey("location.is_null.error")
                .build();
        }
        String[] latLng = locationString.split(",");
        if (latLng.length != 2) { 
            throw new DomainException.Builder()
                .messageKey("location.format.error")
                .addMessageArgument(locationString)
                .build();
        }
        try {
            Location location = new Location();
            location.setLatitude(Double.parseDouble(latLng[0]));
            location.setLongitude(Double.parseDouble(latLng[1]));
            return location;
        } catch (NumberFormatException ex) {
            throw new DomainException.Builder()
                .messageKey("location.format.error")
                .addMessageArgument(locationString)
                .cause(ex)
                .build();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Location other = (Location) obj;
        return Objects.equals(latitude, other.latitude) &&
               Objects.equals(longitude, other.longitude);
    }

    @Override
    public String toString() {
        return "Location [latitude=" + latitude + ", longitude=" + longitude + "]";
    }
}