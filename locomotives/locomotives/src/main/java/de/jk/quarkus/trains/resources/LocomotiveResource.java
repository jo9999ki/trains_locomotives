package de.jk.quarkus.trains.resources;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import de.jk.quarkus.trains.exception.ErrorsResponse;
import de.jk.quarkus.trains.exception.RecordNotFoundException;
import de.jk.quarkus.trains.model.Locomotive;
import io.quarkus.panache.common.Page;

@Tag(name= "Locomotives") //OpenAPI
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
    @Operation(summary = "List of locomotives")
    @Parameters({
    	@Parameter(name = "pageNum", in = ParameterIn.QUERY,required = false, 
    			description = "number of requested page, value >= 0"),
    	@Parameter(name = "pageSize", in = ParameterIn.QUERY,required = false, 
		description = "size of page (number of records), value >= 0" )
    	})
    @APIResponse(responseCode = "200", description = "Total list of locomotives", 
    		content = @Content(mediaType = "application/json",
            		schema = @Schema(type = SchemaType.ARRAY, implementation = Locomotive.class)))
    public Response getPagableList( 
    		@QueryParam("pageNum") @DefaultValue("0") @Min(0) int pageNum, 
    		@QueryParam("pageSize") @DefaultValue("10") @Min(0) int pageSize) {
    	List<Locomotive> locomotives = Locomotive
    			.findAll().page(Page.of(pageNum, pageSize)).list();
    	return Response
    			.ok(locomotives)
        		.header("responsetime", "0815")
    			.build();
    }

    @POST
    @Operation(summary = "Create new locomotive")
    @APIResponse(responseCode = "201", description = "Created locomotive",
                 content = @Content(mediaType = "application/json",
                 	schema = @Schema(implementation = Locomotive.class)))
    @APIResponse(responseCode = "400", description = "Invalid request data",
    content = @Content(mediaType = "application/json",
 	schema = @Schema(implementation = ErrorsResponse.class)))
    @APIResponse(responseCode = "500", description = "Unknown error", 
	content = @Content(mediaType = "application/json",
    		schema = @Schema(implementation = String.class)))    
    public Response add(@Valid Locomotive locomotive) {
		locomotive.id = null;
		locomotive.persist();	
		return Response.status(Response.Status.CREATED).entity(locomotive).build();
    }
    
    @PUT
    @Operation(summary = "Update locomotive")
    @APIResponse(responseCode = "201", description = "Created locomotive",
                 content = @Content(mediaType = "application/json",
                 	schema = @Schema(implementation = Locomotive.class)))
    @APIResponse(responseCode = "400", description = "Invalid request data",
    content = @Content(mediaType = "application/json",
 	schema = @Schema(implementation = ErrorsResponse.class)))
    @APIResponse(responseCode = "500", description = "Unknown error", 
	content = @Content(mediaType = "application/json",
    		schema = @Schema(implementation = String.class)))    
    public Response change(@Valid Locomotive locomotive) {
    	Locomotive foundLocomotive  = new Locomotive().findById(locomotive.id);
    	if (foundLocomotive != null) {
	    	//locomotive.persist(); //Quarkus bug, can't be used as documented
	    	em.merge(locomotive);
			return Response.status(Response.Status.OK).entity(foundLocomotive).build();
    	} else {
    		throw new RecordNotFoundException("record with id " + locomotive.id + " could not be found!");
    	}
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "delete locomotive")
    @APIResponse(responseCode = "201", description = "Created locomotive",
                 content = @Content(mediaType = "application/json",
                 	schema = @Schema(implementation = Locomotive.class)))
    @APIResponse(responseCode = "400", description = "Invalid request data",
    content = @Content(mediaType = "application/json",
 	schema = @Schema(implementation = ErrorsResponse.class)))
    @APIResponse(responseCode = "500", description = "Unknown error", 
	content = @Content(mediaType = "application/json",
    		schema = @Schema(implementation = String.class)))    
    public Response delete(@PathParam("id") Long id) {
    	Locomotive locomotive  = new Locomotive().findById(id);
    	if (locomotive != null) {
    		locomotive.delete();
    	} else {
    		throw new RecordNotFoundException("record with id " + id + " could not be found!");
    	}
    	return Response
        		.status(Response.Status.NO_CONTENT)
        		.build();
    }
}