package com.grayfox.server.util;

import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;

public final class URIs {

    private URIs() {
        throw new IllegalAccessError("This class cannot be instantiated or extended");
    }

    public static URIBuilder builder(String uri) {
        try {
            return new URIBuilder(uri);
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
