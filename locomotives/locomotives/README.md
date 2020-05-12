# locomotives project

This project contains a REST service providing model train control for locomotives

The project uses Quarkus, the Supersonic Subatomic Java Framework (See https://quarkus.io/).

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `locomotives-1.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/locomotives-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

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

Test: ` curl localhost:8080/locomotives`

Show index.html with: `localhost:8080`

