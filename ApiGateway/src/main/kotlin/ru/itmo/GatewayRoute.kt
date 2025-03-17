package ru.itmo

import io.quarkus.vertx.web.Route
import io.quarkus.vertx.web.Route.HttpMethod
import io.quarkus.vertx.web.RoutingExchange
import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.client.WebClient
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import ru.itmo.config.GatewayConfig

@ApplicationScoped
class GatewayRoute
    @Inject
    constructor(
        private val config: GatewayConfig,
        private val webClient: WebClient,
    ) {
        @Route(
            path = "/api/*",
            methods = [
                HttpMethod.GET,
                HttpMethod.POST,
                HttpMethod.PUT,
                HttpMethod.DELETE,
                HttpMethod.OPTIONS,
            ],
        )
        fun handle(routingExchange: RoutingExchange) {
            val requestPath = routingExchange.request().path() // Например, "/api/users/1"

            // Поиск маршрута по предикатам
            val matchingRoute =
                config.routes().find { route ->
                    route.predicates().any { predicate ->
                        predicateMatches(predicate, requestPath)
                    }
                }
            if (matchingRoute == null) {
                routingExchange.response().setStatusCode(404).end("No matching route found")
                return
            }

            // Формируем конечный URL
            val relativePath = requestPath
            val targetUri = matchingRoute.uri().trimEnd('/') + relativePath

            if (matchingRoute.filters()?.contains("LogFilter") == true) {
                println("Forwarding request for [$requestPath] to [$targetUri]")
            }

            val method = routingExchange.request().method()
            val clientRequest = webClient.requestAbs(method, targetUri)
            // Переносим заголовки
            routingExchange.request().headers().forEach { header ->
                clientRequest.putHeader(header.key, header.value)
            }

            if (io.quarkus.vertx.web.Route.HttpMethod.valueOf(method.name()) == io.quarkus.vertx.web.Route.HttpMethod.GET) {
                // GET запросы: тело отсутствует
                clientRequest.send { ar ->
                    if (ar.succeeded()) {
                        val clientResponse = ar.result()
                        routingExchange.response().setStatusCode(clientResponse.statusCode())
                        clientResponse.headers().forEach { header ->
                            routingExchange.response().putHeader(header.key, header.value)
                        }
                        routingExchange.response().end(clientResponse.bodyAsBuffer())
                    } else {
                        routingExchange.response().setStatusCode(500)
                            .end("Error forwarding request: ${ar.cause()?.message}")
                    }
                }
            } else {
                // Обработка запросов с телом
                routingExchange.request().pause()
                routingExchange.request().bodyHandler { bodyBuffer: Buffer ->
                    routingExchange.request().resume()
                    clientRequest.sendBuffer(bodyBuffer) { ar ->
                        if (ar.succeeded()) {
                            val clientResponse = ar.result()
                            routingExchange.response().setStatusCode(clientResponse.statusCode())
                            clientResponse.headers().forEach { header ->
                                routingExchange.response().putHeader(header.key, header.value)
                            }
                            val responseBody: Buffer? = clientResponse.bodyAsBuffer()
                            if (responseBody != null) {
                                routingExchange.response().end(responseBody)
                            } else {
                                routingExchange.response().end()
                            }
                        } else {
                            routingExchange.response().setStatusCode(500)
                                .end("Error forwarding request: ${ar.cause()?.message}")
                        }
                    }
                }
            }
        }

        private fun predicateMatches(
            predicate: String,
            requestPath: String,
        ): Boolean {
            if (predicate.startsWith("Path=")) {
                val pattern = predicate.removePrefix("Path=") // например, "/api/tasks/**"
                val basePath = pattern.removeSuffix("/**")
                val regexPattern = "^" + Regex.escape(basePath) + "(/.*)?$"
                return Regex(regexPattern).matches(requestPath)
            }
            return false
        }
    }
