package de.jk.quarkus.trains.resources;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import de.jk.quarkus.trains.model.Locomotive;

@Path("/locomotives")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class LocomotiveResource {

	@Inject
    EntityManager em;
	
	@Inject
	Validator validator;
	
    @GET
    public Response getTotalList() {
    	List<Locomotive> locomotives = Locomotive.listAll();
    	return Response.ok(locomotives).build();
    }

    @POST
    public Response add(@Valid Locomotive locomotive) {
		locomotive.id = null;
		locomotive.persist();	
		return Response.status(Response.Status.CREATED).entity(locomotive).build();
    }
    
    @PUT
    public Response change(@Valid Locomotive locomotive) {
		//locomotive.persist();
    	em.merge(locomotive);
		return Response.status(Response.Status.OK).entity(locomotive).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
    	Locomotive locomotive  = new Locomotive().findById(id);
    	locomotive.delete();
    	return Response
        		.status(Response.Status.NO_CONTENT)
        		.build();
    }
}