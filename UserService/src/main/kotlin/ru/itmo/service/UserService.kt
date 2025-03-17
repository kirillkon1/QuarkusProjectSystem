package ru.itmo.service

import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import ru.itmo.model.entity.UserEntity
import ru.itmo.model.repository.UserRepository
import ru.itmo.utils.Hasher

@ApplicationScoped
class UserService(private val userRepository: UserRepository) {

    @WithSession
    fun getUserById(id: Long): Uni<UserEntity?> =
        userRepository.findById(id)

    @WithSession
    fun getAllUsers(): Uni<List<UserEntity>> =
        userRepository.listAll()

    @WithTransaction
    fun createUser(user: UserEntity): Uni<UserEntity> {
        user.password = Hasher.hashPassword(user.password)
        return userRepository.persist(user)
    }
    @WithTransaction
    fun updateUser(id: Long, updatedUser: UserEntity): Uni<UserEntity?> =
        userRepository.findById(id)
            .onItem().ifNotNull().transformToUni { existingUser ->
                existingUser.username = updatedUser.username
                existingUser.email = updatedUser.email
                existingUser.password = Hasher.hashPassword(updatedUser.password)
                userRepository.persist(existingUser)
            }

    @WithTransaction
    fun deleteUser(id: Long): Uni<Boolean> =
        userRepository.deleteById(id)
}