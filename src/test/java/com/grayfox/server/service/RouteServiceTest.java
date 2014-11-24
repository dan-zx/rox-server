package com.grayfox.server.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import com.grayfox.server.service.RouteService.Transportation;

import com.grayfox.server.service.model.Location;
import com.grayfox.server.service.model.Poi;
import org.junit.Test;
import com.grayfox.server.test.BaseDbReseterTest;

public class RouteServiceTest extends BaseDbReseterTest {

    @Inject private RouteService routeService;

    @Override
    public void setUp() {
        super.setUp();
        assertThat(routeService).isNotNull();
    }

    @Test
    public void shouldCreateRoute() {
        assertThat(routeService.createRoute(listOfPois(), Transportation.DRIVING))
            .isNotNull().isNotEmpty();
        
        assertThat(routeService.createRoute(listOfPois(), Transportation.WALKING))
            .isNotNull().isNotEmpty();
    }

    private List<Poi> listOfPois() {
        Poi poi1 = new Poi();
        Location location = new Location();
        location.setLatitude(19.412069);
        location.setLongitude(-99.169797);
        poi1.setLocation(location);
        
        Poi poi2 = new Poi();
        location = new Location();
        location.setLatitude(19.411290);
        location.setLongitude(-99.166878);
        poi2.setLocation(location);
        
        Poi poi3 = new Poi();
        location = new Location();
        location.setLatitude(19.412464);
        location.setLongitude(-99.165602);
        poi3.setLocation(location);
        
        return Arrays.asList(poi1, poi2, poi3);
    }
}