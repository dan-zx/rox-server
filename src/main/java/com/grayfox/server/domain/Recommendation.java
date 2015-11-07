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

import java.util.Objects;

public class Recommendation {

    public enum Type { GLOBAL, SELF, SOCIAL }

    private Type type;
    private String reason;
    private Poi poi;

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

    public Poi getPoi() {
        return poi;
    }

    public void setPoi(Poi poi) {
        this.poi = poi;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, reason, poi);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Recommendation other = (Recommendation) obj;
        return Objects.equals(type, other.type) &&
               Objects.equals(reason, other.reason) &&
               Objects.equals(poi, other.poi);
    }

    @Override
    public String toString() {
        return "Recommendation [type=" + type + ", reason=" + reason + ", poi=" + poi + "]";
    }
}