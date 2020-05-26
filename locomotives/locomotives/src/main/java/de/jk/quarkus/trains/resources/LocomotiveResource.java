package de.jk.quarkus.trains.resources;

import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/locomotives")
public class LocomotiveResource {

	@GET
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String hello() {
    	return "hello";
    }
}