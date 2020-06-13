package de.jk.quarkus.trains.openapi;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
	    tags = {
	            @Tag(name="Locomotives", description="Operations for Locomotive and function maintenance"),
	            @Tag(name="status", description="Operations for dcc communication")
	    },
	    info = @Info(
	        title="Locomotive DCC API",
	        description = "OpenAPI document structure standard: version 3.0",
	        version = "1.0.0",
	        contact = @Contact(
	            name = "DCC API Github side",
	            url = "https://github.com/jo9999ki/trains_locomotives",
	            email = "jochen_kirchner@yahoo.com"),
	        license = @License(
	            name = "Apache 2.0",
	            url = "http://www.apache.org/licenses/LICENSE-2.0.html")),
		servers = {
		        @Server(url = "http://localhost:8081"),
		        @Server(url = "http://localhost:51045")
		}
	)
public class OpenAPIApplicationLevelConfiguration extends Application{

}
