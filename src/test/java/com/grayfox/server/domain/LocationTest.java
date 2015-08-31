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
package com.grayfox.server.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

public class LocationTest {

    @Test
    public void testParseOk() {
        Location expectedLocation = new Location();
        expectedLocation.setLatitude(19.78);
        expectedLocation.setLongitude(-98.23);

        Location actualLocation = Location.parse(expectedLocation.getLatitude() + "," + expectedLocation.getLongitude());

        assertThat(actualLocation).isNotNull().isEqualTo(expectedLocation);
    }

    @Test
    public void testParseError() {
        assertThatThrownBy(() -> Location.parse(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Location string must not be null");

        assertThatThrownBy(() -> Location.parse("xx,xx"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Incorrect location format");

        assertThatThrownBy(() -> Location.parse("a string"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Incorrect location format");
    }
}