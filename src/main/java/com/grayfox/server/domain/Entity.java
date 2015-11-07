package com.grayfox.server.domain;

import java.io.Serializable;
import java.util.Objects;

abstract class Entity<ID extends Serializable> {

    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Entity<?> other = (Entity<?>) obj;
        return Objects.equals(id, other.id);
    }
}