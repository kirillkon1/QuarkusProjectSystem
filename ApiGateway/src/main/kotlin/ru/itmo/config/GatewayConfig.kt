package ru.itmo.config

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "gateway")
interface GatewayConfig {
    fun routes(): List<RouteConfig>
}

interface RouteConfig {
    fun id(): String

    fun uri(): String

    fun predicates(): List<String>

    fun filters(): List<String>?
}
