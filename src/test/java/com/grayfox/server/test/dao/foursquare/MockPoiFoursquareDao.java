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

import com.grayfox.server.dao.foursquare.PoiFoursquareDao;

import com.grayfox.server.domain.Location;
import com.grayfox.server.domain.Poi;
import org.springframework.stereotype.Repository;

@Repository("poiFoursquareDao")
public class MockPoiFoursquareDao extends PoiFoursquareDao {

    @Override
    public List<Poi> fetchNext(String poiFoursquareId, int limit, Locale locale) {
        return null;
    }

    @Override
    public List<Poi> fetchNearestByCategory(Location location, Integer radius, String categoryFoursquareId, Locale locale) {
        return null;
    }
}