package de.jk.quarkus.trains.resources;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import de.jk.quarkus.trains.exception.ErrorsResponse;
import de.jk.quarkus.trains.exception.RecordNotFoundException;
import de.jk.quarkus.trains.model.Function;
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
    	long timestamp = System.currentTimeMillis();
    	List<Locomotive> locomotives = Locomotive
    			.findAll().page(Page.of(pageNum, pageSize)).list();
    	return Response
    			.ok(locomotives)
        		.header("responsetime", (System.currentTimeMillis() - timestamp))
    			.build();
    }
    
    @GET
    @Path("/{id}")
    @Operation(summary = "Get Locomotive for id")
    @Parameters({
    	@Parameter(name = "id", in = ParameterIn.PATH, required = true, 
    			description = "unique locomotive identifier")})
    @APIResponse(responseCode = "200", description = "Locomotive for id", 
    		content = @Content(mediaType = "application/json",
            		schema = @Schema(type = SchemaType.ARRAY, implementation = Locomotive.class)))
    @APIResponse(responseCode = "400", description = "Invalid request data",
    content = @Content(mediaType = "application/json",
 	schema = @Schema(implementation = ErrorsResponse.class)))
    @APIResponse(responseCode = "500", description = "Unknown error", 
	content = @Content(mediaType = "application/json",
    		schema = @Schema(implementation = String.class)))    
    public Response getLocomotiveById(
    		@PathParam("id") @NotNull Long id) {
    	long timestamp = System.currentTimeMillis();
    	Locomotive myLocomotive = Locomotive.findById(id);
    	
    	if (myLocomotive != null) {
    	   	return Response
        			.ok(myLocomotive)
            		.header("responsetime", (System.currentTimeMillis() - timestamp))
        			.build();
    	} else {
    		throw new RecordNotFoundException("record with id " + id + " could not be found!");
    	}
    }

    @GET
    @Path("/address/{address}")
    @Operation(summary = "Get Locomotive for dcc address")
    @Parameters({
    	@Parameter(name = "address", in = ParameterIn.PATH, required = true, 
    			description = "substring of identification (take care of spaces")})
    @APIResponse(responseCode = "200", description = "Locomotives for dcc address", 
    		content = @Content(mediaType = "application/json",
            		schema = @Schema(type = SchemaType.ARRAY, implementation = Locomotive.class)))
    public Response getLocomotiveByAddress(
    		@PathParam("address") @NotNull Integer address) {
    	long timestamp = System.currentTimeMillis();
    	Locomotive myLocomotive = Locomotive.findByAddress(address);
    	return Response
    			.ok(myLocomotive)
        		.header("responsetime", (System.currentTimeMillis() - timestamp))
    			.build();

    }

    @GET
    @Path("/identification/{identification}")
    @Operation(summary = "List of locomotives for Indentification like given string")
    @Parameters({
    	@Parameter(name = "identification", in = ParameterIn.PATH, required = true, 
    			description = "substring of identification (take care of spaces"),
    	@Parameter(name = "pageNum", in = ParameterIn.QUERY,required = false, 
    			description = "number of requested page, value >= 0"),
    	@Parameter(name = "pageSize", in = ParameterIn.QUERY,required = false, 
		description = "size of page (number of records), value >= 0" )
    	})
    @APIResponse(responseCode = "200", description = "List of locomotives for identification search string", 
    		content = @Content(mediaType = "application/json",
            		schema = @Schema(type = SchemaType.ARRAY, implementation = Locomotive.class)))
    public Response getPageableListbyIdentificationLike(
    		@PathParam("identification") @NotNull String identification,
    		@QueryParam("pageNum") @DefaultValue("0") @Min(0) int pageNum, 
    		@QueryParam("pageSize") @DefaultValue("10") @Min(0) int pageSize
    		) {
    	long timestamp = System.currentTimeMillis();
    	List<Locomotive> myLocomotiveList = Locomotive.findAllByIdentificationLike(identification);
    	return Response
    			.ok(myLocomotiveList)
        		.header("responsetime", (System.currentTimeMillis() - timestamp))
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
    public Response add(
    		@RequestBody(required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Locomotive.class)))
    		@Valid Locomotive locomotive) {
		locomotive.id = null;
		if (locomotive.functions == null) locomotive.functions = new ArrayList<Function>(); 
		List<Function> functions = new ArrayList<Function>();
		for (int i = 0; i < locomotive.functions.size(); i++) {
			Function function = new Function();
			function.id = null;
			function.dccnumber = locomotive.functions.get(i).dccnumber;
			function.name = locomotive.functions.get(i).name;
			function.imageurl = locomotive.functions.get(i).imageurl;
			functions.add(function);
		}
		locomotive.functions.clear();
		Locomotive storedLocomotive = em.merge(locomotive);
		for (int i = 0; i < functions.size(); i++) {
			Function function = functions.get(i);
			function.locomotive = new Locomotive();
			function.locomotive.id = storedLocomotive.id;
			function.persist();
		}
		storedLocomotive.functions = functions;
		return Response.status(Response.Status.CREATED).entity(storedLocomotive).build();
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
    public Response change(
    		@RequestBody(required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Locomotive.class)))
    		@Valid Locomotive locomotive) {
    	Locomotive foundLocomotive  = new Locomotive().findById(locomotive.id);
    	if (foundLocomotive != null) {
    		if (locomotive.functions == null) locomotive.functions = new ArrayList<Function>();
    		//locomotive.persist(); //Quarkus bug, can't be used as documented
	    	Locomotive storedLocomotive = em.merge(locomotive);
	    	for (int i = 0; i < storedLocomotive.functions.size(); i++) {
				Function function = new Function();
	    		function.locomotive = new Locomotive();
				function.locomotive.id = foundLocomotive.id;
	    		function.id =foundLocomotive.functions.get(i).id;
				function.dccnumber = foundLocomotive.functions.get(i).dccnumber;
				function.name = foundLocomotive.functions.get(i).name;
				function.imageurl = foundLocomotive.functions.get(i).imageurl;
				em.merge(function);
			}
			return Response.status(Response.Status.OK).entity(storedLocomotive).build();
    	} else {
    		throw new RecordNotFoundException("record with id " + locomotive.id + " could not be found!");
    	}
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "delete locomotive")
    @Parameters({
    	@Parameter(name = "id", in = ParameterIn.PATH, required = true, 
    			description = "unique locomotive identifier")})
    @APIResponse(responseCode = "204", description = "Record Locomotive for given id does not exist")
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