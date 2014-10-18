package com.grayfox.server.foursquare;

import com.google.gson.JsonParser;

public class FoursquareException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FoursquareException(String json) {
        super(new JsonParser().parse(json).getAsJsonObject().get("error").getAsString());
    }
}