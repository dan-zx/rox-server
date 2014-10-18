package com.grayfox.server.foursquare.response;

import com.grayfox.server.foursquare.FoursquareException;

public abstract class Response {

    private FoursquareException exception;

    public FoursquareException getException() {
        return exception;
    }

    public void setException(FoursquareException exception) {
        this.exception = exception;
    }
}