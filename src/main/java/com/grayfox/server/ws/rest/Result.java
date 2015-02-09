package com.grayfox.server.ws.rest;

import java.io.Serializable;

public class Result<T> implements Serializable {

    private static final long serialVersionUID = -7239525962816927992L;

    private final T response;

    public Result(T response) {
        this.response = response;
    }

    public T getResponse() {
        return response;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((response == null) ? 0 : response.hashCode());
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Result<T> other = (Result<T>) obj;
        if (response == null) {
            if (other.response != null) return false;
        } else if (!response.equals(other.response)) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Result [response=").append(response).append("]");
        return builder.toString();
    }
}