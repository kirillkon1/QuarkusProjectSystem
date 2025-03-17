package ru.itmo.model.repository

import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import ru.itmo.model.entity.UserEntity

@ApplicationScoped
class UserRepository : PanacheRepository<UserEntity> {
    fun findByName(name: String): Uni<UserEntity> {
        return find("name", name).firstResult()
    }
}
