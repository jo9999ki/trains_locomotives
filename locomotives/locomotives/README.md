# Locomotives Service

## Functional description

This Java project provides a REST service providing model train control for locomotives

## Technical preconditions

* JDK 11 

* Maven 3.6.3 

* Quarkus 1.4.2

* Docker Desktop / Deamon

## locomotives project (content created by io.quarkus)

The project uses Quarkus, the Supersonic Subatomic Java Framework (See https://quarkus.io/).

### Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

### Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `locomotives-1.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/locomotives-1.0-SNAPSHOT-runner.jar`.

### Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/locomotives-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.

# Project Blog

## Project creation

Project created using JDK 11 Maven 3.6.3 Quarkus 1.4.2:

`mvn io.quarkus:quarkus-maven-plugin:1.4.2.Final:create -DprojectGroupId=de.jk.quarkus.trains -DprojectArtifactId=locomotives -DclassName="de.jk.quarkus.trains.LocomotiveResource" -Dpath="/locomotives"`

Open created directory: `cd locomotives`

And compile and start: `mvnw compile quarkus:dev`

Test: `curl localhost:8080/locomotives`

Show index.html with: `localhost:8080`

## Build and run jar file

### JVM jar file
Create jar file with: `mvn package`

Start jar file: `java -jar ./target/locomotives-1.0-SNAPSHOT-runner.jar`
* Application start time: 3,5 secs
* First / next response times in browser (localhost:8080/locomotives): 213 / 5 ms
* First / next response times curl (curl localhost:8080/customers -s -o /dev/null -w "%{time_starttransfer}\n"): 234 / 63 ms
* Jar + lib file size: 38 MB 

### Create native executable

As I don't have GraalVM and necessary visual studio components installed, I decided to run the native executable build (64 bit Linux executable) inside a container. This requires creating a tailored jar file using Red Hats UBI (Universal Base Image) image for a small image size.

Precondition here is local Docker desktop installation, running docker desktop and activated docker deamon (see Docker desktop settings).

--> See https://quarkus.io/guides/building-native-image for details

***
Start Docker Desktop (in my case Windows 10) and activate docker deamon
Create jar file containing native executable build in a container : `mvnw package -Pnative -Dquarkus.native.container-build=true`

Run created native executable in jar file: `java -jar ./target/locomotives-1.0-SNAPSHOT-native-image-source-jar/locomotives-1.0-SNAPSHOT-runner.jar`
* Startup time: 3 secs
* First / next response times in browser (localhost:8080/locomotives): 217 / 5 ms
* First / next response times curl (`curl localhost:8080/customers -s -o /dev/null -w "%{time_starttransfer}\n"`): 234 / 63 ms
* Jar + lib file size: 10 MB

### Comparison with Spring Boot project (Hibernate, H2, Rest Controller)

* Application start time: 8,5 secs
* First / next response times (`curl localhost:8080/customers -s -o /dev/null -w "%{time_starttransfer}\n"`): 422 / 62 ms
* Jar file size: 38 MB

## Create and run docker images

### Docker image based on JVM (mode) jar file

See created docker files in scr/main/docker for jvm and native mode

Create image: `docker build -f src/main/docker/Dockerfile.jvm -t quarkus/locomotives-jvm .`

Check image size: `docker image ls`: 509 MB

Memory usage (docker desktop statistics): 2 GB * 3,75% = 75 MB 

Deploy and run image in docker: `docker run -i --rm -p 8080:8080 quarkus/locomotives-jvm`
* Started in 1,2 secs
* First / next response times in browser (localhost:8080/locomotives): 217 / 5 ms

Remove image: `docker container ls` --> `docker kill <container-id>`

### Docker image based on native (mode) jar file

Create image: `docker build -f src/main/docker/Dockerfile.native -t quarkus/locomotives .`
* Image size: 166 MB

Deploy and run image in docker: `docker run -i --rm -p 8080:8080 quarkus/locomotives`
* Started in 13 ms
* First / next response times in browser (localhost:8080/locomotives): 15 / 4 ms
* Memory usage: 2 GB * 0,26% = 5 MB

### Comparison with Spring Boot Image (Rest controller, Hibernate, H2)

* Image size: 190 MB
* Started in 7,6 secs
* First / next response times in browser (localhost:8080/customers): 428 / 32 ms
* Memory usage: 2 GB * 12,57% = 260 MB

## Add Data model

* Add hibernate and jdbc dependencies to pom:

* Quarkus Extension View: Mark extensions and right click "install extension". Open pom file to check created entries
<pre><code>
&lt;dependency&gt;
	&lt;groupId&gt;io.quarkus&lt;/groupId&gt;
	&lt;artifactId&gt;quarkus-jdbc-h2&lt;/artifactId&gt;
&lt;/dependency&gt;
&lt;dependency&gt;
	&lt;groupId&gt;io.quarkus&lt;/groupId&gt;
	&lt;artifactId&gt;quarkus-hibernate-orm-panache&lt;/artifactId&gt;
&lt;/dependency&gt;
</pre></code>
<br>

* Add h2 configuration to application.properties (db might change for test and/or production later with separate profiles)
<br>
<pre><code>
quarkus.datasource.url=jdbc:h2:mem:default
quarkus.datasource.driver=org.h2.Driver
quarkus.datasource.username=username-default
quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.hibernate-orm.sql-load-script=h2/create_schema_and_records.sql
quarkus.hibernate-orm.log.sql=true
quarkus.hibernate-orm.log.bind-param=true
</pre></code>
<br>

* in folder src/main/resources create empty file
<pre><code>
<br> h2/create_schema_and_records.sql
</pre></code>

* Create simple entity using active record pattern: 
Create new source sub folder "model".	<br>
Create new class and extend with PanacheEntity and annotate class with @Entity.	<br> 
Add columns as public fields. Getter and setter methods will be created by Panache. 	<br>
Add customized queries as static methods.	<br>
A Hibernate repository class is not needed as Panache Entity class provides same methods.
<br><pre><code>
package de.jk.quarkus.trains.model;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Parameters;
<br>
@Entity
public class Locomotive extends PanacheEntity{
	<br>
	//Id added by panache
	<br>
	@NotEmpty
	@Column(name= "identification", length = 20, nullable = false)
	//number or other identifier
	public String identification;
	<br>
	@NotEmpty
	//technical identifier model railroad dcc standard (0 ... 9999)
	public Integer address;
	<br>
	//Last revision date
	public LocalDate revision;
	<br>
	//Customized queries ...
	<br>
	public static Locomotive findByAddress(Integer address){
        return find("address", address).firstResult();
    }
	<br>
	public static List<Locomotive> findAllByIdentificationLike(String identification){
        return find("identification LIKE concat('%', :identification, '%')", 
                Parameters.with("identification", identification)).list();
    }	
}
</pre></code>

* Add following content to `h2/create_schema_and_records.sql` to create table and add first record
<pre><code>
DROP TABLE locomotive IF EXISTS;
CREATE TABLE locomotive (
  id         INTEGER IDENTITY PRIMARY KEY,
  address INTEGER NOT NULL,
  identification VARCHAR(20) NOT NULL,
  revision DATE
);
CREATE INDEX index_locomotive ON locomotive (address);
INSERT INTO locomotive VALUES (0, 1, &#39;99 5906&#39;, &#39;1986-01-01&#39;);
</pre></code>
<br>

* Enhance test class by new test method for Panache Entity. <br>
Don't inject Locomotive class as class loader has already loaded for test. <br>
Use standard Panache methods to list records and create/read/update/delete single records <br>
Use standard Panache paginging for long lists<br>
Run JUnit test.
<pre><code>
@Test
@Transactional
public void testPanacheEntity() {
	<br>
	//Check preloaded data
	List<Locomotive> listLocomotives = Locomotive.listAll();
	assertEquals(1,listLocomotives.get(0).address);
	<br>
	//Add new record
	Locomotive newLocomotive = new Locomotive();
	newLocomotive.address = 2;
	newLocomotive.identification = "99 6001";
	newLocomotive.revision = LocalDate.of(1985, Month.JANUARY, 1);
	newLocomotive.persist();	
	<br>
	//Find all records like identification
	List<Locomotive> myLocomotiveList = Locomotive.findAllByIdentificationLike("99");
	assertThat("listsize", myLocomotiveList.size()>1);
	<br>
	//Find first locomotive with certain address
	Locomotive myLocomotive = Locomotive.findByAddress(2);
	assertEquals("99 6001", myLocomotive.identification);
	<br>
	//Find all records with paging
	PanacheQuery<Locomotive> pagedLocomotiveList = Locomotive.findAll();
	// make it use pages of 25 entries at a time
	pagedLocomotiveList.page(Page.ofSize(25));
	assertEquals(1, pagedLocomotiveList.pageCount());
	// get the first page
	List<Locomotive> firstPage = pagedLocomotiveList.list();
	assertEquals(2, firstPage.size());
	// get the xxx page
	List<Locomotive> page2 = pagedLocomotiveList.page(Page.of(2, 25)).list();
	assertEquals(0, page2.size());
	<br>
	//Update Locomotive
	Long id = myLocomotive.id;
	myLocomotive.address=3;
	myLocomotive.persist();
	Locomotive updatedLocomotive = Locomotive.findById(id);
	assertEquals(3, updatedLocomotive.address);
	<br>
	//Delete Locomotive
	Locomotive.deleteById(id);
	Locomotive deletedLocomotive = Locomotive.findById(id);
	assertEquals(null, deletedLocomotive);
}
</pre></code>
<br>

## Add REST / JSON controller

* Add extension `resteasy-jackson` with Quarkus Plugin
The POM file should now contain following entry:
<br>
<pre><code>
&lt;dependency&gt;
 	&lt;groupId&gt;io.quarkus&lt;/groupId&gt;
	&lt;artifactId&gt;quarkus-resteasy-jackson&lt;/artifactId&gt;
&lt;/dependency&gt;
</pre></code>
<br>

* Add `@RegisterForReflection` to Locomotive Bean Class, that Jackson can use this class with REST controller after compile to Jars / GraalVM<br>
<br>

+ Add ObjectMapperCustomizer and optimize local date format
<pre><code>
package de.jk.quarkus.trains.resources;
<br>import java.text.DateFormat;
<br>import java.text.SimpleDateFormat;
<br>import javax.inject.Singleton;
<br>import com.fasterxml.jackson.databind.ObjectMapper;
<br>import io.quarkus.jackson.ObjectMapperCustomizer;
<br>
<br>@Singleton
<br>public class RegisterCustomModuleCustomizer implements ObjectMapperCustomizer {
<br>public void customize(ObjectMapper mapper) {
<br>	//mapper.registerModule(new JavaTimeModule());
<br>	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
<br>	mapper.setDateFormat(df);
<br>    }
<br>} 
</pre></code>
<br>

* Modify Locomotive Resource for CRUD (create/read/update/delete) methods
<br>
<pre><code>
package de.jk.quarkus.trains.resources;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
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
<br>
@Path(&quot;/locomotives&quot;)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class LocomotiveResource {
<br>
	@Inject
    EntityManager em;
	<br>
    @GET
	public Response getPagableList( 
    		@QueryParam("pageNum") @DefaultValue("0") @Min(0) int pageNum, 
    		@QueryParam("pageSize") @DefaultValue("10") @Min(0) int pageSize) {
    	List<Locomotive> locomotives = Locomotive
    			.findAll().page(Page.of(pageNum, pageSize)).list();
    	return Response.ok(locomotives).build();
    }
	<br>
    @POST
    public Response add(Locomotive locomotive) {
		locomotive.id = null;
		locomotive.persist();	
		return Response.status(Response.Status.CREATED).entity(locomotive).build();
    }
    <br>
    @PUT
    public Response change(Locomotive locomotive) {
		//locomotive.persist();
    	em.merge(locomotive);
		return Response.status(Response.Status.OK).entity(locomotive).build();
    }
	<br>
    @DELETE
    @Path(&quot;/{id}&quot;)
    public Response delete(@PathParam(&quot;id&quot;) Long id) {
    	Locomotive locomotive  = new Locomotive().findById(id);
    	locomotive.delete();
    	return Response
        		.status(Response.Status.NO_CONTENT)
        		.build();
    }
}
</pre></code>
<br>

* Start application and check GET endpoint in Browser: `http://localhost:8080/locomotives`

<br>

* Modify test class and add methods for REST endpoint to check all single methods - example for list endpoint
<pre><code>
@Test
<br>@Order(20)
<br>public void testRESTGetAll() {
<br>    given()
<br>      .when().get("/locomotives")
<br>      .then()
<br>         .statusCode(OK.getStatusCode())
<br>         .body("id", notNullValue())
<br>     	 .body("address", notNullValue());
<br>}
</pre></code>
<br>

### Input validation with Hibernate validator
* Add extension `hibernate-validator` to project
<br>

* Inject validator in REST resource `@Inject Validator validator;`
<br>

* Add @Valid Annotation to Endpoints, where Entity Bean is transferred - throws ConstraintViolationException with details of each violation
<pre><code>
@POST
    public Response add(@Valid Locomotive locomotive) {
</pre></code>
<br>
<pre><code>
@PUT
    public Response change(@Valid Locomotive locomotive) {
</pre></code>
<br>

* Add validation annotations in bean class
<pre><code>
//number or other identifier
@NotBlank(message=&quot;identification cannot be blank&quot;) //Validation
@Length(min = 1, max = 20, message=&quot;identifier length must be between 1 and 20&quot;)//Validation
@Column(name= &quot;identification&quot;, length = 20, nullable = false)	
@NotEmpty //Database
public String identification;
<br>
//technical identifier model railroad dcc standard (0 ... 9999)
@NotNull(message=&quot;DCC address cannot be empty&quot;)//Validation
@Min(0)//Validation
@Max(9999)//Validation
public Integer address;
<br>
//Last revision date
@Past (message=&quot;revision date cannot be in the future&quot;) //Validation
public LocalDate revision;
</pre></code>
<br>

* Add test case for missing mandatory values
<pre><code>
@Test
@Order(22)
public void testRESTPostInputValidationMandatory() {
    Locomotive locomotive = new Locomotive();
    locomotive.address=null;
    locomotive.identification=null;
    locomotive.revision = null;<br>
    ValidatableResponse response = given().contentType(&quot;application/json&quot;).body(locomotive)
            .when().post(&quot;/locomotives&quot;)
            .then()
                .log()
            	.body()
                .statusCode(BAD_REQUEST.getStatusCode())
                .body(&quot;parameterViolations.findAll { it.path == \&quot;add.locomotive.identification\&quot; &amp;&amp; it.value == \&quot;\&quot;}.message&quot;,  hasItem(&quot;identification cannot be blank&quot;))<br>    			.body(&quot;parameterViolations.findAll { it.path == \&quot;add.locomotive.address\&quot; &amp;&amp; it.value == \&quot;\&quot;}.message&quot;, hasItem(&quot;DCC address cannot be empty&quot;));
    
}
</pre></code>
<br>

* See log content - answers with expected http code 400 and gives all information for 100% test coverage per parameter - based on current output:
<pre><code>
{
    &quot;id&quot;: 2,
    &quot;identification&quot;: &quot;99 999&quot;,
    &quot;address&quot;: 2,
    &quot;revision&quot;: [
        1985,
        1,
        1
    ]
}
2
{
    &quot;exception&quot;: null,
    &quot;propertyViolations&quot;: [ ],
    &quot;classViolations&quot;: [ ],
    &quot;parameterViolations&quot;: [
        {
            &quot;constraintType&quot;: &quot;PARAMETER&quot;,
            &quot;path&quot;: &quot;add.locomotive.identification&quot;,
            &quot;message&quot;: &quot;darf nicht leer sein&quot;,
            &quot;value&quot;: &quot;&quot;
        }, {
            &quot;constraintType&quot;: &quot;PARAMETER&quot;,
            &quot;path&quot;: &quot;add.locomotive.identification&quot;,
            &quot;message&quot;: &quot;identification cannot be blank&quot;,
            &quot;value&quot;: &quot;&quot;
        }, {
            &quot;constraintType&quot;: &quot;PARAMETER&quot;,
            &quot;path&quot;: &quot;add.locomotive.address&quot;,
            &quot;message&quot;: &quot;DCC address cannot be empty&quot;,
            &quot;value&quot;: &quot;&quot;
        }
    ],
    &quot;returnValueViolations&quot;: []
}
{
    &quot;id&quot;: 2,
    &quot;identification&quot;: &quot;99 998&quot;,
    &quot;address&quot;: 93,
    &quot;revision&quot;: [
        1986,
        1,
        1
    ]
}
</pre></code>

### Improve error handling standardized error output format

* Add an customized exception mapper to enable output format for input validation errors. 
This enables the output structure definition in swagger files.
Different to same approach in Spring boot it is not possible to have one generalized exception mapper including global exception type as well as customized exceptions. Handling global exceptions would override and break central quarkus exception handling.
In consequence I decided to create on exception handler for Quarkus validation constraints violations and second exception handler for customized business constraints and my customized business exception type.<br>
</p>
* Constraint Violation Exception Mapper
<pre><code>
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper&lt;ConstraintViolationException&gt; 
{
    @Override
    public Response toResponse(ConstraintViolationException exception) 
    {
    	Set&lt;ConstraintViolation&lt;?&gt;&gt; violations = exception.getConstraintViolations();
    	ErrorsResponse errors = new ErrorsResponse();
    	Iterator&lt;ConstraintViolation&lt;?&gt;&gt; iterator = violations.iterator();
        while(iterator.hasNext()) {
            ConstraintViolation&lt;Locomotive&gt; violation = (ConstraintViolation&lt;Locomotive&gt;) iterator.next();
            String value=null;
            if (violation.getInvalidValue() != null) {
            	value = violation.getInvalidValue().toString();
            }else {
            	value = &quot;&quot;;
            }
            errors.getErrorList().add(new ErrorResponse(
            		&quot;400001&quot;, 
            		violation.getMessage(),
            		violation.getPropertyPath().toString(),
            		value,
            		null)
            		);
        }
    	return Response.status(Status.BAD_REQUEST).entity(errors).build();  
    }
}
</pre></code>
</p>

* Standardized error list
<pre><code>
package de.jk.quarkus.trains.exception;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import io.quarkus.runtime.annotations.RegisterForReflection;
<br>
@RegisterForReflection
public class ErrorsResponse {
   private List&lt;ErrorResponse&gt; errorList = new ArrayList&lt;&gt;();
<br>
    //Timestamp error occured
    private String timestamp = Instant.now().toString();
	<br>
    ... Getters and Setters ...
}
</pre></code>
</p>

* Standardized error
<pre><code>
package de.jk.quarkus.trains.exception;
import java.time.Instant;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import io.quarkus.runtime.annotations.RegisterForReflection;
<br>
@RegisterForReflection
public class ErrorResponse
{
    public ErrorResponse(String code, String message, String parameter, String value, List&lt;String&gt; details) {
        super();
        this.code = code;
        this.parameter = parameter;
        this.value = value;
        this.message = message;
        this.details = details;
    }
 <br>
    //Unique error code
    private String code;
    <br>
    //General error message about nature of error
    private String message;
 <br>
    //Input parameter, which caused the error
    private String parameter;
<br>
    //Input parameter, which caused the error
    private String value;
<br>
    //Specific errors in API request processing
    private List&lt;String&gt; details;
<br>
    ... Getters and Setters ....
}
</pre></code>
</p> 

## OpenAPI docu
* Add extension `smallrye-openapi` with Quarkus eclipse plugin or do same by running following maven command: 
<br>`mvnw quarkus:add-extension -Dextensions="openapi"`
</p>

* Start browser and download generated swagger file (open api version 3) with `/openapi` endpoint
</p>

* Show swagger ui with `/swagger-ui` endpoint
</p>
 
* Add annotations to REST resource class and attributes 
</p>
Class level
<br>
<pre><code>
@Tag(name= "Locomotives") //OpenAPI
@Path("/locomotives")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class LocomotiveResource {
</pre></code>
</p>
Method Level
<br>
<pre><code>
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
    		@Valid Locomotive locomotive
    		) {
</pre></code>
</p>


* Add annotations to Panache Entity class and attributes as well as to error output classes
</p>
Class Level
<pre><code>
@Entity
@RegisterForReflection
@Schema(name="Locomotive", description="data required for control of a locomotive") //OpenAPI
public class Locomotive extends PanacheEntity{
</pre></code>
</p>
Attribute Level
<pre><code>
//technical identifier model railroad dcc standard (0 ... 9999)
	@NotNull(message="DCC address cannot be empty")//Validation
	@Min(0)//Validation
	@Max(9999)//Validation
	@Schema(description = "DCC decoder address", required = true, example = "59") //OpenAPI
	public Integer address;
</pre></code>
</p>

* If required, activate `/swagger-ui` endpoint in production profile too (by default dev and test only):
<br>
`quarkus.swagger-ui.always-include=true`
</p>

* Add top information for Swagger-UI pages - requires an JAX-RS Application class
<pre><code>
import javax.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
<br>
@OpenAPIDefinition(
	    tags = {
	            @Tag(name=&quot;Locomotives&quot;, description=&quot;Operations for Locomotive and function maintenance&quot;),
	            @Tag(name=&quot;status&quot;, description=&quot;Operations for dcc communication&quot;)
	    },
	    info = @Info(
	        title=&quot;Locomotive API&quot;,
	        version = &quot;1.0.0&quot;,
	        contact = @Contact(
	            name = &quot;DCC API Github side&quot;,
	            url = &quot;https://github.com/jo9999ki/trains_locomotives&quot;,
	            email = &quot;jochen_kirchner@yahoo.com&quot;),
	        license = @License(
	            name = &quot;Apache 2.0&quot;,
	            url = &quot;http://www.apache.org/licenses/LICENSE-2.0.html&quot;))
	)
public class OpenAPIApplicationLevelConfiguration extends Application{
<br>
}
</pre></code>
</p> 


## Add observability capabilities

### Health check

* Add `SmallRye health` extension or run following maven command:<br>
`mvnw quarkus:add-extension -Dextensions="health"`
</p>

* Check following endpoints
`/health/live` The application is up and running
<pre><code>
{
    &quot;status&quot;: &quot;UP&quot;,
    &quot;checks&quot;: [
    ]
}
</pre></code>
</p> 
`/health/ready` The application is ready to serve requests
<pre><code>
{
    &quot;status&quot;: &quot;UP&quot;,
    &quot;checks&quot;: [
        {
            &quot;name&quot;: &quot;Database connections health check&quot;,
            &quot;status&quot;: &quot;UP&quot;
        }
    ]
}
</pre></code>
</p> 
<br>`/health`: accumulation of all available health checks
<pre><code>
{
    &quot;status&quot;: &quot;UP&quot;,
    &quot;checks&quot;: [
        {
            &quot;name&quot;: &quot;Database connections health check&quot;,
            &quot;status&quot;: &quot;UP&quot;
        }
    ]
}
</pre></code>
</p> 

* Add customized health check for REST resource method with database access
<pre><code>
package de.jk.quarkus.trains.health;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import de.jk.quarkus.trains.resources.LocomotiveResource;
<br>
@Readiness
@ApplicationScoped
//Liveness Health Check
public class LocomotiveListHealthCheck implements HealthCheck {
<br>
    @Inject
    LocomotiveResource locomotiveResource;
<br>
    @Override
    public HealthCheckResponse call() {
    	locomotiveResource.getPagableList(0, 10);
        return HealthCheckResponse.named(&quot;REST method + db health check (list)&quot;).up().build();
    }
}
</pre></code>
</p> 

* Add test method for health check
<pre><code>
   @Test
   @Order(40)
   void testHealthCheck() {
       given()
           .when().get("/health")
           .then()
           .statusCode(OK.getStatusCode());
   }
</pre></code>
</p> 
   
### Metrics





