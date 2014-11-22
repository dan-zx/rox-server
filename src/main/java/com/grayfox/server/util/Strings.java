package com.grayfox.server.util;

public final class Strings {

    public static final String EMPTY_STRING = "";

    private Strings() {
        throw new IllegalAccessError("This class cannot be instantiated or extended");
    }

    public static boolean isNullOrBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}