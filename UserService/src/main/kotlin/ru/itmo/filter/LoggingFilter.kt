package ru.itmo.filter

import io.quarkus.vertx.web.Route
import io.quarkus.vertx.web.Route.HttpMethod
import io.quarkus.vertx.web.RoutingExchange
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class LoggingFilter {
    @Route(
        path = "/*",
        methods = [HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.OPTIONS],
        order = Int.MIN_VALUE,
    )
    fun logRequest(routingExchange: RoutingExchange) {
        val method = routingExchange.request().method()
        val path = routingExchange.request().path()
        println("Incoming request: [$method] $path")
        // Передаём управление следующему обработчику в цепочке
        routingExchange.context().next()
    }
}
