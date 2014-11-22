package com.grayfox.server.test;

import javax.inject.Inject;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsRoute;

import com.grayfox.server.config.TestConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class DirectionsTest {

    @Inject private GeoApiContext apiContext;

    @Test
    public void testDirections() throws Exception {
        // http://maps.googleapis.com/maps/api/directions/json?origin=19.04236,-98.198655&destination=19.045098,-98.189642&waypoints=19.048364,-98.196702|19.051305,-98.194728|19.049865,-98.199921
        DirectionsRoute[] routes = DirectionsApi
            .newRequest(apiContext)
            .origin(latLngToString(19.04236, -98.198655))
            .destination(latLngToString(19.045098, -98.189642))
            .waypoints(latLngToString(19.048364, -98.196702), latLngToString(19.051305, -98.194728), latLngToString(19.049865, -98.199921))
            .await();

        for (DirectionsRoute route : routes) {
            System.out.println(route);
        }
    }

    private String latLngToString(double lat, double lng) {
        return new StringBuilder()
            .append(lat)
            .append(',')
            .append(lng)
            .toString();
    }
}