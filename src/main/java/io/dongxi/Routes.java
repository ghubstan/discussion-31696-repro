package io.dongxi;


import io.quarkus.vertx.web.ReactiveRoutes;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;

@ApplicationScoped
@RouteBase(path = "", produces = ReactiveRoutes.APPLICATION_JSON)
public class Routes {

    private final Logger log = Logger.getLogger(Routes.class);

    private final Vertx vertx;

    @Inject
    public Routes(Vertx vertx) {
        this.vertx = vertx;
    }


    @Route(path = "/", methods = Route.HttpMethod.GET)
    public Uni<JsonObject> home() {
        try {
            var host = InetAddress.getLocalHost();
            var result = new JsonObject().put("host", host.toString());
            return Uni.createFrom().item(result);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Route(path = "/ping", methods = Route.HttpMethod.GET)
    public Uni<JsonObject> ping() {
        try {
            var host = InetAddress.getLocalHost();
            var result = new JsonObject()
                    .put("ping", "pong")
                    .put("from", host.toString());
            return Uni.createFrom().item(result);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
