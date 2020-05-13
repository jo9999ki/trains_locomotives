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

