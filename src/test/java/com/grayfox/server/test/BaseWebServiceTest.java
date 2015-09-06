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
package com.grayfox.server.test;

import javax.ws.rs.core.Application;

import com.grayfox.server.test.config.TestConfig;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public abstract class BaseWebServiceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig()
            .packages("com.grayfox.server.ws.rest")
            .property("contextConfig", new AnnotationConfigApplicationContext(TestConfig.class));
    }
}