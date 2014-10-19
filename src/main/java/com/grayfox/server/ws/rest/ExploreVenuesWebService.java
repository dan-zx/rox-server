package com.grayfox.server.ws.rest;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.foyt.foursquare.api.entities.CompactVenue;

import com.grayfox.server.service.ExploreVenuesService;

@Named
@Path("v1/explore")
public class ExploreVenuesWebService implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(ExploreVenuesWebService.class);
    
    private ExploreVenuesService exploreVenuesService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/recommended-venues")
    public List<CompactVenue> recommendVenues(
            @QueryParam("user_id") Long id, 
            @QueryParam("lat_lng") String latLng) {
        LOG.debug("recommendVenues({}, {})", id, latLng);
        return exploreVenuesService.recommendVenues(id, latLng);
    }

    @Inject
    public void setExploreVenuesService(ExploreVenuesService exploreVenuesService) {
        this.exploreVenuesService = exploreVenuesService;
    }
}