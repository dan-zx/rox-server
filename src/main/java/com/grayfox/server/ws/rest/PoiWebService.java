package com.grayfox.server.ws.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.grayfox.server.domain.Poi;
import com.grayfox.server.service.PoiService;

import org.springframework.stereotype.Controller;

@Controller
@Path("pois")
public class PoiWebService extends BaseRestComponent {

    @Inject private PoiService poiService;

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<List<Poi>> getPois() {
        return new Result<>(poiService.getPois());
    }
}