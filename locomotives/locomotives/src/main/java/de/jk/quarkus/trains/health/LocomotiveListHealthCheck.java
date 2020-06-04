package de.jk.quarkus.trains.health;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import de.jk.quarkus.trains.resources.LocomotiveResource;

@Readiness
@ApplicationScoped
//Liveness Health Check
public class LocomotiveListHealthCheck implements HealthCheck {

    @Inject
    LocomotiveResource locomotiveResource;

    @Override
    public HealthCheckResponse call() {
    	locomotiveResource.getPagableList(0, 10);
        return HealthCheckResponse.named("REST method + db health check (list)").up().build();
    }
}

