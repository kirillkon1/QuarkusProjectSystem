package ru.itmo.config

import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class StarterConfig {
    @ConfigProperty(name = "quarkus.application.name")
    lateinit var applicationName: String

    @ConfigProperty(name = "quarkus.application.version")
    lateinit var applicationVersion: String

    @ConfigProperty(name = "quarkus.http.port")
    lateinit var applicationPort: String

    @Inject
    lateinit var config: GatewayConfig

    fun onStart(
        @Observes ev: StartupEvent,
    ) {
        println("=== Gateway Configuration ===\n")

        println("Application Name: $applicationName version: $applicationVersion")
        println("Port : $applicationPort\n")

        config.routes().forEach { route ->
            println("Route id: ${route.id()}")
            println("  URI: ${route.uri()}")
            println("  Predicates: ${route.predicates()}")
//            println("  Filters: ${route.filters()}")
            println()
        }
        println("=== End of Gateway Configuration ===")
    }
}
