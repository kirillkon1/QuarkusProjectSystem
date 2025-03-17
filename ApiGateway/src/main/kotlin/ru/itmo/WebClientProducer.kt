package ru.itmo

import io.vertx.core.Vertx
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import jakarta.inject.Inject

@ApplicationScoped
class WebClientProducer {
    @Inject
    lateinit var vertx: Vertx

    @Produces
    fun produceWebClient(): WebClient {
        return WebClient.create(vertx, WebClientOptions())
    }
}
