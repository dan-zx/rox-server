package com.grayfox.server.util;

import java.util.Locale;

public final class Constants {

    private Constants() {
        throw new IllegalAccessError("This class cannot be instantiated nor extended");
    }

    public static final class Locales {
        public static final Locale SPANISH = new Locale("es");

        private Locales() {
            throw new IllegalAccessError("This class cannot be instantiated nor extended");
        }
    }

    public static final class Regexs {
        public static final String POSITIVE_INT = "[1-9]\\d*";
        public static final String LOCATION = "(\\-?\\d+(\\.\\d+)?),(\\-?\\d+(\\.\\d+)?)";

        private Regexs() {
            throw new IllegalAccessError("This class cannot be instantiated nor extended");
        }
    }
}