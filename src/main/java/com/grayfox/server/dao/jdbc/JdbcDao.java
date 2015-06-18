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
package com.grayfox.server.dao.jdbc;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;

import com.grayfox.server.util.Constants;
import com.grayfox.server.util.XmlResourceBundleControl;

import org.springframework.jdbc.core.JdbcTemplate;

abstract class JdbcDao {

    private static final Collection<Locale> SUPPORTED_LOCALES = Collections.singletonList(Constants.Locales.SPANISH);
    private static final String RESOURCE_BUNDLE_BASE_NAME = "queries";

    @Inject private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void onPostConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    protected String getQuery(String which) {
        return ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, Locale.ROOT, new XmlResourceBundleControl()).getString(which).trim();
    }

    protected String getQuery(String which, Locale locale) {
        return SUPPORTED_LOCALES.contains(locale) ? ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, locale, new XmlResourceBundleControl()).getString(which).trim() : getQuery(which);
    }
}