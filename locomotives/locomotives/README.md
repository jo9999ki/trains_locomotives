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

### Add Data model

Add hibernate and jdbc dependencies to pom:

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

* Add h2 configuration to application.properties (db might change for test and/or production later with separate profiles)
<pre><code>
#h2 configuration
quarkus.datasource.url=jdbc:h2:mem:default
quarkus.datasource.driver=org.h2.Driver
quarkus.datasource.username=username-default
quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.hibernate-orm.sql-load-script=h2/create_schema_and_records.sql
quarkus.hibernate-orm.log.sql=true
quarkus.hibernate-orm.log.bind-param=true
</pre></code>

* in folder src/main/resources create empty file
<pre><code>
h2/create_schema_and_records.sql
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