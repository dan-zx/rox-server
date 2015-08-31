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
package com.grayfox.server.test.dao.foursquare;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import com.foursquare4j.FoursquareApi;

import com.grayfox.server.dao.foursquare.PoiFoursquareDao;
import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;
import com.grayfox.server.test.util.HttpStatus;
import com.grayfox.server.test.util.Utils;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.springframework.stereotype.Repository;

@Repository("poiFoursquareDao")
public class MockPoiFoursquareDao extends PoiFoursquareDao {

    @Inject private MockWebServer mockWebServer;
    @Inject private FoursquareApi foursquareApi;

    @Override
    public List<Poi> fetchNext(String poiFoursquareId, int limit, Locale locale) {
        if ("4c09270ea1b32d7f172297f0".equals(poiFoursquareId)) {
            mockWebServer.enqueue(new MockResponse()
                .setStatus(HttpStatus.OK.toString())
                .setBody(Utils.getContentFromFileInClasspath("responses/venue.json")));
            mockWebServer.enqueue(new MockResponse()
                .setStatus(HttpStatus.OK.toString())
                .setBody(Utils.getContentFromFileInClasspath("responses/nextvenues_1.json")));
            mockWebServer.enqueue(new MockResponse()
                .setStatus(HttpStatus.OK.toString())
                .setBody(Utils.getContentFromFileInClasspath("responses/nextvenues_2.json")));
        } else {
            mockWebServer.enqueue(new MockResponse()
                .setStatus(HttpStatus.OK.toString())
                .setBody(Utils.getContentFromFileInClasspath("responses/generic_error.json")));
        }
        return fetchNext(foursquareApi, poiFoursquareId, limit);
    }

    @Override
    public List<Poi> fetchNearestByCategory(Location location, Integer radius, String categoryFoursquareId, Locale locale) {
        if (Location.parse("19.04365,-98.197968").equals(location) && "4bf58dd8d48988d151941735".equals(categoryFoursquareId)) {
            mockWebServer.enqueue(new MockResponse()
                .setStatus(HttpStatus.OK.toString())
                .setBody(Utils.getContentFromFileInClasspath("responses/search.json")));
        } else {
            mockWebServer.enqueue(new MockResponse()
                .setStatus(HttpStatus.OK.toString())
                .setBody(Utils.getContentFromFileInClasspath("responses/generic_error.json")));
        }
        return fetchNearestByCategory(foursquareApi, location, radius, categoryFoursquareId);
    }
}