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

    public static final class Ints {
        public static final int EARTH_RADIUS = 6371000;

        private Ints() {
            throw new IllegalAccessError("This class cannot be instantiated nor extended");
        }
    }

    public static final class Strings {
        public static final String MISSING_RESOURCE_KEY_FORMAT = "???%s???";

        private Strings() {
            throw new IllegalAccessError("This class cannot be instantiated nor extended");
        }
    }
}