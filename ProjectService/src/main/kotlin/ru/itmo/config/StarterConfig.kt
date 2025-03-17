package ru.itmo.config

import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class StarterConfig {
    @ConfigProperty(name = "quarkus.application.name")
    lateinit var applicationName: String

    @ConfigProperty(name = "quarkus.application.version")
    lateinit var applicationVersion: String

    @ConfigProperty(name = "quarkus.http.port")
    lateinit var applicationPort: String

    fun onStart(
        @Observes ev: StartupEvent,
    ) {
        println("=== Project Service Configuration ===\n")

        println("Application Name: $applicationName")
        println("Version: $applicationVersion")
        println("Port : $applicationPort\n")

        println("=== End of Project Service Configuration ===")
    }
}
