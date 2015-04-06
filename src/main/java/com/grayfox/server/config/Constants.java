package com.grayfox.server.config;

import java.util.Locale;

public final class Constants {

    public static Locale SPANISH_LOCALE = new Locale("es");
    public static final int DEFAULT_RADIUS = 20_000;

    private Constants() {
        throw new IllegalAccessError("This class cannot be instantiated nor extended");
    }
}