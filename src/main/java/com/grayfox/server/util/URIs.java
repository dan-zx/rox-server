package com.grayfox.server.util;

import java.net.URI;
import java.net.URISyntaxException;

public final class URIs {

    private URIs() {
        throw new IllegalAccessError("This class cannot be instantiated or extended");
    }

    public static URI toUri(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
