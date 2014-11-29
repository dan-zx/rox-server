package com.grayfox.server.config;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class WebServiceConfig extends ResourceConfig {

    public WebServiceConfig() {
        packages("com.grayfox.server.ws.rest");
        register(JacksonFeature.class);
    }
}